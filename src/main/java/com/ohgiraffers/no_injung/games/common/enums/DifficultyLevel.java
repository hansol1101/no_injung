package com.ohgiraffers.no_injung.games.common.enums;

public enum DifficultyLevel {
    EASY(1),
    NORMAL(2),
    HARD(3);

    private final int value;

    DifficultyLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DifficultyLevel fromValue(int value) {
        for (DifficultyLevel level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty level: " + value);
    }
}