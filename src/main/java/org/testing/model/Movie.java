package org.testing.model;

import java.util.List;

public record Movie (String title, String id, List<String> genres) {}
