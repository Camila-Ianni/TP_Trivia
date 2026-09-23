package com.example.triviamaster;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
<<<<<<< HEAD
=======
import android.animation.ValueAnimator;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.databinding.ActivityResultBinding;
import com.example.triviamaster.databinding.DialogGiftClaimedBinding;
import com.example.triviamaster.databinding.DialogGiftUnlockedBinding;
import com.example.triviamaster.databinding.DialogMedalUnlockedBinding;
import java.text.SimpleDateFormat;
<<<<<<< HEAD
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
=======
import java.util.Date;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResultActivity extends AppCompatActivity {
    private static final class MedalInfo {
        private final int iconRes;
        private final String title;
        private final String subtitle;

        private MedalInfo(int iconRes, String title, String subtitle) {
            this.iconRes = iconRes;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

    private ActivityResultBinding binding;
    private PreferencesManager prefsManager;
    private int finalScore;
    private int maxStreak;
    private int correctCount;
    private int livesLeft;
    private int earnedStars;
    private boolean isQuickPlay;
    private boolean isGiftEarnedThisMatch;
<<<<<<< HEAD
    private Integer newlyUnlockedMedal;

=======

    @Nullable
    private Integer newlyUnlockedMedal;

    @NotNull
    public static String EXTRA_FINAL_SCORE = "extra_final_score";

    @NotNull
    public static String EXTRA_MAX_STREAK = "extra_max_streak";

    @NotNull
    public static String EXTRA_CORRECT_COUNT = "extra_correct_count";

    @NotNull
    public static String EXTRA_TOTAL_QUESTIONS = "extra_total_questions";

    @NotNull
    public static String EXTRA_LIVES_LEFT = "extra_lives_left";

    @NotNull
    public static String EXTRA_LEVEL_ID = "extra_level_id";

    @NotNull
    public static String EXTRA_EARNED_STARS = "extra_earned_stars";

    @NotNull
    public static String EXTRA_IS_QUICK_PLAY = "extra_is_quick_play";

    @NotNull
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.ic_brain;
    private int totalQuestions = 5;
    private int levelId = -1;

<<<<<<< HEAD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
=======
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityResultBinding.inflate(getLayoutInflater());
        ActivityResultBinding activityResultBinding = this.binding;
        setContentView(activityResultBinding.getRoot());
        this.prefsManager = new PreferencesManager(this);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        extractIntentData();
        handleProgression();
        displayResults();
        saveResultToRanking();
        setupButtons();
    }

    private void extractIntentData() {
<<<<<<< HEAD
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
=======
        ResultActivity resultActivity = this;
        PreferencesManager preferencesManager = this.prefsManager;
        String lastPlayerName = preferencesManager.getLastPlayerName();
        resultActivity.userName = lastPlayerName == null || lastPlayerName.isBlank()
                ? "Jugador" : lastPlayerName;
        PreferencesManager preferencesManager2 = this.prefsManager;
        this.avatarId = preferencesManager2.getLastAvatarId();
        PreferencesManager preferencesManager3 = this.prefsManager;
        this.avatarResId = preferencesManager3.getAvatarById(this.avatarId).getIconResId();
        this.finalScore = getIntent().getIntExtra(EXTRA_FINAL_SCORE, 0);
        this.maxStreak = getIntent().getIntExtra(EXTRA_MAX_STREAK, 0);
        this.correctCount = getIntent().getIntExtra(EXTRA_CORRECT_COUNT, 0);
        this.totalQuestions = getIntent().getIntExtra(EXTRA_TOTAL_QUESTIONS, 5);
        this.livesLeft = getIntent().getIntExtra(EXTRA_LIVES_LEFT, 0);
        this.levelId = getIntent().getIntExtra("extra_level_id", -1);
        this.earnedStars = getIntent().getIntExtra(EXTRA_EARNED_STARS, 0);
        this.isQuickPlay = getIntent().getBooleanExtra("extra_is_quick_play", false);
    }

    private void handleProgression() {
        if (earnedStars <= 0) {
            return;
        }

        PreferencesManager preferences = prefsManager;
        int previousStars = levelId > 0 && !isQuickPlay
                ? preferences.getLevelStars(levelId)
                : 0;

        if (levelId > 0 && !isQuickPlay) {
            preferences.saveLevelProgress(levelId, earnedStars, finalScore);
        }

        if (isQuickPlay || earnedStars > previousStars) {
            preferences.setAvailableGifts(preferences.getAvailableGifts() + 1);
            isGiftEarnedThisMatch = true;
        }

        if (!isQuickPlay) {
            unlockMedalsReachedByLevel(preferences);
        }
    }

    private void unlockMedalsReachedByLevel(PreferencesManager preferences) {
        if (levelId >= 5 && !preferences.isMedal1Notified()) {
            preferences.setMedal1Notified(true);
        }
        if (levelId >= 10 && !preferences.isMedal2Notified()) {
            preferences.setMedal2Notified(true);
            newlyUnlockedMedal = 2;
        } else if (levelId >= 16 && !preferences.isMedal3Notified()) {
            preferences.setMedal3Notified(true);
            newlyUnlockedMedal = 3;
        } else if (levelId == 5 && newlyUnlockedMedal == null) {
            newlyUnlockedMedal = 1;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        }
    }

    private void displayResults() {
<<<<<<< HEAD
        binding.ivResultAvatar.setImageResource(avatarResId);
        binding.tvResultPlayerName.setText(userName);

        List<ImageView> stars = Arrays.asList(binding.ivStar1, binding.ivStar2, binding.ivStar3);
        for (int i = 0; i < stars.size(); i++) {
            ImageView starView = stars.get(i);
            if (i < earnedStars) {
                starView.setAlpha(1.0f);
                starView.setImageResource(R.drawable.ic_star_golden);
                animateStarBounce(starView, i * 200L);
=======
        String string;
        ActivityResultBinding activityResultBinding = this.binding;
        activityResultBinding.ivResultAvatar.setImageResource(this.avatarResId);
                binding.tvResultPlayerName.setText(this.userName);
        ImageView[] imageViewArr = new ImageView[3];
                imageViewArr[0] = binding.ivStar1;
                imageViewArr[1] = binding.ivStar2;
                imageViewArr[2] = binding.ivStar3;
        ImageView[] stars = {binding.ivStar1, binding.ivStar2, binding.ivStar3};
        for (int index = 0; index < stars.length; index++) {
            ImageView starView = stars[index];
            if (index < this.earnedStars) {
                starView.setAlpha(1.0f);
                starView.setImageResource(R.drawable.ic_star_golden);
                animateStarBounce(starView, index * 200);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            } else {
                starView.setAlpha(0.25f);
                starView.setImageResource(R.drawable.ic_star_golden);
            }
        }
<<<<<<< HEAD

        if (earnedStars > 0) {
            binding.tvResultHeaderTitle.setText("¡NIVEL COMPLETADO!");
            binding.ivVictoryMascot.setImageResource(R.drawable.img_monster_violeta);
            animateVictoryMascot();
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
        binding.tvFinalScore.setText(finalScore + " pts");
        binding.tvAccuracy.setText(correctCount + " / " + totalQuestions + " (" + percentage + "%)");
        binding.tvMaxStreak.setText(maxStreak + " seguidas");
        binding.tvLivesLeft.setText("Vidas: (" + livesLeft + "/3)");

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
=======
        if (this.earnedStars > 0) {
                        binding.tvResultHeaderTitle.setText("¡NIVEL COMPLETADO!");
                        binding.ivVictoryMascot.setImageResource(R.drawable.img_monster_violeta);
            animateVictoryMascot();
        } else {
                        binding.tvResultHeaderTitle.setText("¡FIN DE LA PARTIDA!");
                        binding.ivVictoryMascot.setImageResource(R.drawable.img_monster_violeta);
        }
                ObjectAnimator nextButtonScaleX = ObjectAnimator.ofFloat(
                binding.btnNextLevel, "scaleX", 1.0f, 1.04f, 1.0f);
        nextButtonScaleX.setDuration(1200L);
        nextButtonScaleX.setRepeatCount(ValueAnimator.INFINITE);
        nextButtonScaleX.setRepeatMode(ValueAnimator.REVERSE);
        nextButtonScaleX.start();
                ObjectAnimator nextButtonScaleY = ObjectAnimator.ofFloat(
                binding.btnNextLevel, "scaleY", 1.0f, 1.04f, 1.0f);
        nextButtonScaleY.setDuration(1200L);
        nextButtonScaleY.setRepeatCount(ValueAnimator.INFINITE);
        nextButtonScaleY.setRepeatMode(ValueAnimator.REVERSE);
        nextButtonScaleY.start();
        int percentage = this.totalQuestions > 0 ? (this.correctCount * 100) / this.totalQuestions : 0;
        if (percentage == 100) {
            string = getString(R.string.msg_perfect, new Object[]{this.userName});
        } else if (percentage >= 70) {
            string = getString(R.string.msg_great, new Object[]{this.userName});
        } else {
            string = percentage >= 40 ? getString(R.string.msg_good, new Object[]{this.userName}) : getString(R.string.msg_low, new Object[]{this.userName});
        }
        String personalizedMsg = string;
                binding.tvResultMessage.setText(personalizedMsg);
                binding.tvFinalScore.setText(this.finalScore + " pts");
                binding.tvAccuracy.setText(this.correctCount + " / " + this.totalQuestions + " (" + percentage + "%)");
                binding.tvMaxStreak.setText(this.maxStreak + " seguidas");
        StringBuilder lifeStatus = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i < this.livesLeft) {
                lifeStatus.append("O");
            } else {
                lifeStatus.append("X");
            }
        }
                binding.tvLivesLeft.setText("Vidas: (" + this.livesLeft + "/3)");
        if (this.newlyUnlockedMedal != null) {
                        binding.getRoot().postDelayed(() -> {
                showPendingMedal();
            }, 800L);
            return;
        }
        if (this.isGiftEarnedThisMatch) {
                        binding.getRoot().postDelayed(() -> {
                showPendingGift();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            }, 850L);
        }
    }

<<<<<<< HEAD
    private void showMedalUnlockedCelebrationDialog(int medalNum) {
=======
    private void showPendingMedal() {
        if (!isFinishing() && newlyUnlockedMedal != null) {
            showMedalUnlockedCelebrationDialog(newlyUnlockedMedal);
        }
    }

    private void showPendingGift() {
        if (!isFinishing()) {
            showGiftUnlockedDialog();
        }
    }
    private void showMedalUnlockedCelebrationDialog(int medalNum) {
        MedalInfo medal;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        DialogMedalUnlockedBinding dialogBinding = DialogMedalUnlockedBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
<<<<<<< HEAD

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
=======
        switch (medalNum) {
            case 1:
                medal = new MedalInfo(R.drawable.img_medalla1, "Iniciado de Trivia", "¡Felicidades, " + this.userName + "! Has superado el Nivel 5 del mapa y desbloqueaste tu primera medalla de honor. ¡Consúltala en tu perfil!");
                break;
            case 2:
                medal = new MedalInfo(R.drawable.img_medalla2, "Maestro de Sabiduría", "¡Extraordinario, " + this.userName + "! Has superado el Nivel 10 del mapa. ¡Tu sabiduría crece y desbloqueaste la Medalla de Plata!");
                break;
            default:
                medal = new MedalInfo(R.drawable.img_medalla3, "¡Conquistador del Castillo!", "¡Increíble logro, " + this.userName + "! Has completado con éxito los 16 niveles y conquistado el Gran Castillo Final. ¡Has ganado la máxima distinción del juego!");
                break;
        }
        dialogBinding.ivMedalDialogIcon.setImageResource(medal.iconRes);
        dialogBinding.tvMedalDialogTitle.setText(medal.title);
        dialogBinding.tvMedalDialogSubtitle.setText(medal.subtitle);
        dialogBinding.btnMedalAwesome.setOnClickListener(view -> {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            dialog.dismiss();
            if (isGiftEarnedThisMatch) {
                showGiftUnlockedDialog();
            }
        });
        dialog.show();
    }
<<<<<<< HEAD

=======
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    private void showGiftUnlockedDialog() {
        DialogGiftUnlockedBinding dialogBinding = DialogGiftUnlockedBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
<<<<<<< HEAD

        dialogBinding.tvGiftDialogMessage.setText("¡Felicitaciones, " + userName + "! Por tu gran desempeño has ganado un cofre de regalo sorpresa. En el mapa verás un signo [!] sobre el cofre listo para reclamar.");
        dialogBinding.btnGiftOpenNow.setOnClickListener(v -> {
            dialog.dismiss();
            if (prefsManager.getAvailableGifts() > 0) {
                prefsManager.setAvailableGifts(prefsManager.getAvailableGifts() - 1);
            }
            prefsManager.addChestRewards(1, 100);

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
=======
        dialogBinding.tvGiftDialogMessage.setText("¡Felicitaciones, " + this.userName + "! Por tu gran desempeño has ganado un cofre de regalo sorpresa. En el mapa verás un signo [!] sobre el cofre listo para reclamar.");
        dialogBinding.btnGiftOpenNow.setOnClickListener(view -> {
            dialog.dismiss();
            openGiftClaimDialog();
        });
        dialogBinding.btnGiftLater.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void openGiftClaimDialog() {
        PreferencesManager preferencesManager = this.prefsManager;
        if (preferencesManager.getAvailableGifts() > 0) {
            PreferencesManager preferencesManager2 = this.prefsManager;
            preferencesManager2.setAvailableGifts(preferencesManager2.getAvailableGifts() - 1);
        }
        PreferencesManager preferencesManager3 = this.prefsManager;
        preferencesManager3.addChestRewards(1, 100);
        DialogGiftClaimedBinding claimedBinding = DialogGiftClaimedBinding.inflate(getLayoutInflater());
        AlertDialog claimedDialog = new AlertDialog.Builder(this).setView(claimedBinding.getRoot()).create();
        Window window = claimedDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        claimedBinding.tvClaimedTitle.setText("¡Cofre Abierto!");
        claimedBinding.tvClaimedSubtitle.setText("¡Felicidades, " + userName + "! Abriste tu cofre de regalo y recibiste tus recompensas:");
        claimedBinding.btnClaimedAwesome.setOnClickListener(view -> claimedDialog.dismiss());
        claimedDialog.show();
    }

    private void animateVictoryMascot() {
        ActivityResultBinding activityResultBinding = this.binding;
        activityResultBinding.ivVictoryMascot.setScaleX(0.6f);
                binding.ivVictoryMascot.setScaleY(0.6f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.ivVictoryMascot, "scaleX", 0.6f, 1.15f, 1.0f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.ivVictoryMascot, "scaleY", 0.6f, 1.15f, 1.0f);
                ObjectAnimator mascotFloat = ObjectAnimator.ofFloat(
                binding.ivVictoryMascot, "translationY", 0.0f, -20.0f, 0.0f);
        mascotFloat.setDuration(1000L);
        mascotFloat.setRepeatCount(ValueAnimator.INFINITE);
        mascotFloat.setRepeatMode(ValueAnimator.REVERSE);
        AnimatorSet mascotEntrance = new AnimatorSet();
        mascotEntrance.playTogether(scaleX, scaleY);
        mascotEntrance.setDuration(600L);
        mascotEntrance.setInterpolator(new OvershootInterpolator());
        mascotEntrance.start();
        mascotFloat.start();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    private void animateStarBounce(View view, long delay) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.25f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.25f, 1.0f);
<<<<<<< HEAD
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(500L);
        set.setStartDelay(delay);
        set.setInterpolator(new OvershootInterpolator());
        set.start();
=======
        AnimatorSet starEntrance = new AnimatorSet();
        starEntrance.playTogether(scaleX, scaleY);
        starEntrance.setDuration(500L);
        starEntrance.setStartDelay(delay);
        starEntrance.setInterpolator(new OvershootInterpolator());
        starEntrance.start();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    private void saveResultToRanking() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
<<<<<<< HEAD
        boolean isNewRecord = prefsManager.saveGameScore(userName, avatarResId, finalScore, maxStreak, currentDate);
        if (isNewRecord && finalScore > 0) {
            binding.tvNewRecordBadge.setVisibility(View.VISIBLE);
        } else {
            binding.tvNewRecordBadge.setVisibility(View.GONE);
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
                    startActivity(intent);
                    finish();
                });
            }
        } else {
            binding.btnNextLevel.setText(getString(R.string.btn_play_again));
            binding.btnNextLevel.setOnClickListener(v -> {
                Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
                intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
                intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
                intent.putExtra("extra_is_quick_play", true);
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
=======
        PreferencesManager preferencesManager = this.prefsManager;
        boolean isNewRecord = preferencesManager.saveGameScore(
                this.userName, this.avatarResId, this.finalScore, this.maxStreak, currentDate);
        if (isNewRecord && this.finalScore > 0) {
            ActivityResultBinding activityResultBinding = this.binding;
            activityResultBinding.tvNewRecordBadge.setVisibility(View.VISIBLE);
            return;
        }
                binding.tvNewRecordBadge.setVisibility(View.GONE);
    }

    private void setupButtons() {
        if (this.levelId > 0 && !this.isQuickPlay) {
            if (this.earnedStars > 0 && this.levelId < 16) {
                ActivityResultBinding activityResultBinding = this.binding;
                activityResultBinding.btnNextLevel.setText("Siguiente Nivel (" + (this.levelId + 1) + ")");
                                binding.btnNextLevel.setOnClickListener(view -> startNextLevel());
            } else {
                                binding.btnNextLevel.setText("Reintentar Nivel");
                                binding.btnNextLevel.setOnClickListener(view -> retryLevel());
            }
        } else {
                        binding.btnNextLevel.setText(getString(R.string.btn_play_again));
                        binding.btnNextLevel.setOnClickListener(view -> startQuickPlay());
        }
                binding.btnBackToMap.setOnClickListener(view -> returnToMap());
                binding.btnGoRanking.setOnClickListener(
                view -> startActivity(new Intent(this, RankingActivity.class)));
    }
    private void startNextLevel() {
        launchGame(levelId + 1, false);
    }

    private void retryLevel() {
        launchGame(levelId, false);
    }

    private void startQuickPlay() {
        launchGame(-1, true);
    }

    private void returnToMap() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void launchGame(int gameLevelId, boolean quickPlay) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(MainActivity.EXTRA_USER_NAME, userName);
        intent.putExtra(MainActivity.EXTRA_AVATAR_ID, avatarId);
        intent.putExtra(MainActivity.EXTRA_AVATAR_RES_ID, avatarResId);
        intent.putExtra(EXTRA_LEVEL_ID, gameLevelId);
        intent.putExtra(EXTRA_IS_QUICK_PLAY, quickPlay);
        startActivity(intent);
        finish();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }
}
