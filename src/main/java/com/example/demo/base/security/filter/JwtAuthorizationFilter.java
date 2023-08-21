package com.example.demo.base.security.filter;

import com.example.demo.base.jwt.JwtProvider;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null){
            String token = bearerToken.substring("Bearer ".length());


            //인증된경우
            if(jwtProvider.verify(token)){
                Map<String, Object> claims = jwtProvider.getClaims(token);
                long id = (int)claims.get("id");

                Member member = memberService
                        .findById(id).orElseThrow();

                forceAuthentication(member);

            }
        }

        filterChain.doFilter(request,response);

    }

    //강제 로그인 처리 매서드
    public void forceAuthentication(Member member){
        User user = new User(member.getUsername(),member.getPassword(),member.getAuthorities());

        //스프링 시큐리티 객체에 저장할 authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(user,
                        null,
                        member.getAuthorities());


        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        //context에 authentication 객체 저장
        context.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(context);
    }


}
