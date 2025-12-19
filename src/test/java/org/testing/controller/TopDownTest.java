package org.testing.controller;

import org.junit.jupiter.api.Test;
import org.testing.io.LineReader;
import org.testing.io.LineWriter;
import org.testing.io.MovieParser;
import org.testing.io.UserParser;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;
import org.testing.service.RecommendationEngine;

import java.util.List;

import static org.mockito.Mockito.*;

class TopDownTest {

    // Parsers + RecommendationEngine
    @Test
    void parsersAndEngineTest() throws Exception {
        LineReader reader = mock(LineReader.class);
        LineWriter writer = mock(LineWriter.class);

        MovieParser movieParser = new MovieParser();
        UserParser userParser = new UserParser();
        RecommendationEngine engine = new RecommendationEngine();

        when(reader.read("movies.txt")).thenReturn(List.of(
                "Movie One,MO123",
                "Action",
                "Movie Two,MT123",
                "Action"
        ));

        when(reader.read("users.txt")).thenReturn(List.of(
                "User One,12345678A",
                "MO123"
        ));

        Main main = new Main(
                reader,
                writer,
                movieParser,
                userParser,
                engine
        );

        main.run("movies.txt", "users.txt", "out.txt");

        // Parser parses the given strings to the following:
        User user = new User("User One", "12345678A", List.of("MO123"));
        Movie movie2 = new Movie("Movie Two", "MT123", List.of(Movie.Genre.ACTION));

        // Engine takes the parsed data and gives this output:
        verify(writer).write(eq("out.txt"), eq(List.of(new Recommendation(user, List.of(movie2)))));
    }

    // LineWriter writes first error gotten from MovieParser (before Genre/UserParser error)
    @Test
    void firstErrorWritten() throws Exception {
        LineReader reader = mock(LineReader.class);
        LineWriter writer = mock(LineWriter.class);

        MovieParser movieParser = new MovieParser();
        UserParser userParser = new UserParser();
        RecommendationEngine engine = new RecommendationEngine();

        when(reader.read("movies.txt")).thenReturn(List.of(
                "Movie One,MA123", // bad id letters
                "Actionn" // bad genre
        ));

        when(reader.read("users.txt")).thenReturn(List.of(
                "User One,1234567A", // bad id length
                "MA123"
        ));

        Main main = new Main(
                reader, writer,
                movieParser, userParser, engine
        );

        main.run("movies.txt", "users.txt", "out.txt");

        verify(writer).writeError(eq("out.txt"), eq("ERROR: Movie Id letters MA123 are wrong"));
    }
}