package org.testing.io;

import org.testing.model.Movie;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public class MovieParser implements Parser<Movie> {
    @Override
    public List<Movie> parse(List<String> lines) throws IllegalFormatException {
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 2) {
            if (i + 1 >= lines.size()) {
                throw new IllegalArgumentException("Missing genre line for last movie");
            }

            String titleIdLine = lines.get(i);     //first line
            String genresLine = lines.get(i + 1);  //second line

            Movie movie = validate(titleIdLine, genresLine);
            movies.add(movie);
        }

        return movies;
    }

    private Movie validate(String fl, String sl) {
        String[] firstLine = fl.split(",");
        if (firstLine.length != 2) { //array size must be 2 only(title, id)
            throw new IllegalArgumentException("Invalid title,id line");
        }
        String title = firstLine[0].trim();
        String id = firstLine[1].trim();
        //validate
        title = validateTitle(title);
        id = validateId(id, title);

        //second line: genres
        String[] genresArr = sl.split(",");
        List<Movie.Genre> genres = new ArrayList<>();
        for (String g : genresArr) {
            g = g.trim().toUpperCase();
            try {
                genres.add(Movie.Genre.valueOf(g));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid genre");
            }
        }
        genres = validateGenres(genres);

        return new Movie(title, id, genres);
    }

    private String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        String[] arr = title.split(" ");
        StringBuilder newTitle = new StringBuilder();
        for (String s : arr) {
            if (!Character.isUpperCase(s.charAt(0)))
                throw new IllegalArgumentException("Each word must start with capital letter");

            newTitle.append(s).append(" ");
        }

        return newTitle.toString().trim();
    }

    private String validateId(String id, String title) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        //check capital letters
        char[] c = title.toCharArray();
        StringBuilder cap = new StringBuilder();
        for (char value : c) {
            if (Character.isUpperCase(value)) {
                cap.append(value);
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

    //genres
    private List<Movie.Genre> validateGenres(List<Movie.Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("Movie must have at least one genre");
        }
        return List.copyOf(genres);
    }
}

