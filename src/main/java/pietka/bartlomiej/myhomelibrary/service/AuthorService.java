package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.stereotype.Service;
import pietka.bartlomiej.myhomelibrary.entity.Author;
import pietka.bartlomiej.myhomelibrary.entity.Book;

import java.util.Set;

public interface AuthorService {
    Set<Author> checkIfAuthorsAlreadySaved(Book book);
}
