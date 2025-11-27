package org.testing.model;

import java.util.List;

public record Movie(String title, String id, List<Movie.Genre> genres) {

    public enum Genre {
        COMEDY,
        DRAMA,
        HORROR,
        ACTION,
        ROMANCE,
        SCI_FI,
        THRILLER,
        FANTASY
    }


}


