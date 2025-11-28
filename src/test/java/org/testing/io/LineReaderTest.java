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
                "Forrest Gump, FG123",
                "Drama, Romance",
                "Fight Club, FC456",
                "Crime, Drama, Thriller",
                "Interstellar, I234",
                "Adventure, Drama, Sci-Fi",
                "Goodfellas, G789",
                "Biography, Crime, Drama",
                "The Prestige, TP321",
                "Drama, Mystery, Sci-FI",
                "Home Alone, HA654",
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
                "Karim, 12345678A",
                "FG123, I234, TP321",
                "Abanoub, 23456789B",
                "FG123, G789",
                "Amen, 34567890C",
                "I234, HA654",
                "Lujain, 45678901D",
                "FG123, HA654",
                "Hazem, 56789012E",
                "FC456, TP321",
                "Nadeen, 67890123F",
                "G789",
                "Aya, 78901234G",
                "I234, G789",
                "Mark, 89012345H",
                "TP321, HA654"
        );
        assertEquals(expectedLines, actualLines, "LineReader should read all lines exactly as in the file");
    }

}
