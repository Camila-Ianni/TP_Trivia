package com.example.triviamaster;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
<<<<<<< HEAD
=======
import android.content.DialogInterface;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
<<<<<<< HEAD
=======
import android.widget.ProgressBar;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
<<<<<<< HEAD
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.QuestionBank;
=======
import androidx.lifecycle.LifecycleOwner;
import com.example.triviamaster.data.PreferencesManager;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
import com.example.triviamaster.databinding.ActivityGameBinding;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private PreferencesManager prefsManager;
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.img_avatar_perfil1;
    private int levelId = -1;
    private boolean isQuickPlay;
    private Level currentLevel;

    private List<Question> questions = new ArrayList<>();
=======
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameActivity extends AppCompatActivity {
    private ActivityGameBinding binding;
    private boolean isQuickPlay;

    @Nullable
    private Level currentLevel;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    private int currentQuestionIndex;
    private int score;
    private int currentStreak;
    private int maxStreak;
    private int correctAnswers;
<<<<<<< HEAD
=======

    @Nullable
    private CountDownTimer countDownTimer;
    private boolean isAnsweringLocked;
    private boolean hasCheckedAnswer;
    private List<LinearLayout> optionContainers;
    private List<TextView> optionBadges;
    private List<TextView> optionTexts;

    @NotNull
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.ic_brain;
    private int levelId = -1;

    @NotNull
    private List<Question> questions = Collections.emptyList();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    private int lives = 3;
    private final int questionTimeLimitSeconds = 15;
    private int secondsRemaining = 15;
    private int selectedOptionIndex = -1;

<<<<<<< HEAD
    private CountDownTimer countDownTimer;
    private boolean isAnsweringLocked;
    private boolean hasCheckedAnswer;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final List<Integer> placedLetterIndices = new ArrayList<>();
    private final List<Character> scrambledLetters = new ArrayList<>();
    private final List<TextView> slotViews = new ArrayList<>();
    private final List<TextView> tileViews = new ArrayList<>();

    private List<LinearLayout> optionContainers;
    private List<TextView> optionBadges;
    private List<TextView> optionTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
=======
    @NotNull
    private final Handler handler = new Handler(Looper.getMainLooper());

    @NotNull
    private final List<Integer> placedLetterIndices = new ArrayList<>();

    @NotNull
    private final List<Character> scrambledLetters = new ArrayList<>();

    @NotNull
    private final List<TextView> slotViews = new ArrayList<>();

    @NotNull
    private final List<TextView> tileViews = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityGameBinding.inflate(getLayoutInflater());
        ActivityGameBinding activityGameBinding = this.binding;
        setContentView(activityGameBinding.getRoot());
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        initOptionViews();
        extractIntentData();
        setupUI();
        setupBackNavigation();
        loadGameQuestions();
    }

    private void initOptionViews() {
<<<<<<< HEAD
        optionContainers = Arrays.asList(
                binding.layoutOption0,
                binding.layoutOption1,
                binding.layoutOption2,
                binding.layoutOption3
        );
        optionBadges = Arrays.asList(
                binding.tvBadge0,
                binding.tvBadge1,
                binding.tvBadge2,
                binding.tvBadge3
        );
        optionTexts = Arrays.asList(
                binding.tvOption0,
                binding.tvOption1,
                binding.tvOption2,
                binding.tvOption3
        );
    }

    private void extractIntentData() {
        String lastPlayerName = prefsManager.getLastPlayerName();
        userName = (lastPlayerName == null || lastPlayerName.trim().isEmpty()) ? "Jugador" : lastPlayerName;
        avatarId = prefsManager.getLastAvatarId();
        avatarResId = prefsManager.getAvatarById(avatarId).getIconResId();
        levelId = getIntent().getIntExtra("extra_level_id", -1);
        isQuickPlay = getIntent().getBooleanExtra("extra_is_quick_play", false);
=======
        this.optionContainers = java.util.Arrays.asList(
                binding.layoutOption0,
                binding.layoutOption1,
                binding.layoutOption2,
                binding.layoutOption3);
        this.optionBadges = java.util.Arrays.asList(
                binding.tvBadge0,
                binding.tvBadge1,
                binding.tvBadge2,
                binding.tvBadge3);
        this.optionTexts = java.util.Arrays.asList(
                binding.tvOption0,
                binding.tvOption1,
                binding.tvOption2,
                binding.tvOption3);
    }
    private void extractIntentData() {
        PreferencesManager prefs = new PreferencesManager(this);
        String lastPlayerName = prefs.getLastPlayerName();
        userName = lastPlayerName == null || lastPlayerName.trim().isEmpty()
                ? "Jugador" : lastPlayerName;
        this.avatarId = prefs.getLastAvatarId();
        this.avatarResId = prefs.getAvatarById(this.avatarId).getIconResId();
        this.levelId = getIntent().getIntExtra("extra_level_id", -1);
        this.isQuickPlay = getIntent().getBooleanExtra("extra_is_quick_play", false);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    private void setupUI() {
        updateLivesDisplay();
<<<<<<< HEAD
        binding.btnGameBack.setOnClickListener(v -> showExitConfirmationDialog());
        binding.btnCheckAnswer.setOnClickListener(v -> {
            if (!hasCheckedAnswer) {
                if (currentQuestionIndex < questions.size()) {
                    Question currentQ = questions.get(currentQuestionIndex);
                    if (currentQ.isWordPuzzle()) {
                        if (placedLetterIndices.size() == currentQ.getTargetWord().length()) {
                            checkWordPuzzleAnswer();
                        }
                    } else {
                        if (selectedOptionIndex != -1) {
                            checkSelectedAnswer();
                        }
                    }
                }
            } else {
                advanceToNextQuestion();
            }
        });
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void showExitConfirmationDialog() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_exit_title))
                .setMessage(userName + ", " + getString(R.string.dialog_exit_message))
                .setPositiveButton(getString(R.string.dialog_exit_confirm), (dialog, which) -> finish())
                .setNegativeButton(getString(R.string.dialog_exit_cancel), (dialog, which) -> {
                    dialog.dismiss();
                    if (!isAnsweringLocked && !hasCheckedAnswer) {
                        startQuestionTimer(secondsRemaining);
                    }
                })
                .setCancelable(false)
                .show();
=======
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.btnGameBack.setOnClickListener(view -> showExitConfirmationDialog());
                binding.btnCheckAnswer.setOnClickListener(view -> handleCheckAnswer());
    }

    private void handleCheckAnswer() {
        boolean z;
        if (!hasCheckedAnswer) {
            Question currentQ = currentQuestion();
            if (currentQ != null) {
                z = currentQ.isWordPuzzle();
            } else {
                z = false;
            }
            if (z) {
                if (placedLetterIndices.size() == currentQ.getTargetWord().length()) {
                    checkWordPuzzleAnswer();
                    return;
                }
                return;
            } else {
                if (selectedOptionIndex != -1) {
                    checkSelectedAnswer();
                    return;
                }
                return;
            }
        }
        advanceToNextQuestion();
    }

    @Nullable
    private Question currentQuestion() {
        return currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()
                ? questions.get(currentQuestionIndex) : null;
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback((LifecycleOwner) this, new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                GameActivity.this.showExitConfirmationDialog();
            }
        });
    }
    public void showExitConfirmationDialog() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_exit_title)).setMessage(this.userName + ", " + getString(R.string.dialog_exit_message)).setPositiveButton(getString(R.string.dialog_exit_confirm), (dialog, which) -> {
            finish();
        }).setNegativeButton(getString(R.string.dialog_exit_cancel), (dialog, which) -> {
            if (!isAnsweringLocked && !hasCheckedAnswer) {
                startQuestionTimer(secondsRemaining);
            }
        }).setCancelable(false).show();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    private void loadGameQuestions() {
        if (levelId > 0 && !isQuickPlay) {
<<<<<<< HEAD
            currentLevel = QuestionBank.INSTANCE.getLevelById(levelId);
            if (currentLevel != null && currentLevel.getQuestions() != null && !currentLevel.getQuestions().isEmpty()) {
                questions = new ArrayList<>(currentLevel.getQuestions());
            } else {
                questions = QuestionBank.INSTANCE.getRandomQuiz(5);
            }
            int displayLevel = (currentLevel != null) ? currentLevel.getId() : 1;
            String levelTitle = (currentLevel != null) ? currentLevel.getTitle() : "Aventura";
            binding.tvGameTopic.setText("Nivel " + displayLevel + ": " + levelTitle);
        } else {
            questions = QuestionBank.INSTANCE.getRandomQuiz(10);
            binding.tvGameTopic.setText(getString(R.string.quick_play_title));
        }
=======
            currentLevel = com.example.triviamaster.data.QuestionBank.INSTANCE.getLevelById(levelId);
            if (currentLevel != null && currentLevel.getQuestions() != null) {
                questions = currentLevel.getQuestions();
                binding.tvGameTopic.setText(
                        "Nivel " + currentLevel.getId() + ": " + currentLevel.getTitle()
                );
            } else {
                questions = com.example.triviamaster.data.QuestionBank.INSTANCE.getRandomQuiz(5);
                binding.tvGameTopic.setText(R.string.quick_play_title);
            }
        } else {
            currentLevel = null;
            questions = com.example.triviamaster.data.QuestionBank.INSTANCE.getRandomQuiz(10);
            binding.tvGameTopic.setText(R.string.quick_play_title);
        }

>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        currentQuestionIndex = 0;
        score = 0;
        lives = 3;
        currentStreak = 0;
        maxStreak = 0;
        correctAnswers = 0;
        displayQuestion(currentQuestionIndex);
    }

    private void displayQuestion(int index) {
<<<<<<< HEAD
        if (index >= questions.size()) {
            finishGame();
            return;
        }
        Question question = questions.get(index);
        isAnsweringLocked = false;
        hasCheckedAnswer = false;
        selectedOptionIndex = -1;

        binding.tvFeedbackBanner.setVisibility(View.GONE);
        binding.tvQuestionProgress.setText("Question " + (index + 1) + " of " + questions.size());
        binding.btnCheckAnswer.setText("Check");
        binding.btnCheckAnswer.setAlpha(0.6f);
        binding.btnCheckAnswer.setEnabled(false);

=======
        if (index >= this.questions.size()) {
            finishGame();
            return;
        }
        Question question = this.questions.get(index);
        this.isAnsweringLocked = false;
        this.hasCheckedAnswer = false;
        this.selectedOptionIndex = -1;
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.tvFeedbackBanner.setVisibility(View.GONE);
                binding.tvQuestionProgress.setText("Question " + (index + 1) + " of " + this.questions.size());
                binding.btnCheckAnswer.setText("Check");
                binding.btnCheckAnswer.setAlpha(0.6f);
                binding.btnCheckAnswer.setEnabled(false);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        if (question.isWordPuzzle()) {
            displayWordPuzzle(question);
            return;
        }
<<<<<<< HEAD

        binding.layoutWordPuzzleRoot.setVisibility(View.GONE);
        binding.layoutStandardQuiz.setVisibility(View.VISIBLE);
        binding.tvQuestionText.setText(question.getQuestionText());

        for (int i = 0; i < optionContainers.size(); i++) {
            LinearLayout container = optionContainers.get(i);
            TextView badge = optionBadges.get(i);
            TextView text = optionTexts.get(i);

            if (i < question.getOptions().size()) {
                container.setVisibility(View.VISIBLE);
                text.setText(question.getOptions().get(i));
                badge.setText(String.valueOf((char) ('A' + i)));
                resetOptionAppearance(i);
                container.setEnabled(true);
                final int optIndex = i;
                container.setOnClickListener(v -> {
                    if (!isAnsweringLocked && !hasCheckedAnswer) {
                        selectOption(optIndex);
=======
                binding.layoutWordPuzzleRoot.setVisibility(View.GONE);
                binding.layoutStandardQuiz.setVisibility(View.VISIBLE);
                binding.tvQuestionText.setText(question.getQuestionText());
        List<? extends LinearLayout> list = this.optionContainers;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            List<? extends LinearLayout> list2 = this.optionContainers;
            LinearLayout container = list2.get(i);
            List<? extends TextView> list3 = this.optionBadges;
            TextView badge = list3.get(i);
            List<? extends TextView> list4 = this.optionTexts;
            TextView text = list4.get(i);
            if (i < question.getOptions().size()) {
                container.setVisibility(View.VISIBLE);
                text.setText(question.getOptions().get(i));
                badge.setText(String.valueOf((char) (65 + i)));
                resetOptionAppearance(i);
                container.setEnabled(true);
                int optionIndex = i;
                container.setOnClickListener(view -> {
                    if (!isAnsweringLocked && !hasCheckedAnswer) {
                        selectOption(optionIndex);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                    }
                });
            } else {
                container.setVisibility(View.GONE);
            }
        }
<<<<<<< HEAD
        startQuestionTimer(questionTimeLimitSeconds);
    }

    private void selectOption(int index) {
        selectedOptionIndex = index;
        for (int i = 0; i < optionContainers.size(); i++) {
            if (i == index) {
                optionContainers.get(i).setBackgroundResource(R.drawable.bg_quiz_option_selected);
                optionTexts.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
                optionBadges.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
=======
        startQuestionTimer(this.questionTimeLimitSeconds);
    }

    private void selectOption(int index) {
        this.selectedOptionIndex = index;
        List<? extends LinearLayout> list = this.optionContainers;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i == index) {
                List<? extends LinearLayout> list2 = this.optionContainers;
                list2.get(i).setBackgroundResource(R.drawable.bg_quiz_option_selected);
                List<? extends TextView> list3 = this.optionTexts;
                list3.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
                List<? extends TextView> list4 = this.optionBadges;
                list4.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            } else {
                resetOptionAppearance(i);
            }
        }
<<<<<<< HEAD
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);
    }

    private void resetOptionAppearance(int index) {
        optionContainers.get(index).setBackgroundResource(R.drawable.bg_quiz_option_unselected);
        optionTexts.get(index).setTextColor(Color.WHITE);
        optionBadges.get(index).setTextColor(ContextCompat.getColor(this, R.color.primary));
    }

    private void checkSelectedAnswer() {
        if (isAnsweringLocked || selectedOptionIndex == -1) {
            return;
        }
        isAnsweringLocked = true;
        hasCheckedAnswer = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Question question = questions.get(currentQuestionIndex);
        boolean isCorrect = (selectedOptionIndex == question.getCorrectAnswerIndex());

        for (LinearLayout container : optionContainers) {
            container.setEnabled(false);
        }

        if (isCorrect) {
            correctAnswers++;
            currentStreak++;
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
            int timeBonus = secondsRemaining * 4;
            int streakBonus = (currentStreak - 1) * 25;
            int questionScore = 100 + timeBonus + streakBonus;
            score += questionScore;

            optionContainers.get(selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(selectedOptionIndex).setTextColor(Color.WHITE);
            animateSuccess(optionContainers.get(selectedOptionIndex));

            String bannerText = (currentStreak > 1)
                    ? "¡Excelente, " + userName + "! +" + questionScore + " pts  Racha x" + currentStreak + "!"
                    : "¡Correcto, " + userName + "! +" + questionScore + " pts";
            binding.tvFeedbackBanner.setText(bannerText);
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        } else {
            currentStreak = 0;
            lives--;
            updateLivesDisplay();

            optionContainers.get(selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            optionTexts.get(selectedOptionIndex).setTextColor(Color.WHITE);
            optionContainers.get(question.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(question.getCorrectAnswerIndex()).setTextColor(Color.WHITE);
            animateShake(optionContainers.get(selectedOptionIndex));

            binding.tvFeedbackBanner.setText("¡Incorrecto, " + userName + "! Pierdes 1 vida");
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        }

        binding.btnCheckAnswer.setText("Siguiente");
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);
        handler.postDelayed(() -> {
=======
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.btnCheckAnswer.setEnabled(true);
                binding.btnCheckAnswer.setAlpha(1.0f);
    }
    private void resetOptionAppearance(int index) {
        List<? extends LinearLayout> list = this.optionContainers;
        list.get(index).setBackgroundResource(R.drawable.bg_quiz_option_unselected);
        List<? extends TextView> list2 = this.optionTexts;
        list2.get(index).setTextColor(-1);
        List<? extends TextView> list3 = this.optionBadges;
        list3.get(index).setTextColor(ContextCompat.getColor(this, R.color.primary));
    }
    private void checkSelectedAnswer() {
        if (this.isAnsweringLocked || this.selectedOptionIndex == -1) {
            return;
        }
        this.isAnsweringLocked = true;
        this.hasCheckedAnswer = true;
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Question question = this.questions.get(this.currentQuestionIndex);
        boolean isCorrect = this.selectedOptionIndex == question.getCorrectAnswerIndex();
        for (LinearLayout it : optionContainers) {
            it.setEnabled(false);
        }
        if (isCorrect) {
            this.correctAnswers++;
            this.currentStreak++;
            if (this.currentStreak > this.maxStreak) {
                this.maxStreak = this.currentStreak;
            }
            int timeBonus = this.secondsRemaining * 4;
            int streakBonus = (this.currentStreak - 1) * 25;
            int questionScore = 100 + timeBonus + streakBonus;
            this.score += questionScore;
            List<? extends LinearLayout> list = this.optionContainers;
            list.get(this.selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            List<? extends TextView> list2 = this.optionTexts;
            list2.get(this.selectedOptionIndex).setTextColor(-1);
            List<? extends LinearLayout> list3 = this.optionContainers;
            animateSuccess(list3.get(this.selectedOptionIndex));
            ActivityGameBinding activityGameBinding = this.binding;
            TextView feedbackBanner = activityGameBinding.tvFeedbackBanner;
            if (this.currentStreak > 1) {
                String feedback = "¡Excelente, " + this.userName + "! +" + questionScore
                        + " pts  Racha x" + this.currentStreak + "!";
                feedbackBanner.setText(feedback);
            } else {
                String feedback = "¡Correcto, " + this.userName + "! +" + questionScore + " pts";
                feedbackBanner.setText(feedback);
            }
            feedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            feedbackBanner.setVisibility(View.VISIBLE);
        } else {
            this.currentStreak = 0;
            this.lives--;
            updateLivesDisplay();
            List<? extends LinearLayout> list4 = this.optionContainers;
            list4.get(this.selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            List<? extends TextView> list5 = this.optionTexts;
            list5.get(this.selectedOptionIndex).setTextColor(-1);
            List<? extends LinearLayout> list6 = this.optionContainers;
            list6.get(question.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            List<? extends TextView> list7 = this.optionTexts;
            list7.get(question.getCorrectAnswerIndex()).setTextColor(-1);
            List<? extends LinearLayout> list8 = this.optionContainers;
            animateShake(list8.get(this.selectedOptionIndex));
                        TextView feedbackBanner = binding.tvFeedbackBanner;
            feedbackBanner.setText("¡Incorrecto, " + this.userName + "! Pierdes 1 vida");
            feedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            feedbackBanner.setVisibility(View.VISIBLE);
        }
                binding.btnCheckAnswer.setText("Siguiente");
                binding.btnCheckAnswer.setEnabled(true);
                binding.btnCheckAnswer.setAlpha(1.0f);
        this.handler.postDelayed(() -> {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 1400L);
    }

<<<<<<< HEAD
    private void advanceToNextQuestion() {
        handler.removeCallbacksAndMessages(null);
        if (lives <= 0 || currentQuestionIndex >= questions.size() - 1) {
            finishGame();
        } else {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }
    }

    private void startQuestionTimer(int durationSeconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        secondsRemaining = durationSeconds;
        binding.pbTimer.setMax(questionTimeLimitSeconds);
        binding.pbTimer.setProgress(secondsRemaining);
        binding.tvTimerText.setText(String.format(Locale.ROOT, "00:%02d", secondsRemaining));

        countDownTimer = new CountDownTimer(durationSeconds * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = (int) (millisUntilFinished / 1000);
                binding.pbTimer.setProgress(secondsRemaining);
                binding.tvTimerText.setText(String.format(Locale.ROOT, "00:%02d", secondsRemaining));
                if (secondsRemaining <= 4) {
                    binding.tvTimerText.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.wrong_red));
                } else {
                    binding.tvTimerText.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.ref_yellow_selected));
                }
=======

    private void advanceToNextQuestion() {
        this.handler.removeCallbacksAndMessages(null);
        if (this.lives <= 0 || this.currentQuestionIndex >= this.questions.size() - 1) {
            finishGame();
        } else {
            this.currentQuestionIndex++;
            displayQuestion(this.currentQuestionIndex);
        }
    }
    private void startQuestionTimer(int durationSeconds) {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.secondsRemaining = durationSeconds;
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.pbTimer.setMax(this.questionTimeLimitSeconds);
                binding.pbTimer.setProgress(this.secondsRemaining);
                TextView textView = binding.tvTimerText;
        String format = String.format(Locale.US, "00:%02d", this.secondsRemaining);
        textView.setText(format);
        this.countDownTimer = new CountDownTimer(durationSeconds * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                GameActivity.this.secondsRemaining = (int) (millisUntilFinished / 1000);
                binding.pbTimer.setProgress(GameActivity.this.secondsRemaining);
                String timerText = String.format(
                        Locale.US, "00:%02d", GameActivity.this.secondsRemaining);
                binding.tvTimerText.setText(timerText);
                if (GameActivity.this.secondsRemaining <= 4) {
                    binding.tvTimerText.setTextColor(
                            ContextCompat.getColor(GameActivity.this, R.color.wrong_red));
                    return;
                }
                binding.tvTimerText.setTextColor(
                        ContextCompat.getColor(GameActivity.this, R.color.ref_yellow_selected));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            }

            @Override
            public void onFinish() {
<<<<<<< HEAD
                secondsRemaining = 0;
                binding.pbTimer.setProgress(0);
                binding.tvTimerText.setText("00:00");
                onTimeExpired();
            }
        }.start();
    }

    private void onTimeExpired() {
        if (isAnsweringLocked) {
            return;
        }
        isAnsweringLocked = true;
        hasCheckedAnswer = true;
        Question currentQuestion = questions.get(currentQuestionIndex);

        for (LinearLayout container : optionContainers) {
            container.setEnabled(false);
        }

        currentStreak = 0;
        lives--;
        updateLivesDisplay();

        if (!currentQuestion.isWordPuzzle()) {
            optionContainers.get(currentQuestion.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(currentQuestion.getCorrectAnswerIndex()).setTextColor(Color.WHITE);
        }

        binding.tvFeedbackBanner.setText("¡Se acabó el tiempo! Pierdes 1 vida");
        binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
        binding.tvFeedbackBanner.setVisibility(View.VISIBLE);

        binding.btnCheckAnswer.setText("Siguiente");
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);

        handler.postDelayed(() -> {
=======
                GameActivity.this.secondsRemaining = 0;
                binding.pbTimer.setProgress(0);
                binding.tvTimerText.setText("00:00");
                GameActivity.this.onTimeExpired();
            }
        }.start();
    }
    public void onTimeExpired() {
        if (this.isAnsweringLocked) {
            return;
        }
        this.isAnsweringLocked = true;
        this.hasCheckedAnswer = true;
        Question currentQuestion = this.questions.get(this.currentQuestionIndex);
        for (LinearLayout it : optionContainers) {
            it.setEnabled(false);
        }
        this.currentStreak = 0;
        this.lives--;
        updateLivesDisplay();
        List<? extends LinearLayout> list = this.optionContainers;
        list.get(currentQuestion.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
        List<? extends TextView> list2 = this.optionTexts;
        list2.get(currentQuestion.getCorrectAnswerIndex()).setTextColor(-1);
        ActivityGameBinding activityGameBinding = this.binding;
        TextView feedbackBanner = activityGameBinding.tvFeedbackBanner;
        feedbackBanner.setText("¡Se acabó el tiempo! Pierdes 1 vida");
        feedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
        feedbackBanner.setVisibility(View.VISIBLE);
                binding.btnCheckAnswer.setText("Siguiente");
                binding.btnCheckAnswer.setEnabled(true);
                binding.btnCheckAnswer.setAlpha(1.0f);
        this.handler.postDelayed(() -> {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 1500L);
    }

    private void updateLivesDisplay() {
<<<<<<< HEAD
        List<ImageView> lifeViews = Arrays.asList(binding.ivLife1, binding.ivLife2, binding.ivLife3);
        for (int i = 0; i < lifeViews.size(); i++) {
            if (i < lives) {
=======
        ImageView[] imageViewArr = new ImageView[3];
        ActivityGameBinding activityGameBinding = this.binding;
        imageViewArr[0] = activityGameBinding.ivLife1;
                imageViewArr[1] = binding.ivLife2;
                imageViewArr[2] = binding.ivLife3;
        List<ImageView> lifeViews = java.util.Arrays.asList(imageViewArr);
        for (int i = 0; i < 3; i++) {
            if (i < this.lives) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                lifeViews.get(i).setImageResource(R.drawable.ic_heart_full);
            } else {
                lifeViews.get(i).setImageResource(R.drawable.ic_heart_empty);
            }
        }
    }
<<<<<<< HEAD

    private void displayWordPuzzle(Question question) {
        binding.layoutStandardQuiz.setVisibility(View.GONE);
        binding.layoutWordPuzzleRoot.setVisibility(View.VISIBLE);

        if (question.getClueIconRes() != 0) {
            binding.ivWordClueIcon.setImageResource(question.getClueIconRes());
        }

        binding.layoutWordSlots.removeAllViews();
        binding.layoutLetterTilesRow1.removeAllViews();
        binding.layoutLetterTilesRow2.removeAllViews();

        placedLetterIndices.clear();
        slotViews.clear();
        tileViews.clear();
        scrambledLetters.clear();

        String word = question.getTargetWord().toUpperCase(Locale.ROOT);
        List<Character> originalChars = new ArrayList<>();
        for (char c : word.toCharArray()) {
            originalChars.add(c);
        }

        List<Character> shuffled = new ArrayList<>(originalChars);
        Collections.shuffle(shuffled);
        while (shuffled.equals(originalChars) && originalChars.size() > 1) {
            Collections.shuffle(shuffled);
        }
        scrambledLetters.addAll(shuffled);

        float density = getResources().getDisplayMetrics().density;
        Typeface pixelFont = ResourcesCompat.getFont(this, R.font.pixelify_sans);

        int slotSize;
        if (word.length() <= 4) {
            slotSize = (int) (50 * density);
        } else if (word.length() == 5) {
            slotSize = (int) (46 * density);
        } else {
            slotSize = (int) (40 * density);
        }
        float slotTextSize = word.length() <= 4 ? 22.0f : 19.0f;

        for (int i = 0; i < word.length(); i++) {
            TextView tvSlot = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(slotSize, slotSize);
            lp.setMargins((int) (3 * density), 0, (int) (3 * density), 0);
            tvSlot.setLayoutParams(lp);
            tvSlot.setGravity(17);
            tvSlot.setTextSize(slotTextSize);
            tvSlot.setTypeface(pixelFont != null ? pixelFont : Typeface.DEFAULT_BOLD);
            tvSlot.setTextColor(Color.parseColor("#362547"));
            tvSlot.setBackgroundResource(R.drawable.bg_word_slot_target_empty);
            tvSlot.setText("");

            final int slotIdx = i;
            tvSlot.setOnClickListener(v -> {
                if (!isAnsweringLocked && !hasCheckedAnswer) {
                    onWordSlotClicked(slotIdx);
                }
            });
            slotViews.add(tvSlot);
            binding.layoutWordSlots.addView(tvSlot);
        }

        int tileSize = scrambledLetters.size() <= 4 ? (int) (50 * density) : (int) (46 * density);
        int half = (scrambledLetters.size() + 1) / 2;

        for (int i = 0; i < scrambledLetters.size(); i++) {
            char ch = scrambledLetters.get(i);
            TextView tvTile = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(tileSize, tileSize);
            lp.setMargins((int) (4 * density), 0, (int) (4 * density), 0);
            tvTile.setLayoutParams(lp);
            tvTile.setGravity(17);
            tvTile.setTextSize(slotTextSize);
            tvTile.setTypeface(pixelFont != null ? pixelFont : Typeface.DEFAULT_BOLD);
            tvTile.setTextColor(Color.parseColor("#362547"));
            tvTile.setBackgroundResource(R.drawable.bg_word_pool_tile);
            tvTile.setText(String.valueOf(ch));

            final int tileIdx = i;
            tvTile.setOnClickListener(v -> {
                if (!isAnsweringLocked && !hasCheckedAnswer) {
                    onLetterTileClicked(tileIdx);
                }
            });
            tileViews.add(tvTile);

            if (i < half) {
                binding.layoutLetterTilesRow1.addView(tvTile);
            } else {
                binding.layoutLetterTilesRow2.addView(tvTile);
            }
        }

        updateWordSlotsUI();
        startQuestionTimer(questionTimeLimitSeconds);
    }

    private void onLetterTileClicked(int tileIndex) {
        if (placedLetterIndices.contains(tileIndex)) {
            return;
        }
        String word = questions.get(currentQuestionIndex).getTargetWord();
        if (placedLetterIndices.size() < word.length()) {
            placedLetterIndices.add(tileIndex);
=======
    private void displayWordPuzzle(Question question) {
        List<Character> shuffled;
        int earnedStars;
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.layoutStandardQuiz.setVisibility(View.GONE);
                binding.layoutWordPuzzleRoot.setVisibility(View.VISIBLE);
        if (question.getClueIconRes() != 0) {
                        binding.ivWordClueIcon.setImageResource(question.getClueIconRes());
        }
        binding.tvWordPuzzleHint.setText("Pista");
        String clue = question.getExplanation();
        if (clue == null || clue.trim().isEmpty()) {
            clue = "Ordena las letras para descubrir la palabra.";
        }
        binding.tvWordPuzzleHintDescription.setText(clue);
                binding.layoutWordSlots.removeAllViews();
                binding.layoutLetterTilesRow1.removeAllViews();
                binding.layoutLetterTilesRow2.removeAllViews();
        this.placedLetterIndices.clear();
        this.slotViews.clear();
        this.tileViews.clear();
        this.scrambledLetters.clear();
        String word = question.getTargetWord().toUpperCase(Locale.ROOT);
        List<Character> originalChars = new ArrayList<>();
        for (char character : word.toCharArray()) {
            originalChars.add(character);
        }
        List<Character> shuffled2 = new ArrayList<>(originalChars);
        Collections.shuffle(shuffled2);
        while (true) {
            shuffled = shuffled2;
            if (!shuffled.equals(originalChars) || originalChars.size() <= 1) {
                break;
            } else {
                shuffled2 = new ArrayList<>(originalChars);
                Collections.shuffle(shuffled2);
            }
        }
        this.scrambledLetters.addAll(shuffled);
        float density = getResources().getDisplayMetrics().density;
        Typeface pixelFont = ResourcesCompat.getFont(this, R.font.pixelify_sans);
        int slotSize;
        if (word.length() <= 4) {
            slotSize = (int) (50 * density);
        } else {
            slotSize = word.length() == 5
                    ? (int) (46 * density) : (int) (40 * density);
        }
        float slotTextSize = word.length() <= 4 ? 22.0f : 19.0f;
        int length = word.length();
        for (int slotIndex = 0; slotIndex < length; slotIndex++) {
            TextView slotView = new TextView(this);
            LinearLayout.LayoutParams slotParams = new LinearLayout.LayoutParams(slotSize, slotSize);
            slotParams.setMargins((int) (3 * density), 0, (int) (3 * density), 0);
            slotView.setLayoutParams(slotParams);
            slotView.setGravity(17);
            slotView.setTextSize(slotTextSize);
            Typeface typeface = pixelFont;
            if (typeface == null) {
                typeface = Typeface.DEFAULT_BOLD;
            }
            slotView.setTypeface(typeface);
            slotView.setTextColor(Color.parseColor("#362547"));
            slotView.setBackgroundResource(R.drawable.bg_word_slot_target_empty);
            slotView.setText("");
            final int selectedSlotIndex = slotIndex;
            slotView.setOnClickListener(view -> {
                if (!isAnsweringLocked && !hasCheckedAnswer) {
                    onWordSlotClicked(selectedSlotIndex);
                }
            });
            this.slotViews.add(slotView);
                        binding.layoutWordSlots.addView(slotView);
        }
        int tileSize = this.scrambledLetters.size() <= 4 ? (int) (50 * density) : (int) (46 * density);
        int half = (this.scrambledLetters.size() + 1) / 2;
        int size = this.scrambledLetters.size();
        for (int tileIndex = 0; tileIndex < size; tileIndex++) {
            char ch = this.scrambledLetters.get(tileIndex).charValue();
            TextView tileView = new TextView(this);
            LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(tileSize, tileSize);
            tileParams.setMargins((int) (4 * density), 0, (int) (4 * density), 0);
            tileView.setLayoutParams(tileParams);
            tileView.setGravity(17);
            tileView.setTextSize(slotTextSize);
            Typeface typeface2 = pixelFont;
            if (typeface2 == null) {
                typeface2 = Typeface.DEFAULT_BOLD;
            }
            tileView.setTypeface(typeface2);
            tileView.setTextColor(Color.parseColor("#362547"));
            tileView.setBackgroundResource(R.drawable.bg_word_pool_tile);
            tileView.setText(String.valueOf(ch));
            final int selectedTileIndex = tileIndex;
            tileView.setOnClickListener(view -> {
                if (!isAnsweringLocked && !hasCheckedAnswer) {
                    onLetterTileClicked(selectedTileIndex);
                }
            });
            this.tileViews.add(tileView);
            if (tileIndex < half) {
                                binding.layoutLetterTilesRow1.addView(tileView);
            } else {
                                binding.layoutLetterTilesRow2.addView(tileView);
            }
        }
        updateWordSlotsUI();
        startQuestionTimer(this.questionTimeLimitSeconds);
    }


    private void onLetterTileClicked(int tileIndex) {
        if (this.placedLetterIndices.contains(Integer.valueOf(tileIndex))) {
            return;
        }
        String word = this.questions.get(this.currentQuestionIndex).getTargetWord();
        if (this.placedLetterIndices.size() < word.length()) {
            this.placedLetterIndices.add(Integer.valueOf(tileIndex));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            updateWordSlotsUI();
        }
    }

    private void onWordSlotClicked(int slotIndex) {
<<<<<<< HEAD
        if (slotIndex < placedLetterIndices.size()) {
            placedLetterIndices.remove(slotIndex);
=======
        if (slotIndex < this.placedLetterIndices.size()) {
            this.placedLetterIndices.remove(slotIndex);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            updateWordSlotsUI();
        }
    }

    private void updateWordSlotsUI() {
<<<<<<< HEAD
        String word = questions.get(currentQuestionIndex).getTargetWord();
        for (int i = 0; i < slotViews.size(); i++) {
            TextView tvSlot = slotViews.get(i);
            if (i < placedLetterIndices.size()) {
                int tileIdx = placedLetterIndices.get(i);
                tvSlot.setText(String.valueOf(scrambledLetters.get(tileIdx)));
=======
        String word = this.questions.get(this.currentQuestionIndex).getTargetWord();
        int size = this.slotViews.size();
        for (int i = 0; i < size; i++) {
            TextView tvSlot = this.slotViews.get(i);
            if (i < this.placedLetterIndices.size()) {
                int tileIdx = this.placedLetterIndices.get(i).intValue();
                tvSlot.setText(String.valueOf(this.scrambledLetters.get(tileIdx).charValue()));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                tvSlot.setTextColor(Color.parseColor("#362547"));
                tvSlot.setBackgroundResource(R.drawable.bg_word_slot_target_placed);
            } else {
                tvSlot.setText("");
                tvSlot.setBackgroundResource(R.drawable.bg_word_slot_target_empty);
            }
        }
<<<<<<< HEAD

        for (int i = 0; i < tileViews.size(); i++) {
            TextView tvTile = tileViews.get(i);
            boolean isUsed = placedLetterIndices.contains(i);
=======
        int size2 = this.tileViews.size();
        for (int tileIndex = 0; tileIndex < size2; tileIndex++) {
            TextView tvTile = this.tileViews.get(tileIndex);
            boolean isUsed = this.placedLetterIndices.contains(Integer.valueOf(tileIndex));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            if (isUsed) {
                tvTile.setBackgroundResource(R.drawable.bg_word_pool_tile_disabled);
                tvTile.setTextColor(Color.parseColor("#40362547"));
                tvTile.setEnabled(false);
            } else {
                tvTile.setBackgroundResource(R.drawable.bg_word_pool_tile);
                tvTile.setTextColor(Color.parseColor("#362547"));
                tvTile.setEnabled(true);
            }
        }
<<<<<<< HEAD

        boolean isComplete = (placedLetterIndices.size() == word.length());
        binding.btnCheckAnswer.setEnabled(isComplete);
        binding.btnCheckAnswer.setAlpha(isComplete ? 1.0f : 0.5f);
    }

    private void checkWordPuzzleAnswer() {
        if (isAnsweringLocked) {
            return;
        }
        Question question = questions.get(currentQuestionIndex);
        String target = question.getTargetWord().toUpperCase(Locale.ROOT);
        if (placedLetterIndices.size() != target.length()) {
            return;
        }

        isAnsweringLocked = true;
        hasCheckedAnswer = true;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        StringBuilder formedWordBuilder = new StringBuilder();
        for (int tileIdx : placedLetterIndices) {
            formedWordBuilder.append(scrambledLetters.get(tileIdx));
        }
        String formedWord = formedWordBuilder.toString();
        boolean isCorrect = formedWord.equalsIgnoreCase(target);

        for (TextView slot : slotViews) {
            slot.setEnabled(false);
        }
        for (TextView tile : tileViews) {
            tile.setEnabled(false);
        }

        if (isCorrect) {
            correctAnswers++;
            currentStreak++;
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
            int timeBonus = secondsRemaining * 4;
            int streakBonus = (currentStreak - 1) * 25;
            int questionScore = 120 + timeBonus + streakBonus;
            score += questionScore;

            for (TextView slot : slotViews) {
                slot.setBackgroundResource(R.drawable.bg_quiz_option_correct);
                animateSuccess(slot);
            }

            String bannerText = (currentStreak > 1)
                    ? "¡Palabra Perfecta, " + userName + "! +" + questionScore + " pts  Racha x" + currentStreak + "!"
                    : "¡Correcto, " + userName + "! +" + questionScore + " pts";
            binding.tvFeedbackBanner.setText(bannerText);
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        } else {
            currentStreak = 0;
            lives--;
            updateLivesDisplay();

            for (TextView slot : slotViews) {
                slot.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
                animateShake(slot);
            }

            binding.tvFeedbackBanner.setText("¡Incorrecto! Era '" + target + "'");
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        }

        binding.btnCheckAnswer.setText("Siguiente");
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);
        handler.postDelayed(() -> {
=======
        boolean isComplete = this.placedLetterIndices.size() == word.length();
        ActivityGameBinding activityGameBinding = this.binding;
        activityGameBinding.btnCheckAnswer.setEnabled(isComplete);
                binding.btnCheckAnswer.setAlpha(isComplete ? 1.0f : 0.5f);
    }
    private void checkWordPuzzleAnswer() {
        if (this.isAnsweringLocked) {
            return;
        }
        Question question = this.questions.get(this.currentQuestionIndex);
        String target = question.getTargetWord().toUpperCase(Locale.ROOT);
        if (this.placedLetterIndices.size() != target.length()) {
            return;
        }
        this.isAnsweringLocked = true;
        this.hasCheckedAnswer = true;
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        StringBuilder formedWordBuilder = new StringBuilder();
        for (Integer tileIndex : placedLetterIndices) {
            formedWordBuilder.append(scrambledLetters.get(tileIndex));
        }
        String formedWord = formedWordBuilder.toString();
        boolean isCorrect = formedWord.equalsIgnoreCase(target);
        for (TextView slotView : slotViews) {
            slotView.setEnabled(false);
        }
        for (TextView tileView : tileViews) {
            tileView.setEnabled(false);
        }
        if (isCorrect) {
            this.correctAnswers++;
            this.currentStreak++;
            if (this.currentStreak > this.maxStreak) {
                this.maxStreak = this.currentStreak;
            }
            int timeBonus = this.secondsRemaining * 4;
            int streakBonus = (this.currentStreak - 1) * 25;
            int questionScore = 120 + timeBonus + streakBonus;
            this.score += questionScore;
            for (TextView slotView : slotViews) {
                slotView.setBackgroundResource(R.drawable.bg_quiz_option_correct);
                animateSuccess(slotView);
            }
            ActivityGameBinding activityGameBinding = this.binding;
            TextView feedbackBanner = activityGameBinding.tvFeedbackBanner;
            if (this.currentStreak > 1) {
                String feedback = "¡Palabra Perfecta, " + this.userName + "! +" + questionScore
                        + " pts  Racha x" + this.currentStreak + "!";
                feedbackBanner.setText(feedback);
            } else {
                String feedback = "¡Correcto, " + this.userName + "! +" + questionScore + " pts";
                feedbackBanner.setText(feedback);
            }
            feedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            feedbackBanner.setVisibility(View.VISIBLE);
        } else {
            this.currentStreak = 0;
            this.lives--;
            updateLivesDisplay();
            for (TextView slotView : slotViews) {
                slotView.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
                animateShake(slotView);
            }
                        TextView feedbackBanner = binding.tvFeedbackBanner;
            feedbackBanner.setText("¡Incorrecto! Era '" + target + "'");
            feedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            feedbackBanner.setVisibility(View.VISIBLE);
        }
                binding.btnCheckAnswer.setText("Siguiente");
                binding.btnCheckAnswer.setEnabled(true);
                binding.btnCheckAnswer.setAlpha(1.0f);
        this.handler.postDelayed(() -> {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 1500L);
    }

<<<<<<< HEAD
    private void animateSuccess(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.04f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.04f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(300L);
        set.start();
=======

    private void animateSuccess(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.04f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.04f, 1.0f);
        AnimatorSet successAnimation = new AnimatorSet();
        successAnimation.playTogether(scaleX, scaleY);
        successAnimation.setDuration(300L);
        successAnimation.start();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    private void animateShake(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 0.0f, 14.0f, -14.0f, 10.0f, -10.0f, 5.0f, 0.0f);
        shake.setDuration(400L);
        shake.start();
    }
<<<<<<< HEAD

    private void finishGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);

        int stars;
        if (lives <= 0) {
            stars = 0;
        } else {
            float ratio = !questions.isEmpty() ? (float) correctAnswers / questions.size() : 0.0f;
            if (ratio >= 0.99f) {
                stars = 3;
            } else if (ratio >= 0.69f) {
                stars = 2;
            } else if (ratio >= 0.49f) {
                stars = 1;
            } else {
                stars = 0;
            }
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
        intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
        intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
        intent.putExtra(ResultActivity.EXTRA_FINAL_SCORE, score);
        intent.putExtra(ResultActivity.EXTRA_MAX_STREAK, maxStreak);
        intent.putExtra(ResultActivity.EXTRA_CORRECT_COUNT, correctAnswers);
        intent.putExtra(ResultActivity.EXTRA_TOTAL_QUESTIONS, questions.size());
        intent.putExtra(ResultActivity.EXTRA_LIVES_LEFT, lives);
        intent.putExtra("extra_level_id", levelId);
        intent.putExtra(ResultActivity.EXTRA_EARNED_STARS, stars);
        intent.putExtra("extra_is_quick_play", isQuickPlay);
=======
    private void finishGame() {
        int earnedStars;
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.handler.removeCallbacksAndMessages(null);
        if (this.lives <= 0) {
            earnedStars = 0;
        } else {
            float ratio = !this.questions.isEmpty() ? this.correctAnswers / this.questions.size() : 0.0f;
            if (ratio >= 0.99f) {
                earnedStars = 3;
            } else if (ratio >= 0.69f) {
                earnedStars = 2;
            } else {
                earnedStars = ratio >= 0.49f ? 1 : 0;
            }
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(MainActivity.EXTRA_USER_NAME, this.userName);
        intent.putExtra(MainActivity.EXTRA_AVATAR_ID, this.avatarId);
        intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, this.avatarResId);
        intent.putExtra(ResultActivity.EXTRA_FINAL_SCORE, this.score);
        intent.putExtra(ResultActivity.EXTRA_MAX_STREAK, this.maxStreak);
        intent.putExtra(ResultActivity.EXTRA_CORRECT_COUNT, this.correctAnswers);
        intent.putExtra(ResultActivity.EXTRA_TOTAL_QUESTIONS, this.questions.size());
        intent.putExtra(ResultActivity.EXTRA_LIVES_LEFT, this.lives);
        intent.putExtra(ResultActivity.EXTRA_LEVEL_ID, this.levelId);
        intent.putExtra(ResultActivity.EXTRA_EARNED_STARS, earnedStars);
        intent.putExtra(ResultActivity.EXTRA_IS_QUICK_PLAY, this.isQuickPlay);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        startActivity(intent);
        finish();
    }

<<<<<<< HEAD
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
=======
    protected void onDestroy() {
        super.onDestroy();
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.handler.removeCallbacksAndMessages(null);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }
}
