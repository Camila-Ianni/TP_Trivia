package com.example.triviamaster.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private int coins;
    private int shields;
    private String ownedTitlesSerialized;
    private String equippedTitle;
    private String claimedQuestsSerialized;
    private int xp;
    private String lastDailyClaim;
    private String ownedFramesSerialized;
    private String equippedFrame;
    private int dailyStreak;
    private String wrongQuestionIdsSerialized;
    private String country;

    public static final int XP_PER_LEVEL = 250;

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, "USER", "",
                100, 0, "", "", "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt, String role) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, "",
                100, 0, "", "", "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                100, 0, "", "", "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                coins, shields, ownedTitlesSerialized, equippedTitle, "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle,
                String claimedQuestsSerialized) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                coins, shields, ownedTitlesSerialized, equippedTitle, claimedQuestsSerialized, 0);
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle,
                String claimedQuestsSerialized, int xp) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                coins, shields, ownedTitlesSerialized, equippedTitle, claimedQuestsSerialized, xp,
                "", "", "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle,
                String claimedQuestsSerialized, int xp,
                String lastDailyClaim, String ownedFramesSerialized, String equippedFrame) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                coins, shields, ownedTitlesSerialized, equippedTitle, claimedQuestsSerialized, xp,
                lastDailyClaim, ownedFramesSerialized, equippedFrame, 0, "");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle,
                String claimedQuestsSerialized, int xp,
                String lastDailyClaim, String ownedFramesSerialized, String equippedFrame,
                int dailyStreak, String wrongQuestionIdsSerialized) {
        this(id, username, password, email, avatarId, highScore, maxStreak, maxLevel,
                energy, availableGifts, levelStarsSerialized, createdAt, role, specialCompletedSerialized,
                coins, shields, ownedTitlesSerialized, equippedTitle, claimedQuestsSerialized, xp,
                lastDailyClaim, ownedFramesSerialized, equippedFrame,
                dailyStreak, wrongQuestionIdsSerialized, "AR");
    }

    public User(long id, String username, String password, String email, int avatarId,
                int highScore, int maxStreak, int maxLevel, int energy,
                int availableGifts, String levelStarsSerialized, String createdAt,
                String role, String specialCompletedSerialized,
                int coins, int shields, String ownedTitlesSerialized, String equippedTitle,
                String claimedQuestsSerialized, int xp,
                String lastDailyClaim, String ownedFramesSerialized, String equippedFrame,
                int dailyStreak, String wrongQuestionIdsSerialized, String country) {
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
        this.coins = coins;
        this.shields = shields;
        this.ownedTitlesSerialized = ownedTitlesSerialized != null ? ownedTitlesSerialized : "";
        this.equippedTitle = equippedTitle != null ? equippedTitle : "";
        this.claimedQuestsSerialized = claimedQuestsSerialized != null ? claimedQuestsSerialized : "";
        this.xp = Math.max(0, xp);
        this.lastDailyClaim = lastDailyClaim != null ? lastDailyClaim : "";
        this.ownedFramesSerialized = ownedFramesSerialized != null ? ownedFramesSerialized : "";
        this.equippedFrame = equippedFrame != null ? equippedFrame : "";
        this.dailyStreak = Math.max(0, dailyStreak);
        this.wrongQuestionIdsSerialized = wrongQuestionIdsSerialized != null ? wrongQuestionIdsSerialized : "";
        this.country = country != null && !country.trim().isEmpty() ? country.trim() : "AR";
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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = Math.max(0, coins);
    }

    public int getShields() {
        return shields;
    }

    public void setShields(int shields) {
        this.shields = Math.max(0, shields);
    }

    public String getOwnedTitlesSerialized() {
        return ownedTitlesSerialized;
    }

    public void setOwnedTitlesSerialized(String ownedTitlesSerialized) {
        this.ownedTitlesSerialized = ownedTitlesSerialized != null ? ownedTitlesSerialized : "";
    }

    public String getEquippedTitle() {
        return equippedTitle;
    }

    public void setEquippedTitle(String equippedTitle) {
        this.equippedTitle = equippedTitle != null ? equippedTitle : "";
    }

    public boolean ownsTitle(String titleKey) {
        if (ownedTitlesSerialized == null || ownedTitlesSerialized.isEmpty() || titleKey == null) {
            return false;
        }
        for (String item : ownedTitlesSerialized.split(",")) {
            if (titleKey.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    public void addOwnedTitle(String titleKey) {
        if (titleKey == null || titleKey.trim().isEmpty() || ownsTitle(titleKey)) {
            return;
        }
        if (ownedTitlesSerialized == null || ownedTitlesSerialized.trim().isEmpty()) {
            ownedTitlesSerialized = titleKey.trim();
        } else {
            ownedTitlesSerialized = ownedTitlesSerialized + "," + titleKey.trim();
        }
    }

    public String getClaimedQuestsSerialized() {
        return claimedQuestsSerialized;
    }

    public boolean hasClaimedQuest(String questId) {
        if (claimedQuestsSerialized == null || claimedQuestsSerialized.isEmpty() || questId == null) {
            return false;
        }
        for (String item : claimedQuestsSerialized.split(",")) {
            if (questId.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    public void markQuestClaimed(String questId) {
        if (questId == null || questId.trim().isEmpty() || hasClaimedQuest(questId)) {
            return;
        }
        if (claimedQuestsSerialized == null || claimedQuestsSerialized.trim().isEmpty()) {
            claimedQuestsSerialized = questId.trim();
        } else {
            claimedQuestsSerialized = claimedQuestsSerialized + "," + questId.trim();
        }
    }

    public int getCompletedSpecialCount() {
        if (specialCompletedSerialized == null || specialCompletedSerialized.trim().isEmpty()) {
            return 0;
        }
        int count = 0;
        for (String item : specialCompletedSerialized.split(",")) {
            if (!item.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = Math.max(0, xp);
    }

    public int getAccountLevel() {
        return (xp / XP_PER_LEVEL) + 1;
    }

    public int getXpIntoCurrentLevel() {
        return xp % XP_PER_LEVEL;
    }

    public String getLastDailyClaim() {
        return lastDailyClaim;
    }

    public void setLastDailyClaim(String lastDailyClaim) {
        this.lastDailyClaim = lastDailyClaim != null ? lastDailyClaim : "";
    }

    public String getOwnedFramesSerialized() {
        return ownedFramesSerialized;
    }

    public String getEquippedFrame() {
        return equippedFrame;
    }

    public void setEquippedFrame(String equippedFrame) {
        this.equippedFrame = equippedFrame != null ? equippedFrame : "";
    }

    public boolean ownsFrame(String frameKey) {
        if (ownedFramesSerialized == null || ownedFramesSerialized.isEmpty() || frameKey == null) {
            return false;
        }
        for (String item : ownedFramesSerialized.split(",")) {
            if (frameKey.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    public void addOwnedFrame(String frameKey) {
        if (frameKey == null || frameKey.trim().isEmpty() || ownsFrame(frameKey)) {
            return;
        }
        if (ownedFramesSerialized == null || ownedFramesSerialized.trim().isEmpty()) {
            ownedFramesSerialized = frameKey.trim();
        } else {
            ownedFramesSerialized = ownedFramesSerialized + "," + frameKey.trim();
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

    public int getDailyStreak() {
        return dailyStreak;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = Math.max(0, dailyStreak);
    }

    public String getWrongQuestionIdsSerialized() {
        return wrongQuestionIdsSerialized;
    }

    public void setWrongQuestionIdsSerialized(String wrongQuestionIdsSerialized) {
        this.wrongQuestionIdsSerialized = wrongQuestionIdsSerialized != null ? wrongQuestionIdsSerialized : "";
    }

    public List<Integer> getWrongQuestionIdsList() {
        List<Integer> ids = new ArrayList<>();
        if (wrongQuestionIdsSerialized == null || wrongQuestionIdsSerialized.trim().isEmpty()) {
            return ids;
        }
        String[] parts = wrongQuestionIdsSerialized.split(",");
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                try {
                    int qId = Integer.parseInt(trimmed);
                    if (!ids.contains(qId)) {
                        ids.add(qId);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return ids;
    }

    public void addWrongQuestionId(int questionId) {
        if (questionId <= 0) {
            return;
        }
        List<Integer> ids = getWrongQuestionIdsList();
        if (!ids.contains(questionId)) {
            ids.add(questionId);
            serializeWrongQuestionIds(ids);
        }
    }

    public void removeWrongQuestionId(int questionId) {
        List<Integer> ids = getWrongQuestionIdsList();
        if (ids.remove(Integer.valueOf(questionId))) {
            serializeWrongQuestionIds(ids);
        }
    }

    private void serializeWrongQuestionIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (Integer idVal : ids) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(idVal);
        }
        this.wrongQuestionIdsSerialized = sb.toString();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country != null && !country.trim().isEmpty() ? country.trim() : "AR";
    }

    public boolean isTeacher() {
        return "TEACHER".equalsIgnoreCase(role) || isAdmin();
    }
}
