package org.testing.service;

import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationEngine {
    public List<Recommendation> recommend(List<User> users, List<Movie> allMovies) {
        List<Recommendation> recommendations = new ArrayList<>();

        for (User user : users) {

            Set<Movie.Genre> likedGenres = getLikedGenre(user, allMovies); //gets liked genres

            Set<Movie> recommendedMovies = new HashSet<>(); //gets all movies with this genre
            for (Movie.Genre genre : likedGenres) {
                recommendedMovies.addAll(getMoviesByGenre(genre, allMovies));
            }

            recommendedMovies = filterAlreadyLikedMovies(user, recommendedMovies); //filter already liked ones

            recommendations.add(new Recommendation(user, recommendedMovies.stream().toList()));
        }

        return recommendations;
    }

    Set<Movie.Genre> getLikedGenre(User user, List<Movie> allMovies) {
        Set<Movie.Genre> likedGenres = new HashSet<>();
        List<String> likedMovieIds = user.likedMovieIds();

        for (String id : likedMovieIds) {                    //loops on liked movies by user
            for (Movie allMovie : allMovies) {               //loops on all movies
                if (allMovie.id().equals(id)) {
                    likedGenres.addAll(allMovie.genres());
                    break;
                }
            }
        }

        return likedGenres;
    }

    List<Movie> getMoviesByGenre(Movie.Genre genre, List<Movie> allMovies) {
        List<Movie> moviesByGenre = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (movie.genres().contains(genre)) {
                moviesByGenre.add(movie);
            }
        }

        return moviesByGenre;
    }

    Set<Movie> filterAlreadyLikedMovies(User user, Set<Movie> recommendedMovies) {
        Set<String> likedMovieIds = new HashSet<>(user.likedMovieIds());
        Set<Movie> filtered = new HashSet<>();

        for (Movie m : recommendedMovies) {
            if (!likedMovieIds.contains(m.id())) {
                filtered.add(m);
            }
        }

        return filtered;
    }

}
