package com.example.triviamaster.models;

import java.io.Serializable;

public class Classroom implements Serializable {
    private final long id;
    private final String name;
    private final String code;
    private final long teacherId;
    private final String teacherName;
    private final String createdAt;
    private final String memberStatus;
    private final int questionCount;

    public Classroom(long id, String name, String code, long teacherId,
                     String teacherName, String createdAt, String memberStatus, int questionCount) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.createdAt = createdAt;
        this.memberStatus = memberStatus;
        this.questionCount = questionCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(memberStatus);
    }
}
