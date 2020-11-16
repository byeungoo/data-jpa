package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
@Log
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /*
     * 도메인 클래스 컨버터가 중간에서 동작해서 회원 엔티티로 객체를 반환
     * 조회용으로만 사용해야합니다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    /*
     * 컨트롤러에서 파라미터에 pageable있으면
     * 스프링부트가 PageRequest 객체에 값을 채워서 Pageable에 넣어줌
     */
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member));
        return map;
    }

    //@PostConstruct
    public void init(){
        for(int i = 0 ; i < 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
