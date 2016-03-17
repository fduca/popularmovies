package com.cudaf.myappportfolio.util;

public enum SortOption {

    MOST_POPULAR("popularity.desc"),
    HIGHEST_RATE("vote_average.desc"),
    FAVORITES("favorites");

    private final String value;

    SortOption(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }
}
