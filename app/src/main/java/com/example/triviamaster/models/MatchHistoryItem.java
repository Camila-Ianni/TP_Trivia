package com.example.triviamaster.models;

import java.io.Serializable;

public class MatchHistoryItem implements Serializable {
    private final long id;
    private final long userId;
    private final int levelId;
    private final String modeLabel;
    private final String difficultyName;
    private final int score;
    private final int correctCount;
    private final int totalQuestions;
    private final int stars;
    private final String playedAt;

    public MatchHistoryItem(long id, long userId, int levelId, String modeLabel,
                            String difficultyName, int score, int correctCount,
                            int totalQuestions, int stars, String playedAt) {
        this.id = id;
        this.userId = userId;
        this.levelId = levelId;
        this.modeLabel = modeLabel;
        this.difficultyName = difficultyName;
        this.score = score;
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.stars = stars;
        this.playedAt = playedAt;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public int getLevelId() {
        return levelId;
    }

    public String getModeLabel() {
        return modeLabel;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getStars() {
        return stars;
    }

    public String getPlayedAt() {
        return playedAt;
    }
}
