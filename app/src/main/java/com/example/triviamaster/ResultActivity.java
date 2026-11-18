package com.example.triviamaster;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.MatchHistoryRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityResultBinding;
import com.example.triviamaster.databinding.DialogGiftClaimedBinding;
import com.example.triviamaster.databinding.DialogGiftUnlockedBinding;
import com.example.triviamaster.databinding.DialogMedalUnlockedBinding;
import com.example.triviamaster.models.Difficulty;
import com.example.triviamaster.models.MatchHistoryItem;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.RetroFeedbackManager;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_FINAL_SCORE = "extra_final_score";
    public static final String EXTRA_MAX_STREAK = "extra_max_streak";
    public static final String EXTRA_CORRECT_COUNT = "extra_correct_count";
    public static final String EXTRA_TOTAL_QUESTIONS = "extra_total_questions";
    public static final String EXTRA_LIVES_LEFT = "extra_lives_left";
    public static final String EXTRA_LEVEL_ID = "extra_level_id";
    public static final String EXTRA_EARNED_STARS = "extra_earned_stars";
    public static final String EXTRA_IS_QUICK_PLAY = "extra_is_quick_play";
    public static final String EXTRA_BOSS_DEFEATED = "extra_boss_defeated";

    private ActivityResultBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private MatchHistoryRepository historyRepository;
    private RetroFeedbackManager feedbackManager;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private String specialTitle;
    private int specialMinLevel = 1;
    private int specialMaxLevel = 16;
    private int specialCount = 10;
    private int specialHotspotId = -1;
    private int specialBonusPoints;
    private int specialBonusEnergy;
    private int specialExtraChests;
    private int coinsEarned;
    private boolean bossDefeated;
    private int finalScore;
    private int maxStreak;
    private int correctCount;
    private int livesLeft;
    private int earnedStars;
    private boolean isQuickPlay;
    private boolean isTimeAttack;
    private boolean isGiftEarnedThisMatch;
    private Integer newlyUnlockedMedal;

    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.ic_brain;
    private int totalQuestions = 5;
    private int levelId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        historyRepository = new MatchHistoryRepository(this);
        feedbackManager = new RetroFeedbackManager(this);
        extractIntentData();
        handleProgression();
        displayResults();
        saveResultToRanking();
        setupButtons();
    }

    private void extractIntentData() {
        String lastPlayerName = prefsManager.getLastPlayerName();
        userName = (lastPlayerName == null || lastPlayerName.trim().isEmpty()) ? "Jugador" : lastPlayerName;
        avatarId = prefsManager.getLastAvatarId();
        avatarResId = prefsManager.getAvatarById(avatarId).getIconResId();
        finalScore = getIntent().getIntExtra(EXTRA_FINAL_SCORE, 0);
        maxStreak = getIntent().getIntExtra(EXTRA_MAX_STREAK, 0);
        correctCount = getIntent().getIntExtra(EXTRA_CORRECT_COUNT, 0);
        totalQuestions = getIntent().getIntExtra(EXTRA_TOTAL_QUESTIONS, 5);
        livesLeft = getIntent().getIntExtra(EXTRA_LIVES_LEFT, 0);
        levelId = getIntent().getIntExtra("extra_level_id", -1);
        earnedStars = getIntent().getIntExtra(EXTRA_EARNED_STARS, 0);
        isQuickPlay = getIntent().getBooleanExtra(EXTRA_IS_QUICK_PLAY, false);
        isTimeAttack = getIntent().getBooleanExtra(MapActivity.EXTRA_IS_TIME_ATTACK, false);
        currentDifficulty = Difficulty.fromName(getIntent().getStringExtra(MapActivity.EXTRA_DIFFICULTY));
        specialTitle = getIntent().getStringExtra(MapActivity.EXTRA_SPECIAL_TITLE);
        specialMinLevel = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_MIN_LEVEL, 1);
        specialMaxLevel = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_MAX_LEVEL, 16);
        specialCount = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_COUNT, 10);
        specialHotspotId = getIntent().getIntExtra(MapActivity.EXTRA_SPECIAL_HOTSPOT_ID, -1);
        bossDefeated = getIntent().getBooleanExtra(EXTRA_BOSS_DEFEATED, false);
    }

    private void handleProgression() {
        if (earnedStars > 0) {
            int previousStars = (levelId > 0 && !isQuickPlay) ? prefsManager.getLevelStars(levelId) : 0;
            if (levelId > 0 && !isQuickPlay) {
                prefsManager.saveLevelProgress(levelId, earnedStars, finalScore);
            }
            if (previousStars == 0 || earnedStars > previousStars || isQuickPlay) {
                prefsManager.setAvailableGifts(prefsManager.getAvailableGifts() + 1);
                isGiftEarnedThisMatch = true;
            }
            if (isQuickPlay && (specialHotspotId < 0 || !prefsManager.isSpecialCompleted(specialHotspotId, currentDifficulty))) {
                switch (currentDifficulty) {
                    case EASY:
                        specialBonusEnergy = 1;
                        specialBonusPoints = 50;
                        specialExtraChests = 0;
                        break;
                    case HARD:
                        specialBonusEnergy = 3;
                        specialBonusPoints = 300;
                        specialExtraChests = 1;
                        break;
                    default:
                        specialBonusEnergy = 2;
                        specialBonusPoints = 150;
                        specialExtraChests = 0;
                        break;
                }
                finalScore += specialBonusPoints;
                prefsManager.setPlayerEnergy(prefsManager.getPlayerEnergy() + specialBonusEnergy);
                if (specialExtraChests > 0) {
                    prefsManager.setAvailableGifts(prefsManager.getAvailableGifts() + specialExtraChests);
                }
                if (specialHotspotId >= 0) {
                    prefsManager.markSpecialCompleted(specialHotspotId, currentDifficulty);
                }
                long userId = prefsManager.getCurrentUserId();
                if (userId > 0) {
                    if (specialHotspotId >= 0) {
                        userRepository.markSpecialCompletedAndReward(
                                userId, specialHotspotId, currentDifficulty, specialBonusEnergy, specialExtraChests, null);
                    } else {
                        userRepository.grantSpecialReward(userId, specialBonusEnergy, specialExtraChests, null);
                    }
                }
            }
            if (!isQuickPlay) {
                if (levelId == 5 && !prefsManager.isMedal1Notified()) {
                    prefsManager.setMedal1Notified(true);
                    newlyUnlockedMedal = 1;
                } else if (levelId == 10 && !prefsManager.isMedal2Notified()) {
                    prefsManager.setMedal2Notified(true);
                    newlyUnlockedMedal = 2;
                } else if (levelId == 16 && !prefsManager.isMedal3Notified()) {
                    prefsManager.setMedal3Notified(true);
                    newlyUnlockedMedal = 3;
                }
            }
        }
    }

    private void displayResults() {
        binding.ivResultAvatar.setImageResource(avatarResId);
        binding.tvResultPlayerName.setText(userName);

        List<ImageView> stars = Arrays.asList(binding.ivStar1, binding.ivStar2, binding.ivStar3);
        for (int i = 0; i < stars.size(); i++) {
            ImageView starView = stars.get(i);
            if (i < earnedStars) {
                starView.setAlpha(1.0f);
                starView.setImageResource(R.drawable.ic_star_golden);
                animateStarBounce(starView, i * 200L);
            } else {
                starView.setAlpha(0.25f);
                starView.setImageResource(R.drawable.ic_star_golden);
            }
        }

        if (earnedStars > 0) {
            binding.tvResultHeaderTitle.setText("¡NIVEL COMPLETADO!");
            binding.ivVictoryMascot.setImageResource(R.drawable.img_monster_violeta);
            animateVictoryMascot();
            feedbackManager.playVictory();
        } else {
            binding.tvResultHeaderTitle.setText("¡FIN DE LA PARTIDA!");
            binding.ivVictoryMascot.setImageResource(R.drawable.img_monster_violeta);
        }

        ObjectAnimator animX = ObjectAnimator.ofFloat(binding.btnNextLevel, "scaleX", 1.0f, 1.04f, 1.0f);
        animX.setDuration(1200L);
        animX.setRepeatCount(ObjectAnimator.INFINITE);
        animX.setRepeatMode(ObjectAnimator.REVERSE);
        animX.start();

        ObjectAnimator animY = ObjectAnimator.ofFloat(binding.btnNextLevel, "scaleY", 1.0f, 1.04f, 1.0f);
        animY.setDuration(1200L);
        animY.setRepeatCount(ObjectAnimator.INFINITE);
        animY.setRepeatMode(ObjectAnimator.REVERSE);
        animY.start();

        int percentage = totalQuestions > 0 ? (correctCount * 100) / totalQuestions : 0;
        String personalizedMsg;
        if (percentage == 100) {
            personalizedMsg = getString(R.string.msg_perfect, userName);
        } else if (percentage >= 70) {
            personalizedMsg = getString(R.string.msg_great, userName);
        } else if (percentage >= 40) {
            personalizedMsg = getString(R.string.msg_good, userName);
        } else {
            personalizedMsg = getString(R.string.msg_low, userName);
        }
        binding.tvResultMessage.setText(personalizedMsg);
        coinsEarned = correctCount > 0
                ? Math.max(10, (int) Math.round(correctCount * 10 * currentDifficulty.getScoreMultiplier()))
                : 0;
        if (bossDefeated) {
            coinsEarned += 100;
            prefsManager.setPlayerEnergy(prefsManager.getPlayerEnergy() + 1);
            long userId = prefsManager.getCurrentUserId();
            if (userId > 0) {
                userRepository.grantSpecialReward(userId, 1, 0, null);
            }
        }
        if (coinsEarned > 0) {
            prefsManager.setPlayerCoins(prefsManager.getPlayerCoins() + coinsEarned);
            long userId = prefsManager.getCurrentUserId();
            if (userId > 0) {
                userRepository.grantCoins(userId, coinsEarned, null);
            }
            binding.tvFinalScore.setText(finalScore + " pts • " + getString(R.string.result_coins_earned, coinsEarned));
        } else {
            binding.tvFinalScore.setText(finalScore + " pts");
        }
        int xpEarned = Math.max(10, correctCount * 25 + maxStreak * 10);
        prefsManager.setPlayerXp(prefsManager.getPlayerXp() + xpEarned);
        long xpUserId = prefsManager.getCurrentUserId();
        if (xpUserId > 0) {
            userRepository.grantXpAndCheckLevelUp(xpUserId, xpEarned, new DbCallback<int[]>() {
                @Override
                public void onSuccess(int[] result) {
                    if (isFinishing() || result == null || result.length < 4) {
                        return;
                    }
                    prefsManager.setPlayerXp(result[3]);
                    if (result[1] > result[0]) {
                        prefsManager.setPlayerCoins(prefsManager.getPlayerCoins() + result[2]);
                        Toast.makeText(ResultActivity.this,
                                getString(R.string.result_level_up_msg, result[1], result[2]),
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Exception error) {
                }
            });
        }
        binding.tvAccuracy.setText(correctCount + " / " + totalQuestions + " (" + percentage + "%) • " + getString(R.string.result_xp_earned, xpEarned));
        binding.tvMaxStreak.setText(maxStreak + " seguidas");
        binding.tvLivesLeft.setText(isTimeAttack
                ? getString(R.string.time_attack_title)
                : "Vidas: (" + livesLeft + "/" + currentDifficulty.getInitialLives() + ")");

        if (newlyUnlockedMedal != null) {
            binding.getRoot().postDelayed(() -> {
                if (!isFinishing()) {
                    showMedalUnlockedCelebrationDialog(newlyUnlockedMedal);
                }
            }, 800L);
        } else if (isGiftEarnedThisMatch) {
            binding.getRoot().postDelayed(() -> {
                if (!isFinishing()) {
                    showGiftUnlockedDialog();
                }
            }, 850L);
        }
    }

    private void showMedalUnlockedCelebrationDialog(int medalNum) {
        DialogMedalUnlockedBinding dialogBinding = DialogMedalUnlockedBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        int iconRes;
        String title;
        String subtitle;
        switch (medalNum) {
            case 1:
                iconRes = R.drawable.img_medalla1;
                title = "Iniciado de Trivia";
                subtitle = "¡Felicidades, " + userName + "! Has superado el Nivel 5 del mapa y desbloqueaste tu primera medalla de honor. ¡Consúltala en tu perfil!";
                break;
            case 2:
                iconRes = R.drawable.img_medalla2;
                title = "Maestro de Sabiduría";
                subtitle = "¡Extraordinario, " + userName + "! Has superado el Nivel 10 del mapa. ¡Tu sabiduría crece y desbloqueaste la Medalla de Plata!";
                break;
            default:
                iconRes = R.drawable.img_medalla3;
                title = "¡Conquistador del Castillo!";
                subtitle = "¡Increíble logro, " + userName + "! Has completado con éxito los 16 niveles y conquistado el Gran Castillo Final. ¡Has ganado la máxima distinción del juego!";
                break;
        }

        dialogBinding.ivMedalDialogIcon.setImageResource(iconRes);
        dialogBinding.tvMedalDialogTitle.setText(title);
        dialogBinding.tvMedalDialogSubtitle.setText(subtitle);
        dialogBinding.btnMedalAwesome.setOnClickListener(v -> {
            dialog.dismiss();
            if (isGiftEarnedThisMatch) {
                showGiftUnlockedDialog();
            }
        });
        dialog.show();
    }

    private void showGiftUnlockedDialog() {
        DialogGiftUnlockedBinding dialogBinding = DialogGiftUnlockedBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogBinding.tvGiftDialogMessage.setText("¡Felicitaciones, " + userName + "! Por tu gran desempeño has ganado un cofre de regalo sorpresa. En el mapa verás un signo [!] sobre el cofre listo para reclamar.");
        dialogBinding.btnGiftOpenNow.setOnClickListener(v -> {
            dialog.dismiss();
            if (prefsManager.getAvailableGifts() > 0) {
                prefsManager.setAvailableGifts(prefsManager.getAvailableGifts() - 1);
            }
            prefsManager.addChestRewards(1, 100);
            long userId = prefsManager.getCurrentUserId();
            if (userId > 0) {
                userRepository.claimChestReward(userId, 1, 100, new DbCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        if (result != null) {
                            prefsManager.syncFromUser(result);
                        }
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }

            DialogGiftClaimedBinding claimedBinding = DialogGiftClaimedBinding.inflate(getLayoutInflater());
            AlertDialog claimedDialog = new AlertDialog.Builder(ResultActivity.this).setView(claimedBinding.getRoot()).create();
            Window claimedWindow = claimedDialog.getWindow();
            if (claimedWindow != null) {
                claimedWindow.setBackgroundDrawableResource(android.R.color.transparent);
            }
            claimedBinding.tvClaimedTitle.setText("¡Cofre Abierto!");
            claimedBinding.tvClaimedSubtitle.setText("¡Felicidades, " + userName + "! Abriste tu cofre de regalo y recibiste tus recompensas:");
            claimedBinding.btnClaimedAwesome.setOnClickListener(cv -> claimedDialog.dismiss());
            claimedDialog.show();
        });

        dialogBinding.btnGiftLater.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void animateVictoryMascot() {
        binding.ivVictoryMascot.setScaleX(0.6f);
        binding.ivVictoryMascot.setScaleY(0.6f);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.ivVictoryMascot, "scaleX", 0.6f, 1.15f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.ivVictoryMascot, "scaleY", 0.6f, 1.15f, 1.0f);
        ObjectAnimator bounce = ObjectAnimator.ofFloat(binding.ivVictoryMascot, "translationY", 0.0f, -20.0f, 0.0f);
        bounce.setDuration(1000L);
        bounce.setRepeatCount(ObjectAnimator.INFINITE);
        bounce.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(600L);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
        bounce.start();
    }

    private void animateStarBounce(View view, long delay) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.25f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.25f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(500L);
        set.setStartDelay(delay);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
    }

    private void saveResultToRanking() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        boolean isNewRecord = prefsManager.saveGameScore(userName, avatarResId, finalScore, maxStreak, currentDate);
        if (bossDefeated) {
            binding.tvNewRecordBadge.setVisibility(View.VISIBLE);
            binding.tvNewRecordBadge.setText(getString(R.string.boss_defeated_badge));
        } else if (isQuickPlay && specialBonusPoints > 0) {
            binding.tvNewRecordBadge.setVisibility(View.VISIBLE);
            String rewardText = specialExtraChests > 0
                    ? getString(R.string.special_reward_badge_chest, currentDifficulty.getDisplayName(), specialBonusEnergy, specialBonusPoints)
                    : getString(R.string.special_reward_badge, currentDifficulty.getDisplayName(), specialBonusEnergy, specialBonusPoints);
            binding.tvNewRecordBadge.setText(rewardText);
        } else if (isNewRecord && finalScore > 0) {
            binding.tvNewRecordBadge.setVisibility(View.VISIBLE);
        } else {
            binding.tvNewRecordBadge.setVisibility(View.GONE);
        }

        long userId = prefsManager.getCurrentUserId();
        if (userId > 0) {
            userRepository.saveMatchResult(userId, levelId, earnedStars, finalScore, maxStreak, isQuickPlay,
                    new DbCallback<User>() {
                        @Override
                        public void onSuccess(User updatedUser) {
                            if (updatedUser != null) {
                                prefsManager.syncFromUser(updatedUser);
                            }
                        }

                        @Override
                        public void onError(Exception error) {
                        }
                    });

            String modeLabel = (levelId > 0 && !isQuickPlay)
                    ? getString(R.string.history_item_mode_level, levelId)
                    : (isTimeAttack
                            ? getString(R.string.time_attack_title)
                            : ((specialTitle != null && !specialTitle.trim().isEmpty())
                                    ? specialTitle
                                    : getString(R.string.history_item_mode_cave)));
            MatchHistoryItem historyItem = new MatchHistoryItem(
                    0L,
                    userId,
                    levelId,
                    modeLabel,
                    currentDifficulty.getDisplayName(),
                    finalScore,
                    correctCount,
                    totalQuestions,
                    earnedStars,
                    currentDate
            );
            historyRepository.insertMatch(historyItem, null);
        }
    }

    private void setupButtons() {
        if (levelId > 0 && !isQuickPlay) {
            if (earnedStars > 0 && levelId < 16) {
                binding.btnNextLevel.setText("Siguiente Nivel (" + (levelId + 1) + ")");
                binding.btnNextLevel.setOnClickListener(v -> {
                    Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                    intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
                    intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
                    intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
                    intent.putExtra("extra_level_id", levelId + 1);
                    intent.putExtra("extra_is_quick_play", false);
                    intent.putExtra(MapActivity.EXTRA_DIFFICULTY, currentDifficulty.name());
                    startActivity(intent);
                    finish();
                });
            } else {
                binding.btnNextLevel.setText("Reintentar Nivel");
                binding.btnNextLevel.setOnClickListener(v -> {
                    Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                    intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
                    intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
                    intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
                    intent.putExtra("extra_level_id", levelId);
                    intent.putExtra("extra_is_quick_play", false);
                    intent.putExtra(MapActivity.EXTRA_DIFFICULTY, currentDifficulty.name());
                    startActivity(intent);
                    finish();
                });
            }
        } else if (earnedStars > 0 && specialHotspotId >= 0) {
            binding.btnNextLevel.setText(getString(R.string.btn_back_map));
            binding.btnNextLevel.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, MapActivity.class);
                intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
                intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
                intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        } else {
            binding.btnNextLevel.setText(getString(R.string.btn_play_again));
            binding.btnNextLevel.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
                intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
                intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
                intent.putExtra("extra_is_quick_play", true);
                intent.putExtra(MapActivity.EXTRA_IS_TIME_ATTACK, isTimeAttack);
                intent.putExtra(MapActivity.EXTRA_DIFFICULTY, currentDifficulty.name());
                intent.putExtra(MapActivity.EXTRA_SPECIAL_TITLE, specialTitle);
                intent.putExtra(MapActivity.EXTRA_SPECIAL_MIN_LEVEL, specialMinLevel);
                intent.putExtra(MapActivity.EXTRA_SPECIAL_MAX_LEVEL, specialMaxLevel);
                intent.putExtra(MapActivity.EXTRA_SPECIAL_COUNT, specialCount);
                intent.putExtra(MapActivity.EXTRA_SPECIAL_HOTSPOT_ID, specialHotspotId);
                startActivity(intent);
                finish();
            });
        }

        binding.btnBackToMap.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MapActivity.class);
            intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
            intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
            intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnGoRanking.setOnClickListener(v -> startActivity(new Intent(ResultActivity.this, RankingActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (feedbackManager != null) {
            feedbackManager.release();
        }
    }
}
