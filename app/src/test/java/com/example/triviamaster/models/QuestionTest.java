package com.example.triviamaster.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class QuestionTest {

    @Test
    public void regularQuestionUsesDefaultWordPuzzleValues() {
        Question question = new Question(
                1,
                "Pregunta",
                "Categoría",
                0xFF000000,
                Arrays.asList("A", "B"),
                1,
                "Explicación"
        );

        assertFalse(question.isWordPuzzle());
        assertEquals("", question.getTargetWord());
        assertEquals(0, question.getClueIconRes());
    }

    @Test
    public void wordPuzzleKeepsItsTargetAndClue() {
        Question question = new Question(
                2,
                "Ordena las letras",
                "Naturaleza",
                0xFF000000,
                Arrays.asList(),
                0,
                "Respuesta",
                true,
                "FLOR",
                0,
                42
        );

        assertTrue(question.isWordPuzzle());
        assertEquals("FLOR", question.getTargetWord());
        assertEquals(42, question.getClueIconRes());
    }
}
