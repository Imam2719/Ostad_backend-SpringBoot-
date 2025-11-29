package com.example.Ostad_SpringBoot.Module_15.Repositories;

import com.example.Ostad_SpringBoot.Module_15.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
