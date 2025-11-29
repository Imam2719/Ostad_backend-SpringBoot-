package com.example.Ostad_SpringBoot.Module_15.Service;

import com.example.Ostad_SpringBoot.Module_15.Model.Book;
import com.example.Ostad_SpringBoot.Module_15.Repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book save(Book book) {
        return repo.save(book);
    }

    public List<Book> getAll() {
        return repo.findAll();
    }

    public Book getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Book update(Long id, Book book) {
        Book existing = repo.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setAvailableCopies(book.getAvailableCopies());

        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
