package com.example.triviamaster.models;

import java.io.Serializable;

public class Friendship implements Serializable {
    private final long id;
    private final long senderId;
    private final long receiverId;
    private final long otherUserId;
    private final String otherUsername;
    private final int otherAvatarId;
    private final int otherHighScore;
    private final int otherMaxStreak;
    private final String status;
    private final boolean incoming;

    public Friendship(long id, long senderId, long receiverId, long otherUserId,
                      String otherUsername, int otherAvatarId, int otherHighScore,
                      int otherMaxStreak, String status, boolean incoming) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
        this.otherAvatarId = otherAvatarId;
        this.otherHighScore = otherHighScore;
        this.otherMaxStreak = otherMaxStreak;
        this.status = status;
        this.incoming = incoming;
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public long getOtherUserId() {
        return otherUserId;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public int getOtherAvatarId() {
        return otherAvatarId;
    }

    public int getOtherHighScore() {
        return otherHighScore;
    }

    public int getOtherMaxStreak() {
        return otherMaxStreak;
    }

    public String getStatus() {
        return status;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public boolean isAccepted() {
        return "ACCEPTED".equalsIgnoreCase(status);
    }
}
