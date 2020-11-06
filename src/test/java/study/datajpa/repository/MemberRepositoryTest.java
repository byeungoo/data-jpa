package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());  //spring data jpa가 구현체를 꽂아버림.
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto(){
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        Team team = new Team("teamA");
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for (MemberDto memberDto : usernameList) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : memberList) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");  //null인 경우에는 빈 컬렉션
        Member findMember = memberRepository.findMemberByUsername("BBB");  //단건인 경우에는 없으면 결과가 null임.
        Optional<Member> ccc = memberRepository.findOptionalByUsername("asdasdasd");  //결과가 있을수도있고 없을 수도 있으면 Optional 쓰는게 좋음
    }


}