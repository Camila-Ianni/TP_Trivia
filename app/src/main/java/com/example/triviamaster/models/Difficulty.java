package com.example.triviamaster.models;

public enum Difficulty {
    EASY("Principiante", 20, 3, 1.0, ""),
    NORMAL("Veterano", 15, 3, 1.5, ""),
    HARD("Leyenda", 10, 2, 2.0, "");

    private final String displayName;
    private final int timeLimitSeconds;
    private final int initialLives;
    private final double scoreMultiplier;
    private final String badgeIcon;

    Difficulty(String displayName, int timeLimitSeconds, int initialLives, double scoreMultiplier, String badgeIcon) {
        this.displayName = displayName;
        this.timeLimitSeconds = timeLimitSeconds;
        this.initialLives = initialLives;
        this.scoreMultiplier = scoreMultiplier;
        this.badgeIcon = badgeIcon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public int getInitialLives() {
        return initialLives;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public String getBadgeIcon() {
        return badgeIcon;
    }

    public static Difficulty fromName(String name) {
        if (name != null) {
            for (Difficulty d : values()) {
                if (d.name().equalsIgnoreCase(name)) {
                    return d;
                }
            }
        }
        return NORMAL;
    }
}
