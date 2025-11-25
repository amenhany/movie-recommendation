package org.testing.controller;

import org.testing.io.FileReader;
import org.testing.io.FileWriter;
import org.testing.io.MovieParser;
import org.testing.io.UserParser;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;
import org.testing.service.RecommendationEngine;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Movie movie = new Movie("far Ds", "GH123","genre");
        FileReader fileReader = new FileReader();
        FileWriter fileWriter = new FileWriter();
        MovieParser movieParser = new MovieParser();
        UserParser userParser = new UserParser();
        RecommendationEngine recommendationEngine = new RecommendationEngine();

        try {
            List<String> movieLines = fileReader.read("movies.txt");
            List<String> userLines = fileReader.read("users.txt");

            List<Movie> movies = movieParser.parse(movieLines);
            List<User> users = userParser.parse(userLines);

            List<Recommendation> recommendations = recommendationEngine.recommend(users, movies);
            fileWriter.write("recommendations.txt", recommendations);
        } catch (Exception e) {
            fileWriter.writeError("recommendations.txt", e.getMessage());
        }
    }
}
