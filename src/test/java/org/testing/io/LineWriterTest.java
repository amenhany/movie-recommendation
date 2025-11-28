package org.testing.io;

import org.junit.jupiter.api.Test;
import org.testing.io.LineWriter;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LineWriterTest {

    //normal case
    @Test
    void testWriteMethodFullDataWithoutMap() throws IOException {

        Movie m1 = new Movie("Forrest Gump", "FG123", List.of(Movie.Genre.DRAMA, Movie.Genre.ROMANCE));
        Movie m2 = new Movie("Fight Club", "FC456", List.of(Movie.Genre.CRIME, Movie.Genre.DRAMA, Movie.Genre.THRILLER));
        Movie m3 = new Movie("Interstellar", "I234", List.of(Movie.Genre.ADVENTURE, Movie.Genre.DRAMA, Movie.Genre.SCI_FI));
        Movie m4 = new Movie("Goodfellas", "G789", List.of(Movie.Genre.BIOGRAPHY, Movie.Genre.CRIME, Movie.Genre.DRAMA));
        Movie m5 = new Movie("The Prestige", "TP321", List.of(Movie.Genre.DRAMA, Movie.Genre.MYSTERY, Movie.Genre.SCI_FI));
        Movie m6 = new Movie("Home Alone", "HA654", List.of(Movie.Genre.COMEDY, Movie.Genre.FAMILY));

        User karim = new User("Karim", "12345678A", List.of("FG123", "I234", "TP321"));
        User abanoub = new User("Abanoub", "23456789B", List.of("FG123", "G789"));
        User amen = new User("Amen", "34567890C", List.of("I234", "HA654"));
        User lujain = new User("Lujain", "45678901D", List.of("FG123", "HA654"));
        User hazem = new User("Hazem", "56789012E", List.of("FC456", "TP321"));
        User nadeen = new User("Nadeen", "67890123F", List.of("G789"));
        User aya = new User("Aya", "78901234G", List.of("I234", "G789"));
        User mark = new User("Mark", "89012345H", List.of("TP321", "HA654"));

        List<Recommendation> recommendations = List.of(
                new Recommendation(karim, List.of(m2, m4)),
                new Recommendation(abanoub, List.of(m3, m5)),
                new Recommendation(amen, List.of(m1, m5)),
                new Recommendation(lujain, List.of(m2, m3)),
                new Recommendation(hazem, List.of(m1, m6)),
                new Recommendation(nadeen, List.of(m2, m3)),
                new Recommendation(aya, List.of(m1, m5)),
                new Recommendation(mark, List.of(m3, m4))
        );

        LineWriter lw = new LineWriter();
        Path tempFile = Files.createTempFile("recommendationsTestFull", ".txt");
        lw.write(tempFile.toString(), recommendations);
        List<String> lines = Files.readAllLines(tempFile);

        assertEquals("Karim,12345678A", lines.get(0));
        assertEquals("Fight Club,Goodfellas", lines.get(1));

        assertEquals("Abanoub,23456789B", lines.get(2));
        assertEquals("Interstellar,The Prestige", lines.get(3));

        assertEquals("Amen,34567890C", lines.get(4));
        assertEquals("Forrest Gump,The Prestige", lines.get(5));

        assertEquals("Lujain,45678901D", lines.get(6));
        assertEquals("Fight Club,Interstellar", lines.get(7));

        assertEquals("Hazem,56789012E", lines.get(8));
        assertEquals("Forrest Gump,Home Alone", lines.get(9));

        assertEquals("Nadeen,67890123F", lines.get(10));
        assertEquals("Fight Club,Interstellar", lines.get(11));

        assertEquals("Aya,78901234G", lines.get(12));
        assertEquals("Forrest Gump,The Prestige", lines.get(13));

        assertEquals("Mark,89012345H", lines.get(14));
        assertEquals("Interstellar,Goodfellas", lines.get(15));


        Files.deleteIfExists(tempFile);
    }


    //ONLY ONE USER
    @Test
    void testWriteWithOneUserOneMovie() throws IOException {
        LineWriter lw = new LineWriter();
        Movie m1 = new Movie("Home Alone", "HA654", List.of(Movie.Genre.FAMILY, Movie.Genre.COMEDY));
        User user1 = new User("Nadeen", "67890123F", List.of("HA654"));
        List<Recommendation> recommendations = List.of(
                new Recommendation(user1, List.of(m1))
        );

        Path tempFile = Files.createTempFile("singleUserTest", ".txt");
        lw.write(tempFile.toString(), recommendations);
        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(2, lines.size(), "File should have 2 lines for one user with one movie");
        assertEquals("Nadeen,67890123F", lines.get(0), "First line should be user info");
        assertEquals("Home Alone", lines.get(1), "Second line should be the movie title");

        Files.deleteIfExists(tempFile);
    }


    //test with empty rec list
    @Test
    void testWriteWithEmptyRecommendations() throws IOException {

        LineWriter lw = new LineWriter();
        List<Recommendation> emptyRecommendations = Collections.emptyList();
        Path tempFile = Files.createTempFile("emptyRecommendationsTest", ".txt");
        lw.write(tempFile.toString(), emptyRecommendations);
        List<String> lines = Files.readAllLines(tempFile);
        assertTrue(lines.isEmpty(), "File should be empty when recommendations list is empty");
        Files.deleteIfExists(tempFile);
    }


    // assure recommendations are not written in csv file if the movie title contains comma
    @Test
    void testWriteWithCommaInMovieTitle() throws IOException {
        Movie m1 = new Movie("The Good, The Bad and The Ugly", "FG123", List.of(Movie.Genre.DRAMA));

        User user1 = new User("Nadeen", "67890123F", List.of("FG123"));

        List<Recommendation> recommendations = List.of(new Recommendation(user1, List.of(m1))
        );
        LineWriter lw = new LineWriter();
        Path tempFile = Files.createTempFile("commaTitleTest", ".txt");
        lw.write(tempFile.toString(), recommendations);
        List<String> lines = Files.readAllLines(tempFile);

        assertEquals("Nadeen,67890123F", lines.get(0));
        assertEquals("The Good, The Bad and The Ugly", lines.get(1));

        Files.deleteIfExists(tempFile);
    }


    //using invalid path
    @Test
    void testWriteWithInvalidFilePath() {

        LineWriter lw = new LineWriter();

        Movie m1 = new Movie("Forrest Gump", "FG123", List.of(Movie.Genre.DRAMA));
        User user1 = new User("Nadeen", "67890123F", List.of("FG123"));
        List<Recommendation> recommendations = List.of(
                new Recommendation(user1, List.of(m1))
        );
        String invalidPath = "/invalid/path/recommendations.txt";
        assertThrows(IOException.class, () -> lw.write(invalidPath, recommendations));
    }

    @Test
    void testWriteErrorWithInvalidPath() {
        LineWriter lw = new LineWriter();
        String invalidPath = "/invalid/directory/error.txt";
        lw.writeError(invalidPath, "This is a test error");
    }

}










