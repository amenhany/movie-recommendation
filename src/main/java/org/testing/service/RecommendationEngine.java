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

            List<Movie.Genre> likedGenres = getLikedGenre(user, allMovies); //gets liked genres

            List<Movie> recommendedMovies = new ArrayList<>(); //gets all movies with this genre
            for (Movie.Genre genre : likedGenres) {
                recommendedMovies.addAll(getMoviesByGenre(genre, allMovies));
            }

            recommendedMovies = filterAlreadyLikedMovies(user, recommendedMovies); //filter already liked ones

            Recommendation rec = createRecommendation(user, recommendedMovies);
            recommendations.add(rec);
        }

        return recommendations;
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

    private List<Movie> filterAlreadyLikedMovies(User user, List<Movie> recommendedMovies) {
        Set<String> likedMovieIds = new HashSet<>(user.likedMovieIds());
        List<Movie> filtered = new ArrayList<>();

        for (Movie m : recommendedMovies) {
            if (!likedMovieIds.contains(m.id())) {
                filtered.add(m);
            }
        }

        return filtered;
    }

    private Recommendation createRecommendation(User user, List<Movie> recommendedMovies) {
        return new Recommendation(user, recommendedMovies);
    }

}
