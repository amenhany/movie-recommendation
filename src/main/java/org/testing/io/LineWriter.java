package org.testing.io;

import org.testing.model.Recommendation;

import java.io.IOException;
import java.util.List;

public class LineWriter {
    public void write(String filepath, List<Recommendation> recommendations) throws IOException {}
    public void writeError(String filepath, String message) {
        try {
            throw new IOException();
        } catch (IOException e) {
            System.out.println("Failed to write error to file: " + e.getMessage());
        }
    }
}
