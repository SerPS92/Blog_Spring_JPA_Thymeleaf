package com.example.blog.controller;

import com.example.blog.model.Article;
import com.example.blog.model.User;
import com.example.blog.service.IArticleService;
import com.example.blog.service.IUserService;
import com.example.blog.service.UploadFileService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final UploadFileService uploadFileService;
    private final IArticleService articleService;
    private final IUserService userService;

    private final Logger log = LoggerFactory.getLogger(ArticleController.class);

    public ArticleController(UploadFileService uploadFileService,
                             IArticleService articleService,
                             IUserService userService) {
        this.uploadFileService = uploadFileService;
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create() {
        return "article/create";
    }

    @GetMapping("/articles")
    public String articles(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "9") int size,
                           Model model){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Article> articles = articleService.findAll(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("articlesNumber", articles.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articles.getTotalPages());
        return "article/articles";
    }

    @PostMapping("/save")
    public String save(Article article,
                       @RequestParam("img") MultipartFile file,
                       HttpSession session) throws IOException {
        String nameImage = uploadFileService.saveImage(file);
        Date currentDate = new Date();

        int idUser = (int) session.getAttribute("idUser");
        Optional<User> optionalUser = userService.findById(idUser);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            article.setUser(user);
        }

        article.setImage(nameImage);
        article.setDate(currentDate);

        log.info("Title: {}", article.getTitle());
        log.info("Subtitle: {}", article.getSubtitle());
        log.info("DATE: {}", article.getDate());
        log.info("Id: {}", session.getAttribute("idUser") );
        articleService.save(article);
        return "redirect:/article/articles";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id")int id,
                       Model model){
        Optional<Article> optionalArticle = articleService.findById(id);
        Article article = optionalArticle.orElse(null);
        model.addAttribute("article", article);
        return "article/edit";
    }

    @PostMapping("/update")
    public String update(Article article,
                         @RequestParam("img") MultipartFile file) throws IOException{
        int id = article.getId();
        Date currentDate = new Date();
        Optional<Article> optionalArticle = articleService.findById(id);
        if(!file.isEmpty()){
            String nameImage = uploadFileService.saveImage(file);
            article.setImage(nameImage);
        }else{
            article.setImage(optionalArticle.get().getImage());
        }
        article.setDate(currentDate);
        articleService.update(article);
        return "redirect:/article/articles";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id")int id){
        Optional<Article> optionalArticle = articleService.findById(id);
        if(!optionalArticle.get().getImage().equals("default.jpg")){
            uploadFileService.deleteImage(optionalArticle.get().getImage());
        }
        articleService.deleteById(id);
        return "redirect:/article/articles";
    }


}
