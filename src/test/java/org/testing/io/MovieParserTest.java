package org.testing.io;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testing.model.Movie;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieParserTest {
    private MovieParser parser;
    @BeforeEach
    void init() {
        parser = new MovieParser();
    }

    @Test
    public void testValidMovie1() throws Exception {
        // Example of a valid movie file lines (2 lines per movie)
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Fantasy,Adventure"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(1, movies.size()); // One movie parsed
        Movie movie = movies.get(0);
        assertEquals("Harry Potter", movie.getTitle());
        assertEquals("HP123", movie.getId());
        assertEquals(List.of("Fantasy", "Adventure"), movie.getGenres());
    }

    @Test
    public void testInvalidMovieTitle() {
        List<String> movieLines = List.of(
                "harry Potter,HP123",  // Invalid: first word lowercase
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> {
            parser.parse(movieLines);
        });

        assertEquals("ERROR: Movie Title harry Potter is wrong", exception.getMessage());
    }

    @Test
    public void testInvalidMovieIdLetters() {
        List<String> movieLines = List.of(
                "Harry Potter,HX123",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Movie Id letters HX123 are wrong", exception.getMessage());
    }

    @Test
    public void testInvalidMovieIdNumbers() {
        List<String> movieLines = List.of(
                "Harry Potter,HP112",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Movie Id numbers HP112 aren’t unique", exception.getMessage());
    }

    @Test
    public void testMoviesWithDifferentIdNumbersValid() throws Exception {
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Fantasy",
                "Avengers,A456",
                "Action,Adventure"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(2, movies.size());

        assertEquals("HP123", movies.get(0).getId());
        assertEquals("A456", movies.get(1).getId());
    }

    @Test
    public void testMoviesWithSameIdNumbersInvalid() {
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Fantasy",
                "Avengers,A123",   // Same numeric part 123 → invalid
                "Action,Adventure"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Movie Id numbers 123 aren’t unique", exception.getMessage());
    }

    @Test
    public void testValidMovieTitleWithNumber() throws Exception {
        List<String> movieLines = List.of(
                "Fast And Furious 8,FAF128",  // Title contains a number → valid
                "Action"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(1, movies.size());
        Movie movie = movies.get(0);
        assertEquals("Fast And Furious 8", movie.getTitle());
        assertEquals("FAF128", movie.getId());
        assertEquals(List.of("Action"), movie.getGenres());
    }
}
