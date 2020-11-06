package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);  //파라미터 3개만 되도 너무 길어짐.

    //@Query(name = "Member.findByUsername")  //Named Query 지정. 주석처리해도 Named Query 동작함. Named Query를 먼저 실행해줌
    List<Member> findByUsername(@Param("username") String username); //파라미터 세팅

    @Query("select m from Member m where m.username = :username and m.age = :age")  //애플리케이션 로딩 시점에 오타 잡아줌.
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO 반환
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터 바인딩 (이름기반).
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    //in절 처리
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByUsername(String name); //컬렉션
    Member findMemberByUsername(String name); //단건
    Optional<Member> findOptionalByUsername(String name); //단건 Optional

}
