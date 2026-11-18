package com.example.triviamaster;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.QuestionBank;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityGameBinding;
import com.example.triviamaster.models.Difficulty;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.utils.RetroFeedbackManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private PreferencesManager prefsManager;
    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.img_avatar_perfil1;
    private int levelId = -1;
    private boolean isQuickPlay;
    private String specialTitle;
    private int specialMinLevel = 1;
    private int specialMaxLevel = 16;
    private int specialCount = 10;
    private int specialHotspotId = -1;
    private Level currentLevel;

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex;
    private int score;
    private int currentStreak;
    private int maxStreak;
    private int correctAnswers;
    private int lives = 3;
    private int questionTimeLimitSeconds = 15;
    private int secondsRemaining = 15;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private RetroFeedbackManager feedbackManager;
    private int selectedOptionIndex = -1;

    private CountDownTimer countDownTimer;
    private boolean isAnsweringLocked;
    private boolean hasCheckedAnswer;
    private int playerEnergy = 12;
    private boolean hintUsedInQuestion;
    private boolean timeUsedInQuestion;

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
        questionRepository = new QuestionRepository(this);
        userRepository = new UserRepository(this);
        feedbackManager = new RetroFeedbackManager(this);
        initOptionViews();
        extractIntentData();
        setupUI();
        setupBackNavigation();
        loadGameQuestions();
    }

    private void initOptionViews() {
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
        playerEnergy = prefsManager.getPlayerEnergy();
        levelId = getIntent().getIntExtra("extra_level_id", -1);
        isQuickPlay = getIntent().getBooleanExtra("extra_is_quick_play", false);
        specialTitle = getIntent().getStringExtra(MapActivity.EXTRA_SPECIAL_TITLE);
        specialMinLevel = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_MIN_LEVEL, 1);
        specialMaxLevel = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_MAX_LEVEL, 16);
        specialCount = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_COUNT, 10);
        specialHotspotId = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_HOTSPOT_ID, -1);
        currentDifficulty = Difficulty.fromName(getIntent().getStringExtra(MapActivity.EXTRA_DIFFICULTY));
        questionTimeLimitSeconds = currentDifficulty.getTimeLimitSeconds();
        secondsRemaining = questionTimeLimitSeconds;
        lives = currentDifficulty.getInitialLives();
    }

    private void setupUI() {
        updateLivesDisplay();
        updateEnergyDisplay();
        binding.btnGameBack.setOnClickListener(v -> showExitConfirmationDialog());
        binding.btnPowerupHint.setOnClickListener(v -> useHintPowerup());
        binding.btnPowerupTime.setOnClickListener(v -> useTimePowerup());
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
    }

    private void loadGameQuestions() {
        currentQuestionIndex = 0;
        score = 0;
        lives = currentDifficulty.getInitialLives();
        currentStreak = 0;
        maxStreak = 0;
        correctAnswers = 0;

        if (levelId > 0 && !isQuickPlay) {
            currentLevel = QuestionBank.INSTANCE.getLevelById(levelId);
            int displayLevel = (currentLevel != null) ? currentLevel.getId() : levelId;
            String levelTitle = (currentLevel != null) ? currentLevel.getTitle() : "Aventura";
            binding.tvGameTopic.setText("Nivel " + displayLevel + ": " + levelTitle);

            questionRepository.getQuestionsByLevel(levelId, new DbCallback<List<Question>>() {
                @Override
                public void onSuccess(List<Question> dbQuestions) {
                    if (isFinishing()) {
                        return;
                    }
                    if (dbQuestions != null && !dbQuestions.isEmpty()) {
                        questions = new ArrayList<>(dbQuestions);
                    } else if (currentLevel != null && currentLevel.getQuestions() != null
                            && !currentLevel.getQuestions().isEmpty()) {
                        questions = new ArrayList<>(currentLevel.getQuestions());
                    } else {
                        questions = QuestionBank.INSTANCE.getRandomQuiz(5);
                    }
                    displayQuestion(currentQuestionIndex);
                }

                @Override
                public void onError(Exception error) {
                    if (isFinishing()) {
                        return;
                    }
                    if (currentLevel != null && currentLevel.getQuestions() != null
                            && !currentLevel.getQuestions().isEmpty()) {
                        questions = new ArrayList<>(currentLevel.getQuestions());
                    } else {
                        questions = QuestionBank.INSTANCE.getRandomQuiz(5);
                    }
                    displayQuestion(currentQuestionIndex);
                }
            });
        } else {
            String topicTitle = (specialTitle != null && !specialTitle.trim().isEmpty())
                    ? specialTitle
                    : getString(R.string.quick_play_title);
            binding.tvGameTopic.setText(topicTitle);
            questionRepository.getRandomQuestionsInRange(specialMinLevel, specialMaxLevel, specialCount, new DbCallback<List<Question>>() {
                @Override
                public void onSuccess(List<Question> dbQuestions) {
                    if (isFinishing()) {
                        return;
                    }
                    if (dbQuestions != null && !dbQuestions.isEmpty()) {
                        questions = new ArrayList<>(dbQuestions);
                    } else {
                        questions = QuestionBank.INSTANCE.getRandomQuiz(specialCount);
                    }
                    displayQuestion(currentQuestionIndex);
                }

                @Override
                public void onError(Exception error) {
                    if (isFinishing()) {
                        return;
                    }
                    questions = QuestionBank.INSTANCE.getRandomQuiz(specialCount);
                    displayQuestion(currentQuestionIndex);
                }
            });
        }
    }

    private void displayQuestion(int index) {
        if (index >= questions.size()) {
            finishGame();
            return;
        }
        Question question = questions.get(index);
        isAnsweringLocked = false;
        hasCheckedAnswer = false;
        selectedOptionIndex = -1;
        hintUsedInQuestion = false;
        timeUsedInQuestion = false;

        binding.tvFeedbackBanner.setVisibility(View.GONE);
        binding.tvExplanationText.setVisibility(View.GONE);
        binding.tvQuestionProgress.setText(getString(R.string.game_question_progress, index + 1, questions.size()));
        binding.btnCheckAnswer.setText(getString(R.string.game_btn_check));
        binding.btnCheckAnswer.setAlpha(0.6f);
        binding.btnCheckAnswer.setEnabled(false);

        binding.tvPowerupHintLabel.setText(getString(
                question.isWordPuzzle() ? R.string.game_powerup_letter : R.string.game_powerup_5050));
        updatePowerupButtonsState();

        if (question.isWordPuzzle()) {
            displayWordPuzzle(question);
            return;
        }

        binding.layoutWordPuzzleRoot.setVisibility(View.GONE);
        binding.layoutStandardQuiz.setVisibility(View.VISIBLE);
        binding.tvQuestionText.setText(question.getQuestionText());

        for (int i = 0; i < optionContainers.size(); i++) {
            LinearLayout container = optionContainers.get(i);
            TextView badge = optionBadges.get(i);
            TextView text = optionTexts.get(i);

            if (i < question.getOptions().size()) {
                container.setVisibility(View.VISIBLE);
                container.setAlpha(1.0f);
                text.setText(question.getOptions().get(i));
                badge.setText(String.valueOf((char) ('A' + i)));
                resetOptionAppearance(i);
                container.setEnabled(true);
                final int optIndex = i;
                container.setOnClickListener(v -> {
                    if (!isAnsweringLocked && !hasCheckedAnswer) {
                        selectOption(optIndex);
                    }
                });
            } else {
                container.setVisibility(View.GONE);
            }
        }
        startQuestionTimer(questionTimeLimitSeconds);
    }

    private void selectOption(int index) {
        selectedOptionIndex = index;
        for (int i = 0; i < optionContainers.size(); i++) {
            if (i == index) {
                optionContainers.get(i).setBackgroundResource(R.drawable.bg_quiz_option_selected);
                optionTexts.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
                optionBadges.get(i).setTextColor(ContextCompat.getColor(this, R.color.ref_text_dark));
            } else {
                resetOptionAppearance(i);
            }
        }
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
            int questionScore = (int) Math.round((100 + timeBonus + streakBonus) * currentDifficulty.getScoreMultiplier());
            score += questionScore;
            feedbackManager.playCorrect();

            optionContainers.get(selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(selectedOptionIndex).setTextColor(Color.WHITE);
            animateSuccess(optionContainers.get(selectedOptionIndex));

            String bannerText = (currentStreak > 1)
                    ? getString(R.string.game_feedback_correct_streak, userName, questionScore, currentStreak)
                    : getString(R.string.game_feedback_correct, userName, questionScore);
            binding.tvFeedbackBanner.setText(bannerText);
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        } else {
            currentStreak = 0;
            lives--;
            updateLivesDisplay();
            feedbackManager.playWrong();

            optionContainers.get(selectedOptionIndex).setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            optionTexts.get(selectedOptionIndex).setTextColor(Color.WHITE);
            optionContainers.get(question.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(question.getCorrectAnswerIndex()).setTextColor(Color.WHITE);
            animateShake(optionContainers.get(selectedOptionIndex));

            binding.tvFeedbackBanner.setText(getString(R.string.game_feedback_wrong, userName));
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        }

        showQuestionExplanation(question);
        binding.btnCheckAnswer.setText(getString(R.string.game_btn_next));
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);
        handler.postDelayed(() -> {
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 2600L);
    }

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
            }

            @Override
            public void onFinish() {
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
        feedbackManager.playWrong();

        if (!currentQuestion.isWordPuzzle()) {
            optionContainers.get(currentQuestion.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(currentQuestion.getCorrectAnswerIndex()).setTextColor(Color.WHITE);
        }

        binding.tvFeedbackBanner.setText(getString(R.string.game_feedback_timeout));
        binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
        binding.tvFeedbackBanner.setVisibility(View.VISIBLE);

        showQuestionExplanation(currentQuestion);
        binding.btnCheckAnswer.setText(getString(R.string.game_btn_next));
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);

        handler.postDelayed(() -> {
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 2500L);
    }

    private void updateLivesDisplay() {
        int maxLives = currentDifficulty != null ? currentDifficulty.getInitialLives() : 3;
        List<ImageView> lifeViews = Arrays.asList(binding.ivLife1, binding.ivLife2, binding.ivLife3);
        for (int i = 0; i < lifeViews.size(); i++) {
            if (i >= maxLives) {
                lifeViews.get(i).setVisibility(View.GONE);
                continue;
            }
            lifeViews.get(i).setVisibility(View.VISIBLE);
            if (i < lives) {
                lifeViews.get(i).setImageResource(R.drawable.ic_heart_full);
            } else {
                lifeViews.get(i).setImageResource(R.drawable.ic_heart_empty);
            }
        }
    }

    private void showQuestionExplanation(Question question) {
        if (question != null && question.getExplanation() != null && !question.getExplanation().trim().isEmpty()) {
            binding.tvExplanationText.setText(getString(R.string.game_explanation_format, question.getExplanation().trim()));
            binding.tvExplanationText.setVisibility(View.VISIBLE);
        } else {
            binding.tvExplanationText.setVisibility(View.GONE);
        }
    }

    private void updateEnergyDisplay() {
        binding.tvGameEnergy.setText(playerEnergy + "/12");
        updatePowerupButtonsState();
    }

    private void updatePowerupButtonsState() {
        boolean canHint = !hintUsedInQuestion && playerEnergy > 0;
        boolean canTime = !timeUsedInQuestion && playerEnergy > 0;
        binding.btnPowerupHint.setAlpha(canHint ? 1.0f : 0.45f);
        binding.btnPowerupTime.setAlpha(canTime ? 1.0f : 0.45f);
    }

    private void consumeOneEnergy() {
        playerEnergy = Math.max(0, playerEnergy - 1);
        prefsManager.setPlayerEnergy(playerEnergy);
        updateEnergyDisplay();
        feedbackManager.playPowerup();
        long userId = prefsManager.getCurrentUserId();
        if (userId > 0) {
            userRepository.consumeEnergy(userId, 1, null);
        }
    }

    private void useHintPowerup() {
        if (isAnsweringLocked || hasCheckedAnswer || currentQuestionIndex >= questions.size()) {
            return;
        }
        if (hintUsedInQuestion) {
            Toast.makeText(this, getString(R.string.game_powerup_used), Toast.LENGTH_SHORT).show();
            return;
        }
        if (playerEnergy <= 0) {
            Toast.makeText(this, getString(R.string.game_powerup_no_energy), Toast.LENGTH_SHORT).show();
            return;
        }
        Question current = questions.get(currentQuestionIndex);
        if (current.isWordPuzzle()) {
            String target = current.getTargetWord().toUpperCase(Locale.ROOT);
            if (placedLetterIndices.size() >= target.length()) {
                return;
            }
            int nextSlot = placedLetterIndices.size();
            char neededChar = target.charAt(nextSlot);
            int foundTileIdx = -1;
            for (int i = 0; i < scrambledLetters.size(); i++) {
                if (!placedLetterIndices.contains(i) && scrambledLetters.get(i) == neededChar) {
                    foundTileIdx = i;
                    break;
                }
            }
            if (foundTileIdx == -1) {
                placedLetterIndices.clear();
                for (int i = 0; i < scrambledLetters.size(); i++) {
                    if (scrambledLetters.get(i) == target.charAt(0)) {
                        foundTileIdx = i;
                        break;
                    }
                }
            }
            if (foundTileIdx != -1) {
                placedLetterIndices.add(foundTileIdx);
                updateWordSlotsUI();
                hintUsedInQuestion = true;
                consumeOneEnergy();
            }
        } else {
            List<Integer> wrongIndices = new ArrayList<>();
            for (int i = 0; i < current.getOptions().size(); i++) {
                if (i != current.getCorrectAnswerIndex() && optionContainers.get(i).isEnabled()) {
                    wrongIndices.add(i);
                }
            }
            Collections.shuffle(wrongIndices);
            int toHide = Math.min(2, wrongIndices.size());
            for (int i = 0; i < toHide; i++) {
                int idx = wrongIndices.get(i);
                optionContainers.get(idx).setEnabled(false);
                optionContainers.get(idx).setAlpha(0.25f);
                if (selectedOptionIndex == idx) {
                    selectedOptionIndex = -1;
                    binding.btnCheckAnswer.setEnabled(false);
                    binding.btnCheckAnswer.setAlpha(0.6f);
                }
            }
            hintUsedInQuestion = true;
            consumeOneEnergy();
        }
    }

    private void useTimePowerup() {
        if (isAnsweringLocked || hasCheckedAnswer) {
            return;
        }
        if (timeUsedInQuestion) {
            Toast.makeText(this, getString(R.string.game_powerup_used), Toast.LENGTH_SHORT).show();
            return;
        }
        if (playerEnergy <= 0) {
            Toast.makeText(this, getString(R.string.game_powerup_no_energy), Toast.LENGTH_SHORT).show();
            return;
        }
        timeUsedInQuestion = true;
        consumeOneEnergy();
        int newDuration = secondsRemaining + 5;
        if (newDuration > binding.pbTimer.getMax()) {
            binding.pbTimer.setMax(newDuration);
        }
        startQuestionTimer(newDuration);
        Toast.makeText(this, getString(R.string.game_powerup_time_added), Toast.LENGTH_SHORT).show();
    }

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
            updateWordSlotsUI();
        }
    }

    private void onWordSlotClicked(int slotIndex) {
        if (slotIndex < placedLetterIndices.size()) {
            placedLetterIndices.remove(slotIndex);
            updateWordSlotsUI();
        }
    }

    private void updateWordSlotsUI() {
        String word = questions.get(currentQuestionIndex).getTargetWord();
        for (int i = 0; i < slotViews.size(); i++) {
            TextView tvSlot = slotViews.get(i);
            if (i < placedLetterIndices.size()) {
                int tileIdx = placedLetterIndices.get(i);
                tvSlot.setText(String.valueOf(scrambledLetters.get(tileIdx)));
                tvSlot.setTextColor(Color.parseColor("#362547"));
                tvSlot.setBackgroundResource(R.drawable.bg_word_slot_target_placed);
            } else {
                tvSlot.setText("");
                tvSlot.setBackgroundResource(R.drawable.bg_word_slot_target_empty);
            }
        }

        for (int i = 0; i < tileViews.size(); i++) {
            TextView tvTile = tileViews.get(i);
            boolean isUsed = placedLetterIndices.contains(i);
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
            int questionScore = (int) Math.round((120 + timeBonus + streakBonus) * currentDifficulty.getScoreMultiplier());
            score += questionScore;
            feedbackManager.playCorrect();

            for (TextView slot : slotViews) {
                slot.setBackgroundResource(R.drawable.bg_quiz_option_correct);
                animateSuccess(slot);
            }

            String bannerText = (currentStreak > 1)
                    ? getString(R.string.game_feedback_word_perfect, userName, questionScore, currentStreak)
                    : getString(R.string.game_feedback_correct, userName, questionScore);
            binding.tvFeedbackBanner.setText(bannerText);
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        } else {
            currentStreak = 0;
            lives--;
            updateLivesDisplay();
            feedbackManager.playWrong();

            for (TextView slot : slotViews) {
                slot.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
                animateShake(slot);
            }

            binding.tvFeedbackBanner.setText(getString(R.string.game_feedback_wrong_word, target));
            binding.tvFeedbackBanner.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            binding.tvFeedbackBanner.setVisibility(View.VISIBLE);
        }

        showQuestionExplanation(question);
        binding.btnCheckAnswer.setText(getString(R.string.game_btn_next));
        binding.btnCheckAnswer.setEnabled(true);
        binding.btnCheckAnswer.setAlpha(1.0f);
        handler.postDelayed(() -> {
            if (hasCheckedAnswer && !isFinishing()) {
                advanceToNextQuestion();
            }
        }, 2600L);
    }

    private void animateSuccess(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.04f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.04f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(300L);
        set.start();
    }

    private void animateShake(View view) {
        ObjectAnimator shake = ObjectAnimator.ofFloat(view, "translationX", 0.0f, 14.0f, -14.0f, 10.0f, -10.0f, 5.0f, 0.0f);
        shake.setDuration(400L);
        shake.start();
    }

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
        intent.putExtra(MapActivity.EXTRA_DIFFICULTY, currentDifficulty.name());
        intent.putExtra(MapActivity.EXTRA_SPECIAL_TITLE, specialTitle);
        intent.putExtra(MapActivity.EXTRA_SPECIAL_MIN_LEVEL, specialMinLevel);
        intent.putExtra(MapActivity.EXTRA_SPECIAL_MAX_LEVEL, specialMaxLevel);
        intent.putExtra(MapActivity.EXTRA_SPECIAL_COUNT, specialCount);
        intent.putExtra(MapActivity.EXTRA_SPECIAL_HOTSPOT_ID, specialHotspotId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (feedbackManager != null) {
            feedbackManager.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
