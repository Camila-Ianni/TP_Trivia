package com.example.triviamaster.models;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable {
    private final int id;
    private final int worldId;
    private final int levelNumber;
    private final String title;
    private final String subtitle;
    private final String category;
    private final int categoryColor;
    private final List<Question> questions;

    public Level(int id, int worldId, int levelNumber, String title, String subtitle,
                 String category, int categoryColor, List<Question> questions) {
        this.id = id;
        this.worldId = worldId;
        this.levelNumber = levelNumber;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.categoryColor = categoryColor;
        this.questions = questions;
    }

    public int getId() { return id; }
    public int getWorldId() { return worldId; }
    public int getLevelNumber() { return levelNumber; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getCategory() { return category; }
    public int getCategoryColor() { return categoryColor; }
    public List<Question> getQuestions() { return questions; }
}
