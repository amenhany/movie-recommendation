package org.testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecommendationEngineTest {

    private RecommendationEngine engine;

    @BeforeEach
    public void setup() {
        engine = new RecommendationEngine();
    }

    @Test
    public void testValidActionRecommendation() {
        // Movies
        Movie action1 = new Movie("Fast And Furious", "FAF123", List.of(Movie.Genre.ACTION));
        Movie action2 = new Movie("John Wick", "JW456", List.of(Movie.Genre.ACTION));
        Movie horror1 = new Movie("The Nun", "TN789", List.of(Movie.Genre.HORROR));
        Movie horror2 = new Movie("Scream", "S124", List.of(Movie.Genre.HORROR));

        // User
        User user1 = new User("Ahmed Ali", "12345678A", List.of("FAF123"));

        List<Recommendation> recs = engine.recommend(List.of(user1), List.of(action1, action2, horror1, horror2));
        Recommendation r = recs.getFirst();

        assertEquals("Ahmed Ali", r.user().name());
        assertEquals("12345678A", r.user().id());
        assertEquals(List.of(action2), r.recommendedMovies());
    }

    @Test
    public void testEmptyRecommendation() {
        // Movies
        Movie action1 = new Movie("Fast And Furious", "FAF123", List.of(Movie.Genre.ACTION));
        Movie horror1 = new Movie("The Nun", "TN789", List.of(Movie.Genre.HORROR));
        Movie horror2 = new Movie("Scream", "S124", List.of(Movie.Genre.HORROR));

        // User
        User user1 = new User("Ahmed Ali", "12345678A", List.of("FAF123"));

        List<Recommendation> recs = engine.recommend(List.of(user1), List.of(action1,horror1, horror2));
        Recommendation r = recs.getFirst();

        assertEquals("Ahmed Ali", r.user().name());
        assertEquals("12345678A", r.user().id());
        assertTrue(r.recommendedMovies().isEmpty());
    }

    @Test
    public void testMultipleUsers() {
        Movie action1 = new Movie("Fast And Furious", "FAF123", List.of(Movie.Genre.ACTION));
        Movie action2 = new Movie("John Wick", "JW456", List.of(Movie.Genre.ACTION));
        Movie horror1 = new Movie("The Nun", "TN789", List.of(Movie.Genre.HORROR));
        Movie horror2 = new Movie("Scream", "S145", List.of(Movie.Genre.HORROR));

        User user1 = new User("Ahmed Ali", "12345678A", List.of("FAF123"));
        User user2 = new User("Mona Samir", "98765432B", List.of("TN789"));

        List<Recommendation> recs = engine.recommend(List.of(user1, user2), List.of(action1, action2, horror1, horror2));

        Recommendation r1 = recs.get(0);
        Recommendation r2 = recs.get(1);

        assertEquals("Ahmed Ali", r1.user().name());
        assertEquals(List.of(action2), r1.recommendedMovies());

        assertEquals("Mona Samir", r2.user().name());
        assertEquals(List.of(horror2), r2.recommendedMovies());
    }

    @Test
    public void testUserLikesMultipleGenres() {
        Movie action1 = new Movie("Fast And Furious", "FAF123", List.of(Movie.Genre.ACTION));
        Movie action2 = new Movie("John Wick", "JW456", List.of(Movie.Genre.ACTION));
        Movie horror1 = new Movie("The Nun", "TN789", List.of(Movie.Genre.HORROR));
        Movie horror2 = new Movie("Scream", "SC111", List.of(Movie.Genre.HORROR));

        User user = new User("Noha Yasser", "55556666F", List.of("FAF123", "TN789"));

        List<Recommendation> recs = engine.recommend(List.of(user), List.of(action1, action2, horror1, horror2));
        Recommendation r = recs.getFirst();

        assertEquals(List.of(action2, horror2), r.recommendedMovies());
    }

    @Test
    public void testNonexistentMovieLiked() {
        Movie action1 = new Movie("Fast And Furious 8", "FAF123", List.of(Movie.Genre.ACTION));
        Movie action2 = new Movie("John Wick", "JW456", List.of(Movie.Genre.ACTION));
        Movie horror1 = new Movie("The Nun", "TN789", List.of(Movie.Genre.HORROR));
        Movie horror2 = new Movie("Scream", "SC111", List.of(Movie.Genre.HORROR));

        User user = new User("Hossam", "33334444E", List.of("XYZ999"));

        List<Recommendation> recs = engine.recommend(List.of(user), List.of(action1, action2, horror1, horror2));
        Recommendation r = recs.getFirst();

        assertTrue(r.recommendedMovies().isEmpty());
    }
}