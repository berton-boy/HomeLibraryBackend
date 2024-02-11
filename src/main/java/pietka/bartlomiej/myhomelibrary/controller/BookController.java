package pietka.bartlomiej.myhomelibrary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.BookEvent;
import pietka.bartlomiej.myhomelibrary.entity.BookEventType;
import pietka.bartlomiej.myhomelibrary.entity.User;
import pietka.bartlomiej.myhomelibrary.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
       Optional<Book> createdBook = bookService.createBook(book);

        return createdBook
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/{bookId}/addEvent")
    public ResponseEntity<Book> addEvent(@PathVariable(name = "bookId") Long bookId, @RequestBody BookEvent bookEvent) {
        Optional<Book> bookWithAddedEvent = bookService.addEvent(bookId, bookEvent);

        return bookWithAddedEvent
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/findBookInOLApiByIsbn/{isbn}")
    public Optional<Book> findBookInOLApiByIsbn(@PathVariable(name = "isbn") String isbn) {
        return bookService.findBookInOLApiByIsbn(isbn);
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "bookId") Long bookId) {
        Optional<Book> bookFound = bookService.getBookById(bookId);

        return bookFound
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable(name = "bookId") Long bookId, @RequestBody Book newBookData) {
        Optional<Book> updatedBook = bookService.updateBook(bookId, newBookData);

        return updatedBook
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity deleteBook(@PathVariable(name = "bookId") Long bookId) {
        boolean wasBookDeleted = bookService.deleteBook(bookId);

        return wasBookDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{bookId}/save-image")
    public boolean saveBookCover(@RequestParam("image") MultipartFile image, @PathVariable(name = "bookId") Long bookId) {

        return bookService.saveBookCover(image, bookId);
    }
}



