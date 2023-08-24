package com.example.demo.boundedContext.member.service;

import com.example.demo.base.jwt.JwtProvider;
import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public final JwtProvider jwtProvider;


    public Member join(String username,String password,String email){
        //비밀번호 암호화
        password = passwordEncoder.encode(password);
        Member member = Member.build(username,password,email);

        memberRepository.save(member);
        return member;
    }

    public String genAccessToken(Member member,String password){
        return jwtProvider.genToken(member.toClaims(),60*60*24*365);
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public RsData CanGenAccessToken(Member member, String password) {
        if(!passwordEncoder.matches(password,member.getPassword())){
            return RsData.of("F-1","비밀번호가 일치하지 않습니다.");
        }

        return RsData.of("S-1","토큰 생성 가능");

    }
}
