package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String users(@RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "9") int size,
                        Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAll(pageable);
        model.addAttribute("users", users);
        model.addAttribute("usersNumber", users.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        return "user/users";
    }

    @GetMapping("/create")
    public String create(){
        return "user/create";
    }

    @PostMapping("/save")
    public String save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/user/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id")int id){
        userService.deleteById(id);
        return "redirect:/user/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id")int id,
                       Model model){
        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            model.addAttribute("user", optionalUser.get());
        }
        return "user/edit";
    }

    @PostMapping("/update")
    public String update(User user){

        Optional<User> optionalUser = userService.findById(user.getId());
        if(!optionalUser.get().getPassword().equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(optionalUser.get().getPassword());
        }

        userService.update(user);
        return "redirect:/user/users";
    }

}
