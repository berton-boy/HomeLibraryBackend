package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.stereotype.Service;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.Publisher;

import java.util.Set;

public interface PublisherService {
    Set<Publisher> checkIfPublishersAlreadySaved(Book book);
}
