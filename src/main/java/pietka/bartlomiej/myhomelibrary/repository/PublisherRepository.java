package pietka.bartlomiej.myhomelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pietka.bartlomiej.myhomelibrary.entity.Publisher;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByName(String name);
}
