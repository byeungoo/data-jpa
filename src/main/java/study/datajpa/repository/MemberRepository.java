package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

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

    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")          //카운트 쿼리를 따로 만들어줌. 안그러면 조인이 복잡한 경우 그 복잡한 조인으로 count 쿼리를함.
    Page<Member> findPage2ByAge(int age, Pageable pageable);

    Page<Member> findPage1ByUsername(String name, Pageable pageable); //count 쿼리 사용
    Slice<Member> findPage2ByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
    List<Member> findPage3ByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
    List<Member> findPage4ByUsername(String name, Sort sort);

    @Modifying(clearAutomatically = true)  //벌크성 수정, 삭제 쿼리는 이 어노테이션 사용해야함. clearAutomatically 옵션을 주면 영속성 컨텍스트 비워줌.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})  //내부적으로 fetch join 사용
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m ")
    List<Member> findMemberEntityGraph(); // jpql + EntityGraph 같이 사용

    @EntityGraph(attributePaths = {"team"})
    //@EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);  //메소드이름으로

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)  //쿼리 끝에 for update 붙음.
    List<Member> findLockByUsername(String username);

    //List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    //List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);

    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    /*
     * 정적 쿼리를 이렇게 풀면 됩니다.
     */
    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            "from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
