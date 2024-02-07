package com.example.blog.service;

import com.example.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IArticleService {

    Optional<Article> findById(Integer id);
    Page<Article> findAll(Pageable pageable);
    List<Article> findArticles(Sort sort);
    Article save(Article article);
    void deleteById(Integer id);
    void update(Article article);
}
