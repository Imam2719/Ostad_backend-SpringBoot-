package com.example.Ostad_SpringBoot.controller;

import com.example.Ostad_SpringBoot.Service.Module11_BookService;
import com.example.Ostad_SpringBoot.model.Module11_Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class Module11_BookController {

    @Autowired
    private Module11_BookService bookService;

    // 1. POST /api/books - Add a new book to the library
    @PostMapping
    public ResponseEntity<Module11_Book> addBook(@RequestBody Module11_Book book) {
        Module11_Book savedBook = bookService.addBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    // 2. GET /api/books - Get all books in the library
    @GetMapping
    public ResponseEntity<List<Module11_Book>> getAllBooks() {
        List<Module11_Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // 3. GET /api/books/{id} - Get a specific book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Module11_Book> getBookById(@PathVariable Long id) {
        Optional<Module11_Book> book = bookService.getBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. PUT /api/books/{id} - Update a specific book's information
    @PutMapping("/{id}")
    public ResponseEntity<Module11_Book> updateBook(@PathVariable Long id,
                                                    @RequestBody Module11_Book bookDetails) {
        Module11_Book updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook != null) {
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 5. DELETE /api/books/{id} - Remove a book from the library
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
    }

    // 6. GET /api/books/author/{authorName} - Find all books by a specific author
    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<Module11_Book>> getBooksByAuthor(@PathVariable String authorName) {
        List<Module11_Book> books = bookService.getBooksByAuthor(authorName);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // 7. GET /api/books/genre/{genre} - Find all books in a specific genre
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Module11_Book>> getBooksByGenre(@PathVariable String genre) {
        List<Module11_Book> books = bookService.getBooksByGenre(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // 8. GET /api/books/publication/{publication} - Get all books by publication
    @GetMapping("/publication/{publication}")
    public ResponseEntity<List<Module11_Book>> getBooksByPublication(@PathVariable String publication) {
        List<Module11_Book> books = bookService.getBooksByPublication(publication);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}