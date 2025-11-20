package org.testing.io;

import java.util.IllegalFormatException;
import java.util.List;

public interface Parser<T> {
    List<T> parse(List<String> lines) throws IllegalFormatException;
}
