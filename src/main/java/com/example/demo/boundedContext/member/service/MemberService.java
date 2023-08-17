package com.example.demo.boundedContext.member.service;

import com.example.demo.base.jwt.JwtProvider;
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

    public String genAccessToken(String username,String password){
        Member member = findByUsername(username).orElse(null);

        if(member == null) return null;

        if(!passwordEncoder.matches(password,member.getPassword())){
            return null;
        }

        return jwtProvider.genToken(member.toClaims(),60*60*24*365);
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }
}
