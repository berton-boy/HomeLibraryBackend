package pietka.bartlomiej.myhomelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pietka.bartlomiej.myhomelibrary.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullName(String fullName);
}
