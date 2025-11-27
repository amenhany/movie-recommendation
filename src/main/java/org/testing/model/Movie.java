package org.testing.model;

import org.testing.io.MovieParser;

import java.util.List;

public record Movie(String title, String id, String genre) {
    public Movie {
        //title
        title = MovieParser.validateTitle(title);
        id = MovieParser.validateId(id, title);
        genre = genre.trim();

    }



}


