package com.letscode.moviesbattle.domain.model.type;

import java.util.Arrays;

public enum Genres {
    ADVENTURE,
    ANIMATION,
    BIOGRAPHY,
    CRIME,
    DRAMA,
    FAMILY,
    FANTASY,
    HORROR,
    ROMANCE,
    ACTION;

    public static Genres getRandom() {
        return values()[(int) (Math.random() * Arrays.stream(values()).count())];
    }
}
