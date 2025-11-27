package org.testing.io;

import org.testing.model.Movie;

import java.util.IllegalFormatException;
import java.util.List;

public class MovieParser implements Parser<Movie> {
    @Override
    public List<Movie> parse(List<String> lines) throws IllegalFormatException {
        return List.of();
    }
    public static String validateTitle(String title) {
        String[] arr = title.split(" ");
        StringBuilder newTitle = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (!Character.isUpperCase(arr[i].charAt(0)))
                throw new IllegalArgumentException("Each word must start with capital letter");

            newTitle.append(arr[i]).append(" ");
        }

        return newTitle.toString().trim();
    }

    public static String validateId(String id, String title) {
        //check capital letters
        char[] c = title.toCharArray();
        StringBuilder cap = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if (Character.isUpperCase(c[i])) {
                cap.append(c[i]);
            }
        }

        if (!id.startsWith(cap.toString())) {
            throw new IllegalArgumentException("id must start with capital letters in title");
        }

        //check digits part
        String digits = id.substring(cap.length());
        if (!digits.matches("\\d{3}")) {
            throw new IllegalArgumentException("id digit part must be exactly 3 digits");
        }
        if (digits.charAt(0) == digits.charAt(1) || digits.charAt(1) == digits.charAt(2) || digits.charAt(0) == digits.charAt(2)) {
            throw new IllegalArgumentException("Id digits must be unique");
        }

        return id;

    }

}
