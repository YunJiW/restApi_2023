package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorityList  = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority("MEMBER"));

        return authorityList;
    }

    public static Member build(String username,String password,String email){
        return Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

    }

}
