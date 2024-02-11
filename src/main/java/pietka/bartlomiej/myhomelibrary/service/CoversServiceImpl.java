package pietka.bartlomiej.myhomelibrary.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pietka.bartlomiej.myhomelibrary.repository.BookEventRepository;
import pietka.bartlomiej.myhomelibrary.repository.BookRepository;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.BookEvent;
import pietka.bartlomiej.myhomelibrary.entity.BookEventType;
import pietka.bartlomiej.myhomelibrary.entity.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
public class CoversServiceImpl implements CoversService {

    @Value("${file.upload.path}")
    private String uploadPath;

    private final String SERVER_BASE_URL = "https://localhost:443/";

    private final BookRepository bookRepository;

    private final BookEventRepository bookEventRepository;

    @Autowired
    public CoversServiceImpl(BookRepository bookRepository, BookEventRepository bookEventRepository) {
        this.bookRepository = bookRepository;
        this.bookEventRepository = bookEventRepository;
    }


    private String saveBigCover(BufferedImage bigCover, Book book, Long userId) throws IOException {
        File bigImageFile = new File(uploadPath + "/" + userId + "-" + book.getIsbn() +"-L.jpg");
        ImageIO.write(bigCover, "JPG", bigImageFile);

        return SERVER_BASE_URL + bigImageFile.getName();
    }

    private String saveSmallCover(BufferedImage bigCover, Book book, Long userId) throws IOException {

        BufferedImage thumbnail =
                Thumbnails.of(bigCover)
                        .height(58)
                        .width(37)
                        .outputFormat("JPG")
                        .asBufferedImage();

        File smallImageFile = new File(uploadPath + "/" + userId + "-" + book.getIsbn() +"-S.jpg");
        ImageIO.write(thumbnail, "jpg", smallImageFile);

        return SERVER_BASE_URL + smallImageFile.getName();
    }

    @Override
    public boolean uploadCover(MultipartFile cover, Long id) {
        Optional<Book> book = bookRepository.findById(id);
        Book existingBook = book.get();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId().longValue();

        try {
            BufferedImage originalImage = ImageIO.read(cover.getInputStream());

            String coverURL = saveBigCover(originalImage, existingBook, userId);
            existingBook.setBigCoverUrl(coverURL);

            coverURL = saveSmallCover(originalImage, existingBook, userId);
            existingBook.setSmallCoverUrl(coverURL);



            BookEvent bookEvent = BookEvent.builder()
                    .eventType(BookEventType.DODANIE_OKLADKI)
                    .book(existingBook)
                    .description(BookEventType.DODANIE_OKLADKI.getValue())
                    .eventDate(new Date())
                    .build();


            existingBook.getEventList().add(bookEventRepository.save(bookEvent));

            bookRepository.save(existingBook);

        } catch (IOException e) {
            throw new RuntimeException("Wystąpił błąd podczas zapisywania okładki. Prosimy o kontakt z administratorem.");
        }

        return false;
    }
}
