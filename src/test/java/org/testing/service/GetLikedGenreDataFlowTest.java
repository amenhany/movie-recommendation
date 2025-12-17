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

class GetLikedGenreDataFlowTest {

    private RecommendationEngine engine;
    private List<Movie> allMovies;

    @BeforeEach
    void setUp() {
        engine = new RecommendationEngine();
        allMovies = new ArrayList<>();
        allMovies.add(new Movie("Action Movie", "M1", Arrays.asList(Movie.Genre.ACTION)));
        allMovies.add(new Movie("Comedy Movie", "M2", Arrays.asList(Movie.Genre.COMEDY)));
        allMovies.add(new Movie("Drama Movie", "M3", Arrays.asList(Movie.Genre.DRAMA, Movie.Genre.ROMANCE)));
        allMovies.add(new Movie("Horror Comedy", "M4", Arrays.asList(Movie.Genre.HORROR, Movie.Genre.COMEDY)));
    }

    @Nested
    @DisplayName("All-Defs Coverage")
    class AllDefsCoverage {

        @Test
        @DisplayName("Def of likedGenres at line 2 - reaches return")
        void allDefs_likedGenres() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Def of likedMoviesId at line 3 - used in loop")
        void allDefs_likedMoviesId() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertEquals(1, result.size());
            assertTrue(result.contains(Movie.Genre.ACTION));
        }

        @Test
        @DisplayName("Def of id, allMovie, genre in loops")
        void allDefs_loopVariables() {
            User user = new User("John", "123456789", Arrays.asList("M3"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertEquals(2, result.size());
            assertTrue(result.contains(Movie.Genre.DRAMA));
            assertTrue(result.contains(Movie.Genre.ROMANCE));
        }
    }

    @Nested
    @DisplayName("All-Uses Coverage")
    class AllUsesCoverage {

        @Test
        @DisplayName("likedGenres (2,9) and (2,10) - first genre added")
        void allUses_likedGenres_2_9_and_2_10() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.contains(Movie.Genre.ACTION));
        }

        @Test
        @DisplayName("likedGenres (2,18) - empty list returned directly")
        void allUses_likedGenres_2_18() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("likedGenres (10,9) - duplicate genre check")
        void allUses_likedGenres_10_9() {
            User user = new User("John", "123456789", Arrays.asList("M2", "M4"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            long comedyCount = result.stream().filter(g -> g == Movie.Genre.COMEDY).count();
            assertEquals(1, comedyCount);
        }

        @Test
        @DisplayName("allMovie (6,7) and (6,8) - movie matched and genres extracted")
        void allUses_allMovie_6_7_and_6_8() {
            User user = new User("John", "123456789", Arrays.asList("M3"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.contains(Movie.Genre.DRAMA));
            assertTrue(result.contains(Movie.Genre.ROMANCE));
        }

        @Test
        @DisplayName("genre (8,9) and (8,10) - genre checked and added")
        void allUses_genre_8_9_and_8_10() {
            User user = new User("John", "123456789", Arrays.asList("M4"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.contains(Movie.Genre.HORROR));
            assertTrue(result.contains(Movie.Genre.COMEDY));
        }
    }

    @Nested
    @DisplayName("All-DU-Paths Coverage")
    class AllDUPathsCoverage {

        @Test
        @DisplayName("likedGenres (2,18) path: loop skipped entirely")
        void allDUPaths_likedGenres_2_18_loopSkipped() {
            User user = new User("John", "123456789", Collections.emptyList());
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("likedGenres (2,18) path: through loops to return")
        void allDUPaths_likedGenres_2_18_throughLoops() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertFalse(result.isEmpty());
        }

        @Test
        @DisplayName("likedGenres (10,9) path: genre added then duplicate checked")
        void allDUPaths_likedGenres_10_9() {
            User user = new User("John", "123456789", Arrays.asList("M2", "M4"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("id (5,7) path: movie not found in allMovies")
        void allDUPaths_id_5_7_movieNotFound() {
            User user = new User("John", "123456789", Arrays.asList("M999"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("id (5,7) path: movie found and matched")
        void allDUPaths_id_5_7_movieFound() {
            User user = new User("John", "123456789", Arrays.asList("M1"));
            List<Movie.Genre> result = engine.getLikedGenre(user, allMovies);
            assertEquals(1, result.size());
        }
    }
}
