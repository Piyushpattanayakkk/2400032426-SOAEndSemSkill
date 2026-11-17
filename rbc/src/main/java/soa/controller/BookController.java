package soa.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soa.model.Book;
import soa.service.BookService;

@RestController
@RequestMapping("/rbc/books")
public class BookController {

    BookService BS;

    public BookController(BookService BS) {
        this.BS = BS;
    }

    @PostMapping
    public Object addBook(@RequestBody Book B) {
        return BS.addBook(B);
    }

    @GetMapping
    public Object getAllBooks() {
        return BS.getallbooks();
    }

    @GetMapping("/{id}")
    public Object getBookById(@PathVariable long id) {
        return BS.getBookById(id);
    }

    @PutMapping("/{id}")
    public Object updateBook(@PathVariable Long id, @RequestBody Book B) {
        return BS.updateBook(id, B);
    }

    @DeleteMapping("/{id}")
    public Object deleteBook(@PathVariable Long id) {
        return BS.deleteBook(id);
    }
}