package com.example.triviamaster.models;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private int id;
    private String questionText;
    private String category;
    private int categoryColor;
    private List<String> options;
    private int correctAnswerIndex;
    private String explanation;
    private boolean wordPuzzle;
    private String targetWord;
    private int missingCharIndex;
    private int clueIconRes;

    public Question(int id, String questionText, String category, int categoryColor,
                    List<String> options, int correctAnswerIndex, String explanation) {
        this(id, questionText, category, categoryColor, options, correctAnswerIndex,
                explanation, false, "", 0, 0);
    }

    public Question(int id, String questionText, String category, int categoryColor,
                    List<String> options, int correctAnswerIndex, String explanation,
                    boolean wordPuzzle, String targetWord, int missingCharIndex, int clueIconRes) {
        this.id = id;
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
    }

    public int getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getCategory() { return category; }
    public int getCategoryColor() { return categoryColor; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getExplanation() { return explanation; }
    public boolean isWordPuzzle() { return wordPuzzle; }
    public String getTargetWord() { return targetWord; }
    public int getMissingCharIndex() { return missingCharIndex; }
    public int getClueIconRes() { return clueIconRes; }
}
