package org.testing.service;

import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;

import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {
    public List<Recommendation> recommend(List<User> users, List<Movie> movies) {
        return List.of();
    }

    private List<Movie.Genre> getLikedGenre(User user, List<Movie> allMovies) {
        List<Movie.Genre> likedGenres = new ArrayList<>();
        List<String> likedMoviesId = user.likedMovieIds();

        for (String id : likedMoviesId) {                    //loops on liked movies by user
            for (Movie allMovie : allMovies) {               //loops on all movies
                if (allMovie.id().equals(id)) {
                    for (Movie.Genre genre : allMovie.genres()) {
                        if (!likedGenres.contains(genre)) { //avoid duplicate genres
                            likedGenres.add(genre);
                        }
                    }
                    break;
                }
            }
        }

        return likedGenres;
    }

    private List<Movie> getMoviesByGenre(Movie.Genre genre, List<Movie> allMovies) {
        List<Movie> moviesByGenre = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (movie.genres().contains(genre)) {
                moviesByGenre.add(movie);
            }
        }

        return moviesByGenre;
    }

}
