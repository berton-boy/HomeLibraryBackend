package pietka.bartlomiej.myhomelibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pietka.bartlomiej.myhomelibrary.repository.AuthorRepository;
import pietka.bartlomiej.myhomelibrary.entity.Author;
import pietka.bartlomiej.myhomelibrary.entity.Book;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Set<Author> checkIfAuthorsAlreadySaved(Book book) {
        return book.getAuthors().stream()
                .map(author -> authorRepository.findByFullName(author.getFullName())
                        .orElse(author)).collect(Collectors.toSet());
    }
}
