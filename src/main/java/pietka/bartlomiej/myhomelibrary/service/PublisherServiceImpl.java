package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pietka.bartlomiej.myhomelibrary.repository.PublisherRepository;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.Publisher;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Set<Publisher> checkIfPublishersAlreadySaved(Book book) {
        return book.getPublishers().stream()
                .map(publisher -> publisherRepository.findByName(publisher.getName())
                        .orElse(publisher)).collect(Collectors.toSet());
    }
}
