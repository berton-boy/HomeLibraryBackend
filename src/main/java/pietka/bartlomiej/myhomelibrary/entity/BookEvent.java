package pietka.bartlomiej.myhomelibrary.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "book_event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = BookEvent.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private Date eventDate;

    private BookEventType eventType;
    private String description;
}
