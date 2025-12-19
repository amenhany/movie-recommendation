package org.testing.io;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testing.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserParserTest {
    private UserParser parser;

    @BeforeEach
    void init() {
        parser = new UserParser();
    }

    @Test
    public void testValidUser() {
        List<String> userLines = List.of(
                "Hazem Ali,12345678A",   // Name, ID
                "HP123,A456"             // Liked movie IDs
        );

        List<User> users = parser.parse(userLines);

        assertEquals(1, users.size());
        User user = users.getFirst();
        assertEquals("Hazem Ali", user.name());
        assertEquals("12345678A", user.id());
        assertEquals(List.of("HP123", "A456"), user.likedMovieIds());
    }

    @Test
    public void testInvalidUserName() {
        List<String> userLines = List.of(
                " Hazem Ali,12345678A",   // Starts with space → invalid
                "HP123"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));
        assertEquals("ERROR: User Name  Hazem Ali is wrong", exception.getMessage());
    }

    @Test
    public void testInvalidUserId() {
        List<String> userLines = List.of(
                "Hazem Ali,12A34567B",  // Starts with letters → invalid
                "HP123"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));
        assertEquals("ERROR: User Id 12A34567B is wrong", exception.getMessage());
    }

    @Test
    public void testEmptyUserName() {
        List<String> userLines = List.of(
                ",12345678A",   // Empty name
                "HP123"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));
        assertEquals("ERROR: User Name cannot be empty", exception.getMessage());

    }

    @Test
    public void testUserIdTooShort() {
        List<String> userLines = List.of(
                "Hazem Ali,1234567A",   // Only 8 characters → invalid
                "HP123,AV456"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        assertEquals("ERROR: User Id 1234567A is wrong", exception.getMessage());
    }

    @Test
    public void testUserIdTooLong() {
        List<String> userLines = List.of(
                "Hazem Ali,123456789B",   // 10 characters → invalid
                "HP123,AV456"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        assertEquals("ERROR: User Id 123456789B is wrong", exception.getMessage());
    }

    @Test
    public void testEmptyUserId() {
        List<String> userLines = List.of(
                "Hazem Ali, ",   // Empty user ID
                "HP123,A456"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        assertEquals("ERROR: User Id  is wrong", exception.getMessage());
    }

    @Test
    public void testDuplicateUserIds() {
        List<String> userLines = List.of(
                "Hazem Ali,12345678A",  // User 1
                "HP123",
                "Ahmed Ali,12345678A",  // User 2 with same ID → invalid
                "A456"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        assertEquals("ERROR: User Id 12345678A is not unique", exception.getMessage());
    }

    @Test
    public void testUniqueUserIds() {
        List<String> userLines = List.of(
                "Hazem Ali,12345678A",   // User 1
                "HP123",
                "Ahmed Ali,98765432A",   // User 2 with different ID → valid
                "AV456"
        );

        List<User> users = parser.parse(userLines);

        assertEquals(2, users.size());

        assertEquals("Hazem Ali", users.get(0).name());
        assertEquals("12345678A", users.get(0).id());

        assertEquals("Ahmed Ali", users.get(1).name());
        assertEquals("98765432A", users.get(1).id());
    }

    @Test
    public void testUserNameAndIdBothInvalid() {
        List<String> userLines = List.of(
                " Hazem Ali,1234567A",  // Both name and ID invalid
                // username starts with space
                // userId is too short
                "HP123"
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        // Only the first error (username) should be reported
        assertEquals("ERROR: User Name  Hazem Ali is wrong", exception.getMessage());
    }

    @Test
    public void testValidUserIdEndingWithLowercase() {
        List<String> userLines = List.of(
                "Hazem Ali,12345678a",  // 9 chars, starts with numbers, ends with lowercase letter
                "HP123,A456"
        );

        List<User> users = parser.parse(userLines);

        assertEquals(1, users.size());
        User user = users.getFirst();

        assertEquals("Hazem Ali", user.name());
        assertEquals("12345678a", user.id());
        assertEquals(List.of("HP123","A456"), user.likedMovieIds());
    }

    @Test
    public void testValidLikedMovieIds() {
        List<String> userLines = List.of(
                "Hazem Ali,123456789",  // Valid user
                "FAF128,A456"           // Both movie IDs exist
        );

        List<User> users = parser.parse(userLines);

        assertEquals(1, users.size());
        User user = users.getFirst();

        assertEquals(List.of("FAF128", "A456"), user.likedMovieIds());
    }

    @Test
    public void testLikedMovieIdLettersWrong() {
        List<String> userLines = List.of(
                "Hazem Ali,12345678A",   // Valid user
                "Ff128"                  // Letters not capitalized
        );

        Exception exception = assertThrows(Exception.class, () -> parser.parse(userLines));

        assertEquals("ERROR: Movie Id letters Ff128 are wrong", exception.getMessage());
    }
}