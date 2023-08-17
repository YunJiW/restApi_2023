package com.example.demo.boundedContext.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Post /member/login은 로그인 처리 URL")
    void t1() throws Exception{

        //when
        ResultActions resultActions = mvc.perform(post("/member/login")
                .content("""
                        {
                            "username" : "user1",
                            "password" : "1234"
                        }
                        """.stripIndent())
                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        ).andDo(print());

        resultActions.andExpect(status().is2xxSuccessful());
    }

}