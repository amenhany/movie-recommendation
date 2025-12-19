package org.testing.controller;

import org.junit.jupiter.api.Test;
import org.testing.io.LineReader;
import org.testing.io.MovieParser;
import org.testing.io.UserParser;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;
import org.testing.service.RecommendationEngine;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUpTest {
    //method to get resource path
    private Path getResourcePath(String fileName) throws Exception {
        return Path.of(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(fileName)).toURI());
    }

    // Test 1: LineReader + MovieParser
    // verifies that raw lines read from a movies file are correctly interpreted and converted into Movie objects.
    @Test
    void testLineReaderAndMovieParserIntegration() throws Exception {
        Path moviesFile = getResourcePath("movies_bottomUp.txt");

        LineReader lineReader = new LineReader();
        List<String> lines = lineReader.read(moviesFile.toString());
        MovieParser movieParser = new MovieParser();
        List<Movie> movies = movieParser.parse(lines);

        assertFalse(movies.isEmpty());

        assertEquals("Forrest Gump", movies.getFirst().title());
        assertEquals("FG123", movies.getFirst().id());
        assertTrue(movies.getFirst().genres().contains(Movie.Genre.DRAMA));
    }

    // Test 2: LineReader + UserParser
    // ensures that user information and their liked movie IDs are correctly parsed from a file
    @Test
    void testLineReaderAndUserParserIntegration() throws Exception {
        Path usersFile = getResourcePath("users_bottomUp.txt");

        LineReader lineReader = new LineReader();
        List<String> lines = lineReader.read(usersFile.toString());
        UserParser userParser = new UserParser();
        List<User> users = userParser.parse(lines);

        assertFalse(users.isEmpty());
        assertEquals("Karim", users.getFirst().name());
        assertEquals("12345678K", users.getFirst().id());
        assertTrue(users.getFirst().likedMovieIds().contains("FG123"));
        assertTrue(users.getFirst().likedMovieIds().contains("TP123"));
    }

    // Test 3: MovieParser + RecommendationEngine
    // verifies that movies parsed from text files are correctly consumed by the recommendation engine.
    @Test
    void testMovieParserAndRecommendationEngineIntegration() throws Exception  {
        Path usersFile = getResourcePath("users_bottomUp.txt");
        Path moviesFile = getResourcePath("movies_bottomUp.txt");

        LineReader lineReader = new LineReader();

        List<Movie> movies = new MovieParser().parse(lineReader.read(moviesFile.toString()));
        List<User> users = new UserParser().parse(lineReader.read(usersFile.toString()));

        RecommendationEngine engine = new RecommendationEngine();
        List<Recommendation> recommendations = engine.recommend(users, movies);

        // Example: Check first recommendation
        Recommendation firstRec = recommendations.getFirst();
        User user = firstRec.user();
        List<Movie> recMovies = firstRec.recommendedMovies();

        assertNotNull(user);
        assertNotNull(recMovies);
        assertFalse(recMovies.isEmpty());
    }

    // Test 4: UserParser + RecommendationEngine
    // ensures that liked movie IDs parsed from users are correctly matched with movies inside the recommendation engine
    @Test
    void testUserParserAndRecommendationEngineIntegration() throws Exception{
        Path moviesFile = getResourcePath("movies_bottomUp.txt");
        Path usersFile = getResourcePath("users_bottomUp.txt");

        LineReader lineReader = new LineReader();
        List<Movie> movies = new MovieParser().parse(lineReader.read(moviesFile.toString()));
        List<User> users = new UserParser().parse(lineReader.read(usersFile.toString()));

        RecommendationEngine engine = new RecommendationEngine();
        List<Recommendation> recommendations = engine.recommend(users, movies);

        assertFalse(recommendations.isEmpty());

        // Example check: pick a known user from users.txt
        Recommendation recForKarim = recommendations.stream()
                .filter(r -> r.user().name().equals("Karim"))
                .findFirst()
                .orElse(null);

       // RecommendationEngine successfully linked:UserParser output → Engine output
        assertNotNull(recForKarim);
        //Recommendation → Movie list
        List<Movie> recommendedMovies = recForKarim.recommendedMovies();

        assertFalse(recommendedMovies.isEmpty());
        // Make sure recommended movies are not already liked by Karim
        for (Movie m : recommendedMovies) {
            assertFalse(users.getFirst().likedMovieIds().contains(m.id()));
        }
}

}
