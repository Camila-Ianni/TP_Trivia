package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.triviamaster.models.MatchHistoryItem;
import java.util.ArrayList;
import java.util.List;

public final class MatchHistoryRepository {

    private final TriviaDbHelper dbHelper;
    private final DbExecutor executor;

    public MatchHistoryRepository(Context context) {
        this.dbHelper = TriviaDbHelper.getInstance(context);
        this.executor = DbExecutor.getInstance();
    }

    public void insertMatch(MatchHistoryItem item, DbCallback<Long> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_H_USER_ID, item.getUserId());
            values.put(TriviaDbHelper.COL_H_LEVEL_ID, item.getLevelId());
            values.put(TriviaDbHelper.COL_H_MODE_LABEL, item.getModeLabel());
            values.put(TriviaDbHelper.COL_H_DIFFICULTY, item.getDifficultyName());
            values.put(TriviaDbHelper.COL_H_SCORE, item.getScore());
            values.put(TriviaDbHelper.COL_H_CORRECT, item.getCorrectCount());
            values.put(TriviaDbHelper.COL_H_TOTAL, item.getTotalQuestions());
            values.put(TriviaDbHelper.COL_H_STARS, item.getStars());
            values.put(TriviaDbHelper.COL_H_PLAYED_AT, item.getPlayedAt());
            return db.insertOrThrow(TriviaDbHelper.TABLE_MATCH_HISTORY, null, values);
        }, callback);
    }

    public void getHistoryForUser(long userId, DbCallback<List<MatchHistoryItem>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<MatchHistoryItem> list = new ArrayList<>();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_MATCH_HISTORY
                    + " WHERE " + TriviaDbHelper.COL_H_USER_ID + " = ?"
                    + " ORDER BY " + TriviaDbHelper.COL_H_ID + " DESC LIMIT 50";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
                while (cursor.moveToNext()) {
                    list.add(new MatchHistoryItem(
                            cursor.getLong(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_ID)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_USER_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_LEVEL_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_MODE_LABEL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_DIFFICULTY)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_SCORE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_CORRECT)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_TOTAL)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_STARS)),
                            cursor.getString(cursor.getColumnIndexOrThrow(TriviaDbHelper.COL_H_PLAYED_AT))
                    ));
                }
            }
            return list;
        }, callback);
    }

    public void deleteMatch(long historyId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete(TriviaDbHelper.TABLE_MATCH_HISTORY,
                    TriviaDbHelper.COL_H_ID + " = ?",
                    new String[]{String.valueOf(historyId)});
            return rows > 0;
        }, callback);
    }

    public void clearHistoryForUser(long userId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TriviaDbHelper.TABLE_MATCH_HISTORY,
                    TriviaDbHelper.COL_H_USER_ID + " = ?",
                    new String[]{String.valueOf(userId)});
            return true;
        }, callback);
    }

    public void getUserStatsSummary(long userId, DbCallback<int[]> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT COUNT(*), COALESCE(SUM(" + TriviaDbHelper.COL_H_CORRECT
                    + "), 0), COALESCE(SUM(" + TriviaDbHelper.COL_H_TOTAL
                    + "), 0) FROM " + TriviaDbHelper.TABLE_MATCH_HISTORY
                    + " WHERE " + TriviaDbHelper.COL_H_USER_ID + " = ?";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)})) {
                if (cursor.moveToFirst()) {
                    int totalMatches = cursor.getInt(0);
                    int totalCorrect = cursor.getInt(1);
                    int totalQuestions = cursor.getInt(2);
                    int accuracy = totalQuestions > 0 ? (totalCorrect * 100) / totalQuestions : 0;
                    return new int[]{totalMatches, accuracy};
                }
            }
            return new int[]{0, 0};
        }, callback);
    }

    public void getQuestProgressMetrics(long userId, DbCallback<int[]> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            int totalMatches = 0;
            int hardWins = 0;
            String sqlTotal = "SELECT COUNT(*) FROM " + TriviaDbHelper.TABLE_MATCH_HISTORY
                    + " WHERE " + TriviaDbHelper.COL_H_USER_ID + " = ?";
            try (Cursor cursor = db.rawQuery(sqlTotal, new String[]{String.valueOf(userId)})) {
                if (cursor.moveToFirst()) {
                    totalMatches = cursor.getInt(0);
                }
            }
            String sqlHard = "SELECT COUNT(*) FROM " + TriviaDbHelper.TABLE_MATCH_HISTORY
                    + " WHERE " + TriviaDbHelper.COL_H_USER_ID + " = ? AND "
                    + TriviaDbHelper.COL_H_DIFFICULTY + " = 'Leyenda' AND "
                    + TriviaDbHelper.COL_H_STARS + " > 0";
            try (Cursor cursor = db.rawQuery(sqlHard, new String[]{String.valueOf(userId)})) {
                if (cursor.moveToFirst()) {
                    hardWins = cursor.getInt(0);
                }
            }
            return new int[]{totalMatches, hardWins};
        }, callback);
    }
}
