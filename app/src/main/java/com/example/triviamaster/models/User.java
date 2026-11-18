package com.example.triviamaster.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private long id;
    private String username;
    private String password;
    private String email;
    private int avatarId;
    private int highScore;
    private int maxStreak;
    private int maxLevel;
    private int energy;
    private int availableGifts;
    private String levelStarsSerialized;
    private String createdAt;
    private String role;
    private String specialCompletedSerialized;

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, "USER", "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt, String role) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatarId = avatarId;
        this.highScore = highScore;
        this.maxStreak = maxStreak;
        this.maxLevel = maxLevel;
        this.energy = energy;
        this.availableGifts = availableGifts;
        this.levelStarsSerialized = levelStarsSerialized != null ? levelStarsSerialized : "";
        this.createdAt = createdAt != null ? createdAt : "";
        this.role = role != null && !role.trim().isEmpty() ? role : "USER";
        this.specialCompletedSerialized = specialCompletedSerialized != null ? specialCompletedSerialized : "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getAvailableGifts() {
        return availableGifts;
    }

    public void setAvailableGifts(int availableGifts) {
        this.availableGifts = availableGifts;
    }

    public String getLevelStarsSerialized() {
        return levelStarsSerialized;
    }

    public void setLevelStarsSerialized(String levelStarsSerialized) {
        this.levelStarsSerialized = levelStarsSerialized != null ? levelStarsSerialized : "";
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null && !role.trim().isEmpty() ? role : "USER";
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public String getSpecialCompletedSerialized() {
        return specialCompletedSerialized;
    }

    public void setSpecialCompletedSerialized(String specialCompletedSerialized) {
        this.specialCompletedSerialized = specialCompletedSerialized != null ? specialCompletedSerialized : "";
    }

    public boolean hasCompletedSpecial(int hotspotId, Difficulty difficulty) {
        if (specialCompletedSerialized == null || specialCompletedSerialized.isEmpty()) {
            return false;
        }
        String token = hotspotId + "_" + difficulty.name();
        for (String item : specialCompletedSerialized.split(",")) {
            if (token.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    public void markSpecialCompleted(int hotspotId, Difficulty difficulty) {
        if (hasCompletedSpecial(hotspotId, difficulty)) {
            return;
        }
        String token = hotspotId + "_" + difficulty.name();
        if (specialCompletedSerialized == null || specialCompletedSerialized.trim().isEmpty()) {
            specialCompletedSerialized = token;
        } else {
            specialCompletedSerialized = specialCompletedSerialized + "," + token;
        }
    }

    public Map<Integer, Integer> parseLevelStarsMap() {
        Map<Integer, Integer> map = new HashMap<>();
        if (levelStarsSerialized == null || levelStarsSerialized.trim().isEmpty()) {
            return map;
        }
        String[] entries = levelStarsSerialized.split(",");
        for (String entry : entries) {
            String[] pair = entry.split(":");
            if (pair.length == 2) {
                try {
                    int lvl = Integer.parseInt(pair[0].trim());
                    int stars = Integer.parseInt(pair[1].trim());
                    map.put(lvl, stars);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return map;
    }

    public int getLevelStars(int levelId) {
        Integer stars = parseLevelStarsMap().get(levelId);
        return stars != null ? stars : 0;
    }

    public void setLevelStars(int levelId, int stars) {
        Map<Integer, Integer> map = parseLevelStarsMap();
        int existing = map.containsKey(levelId) ? map.get(levelId) : 0;
        if (stars > existing) {
            map.put(levelId, stars);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(entry.getKey()).append(":").append(entry.getValue());
        }
        this.levelStarsSerialized = sb.toString();
    }

    public int getTotalStars() {
        int total = 0;
        for (int stars : parseLevelStarsMap().values()) {
            total += stars;
        }
        return total;
    }

    public boolean isMedal1Unlocked() {
        return maxLevel >= 6 || getLevelStars(5) >= 1;
    }

    public boolean isMedal2Unlocked() {
        return maxLevel >= 11 || getLevelStars(10) >= 1;
    }

    public boolean isMedal3Unlocked() {
        return maxLevel > 16 || getLevelStars(16) >= 1;
    }
}
