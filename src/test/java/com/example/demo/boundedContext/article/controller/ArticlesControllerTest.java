package com.example.demo.boundedContext.article.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ArticlesControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /articles")
    void t1() throws Exception {

        //when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles"))
                .andDo(print());

        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles[0].id").exists());
    }

    @Test
    @DisplayName("GET /articles/5")
    void t2() throws Exception {

        //when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles/5"))
                .andDo(print());

        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(5));
    }

    @Test
    @DisplayName("POST /articles/1")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/articles")
                                .content("""
                                        {
                                            "subject": "제목 new",
                                            "content": "내용 new"
                                        }
                                        """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article").exists());
    }


    @Test
    @DisplayName("PATCH /articles/5")
    @WithUserDetails("user1")
    void t4() throws Exception {

        //when
        ResultActions resultActions = mvc.perform(
                patch("/api/v1/articles/5")
                        .content("""
                                {
                                    "subject": "제목 5 !!!",
                                    "content": "내용 5 !!!"
                                }
                                """
                        ).contentType(new MediaType(MediaType.APPLICATION_JSON,StandardCharsets.UTF_8))
        ).andDo(print());

        //기대하는 결과값들
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(5))
                .andExpect(jsonPath("$.data.article.subject").value("제목 5 !!!"))
                .andExpect(jsonPath("$.data.article.content").value("내용 5 !!!"));
    }

    @Test
    @DisplayName("PATCH /articles/5 부분 변경")
    @WithUserDetails("user1")
    void t5() throws Exception {

        //when
        ResultActions resultActions = mvc.perform(
                patch("/api/v1/articles/5")
                        .content("""
                                {
                                    "subject": "제목 5 !!!"
                                }
                                """
                        ).contentType(new MediaType(MediaType.APPLICATION_JSON,StandardCharsets.UTF_8))
        ).andDo(print());

        //기대하는 결과값들
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(5))
                .andExpect(jsonPath("$.data.article.subject").value("제목 5 !!!"))
                .andExpect(jsonPath("$.data.article.content").value("내용 5"));
    }

}