package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.triviamaster.models.Difficulty;
import com.example.triviamaster.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class UserRepository {

    private final TriviaDbHelper dbHelper;
    private final DbExecutor executor;

    public UserRepository(Context context) {
        this.dbHelper = TriviaDbHelper.getInstance(context);
        this.executor = DbExecutor.getInstance();
    }

    public void login(String username, String password, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                    + " WHERE " + TriviaDbHelper.COL_USER_USERNAME + " = ? COLLATE NOCASE AND "
                    + TriviaDbHelper.COL_USER_PASSWORD + " = ? LIMIT 1";
            try (Cursor cursor = db.rawQuery(sql, new String[]{username.trim(), password})) {
                if (cursor.moveToFirst()) {
                    return mapCursorToUser(cursor);
                }
                return null;
            }
        }, callback);
    }

    public void registerUser(String username, String password, String email, int avatarId, DbCallback<User> callback) {
        registerUser(username, password, email, avatarId, "AR", "USER", callback);
    }

    public void registerUser(String username, String password, String email, int avatarId, String country, DbCallback<User> callback) {
        registerUser(username, password, email, avatarId, country, "USER", callback);
    }

    public void registerUser(String username, String password, String email, int avatarId, String country, String requestedRole, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanUsername = username.trim();
            String cleanEmail = email != null ? email.trim() : "";
            String cleanCountry = (country != null && !country.trim().isEmpty()) ? country.trim() : "AR";

            if (usernameExistsInternal(db, cleanUsername, -1)) {
                throw new DuplicateRecordException("USERNAME_EXISTS");
            }

            String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            String role;
            if ("mimi".equalsIgnoreCase(cleanUsername)) {
                role = "ADMIN";
            } else if ("TEACHER".equalsIgnoreCase(requestedRole)) {
                role = "TEACHER";
            } else {
                role = "USER";
            }
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_USERNAME, cleanUsername);
            values.put(TriviaDbHelper.COL_USER_PASSWORD, password);
            values.put(TriviaDbHelper.COL_USER_EMAIL, cleanEmail);
            values.put(TriviaDbHelper.COL_USER_AVATAR_ID, avatarId);
            values.put(TriviaDbHelper.COL_USER_HIGH_SCORE, 0);
            values.put(TriviaDbHelper.COL_USER_MAX_STREAK, 0);
            values.put(TriviaDbHelper.COL_USER_MAX_LEVEL, 1);
            values.put(TriviaDbHelper.COL_USER_ENERGY, 12);
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, 1);
            values.put(TriviaDbHelper.COL_USER_LEVEL_STARS, "");
            values.put(TriviaDbHelper.COL_USER_CREATED_AT, createdAt);
            values.put(TriviaDbHelper.COL_USER_ROLE, role);
            values.put(TriviaDbHelper.COL_USER_SPECIAL_COMPLETED, "");
            values.put(TriviaDbHelper.COL_USER_COINS, 100);
            values.put(TriviaDbHelper.COL_USER_SHIELDS, 0);
            values.put(TriviaDbHelper.COL_USER_OWNED_TITLES, "");
            values.put(TriviaDbHelper.COL_USER_EQUIPPED_TITLE, "");
            values.put(TriviaDbHelper.COL_USER_CLAIMED_QUESTS, "");
            values.put(TriviaDbHelper.COL_USER_XP, 0);
            values.put(TriviaDbHelper.COL_USER_LAST_DAILY_CLAIM, "");
            values.put(TriviaDbHelper.COL_USER_OWNED_FRAMES, "");
            values.put(TriviaDbHelper.COL_USER_EQUIPPED_FRAME, "");
            values.put(TriviaDbHelper.COL_USER_DAILY_STREAK, 0);
            values.put(TriviaDbHelper.COL_USER_WRONG_QUESTION_IDS, "");
            values.put(TriviaDbHelper.COL_USER_COUNTRY, cleanCountry);

            long newId = db.insertOrThrow(TriviaDbHelper.TABLE_USERS, null, values);
            return new User(newId, cleanUsername, password, cleanEmail, avatarId,
                    0, 0, 1, 12, 1, "", createdAt, role, "",
                    100, 0, "", "", "", 0, "", "", "", 0, "", cleanCountry);
        }, callback);
    }

    public void getUserById(long userId, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            return getUserByIdInternal(db, userId);
        }, callback);
    }

    public void getUserByUsername(String username, DbCallback<User> callback) {
        executor.execute(() -> {
            if (username == null || username.trim().isEmpty()) {
                return null;
            }
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                    + " WHERE " + TriviaDbHelper.COL_USER_USERNAME + " = ? COLLATE NOCASE LIMIT 1";
            try (Cursor cursor = db.rawQuery(sql, new String[]{username.trim()})) {
                if (cursor.moveToFirst()) {
                    return mapCursorToUser(cursor);
                }
            }
            return null;
        }, callback);
    }

    public void getRandomOpponent(long excludeUserId, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                    + " WHERE " + TriviaDbHelper.COL_USER_ID + " != ?"
                    + " ORDER BY RANDOM() LIMIT 1";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(excludeUserId)})) {
                if (cursor.moveToFirst()) {
                    return mapCursorToUser(cursor);
                }
            }
            return null;
        }, callback);
    }

    public void getAllUsers(String searchQuery, DbCallback<List<User>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<User> list = new ArrayList<>();
            String cleanSearch = searchQuery != null ? searchQuery.trim() : "";
            String sql;
            String[] args;
            if (cleanSearch.isEmpty()) {
                sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                        + " ORDER BY " + TriviaDbHelper.COL_USER_ID + " DESC";
                args = null;
            } else {
                String pattern = "%" + cleanSearch + "%";
                sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                        + " WHERE " + TriviaDbHelper.COL_USER_USERNAME + " LIKE ? OR "
                        + TriviaDbHelper.COL_USER_EMAIL + " LIKE ?"
                        + " ORDER BY " + TriviaDbHelper.COL_USER_ID + " DESC";
                args = new String[]{pattern, pattern};
            }
            try (Cursor cursor = db.rawQuery(sql, args)) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToUser(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void getRankingUsers(DbCallback<List<User>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<User> list = new ArrayList<>();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                    + " ORDER BY " + TriviaDbHelper.COL_USER_HIGH_SCORE + " DESC, "
                    + TriviaDbHelper.COL_USER_MAX_STREAK + " DESC, "
                    + TriviaDbHelper.COL_USER_ID + " ASC";
            try (Cursor cursor = db.rawQuery(sql, null)) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToUser(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void updateUser(User user, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanUsername = user.getUsername().trim();
            if (usernameExistsInternal(db, cleanUsername, user.getId())) {
                throw new DuplicateRecordException("USERNAME_EXISTS");
            }
            user.setUsername(cleanUsername);

            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_USERNAME, cleanUsername);
            values.put(TriviaDbHelper.COL_USER_PASSWORD, user.getPassword());
            values.put(TriviaDbHelper.COL_USER_EMAIL, user.getEmail() != null ? user.getEmail().trim() : "");
            values.put(TriviaDbHelper.COL_USER_AVATAR_ID, user.getAvatarId());
            values.put(TriviaDbHelper.COL_USER_HIGH_SCORE, user.getHighScore());
            values.put(TriviaDbHelper.COL_USER_MAX_STREAK, user.getMaxStreak());
            values.put(TriviaDbHelper.COL_USER_MAX_LEVEL, user.getMaxLevel());
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            values.put(TriviaDbHelper.COL_USER_LEVEL_STARS, user.getLevelStarsSerialized());

            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(user.getId())});
            return user;
        }, callback);
    }

    public void saveMatchResult(long userId, int levelId, int earnedStars, int score,
                                int maxStreak, boolean isQuickPlay, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            boolean earnedGift = false;
            if (earnedStars > 0) {
                int prevStars = (levelId > 0 && !isQuickPlay) ? user.getLevelStars(levelId) : 0;
                if (levelId > 0 && !isQuickPlay) {
                    user.setLevelStars(levelId, earnedStars);
                    if (levelId >= user.getMaxLevel()) {
                        user.setMaxLevel(Math.min(17, levelId + 1));
                    }
                }
                if (prevStars == 0 || earnedStars > prevStars || isQuickPlay) {
                    earnedGift = true;
                }
            }
            if (score > user.getHighScore()) {
                user.setHighScore(score);
            }
            if (maxStreak > user.getMaxStreak()) {
                user.setMaxStreak(maxStreak);
            }
            if (earnedGift) {
                user.setAvailableGifts(user.getAvailableGifts() + 1);
            }

            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_HIGH_SCORE, user.getHighScore());
            values.put(TriviaDbHelper.COL_USER_MAX_STREAK, user.getMaxStreak());
            values.put(TriviaDbHelper.COL_USER_MAX_LEVEL, user.getMaxLevel());
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            values.put(TriviaDbHelper.COL_USER_LEVEL_STARS, user.getLevelStarsSerialized());

            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void claimChestReward(long userId, int energyBonus, int pointsBonus, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setAvailableGifts(Math.max(0, user.getAvailableGifts() - 1));
            user.setEnergy(user.getEnergy() + energyBonus);
            user.setHighScore(user.getHighScore() + pointsBonus);
            user.setCoins(user.getCoins() + 50);

            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            values.put(TriviaDbHelper.COL_USER_HIGH_SCORE, user.getHighScore());
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());

            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void consumeEnergy(long userId, int amount, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setEnergy(Math.max(0, user.getEnergy() - amount));
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void grantSpecialReward(long userId, int energyBonus, int extraChests, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setEnergy(user.getEnergy() + energyBonus);
            user.setAvailableGifts(user.getAvailableGifts() + extraChests);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void markSpecialCompletedAndReward(long userId, int hotspotId, Difficulty difficulty,
                                              int energyBonus, int extraChests, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            if (user.hasCompletedSpecial(hotspotId, difficulty)) {
                return user;
            }
            user.markSpecialCompleted(hotspotId, difficulty);
            user.setEnergy(user.getEnergy() + energyBonus);
            user.setAvailableGifts(user.getAvailableGifts() + extraChests);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_SPECIAL_COMPLETED, user.getSpecialCompletedSerialized());
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void grantCoins(long userId, int coinsToAdd, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setCoins(user.getCoins() + coinsToAdd);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void consumeShield(long userId, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setShields(Math.max(0, user.getShields() - 1));
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_SHIELDS, user.getShields());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void buyShopItem(long userId, String itemType, String titleKey, int price, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            if (user.getCoins() < price) {
                throw new IllegalStateException("INSUFFICIENT_COINS");
            }
            user.setCoins(user.getCoins() - price);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());
            if ("ENERGY".equals(itemType)) {
                user.setEnergy(user.getEnergy() + 5);
                values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            } else if ("SHIELD".equals(itemType)) {
                user.setShields(user.getShields() + 1);
                values.put(TriviaDbHelper.COL_USER_SHIELDS, user.getShields());
            } else if ("TITLE".equals(itemType) && titleKey != null) {
                user.addOwnedTitle(titleKey);
                user.setEquippedTitle(titleKey);
                values.put(TriviaDbHelper.COL_USER_OWNED_TITLES, user.getOwnedTitlesSerialized());
                values.put(TriviaDbHelper.COL_USER_EQUIPPED_TITLE, user.getEquippedTitle());
            } else if ("FRAME".equals(itemType) && titleKey != null) {
                user.addOwnedFrame(titleKey);
                user.setEquippedFrame(titleKey);
                values.put(TriviaDbHelper.COL_USER_OWNED_FRAMES, user.getOwnedFramesSerialized());
                values.put(TriviaDbHelper.COL_USER_EQUIPPED_FRAME, user.getEquippedFrame());
            }
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void equipTitle(long userId, String titleKey, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setEquippedTitle(titleKey != null ? titleKey : "");
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_EQUIPPED_TITLE, user.getEquippedTitle());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void claimQuestReward(long userId, String questId, int coinsBonus, int energyBonus,
                                 int chestBonus, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            if (user.hasClaimedQuest(questId)) {
                return user;
            }
            user.markQuestClaimed(questId);
            user.setCoins(user.getCoins() + coinsBonus);
            user.setEnergy(user.getEnergy() + energyBonus);
            user.setAvailableGifts(user.getAvailableGifts() + chestBonus);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_CLAIMED_QUESTS, user.getClaimedQuestsSerialized());
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            values.put(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS, user.getAvailableGifts());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void grantXpAndCheckLevelUp(long userId, int xpEarned, DbCallback<int[]> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            int oldLevel = user.getAccountLevel();
            user.setXp(user.getXp() + Math.max(0, xpEarned));
            int newLevel = user.getAccountLevel();
            int levelDiff = Math.max(0, newLevel - oldLevel);
            int levelUpCoins = levelDiff * 50;
            if (levelUpCoins > 0) {
                user.setCoins(user.getCoins() + levelUpCoins);
            }
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_XP, user.getXp());
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return new int[]{oldLevel, newLevel, levelUpCoins, user.getXp()};
        }, callback);
    }

    public void equipFrame(long userId, String frameKey, DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.setEquippedFrame(frameKey != null ? frameKey : "");
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_EQUIPPED_FRAME, user.getEquippedFrame());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void claimDailyReward(long userId, String todayDate, String yesterdayDate,
                                 DbCallback<User> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            if (todayDate.equals(user.getLastDailyClaim())) {
                return user;
            }
            int newStreak = (yesterdayDate != null && yesterdayDate.equals(user.getLastDailyClaim()))
                    ? user.getDailyStreak() + 1
                    : 1;
            int coinsBonus = Math.min(150, 30 + (newStreak - 1) * 20);
            int energyBonus = 2;
            user.setLastDailyClaim(todayDate);
            user.setDailyStreak(newStreak);
            user.setCoins(user.getCoins() + coinsBonus);
            user.setEnergy(user.getEnergy() + energyBonus);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_LAST_DAILY_CLAIM, todayDate);
            values.put(TriviaDbHelper.COL_USER_DAILY_STREAK, newStreak);
            values.put(TriviaDbHelper.COL_USER_COINS, user.getCoins());
            values.put(TriviaDbHelper.COL_USER_ENERGY, user.getEnergy());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void recordWrongQuestion(long userId, int questionId, DbCallback<User> callback) {
        executor.execute(() -> {
            if (questionId <= 0) {
                return null;
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.addWrongQuestionId(questionId);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_WRONG_QUESTION_IDS, user.getWrongQuestionIdsSerialized());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void clearWrongQuestion(long userId, int questionId, DbCallback<User> callback) {
        executor.execute(() -> {
            if (questionId <= 0) {
                return null;
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            User user = getUserByIdInternal(db, userId);
            if (user == null) {
                return null;
            }
            user.removeWrongQuestionId(questionId);
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_USER_WRONG_QUESTION_IDS, user.getWrongQuestionIdsSerialized());
            db.update(TriviaDbHelper.TABLE_USERS, values,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return user;
        }, callback);
    }

    public void deleteUser(long userId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete(TriviaDbHelper.TABLE_USERS,
                    TriviaDbHelper.COL_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return rows > 0;
        }, callback);
    }

    private User getUserByIdInternal(SQLiteDatabase db, long userId) {
        String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                + " WHERE " + TriviaDbHelper.COL_USER_ID + " = ? LIMIT 1";
        try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
            if (cursor.moveToFirst()) {
                return mapCursorToUser(cursor);
            }
            return null;
        }
    }

    private boolean usernameExistsInternal(SQLiteDatabase db, String username, long excludeUserId) {
        String sql = "SELECT " + TriviaDbHelper.COL_USER_ID + " FROM " + TriviaDbHelper.TABLE_USERS
                + " WHERE " + TriviaDbHelper.COL_USER_USERNAME + " = ? COLLATE NOCASE"
                + (excludeUserId > 0 ? " AND " + TriviaDbHelper.COL_USER_ID + " != ?" : "")
                + " LIMIT 1";
        String[] args = excludeUserId > 0
                ? new String[]{username, String.valueOf(excludeUserId)}
                : new String[]{username};
        try (Cursor cursor = db.rawQuery(sql, args)) {
            return cursor.moveToFirst();
        }
    }

    private User mapCursorToUser(Cursor c) {
        return new User(
                c.getLong(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_ID)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_USERNAME)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_PASSWORD)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_EMAIL)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_AVATAR_ID)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_HIGH_SCORE)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_MAX_STREAK)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_MAX_LEVEL)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_ENERGY)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_AVAILABLE_GIFTS)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_LEVEL_STARS)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_CREATED_AT)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_ROLE)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_SPECIAL_COMPLETED)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_COINS)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_SHIELDS)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_OWNED_TITLES)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_EQUIPPED_TITLE)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_CLAIMED_QUESTS)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_XP)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_LAST_DAILY_CLAIM)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_OWNED_FRAMES)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_EQUIPPED_FRAME)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_DAILY_STREAK)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_WRONG_QUESTION_IDS)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_COUNTRY))
        );
    }
}
