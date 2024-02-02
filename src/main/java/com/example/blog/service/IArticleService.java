package com.example.blog.service;

import com.example.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IArticleService {

    Optional<Article> findById(Integer id);
    Page<Article> findAll(Pageable pageable);
    Article save(Article article);
    void deleteById(Integer id);
    void update(Article article);
}
