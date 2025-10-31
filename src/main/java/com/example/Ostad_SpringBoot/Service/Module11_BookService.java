package com.example.Ostad_SpringBoot.Service;

import com.example.Ostad_SpringBoot.model.Module11_Book;
import com.example.Ostad_SpringBoot.Repository.Module11_BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Module11_BookService {

    @Autowired
    private Module11_BookRepository bookRepository;

    // Add a new book
    public Module11_Book addBook(Module11_Book book) {
        return bookRepository.save(book);
    }

    // Get all books
    public List<Module11_Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a specific book by ID
    public Optional<Module11_Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Update a book
    public Module11_Book updateBook(Long id, Module11_Book bookDetails) {
        Optional<Module11_Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Module11_Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPublication(bookDetails.getPublication());
            book.setPublicationYear(bookDetails.getPublicationYear());
            book.setAvailableCopies(bookDetails.getAvailableCopies());
            if (bookDetails.getGenre() != null) {
                book.setGenre(bookDetails.getGenre());
            }
            return bookRepository.save(book);
        }
        return null;
    }

    // Delete a book
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Find books by author
    public List<Module11_Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    // Find books by genre
    public List<Module11_Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    // Find books by publication
    public List<Module11_Book> getBooksByPublication(String publication) {
        return bookRepository.findByPublication(publication);
    }
}