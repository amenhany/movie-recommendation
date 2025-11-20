package org.testing.io;

import org.testing.model.User;

import java.util.IllegalFormatException;
import java.util.List;

public class UserParser implements Parser<User> {
    @Override
    public List<User> parse(List<String> lines) throws IllegalFormatException {
        return List.of();
    }
}
