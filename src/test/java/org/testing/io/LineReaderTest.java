package org.testing.io;

import org.junit.jupiter.api.Test;
import org.testing.model.Movie;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineReaderTest {

    //check if movie file exists
    @Test
    void MovieFileExists() {
        Path path = Path.of("src/main/resources/movies.txt");
        assertTrue(Files.exists(path), "movie.txt should exist");
    }

    //check if user file exists
    @Test
    void UserFileExists() {
        Path path = Path.of("src/main/resources/users.txt");
        assertTrue(Files.exists(path), "User.txt should exist");
    }

    //checking if the reader deals correctly if the file is empty
    @Test
    void testReadEmptyFile() throws IOException {

        Path tempFile = Files.createTempFile("emptyFile", ".txt");
        LineReader reader = new LineReader();
        List<String> lines = reader.read(tempFile.toString());
        assertEquals(0, lines.size(), "LineReader should return an empty list for an empty file");
        Files.deleteIfExists(Path.of(tempFile.toString()));
    }

    //check if reading each line as an array element
    @Test
    void testReadMoviesFileValid() throws IOException {
        String filepath = "src/main/resources/movies.txt";
        LineReader reader = new LineReader();
        List<String> actualLines = reader.read(filepath);
        List<String> expectedLines = List.of(
                "Forrest Gump, M1",
                "Drama, Romance",
                "Fight Club, M2",
                "Crime, Drama, Thriller",
                "Interstellar, M3",
                "Adventure, Drama, Sci-Fi",
                "Goodfellas, M4",
                "Biography, Crime, Drama",
                "The Prestige, M5,",
                "Drama, Mystery, Sci-FI",
                "Home Alone, M6,",
                "Comedy, Family"
        );
        assertEquals(expectedLines, actualLines, "LineReader should read all lines exactly as in the file");
    }

    @Test
    void testReadUsersFileValid() throws IOException {
        String filepath = "src/main/resources/users.txt";
        LineReader reader = new LineReader();
        List<String> actualLines = reader.read(filepath);
        List<String> expectedLines = List.of(
                "Karim, U1",
                "M1, M3, M5",
                "Abanoub, U2",
                "M1, M4",
                "Amen, U3",
                "M3, M6",
                "Lujain, U4",
                "M1, M6",
                "Hazem, U5",
                "M2, M5",
                "Nadeen, U6",
                "M4",
                "Aya, U7",
                "M3, M4",
                "Mark, U8",
                "M5, M6"
        );
        assertEquals(expectedLines, actualLines, "LineReader should read all lines exactly as in the file");
    }

    //checking if movie.txt lines is twice the movies
    @Test
    void moviesToLINES() throws IOException {
        LineReader reader = new LineReader();
        List<String> lines = reader.read("movie.txt");
        List<Movie> movies = MovieParser.parse(lines);
        assertEquals(lines.size(), movies.size() * 2, "Each movie should have exactly 2 lines");
    }
}
