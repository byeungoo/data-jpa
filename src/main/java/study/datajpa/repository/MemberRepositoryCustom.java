package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

/*
 * query dsl 사용할 때 custom을 많이 사용함.
 */
public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();

}
