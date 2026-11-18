package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FriendshipRepository {

    public static final String RESULT_SENT = "SENT";
    public static final String RESULT_AUTO_ACCEPTED = "AUTO_ACCEPTED";
    public static final String RESULT_ALREADY_FRIENDS = "ALREADY_FRIENDS";
    public static final String RESULT_ALREADY_PENDING = "ALREADY_PENDING";

    private final TriviaDbHelper dbHelper;
    private final DbExecutor executor;

    public FriendshipRepository(Context context) {
        this.dbHelper = TriviaDbHelper.getInstance(context);
        this.executor = DbExecutor.getInstance();
    }

    public void sendFriendRequest(long senderId, long receiverId, DbCallback<String> callback) {
        executor.execute(() -> {
            if (senderId <= 0 || receiverId <= 0 || senderId == receiverId) {
                return RESULT_ALREADY_PENDING;
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "SELECT " + TriviaDbHelper.COL_F_ID + ", "
                    + TriviaDbHelper.COL_F_SENDER_ID + ", "
                    + TriviaDbHelper.COL_F_STATUS
                    + " FROM " + TriviaDbHelper.TABLE_FRIENDSHIPS
                    + " WHERE (" + TriviaDbHelper.COL_F_SENDER_ID + " = ? AND " + TriviaDbHelper.COL_F_RECEIVER_ID + " = ?)"
                    + " OR (" + TriviaDbHelper.COL_F_SENDER_ID + " = ? AND " + TriviaDbHelper.COL_F_RECEIVER_ID + " = ?)"
                    + " LIMIT 1";
            try (Cursor c = db.rawQuery(sql, new String[]{
                    String.valueOf(senderId), String.valueOf(receiverId),
                    String.valueOf(receiverId), String.valueOf(senderId)})) {
                if (c.moveToFirst()) {
                    long existingId = c.getLong(0);
                    long existingSender = c.getLong(1);
                    String status = c.getString(2);
                    if ("ACCEPTED".equalsIgnoreCase(status)) {
                        return RESULT_ALREADY_FRIENDS;
                    }
                    if (existingSender == receiverId) {
                        ContentValues update = new ContentValues();
                        update.put(TriviaDbHelper.COL_F_STATUS, "ACCEPTED");
                        db.update(TriviaDbHelper.TABLE_FRIENDSHIPS, update,
                                TriviaDbHelper.COL_F_ID + " = ?",
                                new String[]{String.valueOf(existingId)});
                        return RESULT_AUTO_ACCEPTED;
                    }
                    return RESULT_ALREADY_PENDING;
                }
            }

            String now = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_F_SENDER_ID, senderId);
            values.put(TriviaDbHelper.COL_F_RECEIVER_ID, receiverId);
            values.put(TriviaDbHelper.COL_F_STATUS, "PENDING");
            values.put(TriviaDbHelper.COL_F_CREATED_AT, now);
            db.insertOrThrow(TriviaDbHelper.TABLE_FRIENDSHIPS, null, values);
            return RESULT_SENT;
        }, callback);
    }

    public void acceptFriendRequest(long friendshipId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_F_STATUS, "ACCEPTED");
            int rows = db.update(TriviaDbHelper.TABLE_FRIENDSHIPS, values,
                    TriviaDbHelper.COL_F_ID + " = ?",
                    new String[]{String.valueOf(friendshipId)});
            return rows > 0;
        }, callback);
    }

    public void removeFriendship(long friendshipId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete(TriviaDbHelper.TABLE_FRIENDSHIPS,
                    TriviaDbHelper.COL_F_ID + " = ?",
                    new String[]{String.valueOf(friendshipId)});
            return rows > 0;
        }, callback);
    }

    public void getFriendsAndRequests(long currentUserId, DbCallback<List<Friendship>> callback) {
        executor.execute(() -> {
            List<Friendship> list = new ArrayList<>();
            if (currentUserId <= 0) {
                return list;
            }
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT f." + TriviaDbHelper.COL_F_ID
                    + ", f." + TriviaDbHelper.COL_F_SENDER_ID
                    + ", f." + TriviaDbHelper.COL_F_RECEIVER_ID
                    + ", f." + TriviaDbHelper.COL_F_STATUS
                    + ", u." + TriviaDbHelper.COL_USER_ID
                    + ", u." + TriviaDbHelper.COL_USER_USERNAME
                    + ", u." + TriviaDbHelper.COL_USER_AVATAR_ID
                    + ", u." + TriviaDbHelper.COL_USER_HIGH_SCORE
                    + ", u." + TriviaDbHelper.COL_USER_MAX_STREAK
                    + " FROM " + TriviaDbHelper.TABLE_FRIENDSHIPS + " f"
                    + " INNER JOIN " + TriviaDbHelper.TABLE_USERS + " u"
                    + " ON u." + TriviaDbHelper.COL_USER_ID + " = CASE WHEN f." + TriviaDbHelper.COL_F_SENDER_ID
                    + " = ? THEN f." + TriviaDbHelper.COL_F_RECEIVER_ID
                    + " ELSE f." + TriviaDbHelper.COL_F_SENDER_ID + " END"
                    + " WHERE f." + TriviaDbHelper.COL_F_SENDER_ID + " = ? OR f." + TriviaDbHelper.COL_F_RECEIVER_ID + " = ?"
                    + " ORDER BY f." + TriviaDbHelper.COL_F_ID + " DESC";
            String idStr = String.valueOf(currentUserId);
            try (Cursor c = db.rawQuery(sql, new String[]{idStr, idStr, idStr})) {
                while (c.moveToNext()) {
                    long fId = c.getLong(0);
                    long senderId = c.getLong(1);
                    long receiverId = c.getLong(2);
                    String status = c.getString(3);
                    long otherId = c.getLong(4);
                    String otherName = c.getString(5);
                    int otherAvatar = c.getInt(6);
                    int otherScore = c.getInt(7);
                    int otherStreak = c.getInt(8);
                    boolean isIncoming = receiverId == currentUserId && "PENDING".equalsIgnoreCase(status);
                    list.add(new Friendship(fId, senderId, receiverId, otherId,
                            otherName, otherAvatar, otherScore, otherStreak, status, isIncoming));
                }
            }
            return list;
        }, callback);
    }

    public void searchUsersToAdd(long currentUserId, String query, DbCallback<List<User>> callback) {
        executor.execute(() -> {
            List<User> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String clean = query != null ? query.trim() : "";
            String sql;
            String[] args;
            if (clean.isEmpty()) {
                sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                        + " WHERE " + TriviaDbHelper.COL_USER_ID + " != ?"
                        + " ORDER BY " + TriviaDbHelper.COL_USER_HIGH_SCORE + " DESC LIMIT 20";
                args = new String[]{String.valueOf(currentUserId)};
            } else {
                sql = "SELECT * FROM " + TriviaDbHelper.TABLE_USERS
                        + " WHERE " + TriviaDbHelper.COL_USER_ID + " != ?"
                        + " AND " + TriviaDbHelper.COL_USER_USERNAME + " LIKE ?"
                        + " ORDER BY " + TriviaDbHelper.COL_USER_HIGH_SCORE + " DESC LIMIT 20";
                args = new String[]{String.valueOf(currentUserId), "%" + clean + "%"};
            }
            try (Cursor c = db.rawQuery(sql, args)) {
                while (c.moveToNext()) {
                    list.add(new User(
                            c.getLong(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_ID)),
                            c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_USER_USERNAME)),
                            "",
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
                    ));
                }
            }
            return list;
        }, callback);
    }
}
