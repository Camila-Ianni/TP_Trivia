package com.example.triviamaster;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.QuestionBank;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityMapBinding;
import com.example.triviamaster.databinding.DialogCaveChallengeBinding;
import com.example.triviamaster.databinding.DialogGameGuideBinding;
import com.example.triviamaster.models.Difficulty;
import com.example.triviamaster.models.Level;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.AvatarFrameHelper;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class MapActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL_ID = "extra_level_id";
    public static final String EXTRA_IS_QUICK_PLAY = "extra_is_quick_play";
    public static final String EXTRA_DIFFICULTY = "extra_difficulty";
    public static final String EXTRA_SPECIAL_TITLE = "extra_special_title";
    public static final String EXTRA_SPECIAL_MIN_LEVEL = "extra_special_min_level";
    public static final String EXTRA_SPECIAL_MAX_LEVEL = "extra_special_max_level";
    public static final String EXTRA_SPECIAL_COUNT = "extra_special_count";
    public static final String EXTRA_SPECIAL_HOTSPOT_ID = "extra_special_hotspot_id";
    public static final String EXTRA_IS_TIME_ATTACK = "extra_is_time_attack";

    private ActivityMapBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.img_avatar_perfil1;
    private ObjectAnimator giftPulseAnimX;
    private ObjectAnimator giftPulseAnimY;
    private boolean guideAutoChecked;
    private boolean dailyRewardChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        questionRepository = new QuestionRepository(this);
        extractIntentData();
        setupHeader();
        setupBottomNav();
        renderRoadmap();
        checkFirstTimeGuide();
    }

    private void checkFirstTimeGuide() {
        if (guideAutoChecked) {
            return;
        }
        guideAutoChecked = true;
        long userId = prefsManager.getCurrentUserId();
        if (!prefsManager.hasSeenGameGuide(userId)) {
            binding.getRoot().post(() -> {
                if (!isFinishing()) {
                    showGameGuideDialog();
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        extractIntentData();
        updateHeaderStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        extractIntentData();
        updateHeaderStats();
        renderRoadmap();
        refreshUserFromDb();
    }

    private void refreshUserFromDb() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            return;
        }
        userRepository.getUserById(userId, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing() || user == null) {
                    return;
                }
                prefsManager.syncFromUser(user);
                extractIntentData();
                updateHeaderStats();
                updateAdminNavVisibility();
                renderRoadmap();
                checkDailyReward(user);
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }

    private void checkDailyReward(User user) {
        if (dailyRewardChecked || user == null) {
            return;
        }
        long userId = prefsManager.getCurrentUserId();
        if (!prefsManager.hasSeenGameGuide(userId)) {
            return;
        }
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        if (today.equals(user.getLastDailyClaim())) {
            dailyRewardChecked = true;
            return;
        }
        String yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(new Date(System.currentTimeMillis() - 86400000L));
        int nextStreak = yesterday.equals(user.getLastDailyClaim()) ? user.getDailyStreak() + 1 : 1;
        dailyRewardChecked = true;
        showDailyRewardDialog(today, yesterday, nextStreak);
    }

    private void showDailyRewardDialog(String todayDate, String yesterdayDate, int nextStreak) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_gift_claimed, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        TextView tvTitle = dialogView.findViewById(R.id.tvClaimedTitle);
        TextView tvSubtitle = dialogView.findViewById(R.id.tvClaimedSubtitle);
        Button btnAwesome = dialogView.findViewById(R.id.btnClaimedAwesome);

        int previewCoins = Math.min(150, 30 + (nextStreak - 1) * 20);
        if (tvTitle != null) {
            tvTitle.setText(getString(R.string.daily_reward_streak_title, nextStreak));
        }
        if (tvSubtitle != null) {
            tvSubtitle.setText(getString(R.string.daily_reward_streak_desc, nextStreak, userName, previewCoins));
        }
        if (btnAwesome != null) {
            btnAwesome.setText(getString(R.string.daily_reward_btn));
            btnAwesome.setOnClickListener(v -> {
                dialog.dismiss();
                long userId = prefsManager.getCurrentUserId();
                if (userId > 0) {
                    userRepository.claimDailyReward(userId, todayDate, yesterdayDate, new DbCallback<User>() {
                        @Override
                        public void onSuccess(User updated) {
                            if (isFinishing() || updated == null) {
                                return;
                            }
                            prefsManager.syncFromUser(updated);
                            updateHeaderStats();
                        }

                        @Override
                        public void onError(Exception error) {
                        }
                    });
                }
            });
        }
        dialog.show();
    }

    private void extractIntentData() {
        String lastPlayerName = prefsManager.getLastPlayerName();
        userName = (lastPlayerName == null || lastPlayerName.trim().isEmpty()) ? "Jugador" : lastPlayerName;
        avatarId = prefsManager.getLastAvatarId();
        avatarResId = prefsManager.getAvatarById(avatarId).getIconResId();
    }

    private void setupHeader() {
        binding.tvHeaderGreeting.setText(getString(R.string.map_greeting_format, userName));
        binding.ivHeaderAvatar.setImageResource(avatarResId);
        updateHeaderStats();

        binding.btnHeaderAvatarContainer.setOnClickListener(v -> {
            startActivity(new Intent(MapActivity.this, ProfileActivity.class));
        });

        binding.btnHeaderGuide.setOnClickListener(v -> showGameGuideDialog());

        binding.btnTimeAttack.setOnClickListener(v -> launchTimeAttack());

        binding.btnHeaderGift.setOnClickListener(v -> handleGiftClick());
    }

    private void updateHeaderStats() {
        int totalStars = prefsManager.getTotalStars();
        binding.tvHeaderStars.setText(getString(R.string.header_stars_format, totalStars));
        binding.tvHeaderLives.setText(prefsManager.getPlayerEnergy() + "/12");
        updateGiftBadge();
        binding.tvHeaderGreeting.setText(getString(R.string.map_greeting_format, userName));
        binding.ivHeaderAvatar.setImageResource(avatarResId);
        AvatarFrameHelper.applyFrame(binding.btnHeaderAvatarContainer, prefsManager.getEquippedFrame());
    }

    private void updateGiftBadge() {
        int gifts = prefsManager.getAvailableGifts();
        if (gifts > 0) {
            binding.badgeGiftAlert.setVisibility(View.VISIBLE);
            binding.badgeGiftAlert.setText("!");
            if (giftPulseAnimX != null && giftPulseAnimX.isRunning()) {
                return;
            }
            giftPulseAnimX = ObjectAnimator.ofFloat(binding.btnHeaderGift, "scaleX", 1.0f, 1.12f, 1.0f);
            giftPulseAnimX.setDuration(1600L);
            giftPulseAnimX.setRepeatCount(ObjectAnimator.INFINITE);
            giftPulseAnimX.start();

            giftPulseAnimY = ObjectAnimator.ofFloat(binding.btnHeaderGift, "scaleY", 1.0f, 1.12f, 1.0f);
            giftPulseAnimY.setDuration(1600L);
            giftPulseAnimY.setRepeatCount(ObjectAnimator.INFINITE);
            giftPulseAnimY.start();
        } else {
            binding.badgeGiftAlert.setVisibility(View.GONE);
            if (giftPulseAnimX != null) {
                giftPulseAnimX.cancel();
                giftPulseAnimX = null;
            }
            if (giftPulseAnimY != null) {
                giftPulseAnimY.cancel();
                giftPulseAnimY = null;
            }
            binding.btnHeaderGift.setScaleX(1.0f);
            binding.btnHeaderGift.setScaleY(1.0f);
        }
    }

    private void handleGiftClick() {
        int gifts = prefsManager.getAvailableGifts();
        if (gifts > 0) {
            prefsManager.setAvailableGifts(gifts - 1);
            prefsManager.addChestRewards(1, 100);
            long userId = prefsManager.getCurrentUserId();
            if (userId > 0) {
                userRepository.claimChestReward(userId, 1, 100, null);
            }
            updateGiftBadge();
            updateHeaderStats();

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_gift_claimed, (ViewGroup) null);
            AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
            TextView tvTitle = dialogView.findViewById(R.id.tvClaimedTitle);
            TextView tvSubtitle = dialogView.findViewById(R.id.tvClaimedSubtitle);
            Button btnAwesome = dialogView.findViewById(R.id.btnClaimedAwesome);

            if (tvTitle != null) {
                tvTitle.setText(getString(R.string.gift_claimed_title, userName));
            }
            if (tvSubtitle != null) {
                tvSubtitle.setText(getString(R.string.gift_claimed_desc, userName));
            }
            if (btnAwesome != null) {
                btnAwesome.setOnClickListener(v -> dialog.dismiss());
            }
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.gift_none_available), Toast.LENGTH_SHORT).show();
        }
    }

    private void renderRoadmap() {
        int maxUnlocked = prefsManager.getMaxUnlockedLevel();
        binding.roadmapLayout.populateRoadmap(
                maxUnlocked,
                levelId -> prefsManager.getLevelStars(levelId),
                (levelId, isUnlocked, stars) -> {
                    Level level = QuestionBank.INSTANCE.getLevelById(levelId);
                    if (level != null) {
                        showLevelPreviewDialog(level, isUnlocked, stars);
                    }
                },
                () -> {
                    if (maxUnlocked <= 16 && prefsManager.getLevelStars(16) < 1) {
                        Toast.makeText(MapActivity.this, getString(R.string.castle_locked_msg), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showCastleVictoryDialog();
                },
                this::handleHotspotClick,
                hotspotId -> prefsManager.isAllSpecialCompleted(hotspotId)
        );

        binding.scrollMap.post(() -> {
            int currentIdx = Math.max(0, Math.min(maxUnlocked - 1, 16));
            float yFrac = RoadmapLayout.COORDS.get(currentIdx).getYFrac();
            float height = binding.roadmapLayout.getHeight() * yFrac;
            int scrollTarget = Math.max(0, (int) (height - (binding.scrollMap.getHeight() / 2.5f)));
            binding.scrollMap.smoothScrollTo(0, scrollTarget);
        });
    }

    private void showLevelPreviewDialog(Level level, boolean isUnlocked, int stars) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_level_preview, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvCategory = dialogView.findViewById(R.id.tvDialogCategory);
        TextView tvLevelNumber = dialogView.findViewById(R.id.tvDialogLevelNumber);
        TextView tvLevelTitle = dialogView.findViewById(R.id.tvDialogLevelTitle);
        TextView tvLevelDesc = dialogView.findViewById(R.id.tvDialogLevelDesc);
        ImageView ivStatusIcon = dialogView.findViewById(R.id.ivDialogStatusIcon);
        View layoutStars = dialogView.findViewById(R.id.layoutDialogStars);
        TextView tvQuestionCount = dialogView.findViewById(R.id.tvDialogQuestionCount);
        Button btnPlayLevel = dialogView.findViewById(R.id.btnDialogPlayLevel);
        ImageView ivClose = dialogView.findViewById(R.id.ivDialogClose);
        ImageView ivStar1 = dialogView.findViewById(R.id.ivDialogStar1);
        ImageView ivStar2 = dialogView.findViewById(R.id.ivDialogStar2);
        ImageView ivStar3 = dialogView.findViewById(R.id.ivDialogStar3);
        View layoutDifficulty = dialogView.findViewById(R.id.layoutLevelDifficulty);
        TextView chipEasy = dialogView.findViewById(R.id.chipDiffEasy);
        TextView chipNormal = dialogView.findViewById(R.id.chipDiffNormal);
        TextView chipHard = dialogView.findViewById(R.id.chipDiffHard);
        TextView tvDiffSummary = dialogView.findViewById(R.id.tvDiffSummary);
        final Difficulty[] selectedDifficulty = {Difficulty.NORMAL};

        if (tvCategory != null) {
            tvCategory.setText(level.getCategory());
        }
        if (tvLevelNumber != null) {
            tvLevelNumber.setText(getString(R.string.level_dialog_header, level.getId(), level.getCategory()));
        }
        if (tvLevelTitle != null) {
            tvLevelTitle.setText(level.getTitle());
        }
        if (tvLevelDesc != null) {
            tvLevelDesc.setText(getString(R.string.level_dialog_desc_format, userName, level.getSubtitle()));
        }

        if (isUnlocked) {
            if (ivStatusIcon != null) {
                ivStatusIcon.setImageResource(R.drawable.ic_rocket);
            }
            if (layoutStars != null) {
                layoutStars.setVisibility(View.VISIBLE);
            }
            if (layoutDifficulty != null) {
                layoutDifficulty.setVisibility(View.VISIBLE);
                bindDifficultyChips(chipEasy, chipNormal, chipHard, tvDiffSummary, selectedDifficulty);
            }
            if (tvQuestionCount != null) {
                tvQuestionCount.setText(getString(R.string.level_dialog_stats_format, level.getQuestions().size(), level.getQuestions().size() * 120));
                questionRepository.getQuestionsByLevel(level.getId(), new DbCallback<List<Question>>() {
                    @Override
                    public void onSuccess(List<Question> dbQuestions) {
                        if (dialog.isShowing() && dbQuestions != null && !dbQuestions.isEmpty()) {
                            int count = dbQuestions.size();
                            tvQuestionCount.setText(getString(R.string.level_dialog_stats_format, count, count * 120));
                        }
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setText(getString(R.string.dialog_level_play_btn));
            }

            List<ImageView> starViews = Arrays.asList(ivStar1, ivStar2, ivStar3);
            for (int i = 0; i < starViews.size(); i++) {
                ImageView iv = starViews.get(i);
                if (iv != null) {
                    if (i < stars) {
                        iv.setAlpha(1.0f);
                        iv.setImageResource(R.drawable.ic_star_golden);
                    } else {
                        iv.setAlpha(0.35f);
                        iv.setImageResource(R.drawable.ic_star_golden);
                    }
                }
            }

            if (btnPlayLevel != null) {
                btnPlayLevel.setOnClickListener(v -> {
                    dialog.dismiss();
                    launchGameForLevel(level.getId(), selectedDifficulty[0]);
                });
            }
        } else {
            if (ivStatusIcon != null) {
                ivStatusIcon.setImageResource(R.drawable.ic_lock_cute);
            }
            if (layoutStars != null) {
                layoutStars.setVisibility(View.GONE);
            }
            if (layoutDifficulty != null) {
                layoutDifficulty.setVisibility(View.GONE);
            }
            if (tvQuestionCount != null) {
                tvQuestionCount.setText(getString(R.string.level_dialog_locked_req, level.getId() - 1));
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setText(getString(R.string.level_dialog_btn_locked));
                btnPlayLevel.setBackgroundResource(R.drawable.bg_pill_secondary);
                btnPlayLevel.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
                btnPlayLevel.setOnClickListener(v -> dialog.dismiss());
            }
        }

        if (ivClose != null) {
            ivClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void showCastleVictoryDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_medal_unlocked, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvBadge = dialogView.findViewById(R.id.tvMedalUnlockedBadge);
        ImageView ivIcon = dialogView.findViewById(R.id.ivMedalDialogIcon);
        TextView tvTitle = dialogView.findViewById(R.id.tvMedalDialogTitle);
        TextView tvSubtitle = dialogView.findViewById(R.id.tvMedalDialogSubtitle);
        Button btnAwesome = dialogView.findViewById(R.id.btnMedalAwesome);

        if (tvBadge != null) {
            tvBadge.setText(getString(R.string.medal_castle_conquered));
        }
        if (ivIcon != null) {
            ivIcon.setImageResource(R.drawable.img_medalla3);
        }
        if (tvTitle != null) {
            tvTitle.setText(getString(R.string.medal_castle_title));
        }
        if (tvSubtitle != null) {
            tvSubtitle.setText(getString(R.string.medal_castle_desc, userName));
        }
        if (btnAwesome != null) {
            btnAwesome.setText(getString(R.string.medal_castle_btn));
            btnAwesome.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private void bindDifficultyChips(TextView chipEasy, TextView chipNormal, TextView chipHard,
                                       TextView tvSummary, Difficulty[] selectedHolder) {
        bindDifficultyChips(chipEasy, chipNormal, chipHard, tvSummary, selectedHolder, null);
    }

    private void bindDifficultyChips(TextView chipEasy, TextView chipNormal, TextView chipHard,
                                       TextView tvSummary, Difficulty[] selectedHolder,
                                       Runnable onAfterSelect) {
        if (chipEasy == null || chipNormal == null || chipHard == null) {
            return;
        }
        Runnable refresh = () -> {
            Difficulty d = selectedHolder[0];
            chipEasy.setBackgroundResource(d == Difficulty.EASY
                    ? R.drawable.bg_chip_difficulty_easy : R.drawable.bg_chip_difficulty_unselected);
            chipEasy.setTextColor(d == Difficulty.EASY
                    ? 0xFF065F46 : ContextCompat.getColor(this, R.color.ref_text_dark));

            chipNormal.setBackgroundResource(d == Difficulty.NORMAL
                    ? R.drawable.bg_chip_difficulty_normal : R.drawable.bg_chip_difficulty_unselected);
            chipNormal.setTextColor(d == Difficulty.NORMAL
                    ? 0xFF92400E : ContextCompat.getColor(this, R.color.ref_text_dark));

            chipHard.setBackgroundResource(d == Difficulty.HARD
                    ? R.drawable.bg_chip_difficulty_hard : R.drawable.bg_chip_difficulty_unselected);
            chipHard.setTextColor(d == Difficulty.HARD
                    ? 0xFF991B1B : ContextCompat.getColor(this, R.color.ref_text_dark));

            if (tvSummary != null) {
                int summaryRes = d == Difficulty.EASY ? R.string.difficulty_info_easy
                        : (d == Difficulty.HARD ? R.string.difficulty_info_hard : R.string.difficulty_info_normal);
                tvSummary.setText(getString(summaryRes));
            }
            if (onAfterSelect != null) {
                onAfterSelect.run();
            }
        };
        refresh.run();
        chipEasy.setOnClickListener(v -> {
            selectedHolder[0] = Difficulty.EASY;
            refresh.run();
        });
        chipNormal.setOnClickListener(v -> {
            selectedHolder[0] = Difficulty.NORMAL;
            refresh.run();
        });
        chipHard.setOnClickListener(v -> {
            selectedHolder[0] = Difficulty.HARD;
            refresh.run();
        });
    }

    private void launchGameForLevel(int levelId, Difficulty difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_LEVEL_ID, levelId);
        intent.putExtra(EXTRA_IS_QUICK_PLAY, false);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty.name());
        startActivity(intent);
    }

    private void launchTimeAttack() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_LEVEL_ID, -1);
        intent.putExtra(EXTRA_IS_QUICK_PLAY, true);
        intent.putExtra(EXTRA_IS_TIME_ATTACK, true);
        intent.putExtra(EXTRA_DIFFICULTY, Difficulty.NORMAL.name());
        startActivity(intent);
    }

    private void launchSpecialChallenge(int hotspotId, String title, int minLevel, int maxLevel, int count, Difficulty difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_LEVEL_ID, -1);
        intent.putExtra(EXTRA_IS_QUICK_PLAY, true);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty.name());
        intent.putExtra(EXTRA_SPECIAL_TITLE, title);
        intent.putExtra(EXTRA_SPECIAL_MIN_LEVEL, minLevel);
        intent.putExtra(EXTRA_SPECIAL_MAX_LEVEL, maxLevel);
        intent.putExtra(EXTRA_SPECIAL_COUNT, count);
        intent.putExtra(EXTRA_SPECIAL_HOTSPOT_ID, hotspotId);
        startActivity(intent);
    }

    private void handleHotspotClick(int hotspotId) {
        if (hotspotId == RoadmapLayout.HOTSPOT_CAVE) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_violeta,
                    getString(R.string.cave_title),
                    getString(R.string.cave_desc, userName),
                    1, 16, 10, false
            );
            return;
        }
        if (hotspotId == RoadmapLayout.HOTSPOT_MONSTER_START) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_rosa,
                    getString(R.string.special_start_title),
                    getString(R.string.special_start_desc, userName),
                    1, 16, 8, true
            );
            return;
        }
        if (hotspotId == RoadmapLayout.HOTSPOT_MONSTER_RIVER) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_celeste,
                    getString(R.string.special_river_title),
                    getString(R.string.special_river_desc, userName),
                    1, 4, 6, false
            );
            return;
        }
        if (hotspotId == RoadmapLayout.HOTSPOT_MONSTER_LAKE) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_rosa,
                    getString(R.string.special_lake_title),
                    getString(R.string.special_lake_desc, userName),
                    5, 8, 6, false
            );
            return;
        }
        if (hotspotId == RoadmapLayout.HOTSPOT_MONSTER_WATERFALL) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_naranja,
                    getString(R.string.special_waterfall_title),
                    getString(R.string.special_waterfall_desc, userName),
                    9, 12, 6, false
            );
            return;
        }
        if (hotspotId == RoadmapLayout.HOTSPOT_MONSTER_MOUNTAIN) {
            showSpecialChallengeDialog(
                    hotspotId,
                    R.drawable.img_monster_celeste,
                    getString(R.string.special_mountain_title),
                    getString(R.string.special_mountain_desc, userName),
                    13, 16, 6, false
            );
        }
    }

    private void showSpecialChallengeDialog(int hotspotId, int spriteRes, String title, String desc,
                                            int minLevel, int maxLevel, int count, boolean showGuideAction) {
        DialogCaveChallengeBinding dialogBinding = DialogCaveChallengeBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialogBinding.ivCaveSprite.setImageResource(spriteRes);
        dialogBinding.tvCaveTitle.setText(title);
        dialogBinding.tvCaveDesc.setText(desc);

        Difficulty initial = Difficulty.EASY;
        if (prefsManager.isSpecialCompleted(hotspotId, Difficulty.EASY)) {
            if (!prefsManager.isSpecialCompleted(hotspotId, Difficulty.NORMAL)) {
                initial = Difficulty.NORMAL;
            } else if (!prefsManager.isSpecialCompleted(hotspotId, Difficulty.HARD)) {
                initial = Difficulty.HARD;
            }
        }
        final Difficulty[] selectedDifficulty = {initial};

        Runnable updateCompletionUi = () -> {
            Difficulty d = selectedDifficulty[0];
            boolean allDone = prefsManager.isAllSpecialCompleted(hotspotId);
            boolean currentDone = prefsManager.isSpecialCompleted(hotspotId, d);
            if (currentDone) {
                dialogBinding.btnCavePlay.setEnabled(false);
                dialogBinding.btnCavePlay.setAlpha(0.5f);
                dialogBinding.btnCavePlay.setText(getString(R.string.special_already_completed_btn));
                dialogBinding.tvCaveDiffSummary.setText(getString(
                        allDone ? R.string.special_all_completed_info : R.string.special_already_completed_info,
                        d.getDisplayName()));
            } else {
                dialogBinding.btnCavePlay.setEnabled(true);
                dialogBinding.btnCavePlay.setAlpha(1.0f);
                dialogBinding.btnCavePlay.setText(getString(R.string.special_btn_accept));
            }
        };

        bindDifficultyChips(
                dialogBinding.chipCaveDiffEasy,
                dialogBinding.chipCaveDiffNormal,
                dialogBinding.chipCaveDiffHard,
                dialogBinding.tvCaveDiffSummary,
                selectedDifficulty,
                updateCompletionUi
        );
        updateCompletionUi.run();

        dialogBinding.btnCavePlay.setOnClickListener(v -> {
            if (prefsManager.isSpecialCompleted(hotspotId, selectedDifficulty[0])) {
                return;
            }
            dialog.dismiss();
            launchSpecialChallenge(hotspotId, title, minLevel, maxLevel, count, selectedDifficulty[0]);
        });
        if (showGuideAction) {
            dialogBinding.btnCaveOpenGuide.setVisibility(View.VISIBLE);
            dialogBinding.btnCaveOpenGuide.setOnClickListener(v -> {
                dialog.dismiss();
                showGameGuideDialog();
            });
        } else {
            dialogBinding.btnCaveOpenGuide.setVisibility(View.GONE);
        }
        dialogBinding.btnCaveClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showGameGuideDialog() {
        DialogGameGuideBinding guideBinding = DialogGameGuideBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(guideBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        final int[] titles = {
                R.string.guide_step1_title,
                R.string.guide_step2_title,
                R.string.guide_step3_title,
                R.string.guide_step4_title
        };
        final int[] descs = {
                R.string.guide_step1_desc,
                R.string.guide_step2_desc,
                R.string.guide_step3_desc,
                R.string.guide_step4_desc
        };
        final int[] sprites = {
                R.drawable.img_monster_rosa,
                R.drawable.img_monster_celeste,
                R.drawable.img_monster_violeta,
                R.drawable.img_cofre
        };
        final View[] dots = {
                guideBinding.dotGuide0,
                guideBinding.dotGuide1,
                guideBinding.dotGuide2,
                guideBinding.dotGuide3
        };
        final int[] step = {0};

        Runnable renderStep = () -> {
            int idx = step[0];
            guideBinding.tvGuideBadge.setText(getString(R.string.guide_badge_format, idx + 1));
            guideBinding.tvGuideTitle.setText(getString(titles[idx]));
            guideBinding.tvGuideDesc.setText(getString(descs[idx]));
            guideBinding.ivGuideIllustration.setImageResource(sprites[idx]);
            guideBinding.btnGuidePrev.setVisibility(idx == 0 ? View.INVISIBLE : View.VISIBLE);
            guideBinding.btnGuideNext.setText(getString(
                    idx == titles.length - 1 ? R.string.guide_btn_finish : R.string.guide_btn_next));
            for (int i = 0; i < dots.length; i++) {
                dots[i].setBackgroundResource(i == idx ? R.drawable.bg_pill_primary : R.drawable.bg_pill_badge);
            }
        };

        renderStep.run();

        guideBinding.btnGuidePrev.setOnClickListener(v -> {
            if (step[0] > 0) {
                step[0]--;
                renderStep.run();
            }
        });

        guideBinding.btnGuideNext.setOnClickListener(v -> {
            if (step[0] < titles.length - 1) {
                step[0]++;
                renderStep.run();
            } else {
                prefsManager.setGameGuideSeen(prefsManager.getCurrentUserId());
                dialog.dismiss();
            }
        });

        guideBinding.btnGuideSkip.setOnClickListener(v -> {
            prefsManager.setGameGuideSeen(prefsManager.getCurrentUserId());
            dialog.dismiss();
        });

        dialog.setOnCancelListener(d -> prefsManager.setGameGuideSeen(prefsManager.getCurrentUserId()));
        dialog.show();
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavSocial = findViewById(R.id.ivNavSocial);
        ImageView ivNavQuests = findViewById(R.id.ivNavQuests);
        ImageView ivNavQuestions = findViewById(R.id.ivNavQuestions);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavSocial = findViewById(R.id.btnNavSocial);
        View btnNavQuests = findViewById(R.id.btnNavQuests);
        View btnNavQuestions = findViewById(R.id.btnNavQuestions);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) {
            ivNavMap.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        }
        if (ivNavLeaderboard != null) {
            ivNavLeaderboard.setColorFilter(inactiveColor);
        }
        if (ivNavSocial != null) {
            ivNavSocial.setColorFilter(inactiveColor);
        }
        if (ivNavQuests != null) {
            ivNavQuests.setColorFilter(inactiveColor);
        }
        if (ivNavQuestions != null) {
            ivNavQuestions.setColorFilter(inactiveColor);
        }
        if (ivNavProfile != null) {
            ivNavProfile.setColorFilter(inactiveColor);
        }

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> binding.scrollMap.smoothScrollTo(0, 0));
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, RankingActivity.class)));
        }
        if (btnNavSocial != null) {
            btnNavSocial.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, SocialActivity.class)));
        }
        if (btnNavQuests != null) {
            btnNavQuests.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, QuestsActivity.class)));
        }
        if (btnNavQuestions != null) {
            btnNavQuestions.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, QuestionManagerActivity.class)));
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, ProfileActivity.class)));
        }
        updateAdminNavVisibility();
    }

    private void updateAdminNavVisibility() {
        View btnNavQuestions = findViewById(R.id.btnNavQuestions);
        if (btnNavQuestions != null) {
            btnNavQuestions.setVisibility(prefsManager.isCurrentUserAdmin() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (giftPulseAnimX != null) {
            giftPulseAnimX.cancel();
        }
        if (giftPulseAnimY != null) {
            giftPulseAnimY.cancel();
        }
    }
}
