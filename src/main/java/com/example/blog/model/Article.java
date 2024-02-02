package com.example.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "articles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String subtitle;
    private String text;
    private Date date;
    private String category;

    @ManyToOne
    private User user;



}
