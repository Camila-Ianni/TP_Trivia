package com.example.triviamaster.models;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private final int id;
    private final int levelId;
    private final String questionText;
    private final String category;
    private final int categoryColor;
    private final List<String> options;
    private final int correctAnswerIndex;
    private final String explanation;
    private final boolean wordPuzzle;
    private final String targetWord;
    private final int missingCharIndex;
    private final int clueIconRes;
    private final boolean custom;

    public Question(int id, String questionText, String category, int categoryColor,
                    List<String> options, int correctAnswerIndex, String explanation) {
        this(id, 1, questionText, category, categoryColor, options, correctAnswerIndex,
                explanation, false, "", 0, 0, false);
    }

    public Question(int id, String questionText, String category, int categoryColor,
                    List<String> options, int correctAnswerIndex, String explanation,
                    boolean wordPuzzle, String targetWord, int missingCharIndex, int clueIconRes) {
        this(id, 1, questionText, category, categoryColor, options, correctAnswerIndex,
                explanation, wordPuzzle, targetWord, missingCharIndex, clueIconRes, false);
    }

    public Question(int id, int levelId, String questionText, String category, int categoryColor,
                    List<String> options, int correctAnswerIndex, String explanation,
                    boolean wordPuzzle, String targetWord, int missingCharIndex, int clueIconRes,
                    boolean custom) {
        this.id = id;
        this.levelId = levelId;
        this.questionText = questionText;
        this.category = category;
        this.categoryColor = categoryColor;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
        this.wordPuzzle = wordPuzzle;
        this.targetWord = targetWord;
        this.missingCharIndex = missingCharIndex;
        this.clueIconRes = clueIconRes;
        this.custom = custom;
    }

    public int getId() {
        return id;
    }

    public int getLevelId() {
        return levelId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCategory() {
        return category;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean isWordPuzzle() {
        return wordPuzzle;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public int getMissingCharIndex() {
        return missingCharIndex;
    }

    public int getClueIconRes() {
        return clueIconRes;
    }

    public boolean isCustom() {
        return custom;
    }
}
