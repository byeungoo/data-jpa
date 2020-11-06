package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //@Query(name = "Member.findByUsername")  //Named Query 지정. 주석처리해도 Named Query 동작함. Named Query를 먼저 실행해줌
    List<Member> findByUsername(@Param("username") String username); //파라미터 세팅

}
