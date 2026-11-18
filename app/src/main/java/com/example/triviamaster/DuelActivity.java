package com.example.triviamaster;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.QuestionBank;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityDuelBinding;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.network.ActiveDuelSession;
import com.example.triviamaster.utils.RetroFeedbackManager;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DuelActivity extends AppCompatActivity {

    public static final String EXTRA_OPPONENT_NAME = "extra_opponent_name";
    public static final String EXTRA_IS_NETWORK_DUEL = "extra_is_network_duel";

    private static final int TOTAL_DUEL_QUESTIONS = 6;
    private static final int TURN_SECONDS = 15;
    private static final int DUEL_WIN_COINS = 40;

    private ActivityDuelBinding binding;
    private PreferencesManager prefsManager;
    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private RetroFeedbackManager feedbackManager;

    private String player1Name = "Jugador 1";
    private String player2Name = "Jugador 2";
    private int player1Score;
    private int player2Score;
    private int currentQuestionIndex;
    private int secondsRemaining = TURN_SECONDS;
    private boolean isAnsweringLocked;
    private boolean isNetworkDuel;
    private ActiveDuelSession duelSession;
    private CountDownTimer countDownTimer;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private List<Question> duelQuestions = new ArrayList<>();
    private List<LinearLayout> optionButtons;
    private List<TextView> optionTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDuelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final int basePad = (int) (16 * getResources().getDisplayMetrics().density);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            Insets navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
            view.setPadding(basePad, statusBarInsets.top + basePad, basePad, navBarInsets.bottom + basePad);
            return insets;
        });

        prefsManager = new PreferencesManager(this);
        questionRepository = new QuestionRepository(this);
        userRepository = new UserRepository(this);
        feedbackManager = new RetroFeedbackManager(this);

        String savedName = prefsManager.getLastPlayerName();
        if (savedName != null && !savedName.trim().isEmpty()) {
            player1Name = savedName.trim();
        }

        optionButtons = Arrays.asList(
                binding.btnDuelOption0,
                binding.btnDuelOption1,
                binding.btnDuelOption2,
                binding.btnDuelOption3
        );
        optionTexts = Arrays.asList(
                binding.tvDuelOption0,
                binding.tvDuelOption1,
                binding.tvDuelOption2,
                binding.tvDuelOption3
        );

        binding.btnDuelBack.setOnClickListener(v -> finish());
        for (int i = 0; i < optionButtons.size(); i++) {
            final int optionIdx = i;
            optionButtons.get(i).setOnClickListener(v -> handleLocalOptionSelected(optionIdx));
        }

        isNetworkDuel = getIntent().getBooleanExtra(EXTRA_IS_NETWORK_DUEL, false);
        if (isNetworkDuel) {
            duelSession = ActiveDuelSession.getCurrent();
            if (duelSession == null) {
                finish();
                return;
            }
            if (duelSession.isHost()) {
                player1Name = duelSession.getLocalPlayerName();
                player2Name = duelSession.getRemotePlayerName();
            } else {
                player1Name = duelSession.getRemotePlayerName();
                player2Name = duelSession.getLocalPlayerName();
            }
            startNetworkDuel();
            return;
        }

        String opponentName = getIntent().getStringExtra(EXTRA_OPPONENT_NAME);
        if (opponentName != null && !opponentName.trim().isEmpty()) {
            player2Name = opponentName.trim();
            startNewLocalDuel();
        } else {
            showSetupDialog();
        }
    }

    private void showSetupDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_duel_setup, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        dialog.setOnCancelListener(d -> finish());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextInputEditText etP1 = dialogView.findViewById(R.id.etDuelP1Name);
        TextInputEditText etP2 = dialogView.findViewById(R.id.etDuelP2Name);
        Button btnStart = dialogView.findViewById(R.id.btnDuelStartMatch);
        Button btnCancel = dialogView.findViewById(R.id.btnDuelSetupCancel);

        etP1.setText(player1Name);
        etP2.setText(player2Name);

        btnStart.setOnClickListener(v -> {
            String p1 = etP1.getText() != null ? etP1.getText().toString().trim() : "";
            String p2 = etP2.getText() != null ? etP2.getText().toString().trim() : "";
            player1Name = p1.isEmpty() ? "Jugador 1" : p1;
            player2Name = p2.isEmpty() ? "Jugador 2" : p2;
            dialog.dismiss();
            startNewLocalDuel();
        });

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

        dialog.show();
    }

    private void startNetworkDuel() {
        player1Score = 0;
        player2Score = 0;
        currentQuestionIndex = 0;
        updateScoreboard();

        duelSession.startListening(new ActiveDuelSession.Listener() {
            @Override
            public void onRemoteAnswer(int questionIndex, int selectedOptionIndex, int gainedScore) {
                if (isFinishing() || questionIndex != currentQuestionIndex) {
                    return;
                }
                resolveTurnWithAnswer(selectedOptionIndex, gainedScore);
            }

            @Override
            public void onRemoteDisconnected() {
                if (isFinishing() || currentQuestionIndex >= duelQuestions.size()) {
                    return;
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                new AlertDialog.Builder(DuelActivity.this)
                        .setTitle(R.string.duel_title)
                        .setMessage(getString(R.string.duel_opponent_disconnected, duelSession.getRemotePlayerName()))
                        .setCancelable(false)
                        .setPositiveButton(R.string.btn_back_map, (d, w) -> finish())
                        .show();
            }
        });

        List<Integer> qIds = duelSession.getQuestionIds();
        if (qIds != null && !qIds.isEmpty()) {
            questionRepository.getQuestionsByIds(qIds, new DbCallback<List<Question>>() {
                @Override
                public void onSuccess(List<Question> dbQuestions) {
                    if (isFinishing()) return;
                    buildDuelQuestionList(dbQuestions);
                    displayCurrentTurn();
                }

                @Override
                public void onError(Exception error) {
                    if (isFinishing()) return;
                    buildDuelQuestionList(QuestionBank.INSTANCE.getRandomQuiz(12));
                    displayCurrentTurn();
                }
            });
        } else {
            startNewLocalDuel();
        }
    }

    private void startNewLocalDuel() {
        player1Score = 0;
        player2Score = 0;
        currentQuestionIndex = 0;
        updateScoreboard();

        questionRepository.getRandomQuestions(16, new DbCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> dbQuestions) {
                if (isFinishing()) {
                    return;
                }
                buildDuelQuestionList(dbQuestions);
                displayCurrentTurn();
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                buildDuelQuestionList(QuestionBank.INSTANCE.getRandomQuiz(12));
                displayCurrentTurn();
            }
        });
    }

    private void buildDuelQuestionList(List<Question> source) {
        duelQuestions = new ArrayList<>();
        if (source != null) {
            for (Question q : source) {
                if (q != null && !q.isWordPuzzle() && q.getOptions() != null && q.getOptions().size() >= 4) {
                    duelQuestions.add(q);
                    if (duelQuestions.size() >= TOTAL_DUEL_QUESTIONS) {
                        break;
                    }
                }
            }
        }
        if (duelQuestions.size() < TOTAL_DUEL_QUESTIONS) {
            for (Question q : QuestionBank.INSTANCE.getRandomQuiz(12)) {
                if (q != null && !q.isWordPuzzle() && q.getOptions() != null && q.getOptions().size() >= 4) {
                    duelQuestions.add(q);
                    if (duelQuestions.size() >= TOTAL_DUEL_QUESTIONS) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isMyTurn() {
        if (!isNetworkDuel || duelSession == null) {
            return true;
        }
        boolean isP1Turn = (currentQuestionIndex % 2 == 0);
        return duelSession.isHost() == isP1Turn;
    }

    private void displayCurrentTurn() {
        if (currentQuestionIndex >= duelQuestions.size()) {
            showResultDialog();
            return;
        }
        boolean myTurn = isMyTurn();
        isAnsweringLocked = !myTurn;
        binding.tvDuelFeedback.setVisibility(View.GONE);
        Question q = duelQuestions.get(currentQuestionIndex);
        boolean isP1Turn = (currentQuestionIndex % 2 == 0);
        String activePlayer = isP1Turn ? player1Name : player2Name;

        binding.tvDuelQuestionProgress.setText((currentQuestionIndex + 1) + "/" + duelQuestions.size());
        binding.tvDuelTurnBanner.setText(myTurn
                ? getString(R.string.duel_turn_format, activePlayer)
                : getString(R.string.duel_opponent_thinking, activePlayer));
        binding.cardDuelP1.setBackgroundResource(isP1Turn ? R.drawable.bg_pill_score_yellow : R.drawable.bg_btn_pill_white);
        binding.cardDuelP2.setBackgroundResource(isP1Turn ? R.drawable.bg_btn_pill_white : R.drawable.bg_pill_score_yellow);

        binding.tvDuelCategory.setText(q.getCategory());
        binding.tvDuelQuestion.setText(q.getQuestionText());

        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setEnabled(myTurn);
            optionButtons.get(i).setBackgroundResource(R.drawable.bg_quiz_option_unselected);
            optionTexts.get(i).setText(q.getOptions().get(i));
            optionTexts.get(i).setTextColor(Color.WHITE);
        }

        startTurnTimer();
    }

    private void startTurnTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        secondsRemaining = TURN_SECONDS;
        binding.pbDuelTimer.setMax(TURN_SECONDS);
        binding.pbDuelTimer.setProgress(TURN_SECONDS);
        binding.tvDuelTimer.setText(String.format(Locale.US, "00:%02d", secondsRemaining));

        countDownTimer = new CountDownTimer(TURN_SECONDS * 1000L, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = (int) Math.ceil(millisUntilFinished / 1000.0);
                binding.pbDuelTimer.setProgress(secondsRemaining);
                binding.tvDuelTimer.setText(String.format(Locale.US, "00:%02d", secondsRemaining));
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0;
                binding.pbDuelTimer.setProgress(0);
                binding.tvDuelTimer.setText("00:00");
                if (isMyTurn()) {
                    handleLocalOptionSelected(-1);
                }
            }
        }.start();
    }

    private void handleLocalOptionSelected(int selectedIdx) {
        if (isAnsweringLocked || currentQuestionIndex >= duelQuestions.size()) {
            return;
        }
        isAnsweringLocked = true;
        Question q = duelQuestions.get(currentQuestionIndex);
        boolean isCorrect = selectedIdx == q.getCorrectAnswerIndex();
        int gained = isCorrect ? (100 + secondsRemaining * 5) : 0;

        if (isNetworkDuel && duelSession != null) {
            duelSession.sendAnswer(currentQuestionIndex, selectedIdx, gained);
        }
        resolveTurnWithAnswer(selectedIdx, gained);
    }

    private void resolveTurnWithAnswer(int selectedIdx, int gainedScore) {
        if (currentQuestionIndex >= duelQuestions.size()) {
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Question q = duelQuestions.get(currentQuestionIndex);
        boolean isP1Turn = (currentQuestionIndex % 2 == 0);
        String activePlayer = isP1Turn ? player1Name : player2Name;
        boolean isCorrect = selectedIdx == q.getCorrectAnswerIndex();

        for (LinearLayout btn : optionButtons) {
            btn.setEnabled(false);
        }

        if (isCorrect) {
            if (isP1Turn) {
                player1Score += gainedScore;
            } else {
                player2Score += gainedScore;
            }
            feedbackManager.playCorrect();
            optionButtons.get(selectedIdx).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(selectedIdx).setTextColor(Color.WHITE);
            binding.tvDuelFeedback.setText(getString(R.string.game_feedback_correct, activePlayer, gainedScore));
            binding.tvDuelFeedback.setTextColor(ContextCompat.getColor(this, R.color.ref_yellow_selected));
        } else {
            feedbackManager.playWrong();
            if (selectedIdx >= 0 && selectedIdx < optionButtons.size()) {
                optionButtons.get(selectedIdx).setBackgroundResource(R.drawable.bg_quiz_option_wrong);
                optionTexts.get(selectedIdx).setTextColor(Color.WHITE);
            }
            optionButtons.get(q.getCorrectAnswerIndex()).setBackgroundResource(R.drawable.bg_quiz_option_correct);
            optionTexts.get(q.getCorrectAnswerIndex()).setTextColor(Color.WHITE);
            binding.tvDuelFeedback.setText(selectedIdx < 0
                    ? getString(R.string.game_feedback_timeout)
                    : getString(R.string.game_feedback_wrong, activePlayer));
            binding.tvDuelFeedback.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
        }

        binding.tvDuelFeedback.setVisibility(View.VISIBLE);
        updateScoreboard();

        handler.postDelayed(() -> {
            if (!isFinishing()) {
                currentQuestionIndex++;
                displayCurrentTurn();
            }
        }, 1600L);
    }

    private void updateScoreboard() {
        binding.tvDuelP1Name.setText(player1Name);
        binding.tvDuelP2Name.setText(player2Name);
        binding.tvDuelP1Score.setText(getString(R.string.duel_score_format, player1Score));
        binding.tvDuelP2Score.setText(getString(R.string.duel_score_format, player2Score));
    }

    private void showResultDialog() {
        String title;
        boolean localWonNetwork = false;
        if (isNetworkDuel && duelSession != null) {
            int myScore = duelSession.isHost() ? player1Score : player2Score;
            int otherScore = duelSession.isHost() ? player2Score : player1Score;
            localWonNetwork = myScore > otherScore;
        }

        if (player1Score > player2Score) {
            title = getString(R.string.duel_winner_title, player1Name);
            if (!isNetworkDuel || localWonNetwork) {
                long userId = prefsManager.getCurrentUserId();
                if (userId > 0) {
                    userRepository.grantCoins(userId, DUEL_WIN_COINS, null);
                }
            }
        } else if (player2Score > player1Score) {
            title = getString(R.string.duel_winner_title, player2Name);
            if (isNetworkDuel && localWonNetwork) {
                long userId = prefsManager.getCurrentUserId();
                if (userId > 0) {
                    userRepository.grantCoins(userId, DUEL_WIN_COINS, null);
                }
            }
        } else {
            title = getString(R.string.duel_draw_title);
        }

        String summary = getString(R.string.duel_result_summary,
                player1Name, player1Score, player2Name, player2Score);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(summary)
                .setCancelable(false)
                .setNegativeButton(R.string.btn_back_map, (d, w) -> finish());
        if (!isNetworkDuel) {
            builder.setPositiveButton(R.string.duel_btn_rematch, (d, w) -> startNewLocalDuel());
        }
        builder.show();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
        if (isNetworkDuel) {
            ActiveDuelSession.clearCurrent();
        }
        super.onDestroy();
    }
}
