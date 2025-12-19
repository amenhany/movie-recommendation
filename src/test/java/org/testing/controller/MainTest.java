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

class MainTest {

    // All methods called by controller in the correct order
    @Test
    void allModulesCalledInOrder() throws Exception {
        LineReader reader = mock(LineReader.class);
        LineWriter writer = mock(LineWriter.class);
        MovieParser movieParser = mock(MovieParser.class);
        UserParser userParser = mock(UserParser.class);
        RecommendationEngine engine = mock(RecommendationEngine.class);

        List<String> movieLines = List.of("Movie A,MA123", "Action");
        List<String> userLines = List.of("User One,12345678A", "MA123");

        List<Movie> movies = List.of(mock(Movie.class));
        List<User> users = List.of(mock(User.class));
        List<Recommendation> recs = List.of(mock(Recommendation.class));

        when(reader.read("movies.txt")).thenReturn(movieLines);
        when(reader.read("users.txt")).thenReturn(userLines);
        when(movieParser.parse(movieLines)).thenReturn(movies);
        when(userParser.parse(userLines)).thenReturn(users);
        when(engine.recommend(users, movies)).thenReturn(recs);

        Main main = new Main(reader, writer, movieParser, userParser, engine);
        main.run("movies.txt", "users.txt", "out.txt");

        verify(reader).read("movies.txt");
        verify(reader).read("users.txt");
        verify(movieParser).parse(movieLines);
        verify(userParser).parse(userLines);
        verify(engine).recommend(users, movies);
        verify(writer).write("out.txt", recs);
    }

    // Main catches MovieParser IllegalArgumentException, gives it to LineWriter
    @Test
    void parserErrorWritingTest() throws Exception {
        LineReader reader = mock(LineReader.class);
        LineWriter writer = mock(LineWriter.class);
        MovieParser movieParser = mock(MovieParser.class);
        UserParser userParser = mock(UserParser.class);
        RecommendationEngine engine = mock(RecommendationEngine.class);

        when(reader.read(anyString())).thenReturn(List.of("BAD DATA"));
        when(movieParser.parse(any())).thenThrow(
                new IllegalArgumentException("ERROR: Movie Id Wrong")
        );

        Main main = new Main(reader, writer, movieParser, userParser, engine);
        main.run("movies.txt", "users.txt", "out.txt");

        verify(writer).writeError("out.txt", "ERROR: Movie Id Wrong");
        verify(writer, never()).write(anyString(), any());
    }

    // Main catches LineReader IOException, gives it to writeError
    @Test
    void readerErrorWritingTest() throws Exception {
        LineReader reader = mock(LineReader.class);
        LineWriter writer = mock(LineWriter.class);

        when(reader.read(anyString()))
                .thenThrow(new RuntimeException("File not found"));

        Main main = new Main(reader, writer,
                mock(MovieParser.class),
                mock(UserParser.class),
                mock(RecommendationEngine.class));

        main.run("movies.txt", "users.txt", "out.txt");

        verify(writer).writeError("out.txt", "File not found");
    }
}