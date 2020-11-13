package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})  //가급적 연관관계는 @ToString 안하는게 나음
@NamedQuery(  //NamedQuery 보다는 JPQL 많이씀.
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"  //NamedQuery의 가장 큰 장점은 애플리케이션 로딩 시점에 파싱을 해서 오류를 알려줌.
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    //연관 관계 있는쪽도 세팅해줌
    //양방향 연관 관계 한번에 처리
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}