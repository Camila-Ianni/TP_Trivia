package com.example.triviamaster.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DifficultyTest {

    @Test
    public void difficultyValuesMatchExpectedBalance() {
        assertEquals(20, Difficulty.EASY.getTimeLimitSeconds());
        assertEquals(3, Difficulty.EASY.getInitialLives());
        assertEquals(1.0, Difficulty.EASY.getScoreMultiplier(), 0.001);

        assertEquals(15, Difficulty.NORMAL.getTimeLimitSeconds());
        assertEquals(3, Difficulty.NORMAL.getInitialLives());
        assertEquals(1.5, Difficulty.NORMAL.getScoreMultiplier(), 0.001);

        assertEquals(10, Difficulty.HARD.getTimeLimitSeconds());
        assertEquals(2, Difficulty.HARD.getInitialLives());
        assertEquals(2.0, Difficulty.HARD.getScoreMultiplier(), 0.001);
    }

    @Test
    public void fromNameFallsBackToNormal() {
        assertEquals(Difficulty.HARD, Difficulty.fromName("HARD"));
        assertEquals(Difficulty.EASY, Difficulty.fromName("easy"));
        assertEquals(Difficulty.NORMAL, Difficulty.fromName("unknown"));
        assertEquals(Difficulty.NORMAL, Difficulty.fromName(null));
    }
}
