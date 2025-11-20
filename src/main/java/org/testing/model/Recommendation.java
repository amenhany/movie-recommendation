package org.testing.model;

import java.util.List;

public record Recommendation (User user, List<Movie> recommendedMovies) {}
