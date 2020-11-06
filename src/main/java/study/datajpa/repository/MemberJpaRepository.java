package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {

    @PersistenceContext  //스프링 컨테이너가 엔티티 매니저를 가져다줌.
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
