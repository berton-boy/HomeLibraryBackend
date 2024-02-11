package pietka.bartlomiej.myhomelibrary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book_authors")
@Data
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
