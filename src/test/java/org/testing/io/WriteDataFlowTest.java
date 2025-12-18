package org.testing.io;

import org.junit.jupiter.api.*;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteDataFlowTest {

    private LineWriter writer;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        writer = new LineWriter();
        tempFile = Files.createTempFile("test_output", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    private User createUser(String name, String id, List<String> likedIds) {
        return new User(name, id, likedIds);
    }

    private Movie createMovie(String title, String id) {
        return new Movie(title, id, Arrays.asList(Movie.Genre.ACTION));
    }

    @Nested
    @DisplayName("All-Defs Coverage")
    class AllDefsCoverage {

        @Test
        @DisplayName("Def of bw at line 2")
        void allDefs_bw() throws IOException {
            List<Recommendation> recs = Collections.emptyList();
            writer.write(tempFile.toString(), recs);
            assertTrue(Files.exists(tempFile));
        }

        @Test
        @DisplayName("Def of r at line 3")
        void allDefs_r() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(createMovie("Test Movie", "M2"));
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("John"));
        }

        @Test
        @DisplayName("Def of lenOfRecMovies and counter at lines 6-7")
        void allDefs_lenOfRecMovies_counter() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(
                createMovie("Movie One", "M2"),
                createMovie("Movie Two", "M3")
            );
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Movie One"));
            assertTrue(content.contains("Movie Two"));
        }

        @Test
        @DisplayName("Def of m at line 8")
        void allDefs_m() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(createMovie("Single Movie", "M2"));
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Single Movie"));
        }
    }

    @Nested
    @DisplayName("All-Uses Coverage")
    class AllUsesCoverage {

        @Test
        @DisplayName("bw (2,4) and (2,5) - write user info")
        void allUses_bw_2_4_and_2_5() throws IOException {
            User user = createUser("Alice", "123456789", Arrays.asList("M1"));
            Recommendation rec = new Recommendation(user, Collections.emptyList());
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Alice,123456789"));
        }

        @Test
        @DisplayName("bw (2,19) - close after empty recommendations")
        void allUses_bw_2_19_emptyRecs() throws IOException {
            writer.write(tempFile.toString(), Collections.emptyList());
            assertTrue(Files.exists(tempFile));
            assertEquals("", Files.readString(tempFile));
        }

        @Test
        @DisplayName("r (3,4), (3,6), (3,8) - user and movies accessed")
        void allUses_r_3_4_3_6_3_8() throws IOException {
            User user = createUser("Bob", "987654321", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(createMovie("Test", "M2"));
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Bob,987654321"));
            assertTrue(content.contains("Test"));
        }

        @Test
        @DisplayName("counter (7,9) - first iteration check")
        void allUses_counter_7_9() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(createMovie("Only Movie", "M2"));
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Only Movie"));
            assertFalse(content.contains("Only Movie,"));
        }

        @Test
        @DisplayName("counter (14,9) - subsequent iterations")
        void allUses_counter_14_9() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(
                createMovie("First", "M2"),
                createMovie("Second", "M3"),
                createMovie("Third", "M4")
            );
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("First,"));
            assertTrue(content.contains("Second,"));
            assertFalse(content.contains("Third,"));
        }

        @Test
        @DisplayName("m (8,10) and (8,13) - last vs non-last movie")
        void allUses_m_8_10_and_8_13() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(
                createMovie("Not Last", "M2"),
                createMovie("Last One", "M3")
            );
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Not Last,"));
            assertTrue(content.contains("Last One"));
            assertFalse(content.contains("Last One,"));
        }
    }

    @Nested
    @DisplayName("All-DU-Paths Coverage")
    class AllDUPathsCoverage {

        @Test
        @DisplayName("bw (2,19) path: outer loop skipped")
        void allDUPaths_bw_2_19_loopSkipped() throws IOException {
            writer.write(tempFile.toString(), Collections.emptyList());
            assertEquals("", Files.readString(tempFile));
        }

        @Test
        @DisplayName("bw (2,19) path: through outer loop then close")
        void allDUPaths_bw_2_19_throughLoop() throws IOException {
            User user = createUser("Test", "123456789", Arrays.asList("M1"));
            Recommendation rec = new Recommendation(user, Collections.emptyList());
            writer.write(tempFile.toString(), Arrays.asList(rec));
            assertTrue(Files.readString(tempFile).contains("Test"));
        }

        @Test
        @DisplayName("counter (7,9) path: single movie (counter == len on first check)")
        void allDUPaths_counter_7_9_singleMovie() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(createMovie("Solo", "M2"));
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("Solo"));
        }

        @Test
        @DisplayName("counter (7,14) path: counter != len, increment happens")
        void allDUPaths_counter_7_14() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(
                createMovie("First", "M2"),
                createMovie("Second", "M3")
            );
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("First,Second"));
        }

        @Test
        @DisplayName("counter (14,14) path: multiple increments")
        void allDUPaths_counter_14_14() throws IOException {
            User user = createUser("John", "123456789", Arrays.asList("M1"));
            List<Movie> movies = Arrays.asList(
                createMovie("A", "M2"),
                createMovie("B", "M3"),
                createMovie("C", "M4"),
                createMovie("D", "M5")
            );
            Recommendation rec = new Recommendation(user, movies);
            writer.write(tempFile.toString(), Arrays.asList(rec));
            String content = Files.readString(tempFile);
            assertTrue(content.contains("A,B,C,D"));
        }
    }
}
