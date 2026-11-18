package com.example.triviamaster.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.triviamaster.R;
import com.example.triviamaster.models.Avatar;
import com.example.triviamaster.models.Difficulty;
import com.example.triviamaster.models.RankingItem;
import com.example.triviamaster.models.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PreferencesManager {
    private static final String PREFS = "trivia_master_prefs";
    private static final String KEY_NAME = "last_player_name";
    private static final String KEY_AVATAR = "last_avatar_id";
    private static final String KEY_SCORE = "high_score";
    private static final String KEY_RANKING = "ranking_data";
    private static final String KEY_LEVEL = "max_unlocked_level";
    private static final String KEY_STARS = "level_stars_";
    private static final String KEY_GIFT = "gift_claimed";
    private static final String KEY_GIFTS = "available_gifts";
    private static final String KEY_GIFT_INIT = "first_run_gift_init";
    private static final String KEY_MEDAL_1 = "medal_1_notified";
    private static final String KEY_MEDAL_2 = "medal_2_notified";
    private static final String KEY_MEDAL_3 = "medal_3_notified";
    private static final String KEY_ENERGY = "player_energy";
    private static final String KEY_CURRENT_USER_ID = "current_user_id";
    private static final String KEY_GUIDE_SEEN = "game_guide_seen_";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_USER_ROLE = "current_user_role";
    private static final String KEY_SPECIAL_COMPLETED = "special_completed";

    private static final List<Avatar> AVAILABLE_AVATARS = Arrays.asList(
            new Avatar(1, "Wincky", R.drawable.img_avatar_perfil1, "#E0F7FA"),
            new Avatar(2, "Bloop", R.drawable.img_avatar_perfil2, "#FFF0F4"),
            new Avatar(3, "Chomp", R.drawable.img_avatar_perfil3, "#FFF4E6"),
            new Avatar(4, "Zook", R.drawable.img_avatar_perfil4, "#F3EFFF"));

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static List<Avatar> getAvailableAvatars() {
        return AVAILABLE_AVATARS;
    }

    public long getCurrentUserId() {
        return prefs.getLong(KEY_CURRENT_USER_ID, -1L);
    }

    public void setCurrentUserId(long userId) {
        prefs.edit().putLong(KEY_CURRENT_USER_ID, userId).apply();
    }

    public boolean hasSeenGameGuide(long userId) {
        String key = KEY_GUIDE_SEEN + (userId > 0 ? userId : 0L);
        return prefs.getBoolean(key, false);
    }

    public void setGameGuideSeen(long userId) {
        String key = KEY_GUIDE_SEEN + (userId > 0 ? userId : 0L);
        prefs.edit().putBoolean(key, true).apply();
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public boolean isVibrationEnabled() {
        return prefs.getBoolean(KEY_VIBRATION_ENABLED, true);
    }

    public void setVibrationEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }

    public boolean isCurrentUserAdmin() {
        return "ADMIN".equalsIgnoreCase(prefs.getString(KEY_USER_ROLE, "USER"));
    }

    public boolean isSpecialCompleted(int hotspotId, Difficulty difficulty) {
        if (hotspotId < 0 || difficulty == null) {
            return false;
        }
        String raw = prefs.getString(KEY_SPECIAL_COMPLETED, "");
        if (raw.isEmpty()) {
            return false;
        }
        String token = hotspotId + "_" + difficulty.name();
        for (String item : raw.split(",")) {
            if (token.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    public void markSpecialCompleted(int hotspotId, Difficulty difficulty) {
        if (hotspotId < 0 || difficulty == null || isSpecialCompleted(hotspotId, difficulty)) {
            return;
        }
        String raw = prefs.getString(KEY_SPECIAL_COMPLETED, "");
        String token = hotspotId + "_" + difficulty.name();
        String updated = raw.trim().isEmpty() ? token : raw + "," + token;
        prefs.edit().putString(KEY_SPECIAL_COMPLETED, updated).apply();
    }

    public boolean isAllSpecialCompleted(int hotspotId) {
        return isSpecialCompleted(hotspotId, Difficulty.EASY)
                && isSpecialCompleted(hotspotId, Difficulty.NORMAL)
                && isSpecialCompleted(hotspotId, Difficulty.HARD);
    }

    public void syncFromUser(User user) {
        if (user == null) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_CURRENT_USER_ID, user.getId());
        editor.putString(KEY_NAME, user.getUsername());
        editor.putInt(KEY_AVATAR, user.getAvatarId());
        editor.putInt(KEY_SCORE, user.getHighScore());
        editor.putInt(KEY_LEVEL, user.getMaxLevel());
        editor.putInt(KEY_ENERGY, user.getEnergy());
        editor.putInt(KEY_GIFTS, user.getAvailableGifts());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.putString(KEY_SPECIAL_COMPLETED, user.getSpecialCompletedSerialized());
        editor.putBoolean(KEY_GIFT_INIT, true);
        for (int i = 1; i <= 16; i++) {
            editor.putInt(KEY_STARS + i, user.getLevelStars(i));
        }
        editor.apply();
    }

    public void clearSession() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_CURRENT_USER_ID, -1L);
        editor.putString(KEY_NAME, "");
        editor.putInt(KEY_AVATAR, 1);
        editor.putInt(KEY_SCORE, 0);
        editor.putInt(KEY_LEVEL, 1);
        editor.putInt(KEY_ENERGY, 12);
        editor.putInt(KEY_GIFTS, 1);
        editor.putString(KEY_USER_ROLE, "USER");
        editor.putString(KEY_SPECIAL_COMPLETED, "");
        editor.putBoolean(KEY_MEDAL_1, false);
        editor.putBoolean(KEY_MEDAL_2, false);
        editor.putBoolean(KEY_MEDAL_3, false);
        for (int i = 1; i <= 16; i++) {
            editor.putInt(KEY_STARS + i, 0);
        }
        editor.apply();
    }

    public String getLastPlayerName() {
        return prefs.getString(KEY_NAME, "");
    }

    public void setLastPlayerName(String value) {
        prefs.edit().putString(KEY_NAME, value).apply();
    }

    public int getLastAvatarId() {
        return prefs.getInt(KEY_AVATAR, 1);
    }

    public void setLastAvatarId(int value) {
        prefs.edit().putInt(KEY_AVATAR, value).apply();
    }

    public int getHighScore() {
        return prefs.getInt(KEY_SCORE, 0);
    }

    public void setHighScore(int value) {
        prefs.edit().putInt(KEY_SCORE, value).apply();
    }

    public int getMaxUnlockedLevel() {
        return prefs.getInt(KEY_LEVEL, 1);
    }

    public void setMaxUnlockedLevel(int value) {
        prefs.edit().putInt(KEY_LEVEL, value).apply();
    }

    public boolean isGiftClaimed() {
        return prefs.getBoolean(KEY_GIFT, false);
    }

    public void setGiftClaimed(boolean value) {
        prefs.edit().putBoolean(KEY_GIFT, value).apply();
    }

    public int getAvailableGifts() {
        if (!prefs.getBoolean(KEY_GIFT_INIT, false)) {
            prefs.edit().putBoolean(KEY_GIFT_INIT, true).putInt(KEY_GIFTS, 1).apply();
            return 1;
        }
        return prefs.getInt(KEY_GIFTS, 0);
    }

    public void setAvailableGifts(int value) {
        prefs.edit().putInt(KEY_GIFTS, Math.max(0, value)).apply();
    }

    public int getPlayerEnergy() {
        return prefs.getInt(KEY_ENERGY, 12);
    }

    public void setPlayerEnergy(int value) {
        prefs.edit().putInt(KEY_ENERGY, Math.max(0, value)).apply();
    }

    public void addChestRewards() {
        addChestRewards(1, 100);
    }

    public void addChestRewards(int energyBonus, int pointsBonus) {
        setPlayerEnergy(getPlayerEnergy() + energyBonus);
        setHighScore(getHighScore() + pointsBonus);
        List<RankingItem> rankings = new ArrayList<>(getRankings());
        String key = normalize(getLastPlayerName());
        int avatar = getAvatarById(getLastAvatarId()).getIconResId();
        int index = findPlayer(rankings, key);
        if (index >= 0) {
            RankingItem old = rankings.get(index);
            rankings.set(index, new RankingItem(old.getPlayerName(), avatar,
                    Math.max(old.getScore(), getHighScore()), old.getMaxStreak(), old.getDate()));
        } else if (!getLastPlayerName().trim().isEmpty()) {
            rankings.add(new RankingItem(getLastPlayerName(), avatar, getHighScore(), 1, "Hoy"));
        }
        saveRankings(rankings);
    }

    public boolean isMedal1Unlocked() {
        return getMaxUnlockedLevel() >= 6 || getLevelStars(5) >= 1;
    }

    public boolean isMedal2Unlocked() {
        return getMaxUnlockedLevel() >= 11 || getLevelStars(10) >= 1;
    }

    public boolean isMedal3Unlocked() {
        return getMaxUnlockedLevel() > 16 || getLevelStars(16) >= 1;
    }

    public boolean isMedal1Notified() {
        return prefs.getBoolean(KEY_MEDAL_1, false);
    }

    public void setMedal1Notified(boolean value) {
        prefs.edit().putBoolean(KEY_MEDAL_1, value).apply();
    }

    public boolean isMedal2Notified() {
        return prefs.getBoolean(KEY_MEDAL_2, false);
    }

    public void setMedal2Notified(boolean value) {
        prefs.edit().putBoolean(KEY_MEDAL_2, value).apply();
    }

    public boolean isMedal3Notified() {
        return prefs.getBoolean(KEY_MEDAL_3, false);
    }

    public void setMedal3Notified(boolean value) {
        prefs.edit().putBoolean(KEY_MEDAL_3, value).apply();
    }

    public int getLevelStars(int levelId) {
        return prefs.getInt(KEY_STARS + levelId, 0);
    }

    public void saveLevelProgress(int levelId, int stars, int score) {
        if (stars > getLevelStars(levelId)) {
            prefs.edit().putInt(KEY_STARS + levelId, stars).apply();
        }
        if (stars >= 1 && levelId >= getMaxUnlockedLevel()) {
            setMaxUnlockedLevel(levelId + 1);
        }
    }

    public int getTotalStars() {
        int total = 0;
        for (int i = 1; i <= 16; i++) {
            total += getLevelStars(i);
        }
        return total;
    }

    public Avatar getAvatarById(int id) {
        for (Avatar avatar : AVAILABLE_AVATARS) {
            if (avatar.getId() == id) {
                return avatar;
            }
        }
        return AVAILABLE_AVATARS.get(0);
    }

    public void updatePlayerProfile(String newName, int newAvatarId) {
        String oldName = getLastPlayerName();
        setLastPlayerName(newName);
        setLastAvatarId(newAvatarId);
        int avatar = getAvatarById(newAvatarId).getIconResId();
        Map<String, RankingItem> users = new HashMap<>();
        for (RankingItem item : getRankings()) {
            String key = normalize(item.getPlayerName());
            if (key.equals(normalize(oldName)) || key.equals(normalize(newName))) {
                key = normalize(newName);
            }
            RankingItem current = users.get(key);
            users.put(key, merge(current, item, key.equals(normalize(newName)) ? newName : item.getPlayerName(),
                    key.equals(normalize(newName)) ? avatar : item.getAvatarResId()));
        }
        saveRankings(new ArrayList<>(users.values()));
    }

    public boolean saveGameScore(String name, int avatarResId, int score, int maxStreak, String date) {
        boolean newHighScore = score > getHighScore();
        if (newHighScore) {
            setHighScore(score);
        }
        Map<String, RankingItem> users = new HashMap<>();
        for (RankingItem item : getRankings()) {
            users.put(normalize(item.getPlayerName()), item);
        }
        String key = normalize(name);
        RankingItem current = users.get(key);
        users.put(key, merge(current, new RankingItem(name, avatarResId, score, maxStreak, date), name, avatarResId));
        saveRankings(new ArrayList<>(users.values()));
        return newHighScore;
    }

    public List<RankingItem> getRankings() {
        Set<String> bots = new HashSet<>(Arrays.asList(
                "pixel master", "hazel explorer", "orange champ", "aqua quest", "star runner", "retro player"));
        String playerKey = normalize(getLastPlayerName());
        Map<String, RankingItem> users = new HashMap<>();
        String raw = prefs.getString(KEY_RANKING, "");
        if (!raw.trim().isEmpty()) {
            for (String entry : raw.split(";;")) {
                String[] parts = entry.split("::", -1);
                if (parts.length < 5) continue;
                try {
                    RankingItem item = new RankingItem(parts[0], Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4]);
                    String key = normalize(item.getPlayerName());
                    if (bots.contains(key) && !key.equals(playerKey)) continue;
                    users.put(key, merge(users.get(key), item, item.getPlayerName(),
                            key.equals(playerKey) ? getAvatarById(getLastAvatarId()).getIconResId() : item.getAvatarResId()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        if (users.isEmpty() && !playerKey.isEmpty()) {
            users.put(playerKey, new RankingItem(getLastPlayerName(),
                    getAvatarById(getLastAvatarId()).getIconResId(), Math.max(getHighScore(), 1002), 5, "Hoy"));
        }
        List<RankingItem> result = new ArrayList<>(users.values());
        saveRankings(result);
        return result;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static int findPlayer(List<RankingItem> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (normalize(list.get(i).getPlayerName()).equals(key)) {
                return i;
            }
        }
        return -1;
    }

    private static RankingItem merge(RankingItem current, RankingItem incoming, String name, int avatar) {
        if (current == null) {
            return new RankingItem(name, avatar, incoming.getScore(), incoming.getMaxStreak(), incoming.getDate());
        }
        return new RankingItem(name, avatar, Math.max(current.getScore(), incoming.getScore()),
                Math.max(current.getMaxStreak(), incoming.getMaxStreak()),
                incoming.getScore() >= current.getScore() ? incoming.getDate() : current.getDate());
    }

    private void saveRankings(List<RankingItem> rankings) {
        rankings.sort(Comparator.comparingInt(RankingItem::getScore).reversed());
        StringBuilder serialized = new StringBuilder();
        for (int i = 0; i < Math.min(15, rankings.size()); i++) {
            if (i > 0) serialized.append(";;");
            RankingItem item = rankings.get(i);
            serialized.append(item.getPlayerName()).append("::").append(item.getAvatarResId())
                    .append("::").append(item.getScore()).append("::").append(item.getMaxStreak())
                    .append("::").append(item.getDate());
        }
        prefs.edit().putString(KEY_RANKING, serialized.toString()).apply();
    }
}
