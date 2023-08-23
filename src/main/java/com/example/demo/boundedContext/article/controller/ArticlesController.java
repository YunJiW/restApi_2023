package com.example.demo.boundedContext.article.controller;


import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.article.entity.Article;
import com.example.demo.boundedContext.article.service.ArticleService;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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

    private final MemberService memberService;

    //게시물 조회
    @AllArgsConstructor
    @Getter
    public static class ArticlesResponse{
        private final List<Article> articles;
    }

    //단건 조회
    @AllArgsConstructor
    @Getter
    public static class ArticleResponse{
        private final Article article;
    }

    @Data
    public static class WriteRequest{

        @NotBlank
        private String subject;

        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class WriteResponse{
        private final Article article;
    }

    @Data
    public static class ModifyRequest{

        @NotBlank
        private String subject;

        @NotBlank
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class ModifyResponse{
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
    
    
    @PostMapping(value = "")
    @Operation(summary = "게시물 등록",security = @SecurityRequirement(name ="bearerAuth"))
    public RsData<WriteResponse> write(@AuthenticationPrincipal User user,
                                       @Valid @RequestBody WriteRequest writeRequest){
        Member member = memberService.findByUsername(user.getUsername()).orElseThrow();
        RsData<Article> WriteRs = articleService.write(member,writeRequest.getSubject(),writeRequest.getContent());
        //실패시에도 실패 코드 반환
        if(WriteRs.isFail()){
            return (RsData) WriteRs;
        }

        //성공시 성공 RsData 반환
        return RsData.of(WriteRs.getResultCode(),
                WriteRs.getMsg(),
                new WriteResponse(WriteRs.getData()));

    }

    @PatchMapping(value = "/{id}",consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "게시물 수정",security = @SecurityRequirement(name ="bearerAuth"))
    public RsData<ModifyResponse> modify(@AuthenticationPrincipal User user
            ,@Valid @RequestBody ModifyRequest modifyRequest
            ,@PathVariable Long id){

        Member member = memberService.findByUsername(user.getUsername()).orElseThrow();

        Optional<Article> optionalArticle = articleService.findById(id);

        //찾는 게시물이 없는 경우
        if(optionalArticle.isEmpty()){
            return RsData.of("F-1","%d번 게시물은 존재하지 않습니다.".formatted(id),null);
        }

        //수정을 할 수 있는지 체크
        RsData canModify = articleService.canModify(member,optionalArticle.get());

        if(canModify.isFail()){
            return canModify;
        }

        RsData<Article> modifyRs = articleService.modify(optionalArticle.get(),modifyRequest.getSubject(),modifyRequest.getContent());

        return RsData.of(modifyRs.getResultCode(),
                modifyRs.getMsg(),
                new ModifyResponse(modifyRs.getData()));

    }

}
