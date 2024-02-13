package com.example.blog.controller;

import com.example.blog.model.Article;
import com.example.blog.service.IArticleService;
import com.example.blog.service.MailSenderService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final IArticleService articleService;
    private final MailSenderService mailSenderService;

    public HomeController(IArticleService articleService,
                          MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       HttpSession session,
                       Model model) {
        Date currentDate = new Date();
        session.setAttribute("currentDate", currentDate);
        session.setAttribute("position", "home");

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

    @GetMapping("/blog")
    public String blog(@RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       HttpSession session,
                       Model model){
        session.setAttribute("position", "blog");
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Article> articles = articleService.findAll(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("currentPage", page);
        model.addAttribute("articlesNumber", articles.getTotalElements());
        model.addAttribute("totalPages", articles.getTotalPages());
        return "blog";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable(name = "id")int id,
                       Model model){
        Optional<Article> optionalArticle = articleService.findById(id);
        if(optionalArticle.isPresent()){
            Article article = optionalArticle.get();
            model.addAttribute("article", article);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Article> allArticles = articleService.findArticles(sort);
        model.addAttribute("allArticles", allArticles);

        return "article";
    }

    @GetMapping("/contact")
    public String contact(HttpSession session){
        session.setAttribute("position", "contact");
        return "contact";
    }

    @PostMapping("/send")
    public String contact(@RequestParam(name = "name") String name,
                          @RequestParam(name = "email")String email,
                          @RequestParam(name = "subject")String subject,
                          @RequestParam(name = "text")String text){
        String to = "yourEmail";
        String content = "Name: " + name + "\n" + "Email: " + email + "\n" + "\n" + text;
        mailSenderService.sendEmail(to, subject, content);
        return "redirect:/home/";
    }
}
