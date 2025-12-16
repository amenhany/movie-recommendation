package org.testing.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testing.io.LineReader;
import org.testing.io.MovieParser;
import org.testing.io.UserParser;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;
import org.testing.service.RecommendationEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUpTest {
    @TempDir
    Path tempDir;

    // Test 1: LineReader + MovieParser
    @Test
    void testLineReaderAndMovieParserIntegration() throws IOException {
        // Create a temporary movies file
        Path moviesFile = tempDir.resolve("movies.txt"); // resolve: combine tempDir and movie.txt path
        // simulate exact format of movie.txt
        List<String> fileContent = Arrays.asList(
                "The Matrix,TM123",
                "Action",
                "Inception,I456",
                "Thriller");
        // write a real file using real file system
        Files.write(moviesFile, fileContent);
        LineReader lineReader = new LineReader();
        List<String> lines = lineReader.read(moviesFile.toString());
        MovieParser movieParser = new MovieParser();
        List<Movie> movies = movieParser.parse(lines);

        assertEquals(2, movies.size());

        Movie m1 = movies.getFirst();

        assertEquals("The Matrix", m1.title());
        assertEquals("TM123", m1.id());
        // Verifies that parsing preserved data correctly.
        assertTrue(m1.genres().contains(Movie.Genre.ACTION));
        Movie m2 = movies.get(1);
        assertEquals("Inception", m2.title());
        assertEquals("I456", m2.id());
        assertTrue(m2.genres().contains(Movie.Genre.THRILLER));
    }

    // Test 2: LineReader + UserParser
    @Test
    void testLineReaderAndUserParserIntegration() throws IOException {
        // Create a temporary users file
        Path usersFile = tempDir.resolve("users.txt");
        // Simulate user file
        List<String> fileContent = Arrays.asList(
                "Alice Smith,12345678A",
                "TM123,I456",
                "Bob Jones,87654321B",
                "I456");
        Files.write(usersFile, fileContent);

        LineReader lineReader = new LineReader();
        List<String> lines = lineReader.read(usersFile.toString());
        UserParser userParser = new UserParser();
        List<User> users = userParser.parse(lines);

        assertEquals(2, users.size());
        User u1 = users.getFirst();
        assertEquals("Alice Smith", u1.name());
        assertEquals("12345678A", u1.id());
        assertTrue(u1.likedMovieIds().contains("TM123"));
        User u2 = users.get(1);
        assertEquals("Bob Jones", u2.name());
        assertEquals("87654321B", u2.id());
        assertTrue(u2.likedMovieIds().contains("I456"));
    }

    // Test 3: MovieParser + RecommendationEngine
    @Test
    void testMovieParserAndRecommendationEngineIntegration() {
        // Parse movies from simulated lines
        // in memory movie data
        List<String> movieLines = Arrays.asList(
                "The Matrix,TM123", "Action",
                "John Wick,JW789", "Action",
                "Notebook,N123", "Drama");
        MovieParser movieParser = new MovieParser();
        List<Movie> allMovies = movieParser.parse(movieLines);
        // Create a user who likes "The Matrix" (Action)
        User user = new User("Neo", "11111111A", List.of("TM123"));
        List<User> users = List.of(user);

        RecommendationEngine engine = new RecommendationEngine();
        List<Recommendation> recommendations = engine.recommend(users, allMovies);

        assertEquals(1, recommendations.size());
        Recommendation rec = recommendations.getFirst();

        // Should recommend "John Wick" because it's Action, but NOT "The Matrix"
        // (already liked)
        List<Movie> recommendedMovies = rec.recommendedMovies();
        assertEquals(1, recommendedMovies.size());
        // makes sure that same genre recommended and already-liked movie excluded
        assertEquals("John Wick", recommendedMovies.getFirst().title());
    }

    // Test 4: UserParser + RecommendationEngine
    @Test
    void testUserParserAndRecommendationEngineIntegration() {
        // Setup: Parse users from simulated lines
        List<String> userLines = Arrays.asList(
                "Alice,12345678A", "TM123");
        UserParser userParser = new UserParser();
        List<User> users = userParser.parse(userLines);
        // Create movies manually
        Movie matrix = new Movie("The Matrix", "TM123", List.of(Movie.Genre.ACTION));
        Movie johnWick = new Movie("John Wick", "JW789", List.of(Movie.Genre.ACTION));
        List<Movie> allMovies = List.of(matrix, johnWick);

        RecommendationEngine engine = new RecommendationEngine();
        List<Recommendation> recommendations = engine.recommend(users, allMovies);

        assertEquals(1, recommendations.size());
        Recommendation rec = recommendations.getFirst();
        // User likes TM123 (Action), so should get JW789 (Action)
        assertEquals("Alice", rec.user().name());
        assertEquals(1, rec.recommendedMovies().size());
        assertEquals("John Wick", rec.recommendedMovies().getFirst().title());
    }
}
