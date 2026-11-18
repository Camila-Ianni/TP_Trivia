package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.triviamaster.models.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class QuestionRepository {

    private final TriviaDbHelper dbHelper;
    private final DbExecutor executor;

    public QuestionRepository(Context context) {
        this.dbHelper = TriviaDbHelper.getInstance(context);
        this.executor = DbExecutor.getInstance();
    }

    public void getAllQuestions(String searchQuery, int levelFilter, DbCallback<List<Question>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<Question> list = new ArrayList<>();
            String cleanSearch = searchQuery != null ? searchQuery.trim() : "";

            StringBuilder sql = new StringBuilder("SELECT * FROM " + TriviaDbHelper.TABLE_QUESTIONS + " WHERE 1=1");
            List<String> args = new ArrayList<>();

            if (levelFilter > 0) {
                sql.append(" AND ").append(TriviaDbHelper.COL_Q_LEVEL_ID).append(" = ?");
                args.add(String.valueOf(levelFilter));
            }
            if (!cleanSearch.isEmpty()) {
                String pattern = "%" + cleanSearch + "%";
                sql.append(" AND (").append(TriviaDbHelper.COL_Q_TEXT).append(" LIKE ? OR ")
                        .append(TriviaDbHelper.COL_Q_CATEGORY).append(" LIKE ?)");
                args.add(pattern);
                args.add(pattern);
            }
            sql.append(" ORDER BY ")
                    .append(TriviaDbHelper.COL_Q_IS_CUSTOM).append(" DESC, ")
                    .append(TriviaDbHelper.COL_Q_LEVEL_ID).append(" ASC, ")
                    .append(TriviaDbHelper.COL_Q_ID).append(" DESC");

            try (Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]))) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToQuestion(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void getQuestionsByLevel(int levelId, DbCallback<List<Question>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<Question> list = new ArrayList<>();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_QUESTIONS
                    + " WHERE " + TriviaDbHelper.COL_Q_LEVEL_ID + " = ?"
                    + " ORDER BY " + TriviaDbHelper.COL_Q_IS_CUSTOM + " DESC, " + TriviaDbHelper.COL_Q_ID + " ASC";
            try (Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(levelId)})) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToQuestion(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void getRandomQuestions(int count, DbCallback<List<Question>> callback) {
        getRandomQuestionsInRange(1, 16, count, callback);
    }

    public void getRandomQuestionsInRange(int minLevel, int maxLevel, int count, DbCallback<List<Question>> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            List<Question> list = new ArrayList<>();
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_QUESTIONS
                    + " WHERE " + TriviaDbHelper.COL_Q_LEVEL_ID + " BETWEEN ? AND ?"
                    + " ORDER BY RANDOM() LIMIT ?";
            try (Cursor cursor = db.rawQuery(sql,
                    new String[]{String.valueOf(minLevel), String.valueOf(maxLevel), String.valueOf(count)})) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToQuestion(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void getQuestionsByIds(List<Integer> ids, DbCallback<List<Question>> callback) {
        executor.execute(() -> {
            List<Question> list = new ArrayList<>();
            if (ids == null || ids.isEmpty()) {
                return list;
            }
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            StringBuilder placeholders = new StringBuilder();
            String[] args = new String[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) {
                    placeholders.append(",");
                }
                placeholders.append("?");
                args[i] = String.valueOf(ids.get(i));
            }
            String sql = "SELECT * FROM " + TriviaDbHelper.TABLE_QUESTIONS
                    + " WHERE " + TriviaDbHelper.COL_Q_ID + " IN (" + placeholders + ")"
                    + " ORDER BY RANDOM()";
            try (Cursor cursor = db.rawQuery(sql, args)) {
                while (cursor.moveToNext()) {
                    list.add(mapCursorToQuestion(cursor));
                }
            }
            return list;
        }, callback);
    }

    public void insertQuestion(Question question, DbCallback<Long> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanText = question.getQuestionText().trim();
            if (questionExistsInternal(db, cleanText, question.getLevelId(), -1)) {
                throw new DuplicateRecordException("QUESTION_EXISTS");
            }
            ContentValues values = toContentValues(question, true);
            return db.insertOrThrow(TriviaDbHelper.TABLE_QUESTIONS, null, values);
        }, callback);
    }

    public void updateQuestion(Question question, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanText = question.getQuestionText().trim();
            if (questionExistsInternal(db, cleanText, question.getLevelId(), question.getId())) {
                throw new DuplicateRecordException("QUESTION_EXISTS");
            }
            ContentValues values = toContentValues(question, question.isCustom());
            int rows = db.update(TriviaDbHelper.TABLE_QUESTIONS, values,
                    TriviaDbHelper.COL_Q_ID + " = ?",
                    new String[]{String.valueOf(question.getId())});
            return rows > 0;
        }, callback);
    }

    public void deleteQuestion(int questionId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete(TriviaDbHelper.TABLE_QUESTIONS,
                    TriviaDbHelper.COL_Q_ID + " = ?",
                    new String[]{String.valueOf(questionId)});
            return rows > 0;
        }, callback);
    }

    private boolean questionExistsInternal(SQLiteDatabase db, String text, int levelId, int excludeId) {
        String sql = "SELECT " + TriviaDbHelper.COL_Q_ID + " FROM " + TriviaDbHelper.TABLE_QUESTIONS
                + " WHERE " + TriviaDbHelper.COL_Q_TEXT + " = ? COLLATE NOCASE AND "
                + TriviaDbHelper.COL_Q_LEVEL_ID + " = ?"
                + (excludeId > 0 ? " AND " + TriviaDbHelper.COL_Q_ID + " != ?" : "")
                + " LIMIT 1";
        String[] args = excludeId > 0
                ? new String[]{text, String.valueOf(levelId), String.valueOf(excludeId)}
                : new String[]{text, String.valueOf(levelId)};
        try (Cursor cursor = db.rawQuery(sql, args)) {
            return cursor.moveToFirst();
        }
    }

    private ContentValues toContentValues(Question q, boolean isCustom) {
        ContentValues values = new ContentValues();
        values.put(TriviaDbHelper.COL_Q_LEVEL_ID, q.getLevelId());
        values.put(TriviaDbHelper.COL_Q_TEXT, q.getQuestionText().trim());
        values.put(TriviaDbHelper.COL_Q_CATEGORY, q.getCategory().trim());
        values.put(TriviaDbHelper.COL_Q_CATEGORY_COLOR, q.getCategoryColor());
        List<String> opts = q.getOptions();
        values.put(TriviaDbHelper.COL_Q_OPTION_A, opts != null && opts.size() > 0 ? opts.get(0).trim() : "");
        values.put(TriviaDbHelper.COL_Q_OPTION_B, opts != null && opts.size() > 1 ? opts.get(1).trim() : "");
        values.put(TriviaDbHelper.COL_Q_OPTION_C, opts != null && opts.size() > 2 ? opts.get(2).trim() : "");
        values.put(TriviaDbHelper.COL_Q_OPTION_D, opts != null && opts.size() > 3 ? opts.get(3).trim() : "");
        values.put(TriviaDbHelper.COL_Q_CORRECT_INDEX, q.getCorrectAnswerIndex());
        values.put(TriviaDbHelper.COL_Q_EXPLANATION, q.getExplanation() != null ? q.getExplanation().trim() : "");
        values.put(TriviaDbHelper.COL_Q_IS_WORD_PUZZLE, q.isWordPuzzle() ? 1 : 0);
        values.put(TriviaDbHelper.COL_Q_TARGET_WORD, q.getTargetWord() != null ? q.getTargetWord() : "");
        values.put(TriviaDbHelper.COL_Q_CLUE_ICON_RES, q.getClueIconRes());
        values.put(TriviaDbHelper.COL_Q_IS_CUSTOM, isCustom ? 1 : 0);
        return values;
    }

    private Question mapCursorToQuestion(Cursor c) {
        boolean isWordPuzzle = c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_IS_WORD_PUZZLE)) == 1;
        List<String> options;
        if (isWordPuzzle) {
            options = Collections.emptyList();
        } else {
            options = Arrays.asList(
                    c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_OPTION_A)),
                    c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_OPTION_B)),
                    c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_OPTION_C)),
                    c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_OPTION_D))
            );
        }
        return com.example.triviamaster.data.QuestionTranslator.localize(new Question(
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_ID)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_LEVEL_ID)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_TEXT)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_CATEGORY)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_CATEGORY_COLOR)),
                options,
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_CORRECT_INDEX)),
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_EXPLANATION)),
                isWordPuzzle,
                c.getString(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_TARGET_WORD)),
                0,
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_CLUE_ICON_RES)),
                c.getInt(c.getColumnIndexOrThrow(TriviaDbHelper.COL_Q_IS_CUSTOM)) == 1
        ));
    }
}
