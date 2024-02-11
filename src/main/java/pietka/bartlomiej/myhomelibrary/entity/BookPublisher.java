package pietka.bartlomiej.myhomelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_publishers")
@Data
public class BookPublisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
