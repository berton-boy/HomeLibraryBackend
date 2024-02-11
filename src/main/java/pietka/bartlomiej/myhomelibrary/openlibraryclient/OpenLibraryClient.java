package pietka.bartlomiej.myhomelibrary.openlibraryclient;

import pietka.bartlomiej.myhomelibrary.entity.Book;

import java.util.Optional;

public interface OpenLibraryClient {
    Optional<Book> findBookInOLApiByIsbn(String isbn);
}
