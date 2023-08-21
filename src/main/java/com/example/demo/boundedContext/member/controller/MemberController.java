package com.example.demo.boundedContext.member.controller;

import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.member.dto.MemberDto;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE,consumes = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name ="MemberController",description = "로그인,로그인 된 회원의 정보 제공")
public class MemberController {

    private final MemberService memberService;

    @Data
    public static class LoginRequest{
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @AllArgsConstructor
    @Getter
    public static class LoginResponse{
        private final String accessToken;
    }


    @PostMapping("/login")
    @Operation(summary = "로그인,액세스 발급")
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        String accressToken = memberService.genAccessToken(loginRequest.getUsername(),loginRequest.getPassword());

        return RsData.of("S-1",
                "액세스 토큰 생성 완료",
                new LoginResponse(accressToken));
    }

    @AllArgsConstructor
    @Getter
    public static class MeResponse{
        private final MemberDto member;
    }

    //@AuthenticationPrincipal 을 통해서 SpringSecurity에서 현재 회원이 누군지 알 수 있음.
    // - 추가 설명 : 스프링 시큐리티에 등록되어있는 유저를 가져온다.
    @GetMapping(value = "/me",consumes = ALL_VALUE)
    @Operation(summary = "로그인 된 사용자 정보",security = @SecurityRequirement(name="bearerAuth"))
    public RsData<MeResponse> me(@AuthenticationPrincipal User user){
        Member member = memberService.findByUsername(user.getUsername()).get();

        return RsData.of("S-1",
                "성공",
                new MeResponse(MemberDto.of(member)));

    }
}
