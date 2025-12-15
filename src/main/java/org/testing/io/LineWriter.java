package org.testing.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.testing.model.Recommendation;

import java.io.IOException;
import java.util.List;
import org.testing.model.Movie;


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
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            bw.write(message);
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to write error to file: " + e.getMessage());
        }
    }
}
