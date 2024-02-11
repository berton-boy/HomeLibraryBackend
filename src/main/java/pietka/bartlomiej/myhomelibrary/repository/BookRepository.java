package pietka.bartlomiej.myhomelibrary.repository;

import pietka.bartlomiej.myhomelibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitle(String title);
    Optional<Book> findBookByIsbnAndUser_Id(String isbn, Long userId);

    Optional<Book> findBookByIdAndUser_Id(Long bookId, Long userId);

    List<Book> findAllByUser_Id(Long userId);
}
