package com.example.demo.boundedContext.article.service;

import com.example.demo.base.rsData.RsData;
import com.example.demo.boundedContext.article.entity.Article;
import com.example.demo.boundedContext.article.repository.ArticleRepository;
import com.example.demo.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    //등록
    public RsData<Article> write(Member author,String subject,String content){
        Article article = Article.build(author,subject,content);

        this.articleRepository.save(article);

        return RsData.of("S-1","게시물 생성", article);
    }

    public List<Article> findAll(){
        return articleRepository.findAllByOrderByIdDesc();
    }

    public Optional<Article> findById(Long id){
        return articleRepository.findById(id);
    }

    public RsData<Article> canDelete(Member actor,Article article){
        if(Objects.equals(actor.getId(),article.getMember().getId())){
            return RsData.of("S-1","삭제가 가능합니다.");
        }

        return RsData.of("F-1","삭제권한이 없습니다.");
    }

    //삭제
    public void delete(Article article){
        articleRepository.delete(article);
    }

    public void modify(Article article,String subject,String content){
        article.modify(article,subject,content);
        articleRepository.save(article);

    }
    
    //수정 할수 있는지 체크
    public RsData<Article> canModify(Member actor,Article article){
        if(Objects.equals(actor.getId(),article.getMember().getId())){
            return RsData.of("S-1",
                    "게시물 수정이 가능합니다");
        }
        return RsData.of("F-1","게시물 수정 권한이 없습니다.");
    }

}
