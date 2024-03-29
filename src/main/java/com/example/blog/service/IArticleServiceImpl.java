package com.example.blog.service;

import com.example.blog.model.Article;
import com.example.blog.repository.IArticleRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IArticleServiceImpl implements IArticleService {

    private final IArticleRepo articleRepo;

    public IArticleServiceImpl(IArticleRepo articleRepo) {
        this.articleRepo = articleRepo;
    }

    @Override
    public Optional<Article> findById(Integer id) {
        return articleRepo.findById(id);
    }
    @Override
    public Page<Article> findAll(Pageable pageable) {
        return articleRepo.findAll(pageable);
    }

    @Override
    public List<Article> findArticles(Sort sort) {
        return articleRepo.findAll(sort);
    }

    @Override
    public List<Article> findByCategory(String category) {
        return articleRepo.findByCategory(category);
    }

    @Override
    public void save(Article article) {
        articleRepo.save(article);
    }

    @Override
    public void deleteById(Integer id) {
        articleRepo.deleteById(id);
    }

    @Override
    public void update(Article article) {
        articleRepo.save(article);
    }
}
