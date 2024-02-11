package pietka.bartlomiej.myhomelibrary.jsonobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorKey {
    @JsonProperty("key")
    private String key;
}
