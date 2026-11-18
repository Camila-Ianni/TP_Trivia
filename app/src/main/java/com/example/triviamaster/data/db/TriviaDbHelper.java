package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.triviamaster.data.QuestionBank;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import java.util.List;

public class TriviaDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "triviamaster.db";
    public static final int DATABASE_VERSION = 11;

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_USERNAME = "username";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_AVATAR_ID = "avatar_id";
    public static final String COL_USER_HIGH_SCORE = "high_score";
    public static final String COL_USER_MAX_STREAK = "max_streak";
    public static final String COL_USER_MAX_LEVEL = "max_level";
    public static final String COL_USER_ENERGY = "energy";
    public static final String COL_USER_AVAILABLE_GIFTS = "available_gifts";
    public static final String COL_USER_LEVEL_STARS = "level_stars";
    public static final String COL_USER_CREATED_AT = "created_at";
    public static final String COL_USER_ROLE = "role";
    public static final String COL_USER_SPECIAL_COMPLETED = "special_completed";
    public static final String COL_USER_COINS = "coins";
    public static final String COL_USER_SHIELDS = "shields";
    public static final String COL_USER_OWNED_TITLES = "owned_titles";
    public static final String COL_USER_EQUIPPED_TITLE = "equipped_title";
    public static final String COL_USER_CLAIMED_QUESTS = "claimed_quests";
    public static final String COL_USER_XP = "xp";
    public static final String COL_USER_LAST_DAILY_CLAIM = "last_daily_claim";
    public static final String COL_USER_OWNED_FRAMES = "owned_frames";
    public static final String COL_USER_EQUIPPED_FRAME = "equipped_frame";
    public static final String COL_USER_DAILY_STREAK = "daily_streak";
    public static final String COL_USER_WRONG_QUESTION_IDS = "wrong_question_ids";
    public static final String COL_USER_COUNTRY = "country";

    public static final String TABLE_FRIENDSHIPS = "friendships";
    public static final String COL_F_ID = "id";
    public static final String COL_F_SENDER_ID = "sender_id";
    public static final String COL_F_RECEIVER_ID = "receiver_id";
    public static final String COL_F_STATUS = "status";
    public static final String COL_F_CREATED_AT = "created_at";

    public static final String TABLE_QUESTIONS = "questions";
    public static final String COL_Q_ID = "id";
    public static final String COL_Q_LEVEL_ID = "level_id";
    public static final String COL_Q_TEXT = "question_text";
    public static final String COL_Q_CATEGORY = "category";
    public static final String COL_Q_CATEGORY_COLOR = "category_color";
    public static final String COL_Q_OPTION_A = "option_a";
    public static final String COL_Q_OPTION_B = "option_b";
    public static final String COL_Q_OPTION_C = "option_c";
    public static final String COL_Q_OPTION_D = "option_d";
    public static final String COL_Q_CORRECT_INDEX = "correct_index";
    public static final String COL_Q_EXPLANATION = "explanation";
    public static final String COL_Q_IS_WORD_PUZZLE = "is_word_puzzle";
    public static final String COL_Q_TARGET_WORD = "target_word";
    public static final String COL_Q_CLUE_ICON_RES = "clue_icon_res";
    public static final String COL_Q_IS_CUSTOM = "is_custom";

    public static final String TABLE_MATCH_HISTORY = "match_history";
    public static final String COL_H_ID = "id";
    public static final String COL_H_USER_ID = "user_id";
    public static final String COL_H_LEVEL_ID = "level_id";
    public static final String COL_H_MODE_LABEL = "mode_label";
    public static final String COL_H_DIFFICULTY = "difficulty";
    public static final String COL_H_SCORE = "score";
    public static final String COL_H_CORRECT = "correct_count";
    public static final String COL_H_TOTAL = "total_questions";
    public static final String COL_H_STARS = "stars";
    public static final String COL_H_PLAYED_AT = "played_at";

    private static TriviaDbHelper instance;

    public static synchronized TriviaDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TriviaDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private TriviaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersSql = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_USERNAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, "
                + COL_USER_PASSWORD + " TEXT NOT NULL, "
                + COL_USER_EMAIL + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_AVATAR_ID + " INTEGER NOT NULL DEFAULT 1, "
                + COL_USER_HIGH_SCORE + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_MAX_STREAK + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_MAX_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + COL_USER_ENERGY + " INTEGER NOT NULL DEFAULT 12, "
                + COL_USER_AVAILABLE_GIFTS + " INTEGER NOT NULL DEFAULT 1, "
                + COL_USER_LEVEL_STARS + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_CREATED_AT + " TEXT NOT NULL, "
                + COL_USER_ROLE + " TEXT NOT NULL DEFAULT 'USER', "
                + COL_USER_SPECIAL_COMPLETED + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_COINS + " INTEGER NOT NULL DEFAULT 100, "
                + COL_USER_SHIELDS + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_OWNED_TITLES + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_EQUIPPED_TITLE + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_CLAIMED_QUESTS + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_XP + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_LAST_DAILY_CLAIM + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_OWNED_FRAMES + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_EQUIPPED_FRAME + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_DAILY_STREAK + " INTEGER NOT NULL DEFAULT 0, "
                + COL_USER_WRONG_QUESTION_IDS + " TEXT NOT NULL DEFAULT '', "
                + COL_USER_COUNTRY + " TEXT NOT NULL DEFAULT 'AR'"
                + ")";
        db.execSQL(createUsersSql);

        String createQuestionsSql = "CREATE TABLE " + TABLE_QUESTIONS + " ("
                + COL_Q_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_Q_LEVEL_ID + " INTEGER NOT NULL DEFAULT 1, "
                + COL_Q_TEXT + " TEXT NOT NULL, "
                + COL_Q_CATEGORY + " TEXT NOT NULL, "
                + COL_Q_CATEGORY_COLOR + " INTEGER NOT NULL, "
                + COL_Q_OPTION_A + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_OPTION_B + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_OPTION_C + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_OPTION_D + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_CORRECT_INDEX + " INTEGER NOT NULL DEFAULT 0, "
                + COL_Q_EXPLANATION + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_IS_WORD_PUZZLE + " INTEGER NOT NULL DEFAULT 0, "
                + COL_Q_TARGET_WORD + " TEXT NOT NULL DEFAULT '', "
                + COL_Q_CLUE_ICON_RES + " INTEGER NOT NULL DEFAULT 0, "
                + COL_Q_IS_CUSTOM + " INTEGER NOT NULL DEFAULT 0"
                + ")";
        db.execSQL(createQuestionsSql);
        createMatchHistoryTable(db);
        createFriendshipsTable(db);

        seedInitialQuestions(db);
    }

    private void createFriendshipsTable(SQLiteDatabase db) {
        String createFriendshipsSql = "CREATE TABLE IF NOT EXISTS " + TABLE_FRIENDSHIPS + " ("
                + COL_F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_F_SENDER_ID + " INTEGER NOT NULL, "
                + COL_F_RECEIVER_ID + " INTEGER NOT NULL, "
                + COL_F_STATUS + " TEXT NOT NULL DEFAULT 'PENDING', "
                + COL_F_CREATED_AT + " TEXT NOT NULL"
                + ")";
        db.execSQL(createFriendshipsSql);
    }

    private void createMatchHistoryTable(SQLiteDatabase db) {
        String createHistorySql = "CREATE TABLE IF NOT EXISTS " + TABLE_MATCH_HISTORY + " ("
                + COL_H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_H_USER_ID + " INTEGER NOT NULL, "
                + COL_H_LEVEL_ID + " INTEGER NOT NULL DEFAULT -1, "
                + COL_H_MODE_LABEL + " TEXT NOT NULL, "
                + COL_H_DIFFICULTY + " TEXT NOT NULL, "
                + COL_H_SCORE + " INTEGER NOT NULL DEFAULT 0, "
                + COL_H_CORRECT + " INTEGER NOT NULL DEFAULT 0, "
                + COL_H_TOTAL + " INTEGER NOT NULL DEFAULT 0, "
                + COL_H_STARS + " INTEGER NOT NULL DEFAULT 0, "
                + COL_H_PLAYED_AT + " TEXT NOT NULL"
                + ")";
        db.execSQL(createHistorySql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            createMatchHistoryTable(db);
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_ROLE + " TEXT NOT NULL DEFAULT 'USER'");
            db.execSQL("UPDATE " + TABLE_USERS + " SET " + COL_USER_ROLE + " = 'ADMIN' WHERE LOWER(" + COL_USER_USERNAME + ") = 'mimi'");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_SPECIAL_COMPLETED + " TEXT NOT NULL DEFAULT ''");
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_COINS + " INTEGER NOT NULL DEFAULT 100");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_SHIELDS + " INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_OWNED_TITLES + " TEXT NOT NULL DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_EQUIPPED_TITLE + " TEXT NOT NULL DEFAULT ''");
        }
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_CLAIMED_QUESTS + " TEXT NOT NULL DEFAULT ''");
        }
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_XP + " INTEGER NOT NULL DEFAULT 0");
        }
        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_LAST_DAILY_CLAIM + " TEXT NOT NULL DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_OWNED_FRAMES + " TEXT NOT NULL DEFAULT ''");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_EQUIPPED_FRAME + " TEXT NOT NULL DEFAULT ''");
        }
        if (oldVersion < 9) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_DAILY_STREAK + " INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_WRONG_QUESTION_IDS + " TEXT NOT NULL DEFAULT ''");
        }
        if (oldVersion < 10) {
            createFriendshipsTable(db);
        }
        if (oldVersion < 11) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_COUNTRY + " TEXT NOT NULL DEFAULT 'AR'");
        }
    }

    private void seedInitialQuestions(SQLiteDatabase db) {
        List<Level> levels = QuestionBank.INSTANCE.getAllLevels();
        for (Level level : levels) {
            for (Question q : level.getQuestions()) {
                ContentValues values = new ContentValues();
                values.put(COL_Q_LEVEL_ID, level.getId());
                values.put(COL_Q_TEXT, q.getQuestionText());
                values.put(COL_Q_CATEGORY, q.getCategory());
                values.put(COL_Q_CATEGORY_COLOR, q.getCategoryColor());
                List<String> opts = q.getOptions();
                values.put(COL_Q_OPTION_A, opts.size() > 0 ? opts.get(0) : "");
                values.put(COL_Q_OPTION_B, opts.size() > 1 ? opts.get(1) : "");
                values.put(COL_Q_OPTION_C, opts.size() > 2 ? opts.get(2) : "");
                values.put(COL_Q_OPTION_D, opts.size() > 3 ? opts.get(3) : "");
                values.put(COL_Q_CORRECT_INDEX, q.getCorrectAnswerIndex());
                values.put(COL_Q_EXPLANATION, q.getExplanation());
                values.put(COL_Q_IS_WORD_PUZZLE, q.isWordPuzzle() ? 1 : 0);
                values.put(COL_Q_TARGET_WORD, q.getTargetWord() != null ? q.getTargetWord() : "");
                values.put(COL_Q_CLUE_ICON_RES, q.getClueIconRes());
                values.put(COL_Q_IS_CUSTOM, 0);
                db.insert(TABLE_QUESTIONS, null, values);
            }
        }
    }
}
