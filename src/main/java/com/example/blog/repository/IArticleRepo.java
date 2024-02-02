package com.example.blog.repository;

import com.example.blog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IArticleRepo extends JpaRepository<Article, Integer> {
}
