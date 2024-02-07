package com.example.blog.controller;

import com.example.blog.model.Article;
import com.example.blog.service.IArticleService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final IArticleService articleService;

    public HomeController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "4") int size,
                       HttpSession session,
                       Model model) {
        Date currentDate = new Date();
        session.setAttribute("currentDate", currentDate);

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Article> allArticles = articleService.findArticles(sort);

        if (!allArticles.isEmpty()) {
            Article latestArticle = allArticles.get(allArticles.size() - 1);
            model.addAttribute("latestArticle", latestArticle);
        }
        if (allArticles.size() >= 5) {
            List<Article> homeArticles = allArticles.subList(allArticles.size() - 5, allArticles.size() - 1);
            model.addAttribute("homeArticles", homeArticles);
        }

        Sort sort1 = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort1);
        Page<Article> articles = articleService.findAll(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("articlesNumber", articles.getTotalElements());
        model.addAttribute("totalPages", articles.getTotalPages());

        return "index";
    }
}
