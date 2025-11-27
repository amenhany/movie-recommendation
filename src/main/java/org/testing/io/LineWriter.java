package org.testing.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.testing.model.Recommendation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testing.model.Movie;
import org.testing.model.User;

public class LineWriter {
    public void write(String filepath, List<Recommendation> recommendations) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
        for(Recommendation r : recommendations){
            bw.write(r.user().name() + "," + r.user().id());
            bw.write('\n');
            int lenOfRecMovies = r.recommendedMovies().size();
            int counter = 1;
            for(Movie m : r.recommendedMovies()){
                if(counter == lenOfRecMovies){
                    bw.write(m.title());
                }
                else{
                    bw.write(m.title() + ',');
                    counter++;
                }
            }
            bw.write('\n');
        }
        bw.close();
    }
   
    public void writeError(String filepath, String message) {
        try {
            throw new IOException();
        } catch (IOException e) {
            System.out.println("Failed to write error to file: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        LineWriter lw = new LineWriter();
        List<Movie.Genre> genre1 = new ArrayList<>();
        List<Movie.Genre> genre2 = new ArrayList<>();
        genre1.add(Movie.Genre.ACTION);
        genre2.add(Movie.Genre.COMEDY);
        Movie m1 = new Movie("Forrest Gump", "M2", genre1);
        Movie m2 = new Movie("Goodfellas", "M3", genre2);
        List<Movie> recommendedMovies = new ArrayList<>();
        recommendedMovies.add(m1);
        recommendedMovies.add(m2);
        
        List<String> user1LikedMovieIds = new ArrayList<>();
        user1LikedMovieIds.add("M1");
        user1LikedMovieIds.add("M3");
        
        List<String> user2LikedMovieIds = new ArrayList<>();
        user1LikedMovieIds.add("M4");
        user1LikedMovieIds.add("M6");
        
        User karim = new User("Karim", "U1", user1LikedMovieIds);
        User abanoub = new User("Abanoub", "U2", user2LikedMovieIds);
        
        Recommendation r1 = new Recommendation(karim, recommendedMovies);
        Recommendation r2 = new Recommendation(abanoub, recommendedMovies);
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(r1);
        recommendations.add(r2);
        try{
            lw.write("src/main/resources/recommendationsTestingLineWriter.txt", recommendations);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
