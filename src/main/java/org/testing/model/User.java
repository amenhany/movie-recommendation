package org.testing.model;

import java.util.List;

public record User (String name, String id, List<String> likedMovieIds) {}
