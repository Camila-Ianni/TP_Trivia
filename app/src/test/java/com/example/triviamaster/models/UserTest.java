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
}
