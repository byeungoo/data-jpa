package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/*
 * 클래스 뒤에 이름 Impl은 맞춰야함.
 * 하지만 화면에 맞춘 쿼리와 핵심 비즈니스 로직은 분리해야함
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom(){
        return em.createQuery("select m from Member m ")
                .getResultList();
    }

}
