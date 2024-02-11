package pietka.bartlomiej.myhomelibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pietka.bartlomiej.myhomelibrary.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
