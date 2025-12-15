package org.testing.controller;

import org.testing.io.LineReader;
import org.testing.io.LineWriter;
import org.testing.io.MovieParser;
import org.testing.io.UserParser;
import org.testing.model.Movie;
import org.testing.model.Recommendation;
import org.testing.model.User;
import org.testing.service.RecommendationEngine;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        new Main().run(
                "src/main/resources/movies.txt",
                "src/main/resources/users.txt",
                "src/main/resources/recommendations.txt"
        );
    }

    public void run(String moviesPath, String usersPath, String outputPath) {
        LineReader lineReader = new LineReader();
        LineWriter lineWriter = new LineWriter();
        MovieParser movieParser = new MovieParser();
        UserParser userParser = new UserParser();
        RecommendationEngine recommendationEngine = new RecommendationEngine();

        try {
            List<String> movieLines = lineReader.read(moviesPath);
            List<String> userLines = lineReader.read(usersPath);

            List<Movie> movies = movieParser.parse(movieLines);
            List<User> users = userParser.parse(userLines);

            List<Recommendation> recommendations = recommendationEngine.recommend(users, movies);
            lineWriter.write(outputPath, recommendations);
        } catch (Exception e) {
            lineWriter.writeError(outputPath, e.getMessage());
        }
    }
}
