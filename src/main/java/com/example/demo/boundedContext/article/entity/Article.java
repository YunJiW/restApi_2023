package com.example.demo.boundedContext.article.entity;


import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {


    @ManyToOne
    private Member member;
    private String subject;
    private String content;


    public static Article build(Member author,String subject,String content){
        return Article.builder()
                .member(author)
                .subject(subject)
                .content(content)
                .build();
    }

    public Article modify(Article article,String subject,String content){
        article.subject = subject;
        article.content = content;

        return article;
    }
}
