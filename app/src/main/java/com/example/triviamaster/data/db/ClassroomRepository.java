package com.example.triviamaster.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.triviamaster.models.Classroom;
import com.example.triviamaster.models.ClassroomMember;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public final class ClassroomRepository {

    public static final String JOIN_SENT = "SENT";
    public static final String JOIN_NOT_FOUND = "NOT_FOUND";
    public static final String JOIN_ALREADY_MEMBER = "ALREADY_MEMBER";
    public static final String JOIN_ALREADY_PENDING = "ALREADY_PENDING";
    public static final String JOIN_IS_TEACHER = "IS_TEACHER";

    private final TriviaDbHelper dbHelper;
    private final DbExecutor executor;

    public ClassroomRepository(Context context) {
        this.dbHelper = TriviaDbHelper.getInstance(context);
        this.executor = DbExecutor.getInstance();
    }

    public void createClassroom(long teacherId, String name, DbCallback<Classroom> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanName = name != null ? name.trim() : "";
            String code = generateUniqueCode(db);
            String now = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_C_NAME, cleanName);
            values.put(TriviaDbHelper.COL_C_CODE, code);
            values.put(TriviaDbHelper.COL_C_TEACHER_ID, teacherId);
            values.put(TriviaDbHelper.COL_C_CREATED_AT, now);

            long id = db.insertOrThrow(TriviaDbHelper.TABLE_CLASSROOMS, null, values);
            return new Classroom(id, cleanName, code, teacherId, "", now, "APPROVED", 0);
        }, callback);
    }

    public void getClassroomsForTeacher(long teacherId, DbCallback<List<Classroom>> callback) {
        executor.execute(() -> {
            List<Classroom> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT c." + TriviaDbHelper.COL_C_ID
                    + ", c." + TriviaDbHelper.COL_C_NAME
                    + ", c." + TriviaDbHelper.COL_C_CODE
                    + ", c." + TriviaDbHelper.COL_C_TEACHER_ID
                    + ", c." + TriviaDbHelper.COL_C_CREATED_AT
                    + ", (SELECT COUNT(*) FROM " + TriviaDbHelper.TABLE_QUESTIONS + " q WHERE q."
                    + TriviaDbHelper.COL_Q_CLASSROOM_ID + " = c." + TriviaDbHelper.COL_C_ID + ") AS q_count"
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOMS + " c"
                    + " WHERE c." + TriviaDbHelper.COL_C_TEACHER_ID + " = ?"
                    + " ORDER BY c." + TriviaDbHelper.COL_C_ID + " DESC";
            try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(teacherId)})) {
                while (c.moveToNext()) {
                    list.add(new Classroom(
                            c.getLong(0),
                            c.getString(1),
                            c.getString(2),
                            c.getLong(3),
                            "",
                            c.getString(4),
                            "APPROVED",
                            c.getInt(5)
                    ));
                }
            }
            return list;
        }, callback);
    }

    public void getClassroomsForStudent(long studentId, DbCallback<List<Classroom>> callback) {
        executor.execute(() -> {
            List<Classroom> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT c." + TriviaDbHelper.COL_C_ID
                    + ", c." + TriviaDbHelper.COL_C_NAME
                    + ", c." + TriviaDbHelper.COL_C_CODE
                    + ", c." + TriviaDbHelper.COL_C_TEACHER_ID
                    + ", u." + TriviaDbHelper.COL_USER_USERNAME
                    + ", c." + TriviaDbHelper.COL_C_CREATED_AT
                    + ", m." + TriviaDbHelper.COL_CM_STATUS
                    + ", (SELECT COUNT(*) FROM " + TriviaDbHelper.TABLE_QUESTIONS + " q WHERE q."
                    + TriviaDbHelper.COL_Q_CLASSROOM_ID + " = c." + TriviaDbHelper.COL_C_ID + ") AS q_count"
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOM_MEMBERS + " m"
                    + " INNER JOIN " + TriviaDbHelper.TABLE_CLASSROOMS + " c"
                    + " ON c." + TriviaDbHelper.COL_C_ID + " = m." + TriviaDbHelper.COL_CM_CLASSROOM_ID
                    + " INNER JOIN " + TriviaDbHelper.TABLE_USERS + " u"
                    + " ON u." + TriviaDbHelper.COL_USER_ID + " = c." + TriviaDbHelper.COL_C_TEACHER_ID
                    + " WHERE m." + TriviaDbHelper.COL_CM_STUDENT_ID + " = ?"
                    + " ORDER BY m." + TriviaDbHelper.COL_CM_ID + " DESC";
            try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(studentId)})) {
                while (c.moveToNext()) {
                    list.add(new Classroom(
                            c.getLong(0),
                            c.getString(1),
                            c.getString(2),
                            c.getLong(3),
                            c.getString(4),
                            c.getString(5),
                            c.getString(6),
                            c.getInt(7)
                    ));
                }
            }
            return list;
        }, callback);
    }

    public void requestJoinByCode(long studentId, String code, DbCallback<String> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String cleanCode = code != null ? code.trim().toUpperCase(Locale.US) : "";
            if (cleanCode.isEmpty()) {
                return JOIN_NOT_FOUND;
            }
            long classroomId = -1L;
            long teacherId = -1L;
            String findSql = "SELECT " + TriviaDbHelper.COL_C_ID + ", " + TriviaDbHelper.COL_C_TEACHER_ID
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOMS
                    + " WHERE " + TriviaDbHelper.COL_C_CODE + " = ? COLLATE NOCASE LIMIT 1";
            try (Cursor c = db.rawQuery(findSql, new String[]{cleanCode})) {
                if (c.moveToFirst()) {
                    classroomId = c.getLong(0);
                    teacherId = c.getLong(1);
                }
            }
            if (classroomId <= 0) {
                return JOIN_NOT_FOUND;
            }
            if (teacherId == studentId) {
                return JOIN_IS_TEACHER;
            }
            String checkSql = "SELECT " + TriviaDbHelper.COL_CM_STATUS
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOM_MEMBERS
                    + " WHERE " + TriviaDbHelper.COL_CM_CLASSROOM_ID + " = ? AND "
                    + TriviaDbHelper.COL_CM_STUDENT_ID + " = ? LIMIT 1";
            try (Cursor c = db.rawQuery(checkSql, new String[]{String.valueOf(classroomId), String.valueOf(studentId)})) {
                if (c.moveToFirst()) {
                    String status = c.getString(0);
                    return "APPROVED".equalsIgnoreCase(status) ? JOIN_ALREADY_MEMBER : JOIN_ALREADY_PENDING;
                }
            }

            String now = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_CM_CLASSROOM_ID, classroomId);
            values.put(TriviaDbHelper.COL_CM_STUDENT_ID, studentId);
            values.put(TriviaDbHelper.COL_CM_STATUS, "PENDING");
            values.put(TriviaDbHelper.COL_CM_CREATED_AT, now);
            db.insertOrThrow(TriviaDbHelper.TABLE_CLASSROOM_MEMBERS, null, values);
            return JOIN_SENT;
        }, callback);
    }

    public void getApprovedStudentsForClassroom(long classroomId, DbCallback<List<ClassroomMember>> callback) {
        executor.execute(() -> {
            List<ClassroomMember> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT m." + TriviaDbHelper.COL_CM_ID
                    + ", c." + TriviaDbHelper.COL_C_ID
                    + ", c." + TriviaDbHelper.COL_C_NAME
                    + ", c." + TriviaDbHelper.COL_C_CODE
                    + ", u." + TriviaDbHelper.COL_USER_ID
                    + ", u." + TriviaDbHelper.COL_USER_USERNAME
                    + ", u." + TriviaDbHelper.COL_USER_AVATAR_ID
                    + ", m." + TriviaDbHelper.COL_CM_STATUS
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOM_MEMBERS + " m"
                    + " INNER JOIN " + TriviaDbHelper.TABLE_CLASSROOMS + " c"
                    + " ON c." + TriviaDbHelper.COL_C_ID + " = m." + TriviaDbHelper.COL_CM_CLASSROOM_ID
                    + " INNER JOIN " + TriviaDbHelper.TABLE_USERS + " u"
                    + " ON u." + TriviaDbHelper.COL_USER_ID + " = m." + TriviaDbHelper.COL_CM_STUDENT_ID
                    + " WHERE m." + TriviaDbHelper.COL_CM_CLASSROOM_ID + " = ?"
                    + " AND m." + TriviaDbHelper.COL_CM_STATUS + " = 'APPROVED'"
                    + " ORDER BY u." + TriviaDbHelper.COL_USER_USERNAME + " COLLATE NOCASE ASC";
            try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(classroomId)})) {
                while (c.moveToNext()) {
                    list.add(new ClassroomMember(
                            c.getLong(0),
                            c.getLong(1),
                            c.getString(2),
                            c.getString(3),
                            c.getLong(4),
                            c.getString(5),
                            c.getInt(6),
                            c.getString(7)
                    ));
                }
            }
            return list;
        }, callback);
    }

    public void getPendingRequestsForTeacher(long teacherId, DbCallback<List<ClassroomMember>> callback) {
        executor.execute(() -> {
            List<ClassroomMember> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "SELECT m." + TriviaDbHelper.COL_CM_ID
                    + ", c." + TriviaDbHelper.COL_C_ID
                    + ", c." + TriviaDbHelper.COL_C_NAME
                    + ", c." + TriviaDbHelper.COL_C_CODE
                    + ", u." + TriviaDbHelper.COL_USER_ID
                    + ", u." + TriviaDbHelper.COL_USER_USERNAME
                    + ", u." + TriviaDbHelper.COL_USER_AVATAR_ID
                    + ", m." + TriviaDbHelper.COL_CM_STATUS
                    + " FROM " + TriviaDbHelper.TABLE_CLASSROOM_MEMBERS + " m"
                    + " INNER JOIN " + TriviaDbHelper.TABLE_CLASSROOMS + " c"
                    + " ON c." + TriviaDbHelper.COL_C_ID + " = m." + TriviaDbHelper.COL_CM_CLASSROOM_ID
                    + " INNER JOIN " + TriviaDbHelper.TABLE_USERS + " u"
                    + " ON u." + TriviaDbHelper.COL_USER_ID + " = m." + TriviaDbHelper.COL_CM_STUDENT_ID
                    + " WHERE c." + TriviaDbHelper.COL_C_TEACHER_ID + " = ?"
                    + " AND m." + TriviaDbHelper.COL_CM_STATUS + " = 'PENDING'"
                    + " ORDER BY m." + TriviaDbHelper.COL_CM_ID + " DESC";
            try (Cursor c = db.rawQuery(sql, new String[]{String.valueOf(teacherId)})) {
                while (c.moveToNext()) {
                    list.add(new ClassroomMember(
                            c.getLong(0),
                            c.getLong(1),
                            c.getString(2),
                            c.getString(3),
                            c.getLong(4),
                            c.getString(5),
                            c.getInt(6),
                            c.getString(7)
                    ));
                }
            }
            return list;
        }, callback);
    }

    public void approveMember(long memberId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TriviaDbHelper.COL_CM_STATUS, "APPROVED");
            int rows = db.update(TriviaDbHelper.TABLE_CLASSROOM_MEMBERS, values,
                    TriviaDbHelper.COL_CM_ID + " = ?",
                    new String[]{String.valueOf(memberId)});
            return rows > 0;
        }, callback);
    }

    public void rejectMember(long memberId, DbCallback<Boolean> callback) {
        executor.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rows = db.delete(TriviaDbHelper.TABLE_CLASSROOM_MEMBERS,
                    TriviaDbHelper.COL_CM_ID + " = ?",
                    new String[]{String.valueOf(memberId)});
            return rows > 0;
        }, callback);
    }

    private String generateUniqueCode(SQLiteDatabase db) {
        Random random = new Random();
        for (int attempt = 0; attempt < 20; attempt++) {
            int num = 1000 + random.nextInt(9000);
            String candidate = "CLS-" + num;
            try (Cursor c = db.rawQuery(
                    "SELECT " + TriviaDbHelper.COL_C_ID + " FROM " + TriviaDbHelper.TABLE_CLASSROOMS
                            + " WHERE " + TriviaDbHelper.COL_C_CODE + " = ? COLLATE NOCASE LIMIT 1",
                    new String[]{candidate})) {
                if (!c.moveToFirst()) {
                    return candidate;
                }
            }
        }
        return "CLS-" + (System.currentTimeMillis() % 10000);
    }
}
