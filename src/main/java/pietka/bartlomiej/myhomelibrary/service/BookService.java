package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.BookEvent;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> createBook(Book book);

    Optional<Book> addEvent(Long bookId, BookEvent bookEvent);

    Optional<Book> findBookInOLApiByIsbn(String isbn);

    Optional<Book> getBookById(Long bookId);
    Optional<Book> updateBook(Long bookId, Book bookDetails);
    boolean deleteBook(Long bookId);
    boolean saveBookCover(MultipartFile image, Long id);

}