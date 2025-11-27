package org.testing.io;

import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class LineReader {
    public List<String> read(String filepath) throws IOException {
        String line;
        List<String> movieLines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        while((line = br.readLine()) != null){
            movieLines.add(line);
        }
        br.close();
        return movieLines;
    }
    public static void main(String[] args) throws IOException {
        /*
        File Specific main function to test functionality of LineReader
        */
        LineReader lr = new LineReader();
        List<String> movieLines = lr.read("src/main/resources/movies.txt");
        System.out.println("First index is " + movieLines.get(0));
        System.out.println("Second index is " + movieLines.get(1));
    }
}
