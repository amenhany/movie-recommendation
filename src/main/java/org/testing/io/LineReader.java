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
}
