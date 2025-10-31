package com.example.Ostad_SpringBoot.Repository;

import com.example.Ostad_SpringBoot.model.Module11_Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Module11_BookRepository extends JpaRepository<Module11_Book, Long> {

    // Custom query methods for search functionalities
    List<Module11_Book> findByAuthor(String author);

    List<Module11_Book> findByGenre(String genre);

    List<Module11_Book> findByPublication(String publication);
}