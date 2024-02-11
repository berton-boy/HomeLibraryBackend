package pietka.bartlomiej.myhomelibrary.jsonobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookJSON implements Serializable {
    @JsonProperty("title")
    private String title;
    @JsonProperty("publishers")
    private List<String> publishers;
    @JsonProperty("publish_date")
    private String releaseDate;

    @JsonProperty("authors")
    private List<AuthorKey> authorList;

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", publishers='" + publishers + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @JsonProperty("covers")
    private String[] coverId;
}
