package com.example.triviamaster;

import android.animation.ObjectAnimator;
<<<<<<< HEAD
=======
import android.animation.ValueAnimator;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
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
import com.example.triviamaster.databinding.ActivityMapBinding;
import com.example.triviamaster.models.Level;
<<<<<<< HEAD
import java.util.Arrays;
import java.util.List;

public final class MapActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL_ID = "extra_level_id";
    public static final String EXTRA_IS_QUICK_PLAY = "extra_is_quick_play";

    private ActivityMapBinding binding;
    private PreferencesManager prefsManager;
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.img_avatar_perfil1;
    private ObjectAnimator giftPulseAnimX;
    private ObjectAnimator giftPulseAnimY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
=======
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapActivity extends AppCompatActivity {

    private ActivityMapBinding binding;
    private PreferencesManager prefsManager;

    @NotNull
    private String userName = "Jugador";
    private int avatarId = 1;
    private int avatarResId = R.drawable.img_avatar_perfil1;

    @Nullable
    private ObjectAnimator giftPulseAnimX;

    @Nullable
    private ObjectAnimator giftPulseAnimY;

    @NotNull
    public static String EXTRA_LEVEL_ID = "extra_level_id";

    @NotNull
    public static String EXTRA_IS_QUICK_PLAY = "extra_is_quick_play";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMapBinding.inflate(getLayoutInflater());
        ActivityMapBinding activityMapBinding = this.binding;
        setContentView(activityMapBinding.getRoot());
        this.prefsManager = new PreferencesManager(this);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        extractIntentData();
        setupHeader();
        setupBottomNav();
        renderRoadmap();
    }

<<<<<<< HEAD
    @Override
    protected void onNewIntent(Intent intent) {
=======
    protected void onNewIntent(@NotNull Intent intent) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        super.onNewIntent(intent);
        setIntent(intent);
        extractIntentData();
        updateHeaderStats();
<<<<<<< HEAD
    }

    @Override
=======
        renderRoadmap();
    }

>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    protected void onResume() {
        super.onResume();
        extractIntentData();
        updateHeaderStats();
        renderRoadmap();
    }

    private void extractIntentData() {
<<<<<<< HEAD
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

        binding.btnHeaderGift.setOnClickListener(v -> handleGiftClick());
    }

    private void updateHeaderStats() {
        int totalStars = prefsManager.getTotalStars();
        binding.tvHeaderStars.setText(getString(R.string.header_stars_format, totalStars));
        binding.tvHeaderLives.setText(prefsManager.getPlayerEnergy() + "/12");
        updateGiftBadge();
        binding.tvHeaderGreeting.setText(getString(R.string.map_greeting_format, userName));
        binding.ivHeaderAvatar.setImageResource(avatarResId);
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
            updateGiftBadge();
            updateHeaderStats();

=======
        PreferencesManager preferencesManager = this.prefsManager;
        String lastPlayerName = preferencesManager.getLastPlayerName();
        this.userName = lastPlayerName == null || lastPlayerName.isBlank()
                ? getString(R.string.default_player_name) : lastPlayerName;
        PreferencesManager preferencesManager2 = this.prefsManager;
        this.avatarId = preferencesManager2.getLastAvatarId();
        PreferencesManager preferencesManager3 = this.prefsManager;
        this.avatarResId = preferencesManager3.getAvatarById(this.avatarId).getIconResId();
    }

    private void setupHeader() {
        ActivityMapBinding activityMapBinding = this.binding;
        activityMapBinding.tvHeaderGreeting.setText(getString(R.string.map_greeting_format, this.userName));
                binding.ivHeaderAvatar.setImageResource(this.avatarResId);
        updateHeaderStats();
                binding.btnHeaderAvatarContainer.setOnClickListener(
                view -> openProfile());
                binding.btnHeaderGift.setOnClickListener(view -> handleGiftClick());
    }
    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void updateHeaderStats() {
        PreferencesManager preferencesManager = this.prefsManager;
        int totalStars = preferencesManager.getTotalStars();
        ActivityMapBinding activityMapBinding = this.binding;
        activityMapBinding.tvHeaderStars.setText(getString(R.string.header_stars_format, totalStars));
                TextView textView = binding.tvHeaderLives;
        PreferencesManager preferencesManager2 = this.prefsManager;
        textView.setText(getString(R.string.header_lives_format, preferencesManager2.getPlayerEnergy()));
        updateGiftBadge();
                binding.tvHeaderGreeting.setText(getString(R.string.map_greeting_format, this.userName));
                binding.ivHeaderAvatar.setImageResource(this.avatarResId);
    }

    private void updateGiftBadge() {
        PreferencesManager preferencesManager = this.prefsManager;
        int gifts = preferencesManager.getAvailableGifts();
        if (gifts > 0) {
            ActivityMapBinding activityMapBinding = this.binding;
            activityMapBinding.badgeGiftAlert.setVisibility(View.VISIBLE);
                        binding.badgeGiftAlert.setText("!");
            if (this.giftPulseAnimX != null) {
                ObjectAnimator objectAnimator = this.giftPulseAnimX;
                if (objectAnimator.isRunning()) {
                    return;
                }
            }
                        ObjectAnimator pulseX = ObjectAnimator.ofFloat(
                    binding.btnHeaderGift, "scaleX", 1.0f, 1.12f, 1.0f);
            pulseX.setDuration(1600L);
            pulseX.setRepeatCount(ValueAnimator.INFINITE);
            pulseX.start();
            this.giftPulseAnimX = pulseX;
                        ObjectAnimator pulseY = ObjectAnimator.ofFloat(
                    binding.btnHeaderGift, "scaleY", 1.0f, 1.12f, 1.0f);
            pulseY.setDuration(1600L);
            pulseY.setRepeatCount(ValueAnimator.INFINITE);
            pulseY.start();
            this.giftPulseAnimY = pulseY;
            return;
        }
                binding.badgeGiftAlert.setVisibility(View.GONE);
        ObjectAnimator objectAnimator2 = this.giftPulseAnimX;
        if (objectAnimator2 != null) {
            objectAnimator2.cancel();
        }
        ObjectAnimator objectAnimator3 = this.giftPulseAnimY;
        if (objectAnimator3 != null) {
            objectAnimator3.cancel();
        }
        this.giftPulseAnimX = null;
        this.giftPulseAnimY = null;
                binding.btnHeaderGift.setScaleX(1.0f);
                binding.btnHeaderGift.setScaleY(1.0f);
    }
    private void handleGiftClick() {
        PreferencesManager preferencesManager = this.prefsManager;
        int gifts = preferencesManager.getAvailableGifts();
        if (gifts > 0) {
            PreferencesManager preferencesManager2 = this.prefsManager;
            preferencesManager2.setAvailableGifts(preferencesManager2.getAvailableGifts() - 1);
            PreferencesManager preferencesManager3 = this.prefsManager;
            preferencesManager3.addChestRewards(1, 100);
            updateGiftBadge();
            updateHeaderStats();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_gift_claimed, (ViewGroup) null);
            AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
<<<<<<< HEAD
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
                }
        );

        binding.scrollMap.post(() -> {
            int currentIdx = Math.max(0, Math.min(maxUnlocked - 1, 16));
            float yFrac = RoadmapLayout.COORDS.get(currentIdx).getYFrac();
            float height = binding.roadmapLayout.getHeight() * yFrac;
            int scrollTarget = Math.max(0, (int) (height - (binding.scrollMap.getHeight() / 2.5f)));
            binding.scrollMap.smoothScrollTo(0, scrollTarget);
        });
    }

=======
            TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvClaimedTitle);
            TextView tvSubtitle = (TextView) dialogView.findViewById(R.id.tvClaimedSubtitle);
            Button btnAwesome = (Button) dialogView.findViewById(R.id.btnClaimedAwesome);
            if (tvTitle != null) {
                tvTitle.setText(getString(R.string.gift_claimed_title, new Object[]{this.userName}));
            }
            if (tvSubtitle != null) {
                tvSubtitle.setText(getString(R.string.gift_claimed_desc, new Object[]{this.userName}));
            }
            if (btnAwesome != null) {
                btnAwesome.setOnClickListener(view -> dialog.dismiss());
            }
            dialog.show();
            return;
        }
        Toast.makeText(this, getString(R.string.gift_none_available), Toast.LENGTH_SHORT).show();
    }

    private void renderRoadmap() {
        PreferencesManager preferencesManager = this.prefsManager;
        int maxUnlocked = preferencesManager.getMaxUnlockedLevel();
        ActivityMapBinding activityMapBinding = this.binding;
        activityMapBinding.roadmapLayout.populateRoadmap(
                maxUnlocked,
                this::getStarsForLevel,
                this::showLevelPreview,
                () -> handleCastleClick(maxUnlocked));
                binding.scrollMap.post(() -> {
            scrollToCurrentLevel(maxUnlocked);
        });
    }

    private int getStarsForLevel(int levelId) {
        PreferencesManager preferencesManager = this.prefsManager;
        return preferencesManager.getLevelStars(levelId);
    }

    private void showLevelPreview(int levelId, boolean isUnlocked, int stars) {
        Level level = QuestionBank.INSTANCE.getLevelById(levelId);
        if (level != null) {
            showLevelPreviewDialog(level, isUnlocked, stars);
        }
    }
    private void handleCastleClick(int maxUnlocked) {
        if (maxUnlocked <= 16) {
            PreferencesManager preferencesManager = prefsManager;
            if (preferencesManager.getLevelStars(16) < 1) {
                Toast.makeText(this, getString(R.string.castle_locked_msg), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        showCastleVictoryDialog();
    }

    private void scrollToCurrentLevel(int maxUnlocked) {
        int currentIdx = Math.max(0, Math.min(maxUnlocked - 1, 16));
        float yFrac = RoadmapLayout.getCoords().get(currentIdx).getYFrac();
        ActivityMapBinding activityMapBinding = this.binding;
        float height = activityMapBinding.roadmapLayout.getHeight() * yFrac;
                int scrollTarget = Math.max(
                (int) (height - (binding.scrollMap.getHeight() / 2.5f)), 0);
                binding.scrollMap.smoothScrollTo(0, scrollTarget);
    }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    private void showLevelPreviewDialog(Level level, boolean isUnlocked, int stars) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_level_preview, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
<<<<<<< HEAD

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

=======
        TextView tvCategory = (TextView) dialogView.findViewById(R.id.tvDialogCategory);
        TextView tvLevelNumber = (TextView) dialogView.findViewById(R.id.tvDialogLevelNumber);
        TextView tvLevelTitle = (TextView) dialogView.findViewById(R.id.tvDialogLevelTitle);
        TextView tvLevelDesc = (TextView) dialogView.findViewById(R.id.tvDialogLevelDesc);
        ImageView ivStatusIcon = (ImageView) dialogView.findViewById(R.id.ivDialogStatusIcon);
        View layoutStars = dialogView.findViewById(R.id.layoutDialogStars);
        TextView tvQuestionCount = (TextView) dialogView.findViewById(R.id.tvDialogQuestionCount);
        Button btnPlayLevel = (Button) dialogView.findViewById(R.id.btnDialogPlayLevel);
        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.ivDialogClose);
        ImageView ivStar1 = (ImageView) dialogView.findViewById(R.id.ivDialogStar1);
        ImageView ivStar2 = (ImageView) dialogView.findViewById(R.id.ivDialogStar2);
        ImageView ivStar3 = (ImageView) dialogView.findViewById(R.id.ivDialogStar3);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        if (tvCategory != null) {
            tvCategory.setText(level.getCategory());
        }
        if (tvLevelNumber != null) {
<<<<<<< HEAD
            tvLevelNumber.setText(getString(R.string.level_dialog_header, level.getId(), level.getCategory()));
=======
            tvLevelNumber.setText(getString(R.string.level_dialog_header, new Object[]{Integer.valueOf(level.getId()), level.getCategory()}));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        }
        if (tvLevelTitle != null) {
            tvLevelTitle.setText(level.getTitle());
        }
        if (tvLevelDesc != null) {
<<<<<<< HEAD
            tvLevelDesc.setText(getString(R.string.level_dialog_desc_format, userName, level.getSubtitle()));
        }

=======
            tvLevelDesc.setText(getString(R.string.level_dialog_desc_format, new Object[]{this.userName, level.getSubtitle()}));
        }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        if (isUnlocked) {
            if (ivStatusIcon != null) {
                ivStatusIcon.setImageResource(R.drawable.ic_rocket);
            }
            if (layoutStars != null) {
                layoutStars.setVisibility(View.VISIBLE);
            }
            if (tvQuestionCount != null) {
<<<<<<< HEAD
                tvQuestionCount.setText(getString(R.string.level_dialog_stats_format, level.getQuestions().size(), level.getQuestions().size() * 120));
=======
                tvQuestionCount.setText(getString(R.string.level_dialog_stats_format, new Object[]{Integer.valueOf(level.getQuestions().size()), Integer.valueOf(level.getQuestions().size() * 120)}));
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setText(getString(R.string.dialog_level_play_btn));
            }
<<<<<<< HEAD

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
=======
            ImageView[] starViews = {ivStar1, ivStar2, ivStar3};
            for (int index = 0; index < starViews.length; index++) {
                ImageView iv = starViews[index];
                if (iv == null) {
                    continue;
                }
                if (index < stars) {
                    iv.setAlpha(1.0f);
                    iv.setImageResource(R.drawable.ic_star_golden);
                } else {
                    iv.setAlpha(0.35f);
                    iv.setImageResource(R.drawable.ic_star_golden);
                }
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setOnClickListener(view -> {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                    dialog.dismiss();
                    launchGameForLevel(level.getId());
                });
            }
        } else {
            if (ivStatusIcon != null) {
                ivStatusIcon.setImageResource(R.drawable.ic_lock_cute);
            }
            if (layoutStars != null) {
                layoutStars.setVisibility(View.GONE);
            }
            if (tvQuestionCount != null) {
<<<<<<< HEAD
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

=======
                tvQuestionCount.setText(getString(R.string.level_dialog_locked_req, new Object[]{Integer.valueOf(level.getId() - 1)}));
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setText(getString(R.string.level_dialog_btn_locked));
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setBackgroundResource(R.drawable.bg_pill_secondary);
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            }
            if (btnPlayLevel != null) {
                btnPlayLevel.setOnClickListener(view -> dialog.dismiss());
            }
        }
        if (ivClose != null) {
            ivClose.setOnClickListener(view -> dialog.dismiss());
        }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        dialog.show();
    }

    private void showCastleVictoryDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_medal_unlocked, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
<<<<<<< HEAD

        TextView tvBadge = dialogView.findViewById(R.id.tvMedalUnlockedBadge);
        ImageView ivIcon = dialogView.findViewById(R.id.ivMedalDialogIcon);
        TextView tvTitle = dialogView.findViewById(R.id.tvMedalDialogTitle);
        TextView tvSubtitle = dialogView.findViewById(R.id.tvMedalDialogSubtitle);
        Button btnAwesome = dialogView.findViewById(R.id.btnMedalAwesome);

=======
        TextView tvBadge = (TextView) dialogView.findViewById(R.id.tvMedalUnlockedBadge);
        ImageView ivIcon = (ImageView) dialogView.findViewById(R.id.ivMedalDialogIcon);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvMedalDialogTitle);
        TextView tvSubtitle = (TextView) dialogView.findViewById(R.id.tvMedalDialogSubtitle);
        Button btnAwesome = (Button) dialogView.findViewById(R.id.btnMedalAwesome);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
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
<<<<<<< HEAD
            tvSubtitle.setText(getString(R.string.medal_castle_desc, userName));
        }
        if (btnAwesome != null) {
            btnAwesome.setText(getString(R.string.medal_castle_btn));
            btnAwesome.setOnClickListener(v -> dialog.dismiss());
=======
            tvSubtitle.setText(getString(R.string.medal_castle_desc, new Object[]{this.userName}));
        }
        if (btnAwesome != null) {
            btnAwesome.setText(getString(R.string.medal_castle_btn));
        }
        if (btnAwesome != null) {
            btnAwesome.setOnClickListener(view -> dialog.dismiss());
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        }
        dialog.show();
    }

    private void launchGameForLevel(int levelId) {
        Intent intent = new Intent(this, GameActivity.class);
<<<<<<< HEAD
        intent.putExtra("extra_level_id", levelId);
        intent.putExtra("extra_is_quick_play", false);
        startActivity(intent);
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

=======
        intent.putExtra(EXTRA_LEVEL_ID, levelId);
        intent.putExtra(EXTRA_IS_QUICK_PLAY, false);
        startActivity(intent);
    }
    private void setupBottomNav() {
        ImageView ivNavMap = (ImageView) findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = (ImageView) findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavProfile = (ImageView) findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavProfile = findViewById(R.id.btnNavProfile);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) {
            ivNavMap.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        }
        if (ivNavLeaderboard != null) {
            ivNavLeaderboard.setColorFilter(inactiveColor);
        }
        if (ivNavProfile != null) {
            ivNavProfile.setColorFilter(inactiveColor);
        }
<<<<<<< HEAD

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> binding.scrollMap.smoothScrollTo(0, 0));
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, RankingActivity.class)));
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> startActivity(new Intent(MapActivity.this, ProfileActivity.class)));
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
=======
        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(view -> binding.scrollMap.smoothScrollTo(0, 0));
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(
                    view -> startActivity(new Intent(this, RankingActivity.class)));
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(
                    view -> startActivity(new Intent(this, ProfileActivity.class)));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        ObjectAnimator objectAnimator = this.giftPulseAnimX;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        ObjectAnimator objectAnimator2 = this.giftPulseAnimY;
        if (objectAnimator2 != null) {
            objectAnimator2.cancel();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        }
    }
}
