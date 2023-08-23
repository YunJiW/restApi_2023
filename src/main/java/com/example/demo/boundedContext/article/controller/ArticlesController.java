package com.example.demo.boundedContext.article.controller;


import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.article.entity.Article;
import com.example.demo.boundedContext.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/articles",produces = APPLICATION_JSON_VALUE)
@Tag(name = "ArticleController",description = "게시물 CRUD 컨트롤러")
public class ArticlesController {

    private final ArticleService articleService;

    @AllArgsConstructor
    @Getter
    public static class ArticlesResponse{
        private final List<Article> articles;
    }

    @AllArgsConstructor
    @Getter
    public static class ArticleResponse{
        private final Article article;
    }


    @GetMapping(value = "")
    @Operation(summary = "게시물들 조회")
    public RsData<ArticlesResponse> articles(){
        List<Article> article = articleService.findAll();

        return RsData.of("S-1","성공",new ArticlesResponse(article));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "게시물 단건 조회")
    public RsData<ArticleResponse> article(@PathVariable Long id){
        Optional<Article> article = articleService.findById(id);

        if(article.isEmpty()){
            return RsData.of("F-1","%d번 게시물이 존재하지 않습니다".formatted(id),null);
        }

        return RsData.of("S-1","성공",new ArticleResponse(article.get()));

    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "게시물 수정")
    public RsData<Article> update(@PathVariable Long id){
        return  null;

    }

}
