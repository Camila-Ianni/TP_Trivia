package com.example.triviamaster.models;

public class QuestItem {
    private final String id;
    private final String title;
    private final String description;
    private final String rewardLabel;
    private final int currentProgress;
    private final int targetProgress;
    private final int coinsBonus;
    private final int energyBonus;
    private final int chestBonus;
    private final boolean claimed;

    public QuestItem(String id, String title, String description, String rewardLabel,
                     int currentProgress, int targetProgress,
                     int coinsBonus, int energyBonus, int chestBonus, boolean claimed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rewardLabel = rewardLabel;
        this.currentProgress = Math.min(currentProgress, targetProgress);
        this.targetProgress = targetProgress;
        this.coinsBonus = coinsBonus;
        this.energyBonus = energyBonus;
        this.chestBonus = chestBonus;
        this.claimed = claimed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRewardLabel() {
        return rewardLabel;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getTargetProgress() {
        return targetProgress;
    }

    public int getCoinsBonus() {
        return coinsBonus;
    }

    public int getEnergyBonus() {
        return energyBonus;
    }

    public int getChestBonus() {
        return chestBonus;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public boolean isCompleted() {
        return currentProgress >= targetProgress;
    }
}
