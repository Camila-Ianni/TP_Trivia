package com.example.triviamaster.models;

import java.io.Serializable;

public class ClassroomMember implements Serializable {
    private final long id;
    private final long classroomId;
    private final String classroomName;
    private final String classroomCode;
    private final long studentId;
    private final String studentUsername;
    private final int studentAvatarId;
    private final String status;

    public ClassroomMember(long id, long classroomId, String classroomName, String classroomCode,
                           long studentId, String studentUsername, int studentAvatarId, String status) {
        this.id = id;
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.classroomCode = classroomCode;
        this.studentId = studentId;
        this.studentUsername = studentUsername;
        this.studentAvatarId = studentAvatarId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getClassroomId() {
        return classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public int getStudentAvatarId() {
        return studentAvatarId;
    }

    public String getStatus() {
        return status;
    }
}
