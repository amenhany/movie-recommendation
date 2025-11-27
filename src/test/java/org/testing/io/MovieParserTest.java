package org.testing.io;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testing.model.Movie;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MovieParserTest {
    private MovieParser parser;
    @BeforeEach
    void init() {
        parser = new MovieParser();
    }

    @Test
    public void testValidMovie1() {
        // Example of a valid movie file lines (2 lines per movie)
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Fantasy,Adventure"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(1, movies.size()); // One movie parsed
        Movie movie = movies.getFirst();
        assertEquals("Harry Potter", movie.title());
        assertEquals("HP123", movie.id());
        assertEquals(List.of(Movie.Genre.FANTASY, Movie.Genre.ADVENTURE), movie.genres());
    }

    @Test
    public void testInvalidMovieTitleCasing() {
        List<String> movieLines = List.of(
                "harry Potter,HP123",  // Invalid: first word lowercase
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Movie Title harry Potter is wrong", exception.getMessage());
    }

    @Test
    public void testEmptyMovieTitle() {
        List<String> movieLines = List.of(
                ",HP123",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Movie Title cannot be null or blank", exception.getMessage());
    }

    @Test
    public void testEmptyMovieLine() {
        List<String> movieLines = List.of(
                "",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Invalid title, id line", exception.getMessage());
    }

    @Test
    public void testEmptyMovieId() {
        List<String> movieLines = List.of(
                "Avengers, ",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Movie Id cannot be null or blank", exception.getMessage());
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
    public void testInvalidMovieIdDuplicateNumbers() {
        List<String> movieLines = List.of(
                "Harry Potter,HP112",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Movie Id numbers HP112 aren't unique", exception.getMessage());
    }

    @Test
    public void testInvalidMovieIdNumberLength() {
        List<String> movieLines = List.of(
                "Harry Potter,HP12",
                "Fantasy"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Movie Id digits HP12 must be exactly 3 digits", exception.getMessage());
    }

    @Test
    public void testMoviesWithDifferentIdNumbersValid() {
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Fantasy",
                "Avengers,A456",
                "Action,Adventure"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(2, movies.size());

        assertEquals("HP123", movies.get(0).id());
        assertEquals("A456", movies.get(1).id());
    }

    @Test
    public void testMoviesWithSameIdNumbersInvalid() {
        List<String> movieLines = List.of(
                "Aliens,A123",
                "Fantasy",
                "Avengers,A123",   // Same numeric part 123 → invalid
                "Action,Adventure"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));

        assertEquals("ERROR: Movie Id numbers A123 aren't unique", exception.getMessage());
    }

    @Test
    public void testInvalidMovieGenre() {
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                "Idiot"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Invalid genre IDIOT", exception.getMessage());
    }

    @Test
    public void testEmptyMovieGenre() {
        List<String> movieLines = List.of(
                "Harry Potter,HP123",
                ""
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(movieLines));
        assertEquals("ERROR: Invalid genre ", exception.getMessage());
    }

    @Test
    public void testValidMovieTitleWithNumber() {
        List<String> movieLines = List.of(
                "Fast And Furious,FAF128",  // Title contains a number → valid
                "Action"
        );

        List<Movie> movies = parser.parse(movieLines);

        assertEquals(1, movies.size());
        Movie movie = movies.getFirst();
        assertEquals("Fast And Furious", movie.title());
        assertEquals("FAF128", movie.id());
        assertEquals(List.of(Movie.Genre.ACTION), movie.genres());
    }

}
