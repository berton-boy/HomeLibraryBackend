package pietka.bartlomiej.myhomelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pietka.bartlomiej.myhomelibrary.entity.BookEvent;

import java.util.List;

@Repository
public interface BookEventRepository extends JpaRepository<BookEvent, Long> {
    List<BookEvent> findAllByBook_Id(Long bookId);
}
