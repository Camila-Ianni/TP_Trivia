package com.example.triviamaster.models;

public class RankingItem {
    private final String playerName;
    private final int avatarResId;
    private final int score;
    private final int maxStreak;
    private final String date;

    public RankingItem(String playerName, int avatarResId, int score, int maxStreak, String date) {
        this.playerName = playerName;
        this.avatarResId = avatarResId;
        this.score = score;
        this.maxStreak = maxStreak;
        this.date = date;
    }

    public String getPlayerName() { return playerName; }
    public int getAvatarResId() { return avatarResId; }
    public int getScore() { return score; }
    public int getMaxStreak() { return maxStreak; }
    public String getDate() { return date; }
}
