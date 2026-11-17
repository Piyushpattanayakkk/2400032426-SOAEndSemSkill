package soa.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import soa.model.Book;
import soa.repo.BookRepo;

@Service
public class BookService {

    BookRepo BR;

    public BookService(BookRepo BR) {
        this.BR = BR;
    }

    public Object addBook(Book B) {

        Map<String, Object> response = new HashMap<>();

        try {
            BR.save(B);

            response.put("status", "success");
            response.put("message", "New book has been added");

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object getallbooks() {

        Map<String, Object> response = new HashMap<>();

        try {
            response.put("status", "success");
            response.put("books", BR.findAll());

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object getBookById(long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            Book book = BR.findById(id).orElseThrow();

            response.put("status", "success");
            response.put("book", book);

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object updateBook(Long id, Book B) {

        Map<String, Object> response = new HashMap<>();

        try {
            Book existing = BR.findById(id).orElseThrow();

            existing.setTitle(B.getTitle());
            existing.setAuthor(B.getAuthor());
            existing.setIsbn(B.getIsbn());
            BR.save(existing);

            response.put("status", "success");
            response.put("message", "Updated successfully");

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }

    public Object deleteBook(Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            BR.deleteById(id);

            response.put("status", "success");
            response.put("message", "Deleted successfully");

        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }
}