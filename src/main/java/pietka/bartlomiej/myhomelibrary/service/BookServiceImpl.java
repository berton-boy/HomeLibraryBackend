package pietka.bartlomiej.myhomelibrary.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pietka.bartlomiej.myhomelibrary.openlibraryclient.OpenLibraryClient;
import pietka.bartlomiej.myhomelibrary.repository.AuthorRepository;
import pietka.bartlomiej.myhomelibrary.repository.BookEventRepository;
import pietka.bartlomiej.myhomelibrary.repository.BookRepository;
import pietka.bartlomiej.myhomelibrary.repository.PublisherRepository;
import pietka.bartlomiej.myhomelibrary.entity.*;

import java.util.*;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final OpenLibraryClient openLibraryClient;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    private final CoversService coversService;

    private final BookEventRepository bookEventRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, OpenLibraryClient openLibraryClient,
                           AuthorService authorService, PublisherService publisherService,
                           AuthorRepository authorRepository, PublisherRepository publisherRepository,
                           CoversService coversService, BookEventRepository bookEventRepository) {
        this.bookRepository = bookRepository;
        this.openLibraryClient = openLibraryClient;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.coversService = coversService;
        this.bookEventRepository = bookEventRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAllByUser_Id(getAuthenticatedUserId());
    }

    @Transactional
    @Override
    public Optional<Book> createBook(Book book) {
        Optional<Book> bookAdded = Optional.empty();
        if (!checkIfBookAlreadyExistsByISBN(book.getIsbn())) {
            Set<Author> authorsToSave = authorService.checkIfAuthorsAlreadySaved(book);
            Set<Publisher> publishersToSave = publisherService.checkIfPublishersAlreadySaved(book);
            book.setPublishers(publishersToSave);
            book.setAuthors(authorsToSave);

            String coversUrl = "https://covers.openlibrary.org/b/isbn/%s-%s.jpg";
            book.setSmallCoverUrl(String.format(coversUrl, book.getIsbn(), "S"));
            book.setBigCoverUrl(String.format(coversUrl, book.getIsbn(), "L"));
            book.setUser(getAuthenticatedUser());


            book.getEventList().add(BookEvent.builder()
                    .eventType(BookEventType.DODANIE_KSIAZKI)
                    .book(book)
                    .description(BookEventType.DODANIE_KSIAZKI.getValue())
                    .eventDate(new Date())
                    .build());

            bookAdded = Optional.of(bookRepository.save(book));

        }
        return bookAdded;
    }

    @Override
    public Optional<Book> findBookInOLApiByIsbn(String isbn) {
        return openLibraryClient.findBookInOLApiByIsbn(isbn);
    }

    @Override
    public Optional<Book> getBookById(Long bookId) {
        return bookRepository.findBookByIdAndUser_Id(bookId, getAuthenticatedUserId());
    }

    @Transactional
    @Override
    public Optional<Book> updateBook(Long bookId, Book bookDetails) {
        Optional<Book> existingBook = bookRepository.findBookByIdAndUser_Id(bookId, getAuthenticatedUserId());
        Book updatedBook = null;

        if (existingBook.isPresent()) {
            updatedBook = existingBook.get();
            updatedBook.setTitle(bookDetails.getTitle());
            updatedBook.setIsbn(bookDetails.getIsbn());
            updatedBook.setReleaseYear(bookDetails.getReleaseYear());
            updatedBook.setAuthors(authorService.checkIfAuthorsAlreadySaved(bookDetails));
            updatedBook.setPublishers(publisherService.checkIfPublishersAlreadySaved(bookDetails));

            Optional.ofNullable(bookDetails.getEventList())
                    .ifPresent(updatedBook::setEventList);

            BookEvent bookEvent = BookEvent.builder()
                    .eventType(BookEventType.EDYCJA_KSIAZKI)
                    .book(updatedBook)
                    .description(BookEventType.EDYCJA_KSIAZKI.getValue())
                    .eventDate(new Date())
                    .build();


            updatedBook.getEventList().add(bookEventRepository.save(bookEvent));

            bookRepository.save(updatedBook);


        }

        return existingBook.isPresent() ? Optional.of(updatedBook) : Optional.empty();

    }

    @Override
    public Optional<Book> addEvent(Long bookId, BookEvent bookEvent) {
        Optional<Book> existingBook = bookRepository.findBookByIdAndUser_Id(bookId, getAuthenticatedUserId());

        if (existingBook.isPresent()) {
            Set<BookEvent> bookEventSet = existingBook.get().getEventList();
            List<BookEvent> bookEventList = new ArrayList<>(bookEventSet);
            bookEventList.sort(Comparator.comparing(BookEvent::getEventDate));

            if ("P".equals(bookEventList.get(bookEventList.size() - 1).getEventType().getKey())
                    && "P".equals(bookEvent.getEventType().getKey())) {
                existingBook = Optional.empty();
            } else {
                String eventDescription = bookEvent.getEventType().getValue();
                bookEvent.setDescription(eventDescription + ": " + bookEvent.getDescription());
                bookEvent.setBook(existingBook.get());
                existingBook.get().getEventList().add(bookEvent);
                bookRepository.save(existingBook.get());
            }
        }

        return existingBook;
    }

    @Transactional
    @Override
    public boolean deleteBook(Long bookId) {
        boolean wasBookDeleted = false;
        Optional<Book> foundBook = bookRepository.findBookByIdAndUser_Id(bookId, getAuthenticatedUserId());

        if (foundBook.isPresent()) {
            Set<Author> authors = foundBook.get().getAuthors();
            Set<Publisher> publishers = foundBook.get().getPublishers();
            bookRepository.deleteById(bookId);
            wasBookDeleted = true;

            for (Author author : authors) {
                if (author.getBooks().isEmpty()) {
                    authorRepository.delete(author);
                }
            }

            for (Publisher publisher : publishers) {
                if (publisher.getBooks().isEmpty()) {
                    publisherRepository.delete(publisher);
                }
            }

        }
        return wasBookDeleted;
    }

    public boolean checkIfBookAlreadyExistsByISBN(String isbn) {
        Optional<Book> existingBook = bookRepository.findBookByIsbnAndUser_Id(isbn, getAuthenticatedUserId());
        return existingBook.isPresent();
    }

    @Override
    public boolean saveBookCover(MultipartFile image, Long id) {
        return coversService.uploadCover(image, id);
    }

    private Long getAuthenticatedUserId() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getId().longValue();
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
