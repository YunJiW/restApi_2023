package com.example.demo.boundedContext.member.dto;


import com.example.demo.boundedContext.member.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {

    private long id;

    private LocalDateTime regDate;
    private String username;

    private MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.regDate = member.getCreateDate();
    }

    public static MemberDto of(Member member){
        return new MemberDto(member);
    }
}
