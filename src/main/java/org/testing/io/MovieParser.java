package org.testing.io;

import org.testing.model.Movie;

import java.util.IllegalFormatException;
import java.util.List;

public class MovieParser implements Parser<Movie> {
    @Override
    public List<Movie> parse(List<String> lines) throws IllegalFormatException {
        return List.of();
    }
}
