package pietka.bartlomiej.myhomelibrary.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class)
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<Book> books = new HashSet<>();


    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}

