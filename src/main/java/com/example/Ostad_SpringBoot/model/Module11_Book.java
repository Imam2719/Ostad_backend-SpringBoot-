package com.example.Ostad_SpringBoot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "module11_books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module11_Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publication;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer availableCopies;

    // Optional: if you want to support genre search as per requirement #7
    private String genre;
}