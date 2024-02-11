package pietka.bartlomiej.myhomelibrary.openlibraryclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import pietka.bartlomiej.myhomelibrary.jsonobjects.AuthorJSON;
import pietka.bartlomiej.myhomelibrary.jsonobjects.BookJSON;
import pietka.bartlomiej.myhomelibrary.entity.Author;
import pietka.bartlomiej.myhomelibrary.entity.Book;
import pietka.bartlomiej.myhomelibrary.entity.Publisher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OpenLibraryClientImpl implements OpenLibraryClient {
    private String findByIsbnURL = "https://openlibrary.org/isbn/%s.json";
    private String findAuthorByKeyURL = "https://openlibrary.org/authors/%s.json";

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<Book> findBookInOLApiByIsbn(String isbn) {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url;
        Optional<Book> book = Optional.empty();
        Set<Author> authors = new HashSet<>();
        Set<Publisher> publishers = new HashSet<>();
        int releaseYear = 0;


        try {
            url = new URL(String.format(findByIsbnURL, isbn));
            BookJSON apiBookResponse = objectMapper.readValue(url, BookJSON.class);

            if(apiBookResponse.getAuthorList() != null) {
                List<String> authorKeysList = apiBookResponse.getAuthorList().stream()
                        .map(authorKey -> StringUtils.substringAfterLast(authorKey.getKey(), "/"))
                        .collect(Collectors.toList());

                authors = findAuthorsInOLApi(authorKeysList);
            }

            if(apiBookResponse.getReleaseDate() != null) {
                releaseYear = parseReleaseYear(apiBookResponse.getReleaseDate());
            }

            if(apiBookResponse.getPublishers() != null && !apiBookResponse.getPublishers().isEmpty()) {
                publishers = convertPublishersStringListToSet(apiBookResponse.getPublishers());
            }

            book = Optional.of(
                    Book.builder()
                            .isbn(isbn)
                            .title(apiBookResponse.getTitle())
                            .releaseYear(releaseYear)
                            .publishers(publishers)
                            .authors(authors)
                            .build()
            );

        } catch (FileNotFoundException e) {
            return book;
        } catch (Exception e){
            e.printStackTrace();
        }

        return book;
    }

    private Set<Publisher> convertPublishersStringListToSet(List<String> publisherNames) {
        Set<Publisher> publishers = new HashSet<>();
        publishers = publisherNames.stream()
                .map(publisherName -> Publisher.builder()
                        .name(publisherName)
                        .build())
                .collect(Collectors.toSet());

        return publishers;
    }

    private int parseReleaseYear(String releaseDate) {
        Pattern pattern = Pattern.compile(".*,(\\s*\\d{4})$");
        Matcher matcher = pattern.matcher(releaseDate);
        int releaseYear;

        if (matcher.find()) {
            String year = matcher.group(1);

            releaseYear = Integer.parseInt(year.trim());
        } else {
            releaseYear = Integer.parseInt(releaseDate);
        }
        return releaseYear;
    }

    private Set<Author> findAuthorsInOLApi (List<String> authorKeysList) {
        Set<Author> authors = new HashSet<>();
        Set<AuthorJSON> authorsJSON = new HashSet<>();

        authorKeysList.stream().forEach(key -> {
            Optional<AuthorJSON> author = Optional.empty();

            try {
                String authorOLApiString = String.format(findAuthorByKeyURL, key);
                URL authorApiURL = new URL(authorOLApiString);
                author = Optional.ofNullable(objectMapper.readValue(authorApiURL, AuthorJSON.class));
                author.ifPresent(authorsJSON::add);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if(!authorsJSON.isEmpty()) {
            authors = authorsJSON.stream().map(authorJSON -> Author.builder()
                            .fullName(authorJSON.getFullName())
                            .build())
                    .collect(Collectors.toSet());
        }

        return authors;
    }
}
