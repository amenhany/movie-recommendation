package org.testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testing.model.Movie;
import org.testing.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterAlreadyLikedMoviesDataFlowTest {

    private RecommendationEngine engine;
    private List<Movie> recommendedMovies;

    @BeforeEach
    void setUp() {
        engine = new RecommendationEngine();
        recommendedMovies = new ArrayList<>();
        recommendedMovies.add(new Movie("Action Movie", "M1", Arrays.asList(Movie.Genre.ACTION)));
        recommendedMovies.add(new Movie("Comedy Movie", "M2", Arrays.asList(Movie.Genre.COMEDY)));
        recommendedMovies.add(new Movie("Drama Movie", "M3", Arrays.asList(Movie.Genre.DRAMA)));
    }

    @Nested
    @DisplayName("All-Defs Coverage")
    class AllDefsCoverage {

        @Test
        @DisplayName("Def of likedMovieIds at line 2")
        void allDefs_likedMovieIds() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Def of filtered at line 3 - empty recommendedMovies")
        void allDefs_filtered_emptyInput() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, Collections.emptyList());
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Def of m at line 5 - loop variable")
        void allDefs_m() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(3, result.size());
        }
    }

    @Nested
    @DisplayName("All-Uses Coverage")
    class AllUsesCoverage {

        @Test
        @DisplayName("likedMovieIds (2,6) - used in contains check")
        void allUses_likedMovieIds_2_6() {
            User user = new User("John", "123456789", Arrays.asList("M1", "M2"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(1, result.size());
            assertEquals("M3", result.get(0).id());
        }

        @Test
        @DisplayName("filtered (3,11) - loop skipped, empty list returned")
        void allUses_filtered_3_11_loopSkipped() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie> result = engine.filterAlreadyLikedMovies(user, Collections.emptyList());
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("filtered (3,7) - movie added to filtered")
        void allUses_filtered_3_7() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(3, result.size());
        }

        @Test
        @DisplayName("filtered (7,7) - multiple movies added")
        void allUses_filtered_7_7() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("m (5,6) and (5,7) - movie checked and added")
        void allUses_m_5_6_and_5_7() {
            User user = new User("John", "123456789", Arrays.asList("M2"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertTrue(result.stream().anyMatch(m -> m.id().equals("M1")));
            assertTrue(result.stream().anyMatch(m -> m.id().equals("M3")));
        }
    }

    @Nested
    @DisplayName("All-DU-Paths Coverage")
    class AllDUPathsCoverage {

        @Test
        @DisplayName("filtered (3,11) path: loop skipped")
        void allDUPaths_filtered_3_11_loopSkipped() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie> result = engine.filterAlreadyLikedMovies(user, Collections.emptyList());
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("filtered (3,11) path: through loop, all filtered out")
        void allDUPaths_filtered_3_11_allFilteredOut() {
            User user = new User("John", "123456789", Arrays.asList("M1", "M2", "M3"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("filtered (7,11) path: some added then returned")
        void allDUPaths_filtered_7_11() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("filtered (7,7) path: multiple iterations adding")
        void allDUPaths_filtered_7_7_multipleAdds() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie> result = engine.filterAlreadyLikedMovies(user, recommendedMovies);
            assertEquals(3, result.size());
        }
    }
}
