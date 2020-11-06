package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})  //가급적 연관관계는 @ToString 안하는게 나음
public class Member {

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