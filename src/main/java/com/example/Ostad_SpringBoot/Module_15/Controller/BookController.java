package com.example.Ostad_SpringBoot.Module_15.Controller;

import com.example.Ostad_SpringBoot.Module_15.Model.Book;
import com.example.Ostad_SpringBoot.Module_15.Service.BookService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // Create book (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Book create(@RequestBody Book book) {
        return service.save(book);
    }

    // Get all books (ADMIN / USER)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<Book> getAll() {
        return service.getAll();
    }

    // Get book by ID (ADMIN / USER)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Book getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Update book (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Book update(@PathVariable Long id, @RequestBody Book book) {
        return service.update(id, book);
    }

    // Delete (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
