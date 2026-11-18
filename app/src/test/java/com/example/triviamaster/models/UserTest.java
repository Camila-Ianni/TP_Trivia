package com.example.triviamaster.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserTest {

    @Test
    public void adminRoleIsDetectedCorrectly() {
        User admin = new User(1L, "mimi", "1234", "mimi@mail.com", 1,
                0, 0, 1, 12, 1, "", "hoy", "ADMIN", "");
        User regular = new User(2L, "juan", "1234", "juan@mail.com", 2,
                0, 0, 1, 12, 1, "", "hoy", "USER", "");

        assertTrue(admin.isAdmin());
        assertFalse(regular.isAdmin());
    }

    @Test
    public void levelStarsAndMedalsUpdateProperly() {
        User user = new User(1L, "mimi", "1234", "mimi@mail.com", 1,
                100, 2, 1, 12, 1, "", "hoy");

        user.setLevelStars(1, 2);
        user.setLevelStars(1, 1);
        user.setLevelStars(5, 3);
        user.setMaxLevel(6);

        assertEquals(2, user.getLevelStars(1));
        assertEquals(3, user.getLevelStars(5));
        assertEquals(5, user.getTotalStars());
        assertTrue(user.isMedal1Unlocked());
        assertFalse(user.isMedal2Unlocked());
    }

    @Test
    public void specialChallengesMarkOncePerDifficulty() {
        User user = new User(1L, "mimi", "1234", "mimi@mail.com", 1,
                0, 0, 1, 12, 1, "", "hoy");

        assertFalse(user.hasCompletedSpecial(1, Difficulty.EASY));
        user.markSpecialCompleted(1, Difficulty.EASY);
        user.markSpecialCompleted(1, Difficulty.EASY);

        assertTrue(user.hasCompletedSpecial(1, Difficulty.EASY));
        assertFalse(user.hasCompletedSpecial(1, Difficulty.NORMAL));
        assertEquals("1_EASY", user.getSpecialCompletedSerialized());
    }

    @Test
    public void shopCoinsShieldsAndTitlesWorkCorrectly() {
        User user = new User(1L, "mimi", "1234", "mimi@mail.com", 1,
                0, 0, 1, 12, 1, "", "hoy");

        assertEquals(100, user.getCoins());
        user.setCoins(250);
        user.setShields(2);
        assertFalse(user.ownsTitle("Leyenda Pixel"));
        user.addOwnedTitle("Leyenda Pixel");
        user.setEquippedTitle("Leyenda Pixel");

        assertEquals(250, user.getCoins());
        assertEquals(2, user.getShields());
        assertTrue(user.ownsTitle("Leyenda Pixel"));
        assertEquals("Leyenda Pixel", user.getEquippedTitle());

        assertFalse(user.hasClaimedQuest("q_play_5"));
        user.markQuestClaimed("q_play_5");
        assertTrue(user.hasClaimedQuest("q_play_5"));
        assertEquals(0, user.getCompletedSpecialCount());
        user.markSpecialCompleted(0, Difficulty.EASY);
        assertEquals(1, user.getCompletedSpecialCount());

        user.setXp(310);
        assertEquals(2, user.getAccountLevel());
        assertEquals(60, user.getXpIntoCurrentLevel());
    }

    @Test
    public void dailyStreakAndWrongQuestionsWorkCorrectly() {
        User user = new User(2L, "alex", "pass", "alex@mail.com", 2,
                0, 0, 1, 12, 1, "", "hoy");

        assertEquals(0, user.getDailyStreak());
        user.setDailyStreak(4);
        assertEquals(4, user.getDailyStreak());

        assertTrue(user.getWrongQuestionIdsList().isEmpty());
        user.addWrongQuestionId(10);
        user.addWrongQuestionId(25);
        user.addWrongQuestionId(10);
        assertEquals(2, user.getWrongQuestionIdsList().size());
        assertTrue(user.getWrongQuestionIdsList().contains(10));
        assertTrue(user.getWrongQuestionIdsList().contains(25));

        user.removeWrongQuestionId(10);
        assertEquals(1, user.getWrongQuestionIdsList().size());
        assertFalse(user.getWrongQuestionIdsList().contains(10));
        assertEquals("25", user.getWrongQuestionIdsSerialized());
    }
}
