package com.example.triviamaster;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.adapters.AvatarAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.DuplicateRecordException;
import com.example.triviamaster.data.db.MatchHistoryRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.DialogMonsterInfoBinding;
import com.example.triviamaster.models.Avatar;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.AvatarFrameHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;

public final class ProfileActivity extends AppCompatActivity {

    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private MatchHistoryRepository historyRepository;
    private User currentUser;
    private AvatarAdapter avatarAdapter;
    private TextInputEditText etProfileName;
    private TextInputEditText etProfileEmail;
    private TextInputEditText etProfilePassword;
    private TextInputLayout tilProfileName;
    private TextInputLayout tilProfileEmail;
    private TextInputLayout tilProfilePassword;
    private ImageView ivCurrentProfileAvatar;
    private TextView tvProfileTotalStars;
    private TextView tvProfileHighScore;
    private TextView tvProfileMaxLevel;
    private TextView tvProfileMatchesCount;
    private TextView tvProfileAccuracy;
    private TextView tvPlayerTitleBadge;
    private TextView tvProfileAccountLevel;
    private TextView tvProfileXpProgress;
    private ProgressBar pbProfileXp;
    private RecyclerView rvProfileAvatars;
    private ImageView ivMedal1;
    private TextView tvMedal1Status;
    private ImageView ivMedal2;
    private TextView tvMedal2Status;
    private ImageView ivMedal3;
    private TextView tvMedal3Status;
    private View cardMedal1;
    private View cardMedal2;
    private View cardMedal3;
    private ImageView ivGuardian1;
    private ImageView ivGuardian2;
    private ImageView ivGuardian3;
    private ImageView ivGuardian4;
    private TextView tvGuardian1Status;
    private TextView tvGuardian2Status;
    private TextView tvGuardian3Status;
    private TextView tvGuardian4Status;
    private View cardGuardian1;
    private View cardGuardian2;
    private View cardGuardian3;
    private View cardGuardian4;
    private View btnProfileBack;
    private Button btnSaveProfile;
    private Button btnManageUsers;
    private Button btnManageQuestions;
    private Button btnMatchHistory;
    private Button btnOpenQuests;
    private Button btnReviewMistakes;
    private Button btnOpenShop;
    private Button btnLogout;
    private Button btnDeleteAccount;
    private SwitchCompat switchSound;
    private SwitchCompat switchVibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        historyRepository = new MatchHistoryRepository(this);
        initViews();
        setupStats();
        setupAvatarList();
        setupMedals();
        setupBestiary();
        setupListeners();
        setupBottomNav();
        applyRolePermissions();
        loadUserFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserFromDb();
    }

    private void initViews() {
        etProfileName = findViewById(R.id.etProfileName);
        etProfileEmail = findViewById(R.id.etProfileEmail);
        etProfilePassword = findViewById(R.id.etProfilePassword);
        tilProfileName = findViewById(R.id.tilProfileName);
        tilProfileEmail = findViewById(R.id.tilProfileEmail);
        tilProfilePassword = findViewById(R.id.tilProfilePassword);
        ivCurrentProfileAvatar = findViewById(R.id.ivCurrentProfileAvatar);
        tvProfileTotalStars = findViewById(R.id.tvProfileTotalStars);
        tvProfileHighScore = findViewById(R.id.tvProfileHighScore);
        tvProfileMaxLevel = findViewById(R.id.tvProfileMaxLevel);
        tvProfileMatchesCount = findViewById(R.id.tvProfileMatchesCount);
        tvProfileAccuracy = findViewById(R.id.tvProfileAccuracy);
        tvPlayerTitleBadge = findViewById(R.id.tvPlayerTitleBadge);
        tvProfileAccountLevel = findViewById(R.id.tvProfileAccountLevel);
        tvProfileXpProgress = findViewById(R.id.tvProfileXpProgress);
        pbProfileXp = findViewById(R.id.pbProfileXp);
        rvProfileAvatars = findViewById(R.id.rvProfileAvatars);
        ivMedal1 = findViewById(R.id.ivMedal1);
        tvMedal1Status = findViewById(R.id.tvMedal1Status);
        ivMedal2 = findViewById(R.id.ivMedal2);
        tvMedal2Status = findViewById(R.id.tvMedal2Status);
        ivMedal3 = findViewById(R.id.ivMedal3);
        tvMedal3Status = findViewById(R.id.tvMedal3Status);
        cardMedal1 = findViewById(R.id.cardMedal1);
        cardMedal2 = findViewById(R.id.cardMedal2);
        cardMedal3 = findViewById(R.id.cardMedal3);
        ivGuardian1 = findViewById(R.id.ivGuardian1);
        ivGuardian2 = findViewById(R.id.ivGuardian2);
        ivGuardian3 = findViewById(R.id.ivGuardian3);
        ivGuardian4 = findViewById(R.id.ivGuardian4);
        tvGuardian1Status = findViewById(R.id.tvGuardian1Status);
        tvGuardian2Status = findViewById(R.id.tvGuardian2Status);
        tvGuardian3Status = findViewById(R.id.tvGuardian3Status);
        tvGuardian4Status = findViewById(R.id.tvGuardian4Status);
        cardGuardian1 = findViewById(R.id.cardGuardian1);
        cardGuardian2 = findViewById(R.id.cardGuardian2);
        cardGuardian3 = findViewById(R.id.cardGuardian3);
        cardGuardian4 = findViewById(R.id.cardGuardian4);
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageQuestions = findViewById(R.id.btnManageQuestions);
        btnMatchHistory = findViewById(R.id.btnMatchHistory);
        btnOpenQuests = findViewById(R.id.btnOpenQuests);
        btnReviewMistakes = findViewById(R.id.btnReviewMistakes);
        btnOpenShop = findViewById(R.id.btnOpenShop);
        btnLogout = findViewById(R.id.btnLogout);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        switchSound = findViewById(R.id.switchSound);
        switchVibration = findViewById(R.id.switchVibration);
        switchSound.setChecked(prefsManager.isSoundEnabled());
        switchVibration.setChecked(prefsManager.isVibrationEnabled());
    }

    private void loadUserFromDb() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            return;
        }
        historyRepository.getUserStatsSummary(userId, new DbCallback<int[]>() {
            @Override
            public void onSuccess(int[] summary) {
                if (isFinishing() || summary == null || summary.length < 2) {
                    return;
                }
                tvProfileMatchesCount.setText(String.valueOf(summary[0]));
                tvProfileAccuracy.setText(getString(R.string.profile_accuracy_format, summary[1]));
            }

            @Override
            public void onError(Exception error) {
            }
        });
        userRepository.getUserById(userId, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing() || user == null) {
                    return;
                }
                currentUser = user;
                prefsManager.syncFromUser(user);
                etProfileName.setText(user.getUsername());
                etProfileEmail.setText(user.getEmail());
                etProfilePassword.setText(user.getPassword());
                setupAvatarList();
                setupStats();
                setupMedals();
                setupBestiary();
                applyRolePermissions();
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }

    private void setupStats() {
        String lastPlayerName = currentUser != null ? currentUser.getUsername() : prefsManager.getLastPlayerName();
        String currentName = (lastPlayerName == null || lastPlayerName.trim().isEmpty()) ? "Jugador" : lastPlayerName;
        if (etProfileName.getText() == null || etProfileName.getText().toString().trim().isEmpty()) {
            etProfileName.setText(currentName);
        }

        int avatarId = currentUser != null ? currentUser.getAvatarId() : prefsManager.getLastAvatarId();
        Avatar currentAvatar = prefsManager.getAvatarById(avatarId);
        ivCurrentProfileAvatar.setImageResource(currentAvatar.getIconResId());
        String frameKey = currentUser != null ? currentUser.getEquippedFrame() : prefsManager.getEquippedFrame();
        AvatarFrameHelper.applyFrame(ivCurrentProfileAvatar, frameKey);

        int totalStars = currentUser != null ? currentUser.getTotalStars() : prefsManager.getTotalStars();
        int highScore = currentUser != null ? currentUser.getHighScore() : prefsManager.getHighScore();
        int maxLevel = currentUser != null ? currentUser.getMaxLevel() : prefsManager.getMaxUnlockedLevel();
        tvProfileTotalStars.setText(getString(R.string.profile_stars_format, totalStars));
        tvProfileHighScore.setText(getString(R.string.profile_score_format, highScore));
        tvProfileMaxLevel.setText(getString(R.string.profile_level_format, maxLevel));

        boolean m1 = currentUser != null ? currentUser.isMedal1Unlocked() : prefsManager.isMedal1Unlocked();
        boolean m2 = currentUser != null ? currentUser.isMedal2Unlocked() : prefsManager.isMedal2Unlocked();
        boolean m3 = currentUser != null ? currentUser.isMedal3Unlocked() : prefsManager.isMedal3Unlocked();

        String equippedTitle = currentUser != null ? currentUser.getEquippedTitle() : prefsManager.getEquippedTitle();
        String title;
        if (equippedTitle != null && !equippedTitle.trim().isEmpty()) {
            title = equippedTitle;
        } else if (m3) {
            title = getString(R.string.profile_title_lord);
        } else if (m2) {
            title = getString(R.string.profile_title_master);
        } else if (m1) {
            title = getString(R.string.profile_title_initiate);
        } else {
            title = getString(R.string.profile_title_adventurer, currentName);
        }
        tvPlayerTitleBadge.setText(title);

        int xp = currentUser != null ? currentUser.getXp() : prefsManager.getPlayerXp();
        int accountLevel = (xp / User.XP_PER_LEVEL) + 1;
        int xpIntoLevel = xp % User.XP_PER_LEVEL;
        tvProfileAccountLevel.setText(getString(R.string.profile_account_level_format, accountLevel));
        tvProfileXpProgress.setText(getString(R.string.profile_xp_progress_format, xpIntoLevel, User.XP_PER_LEVEL));
        pbProfileXp.setMax(User.XP_PER_LEVEL);
        pbProfileXp.setProgress(xpIntoLevel);

        int wrongCount = currentUser != null ? currentUser.getWrongQuestionIdsList().size() : 0;
        btnReviewMistakes.setText(getString(R.string.btn_review_mistakes, wrongCount));
    }

    private void setupAvatarList() {
        List<Avatar> avatars = PreferencesManager.getAvailableAvatars();
        int selectedId = currentUser != null ? currentUser.getAvatarId() : prefsManager.getLastAvatarId();
        avatarAdapter = new AvatarAdapter(avatars, selectedId, avatar -> {
            if (currentUser != null) {
                currentUser.setAvatarId(avatar.getId());
            }
            prefsManager.setLastAvatarId(avatar.getId());
            ivCurrentProfileAvatar.setImageResource(avatar.getIconResId());
        });
        rvProfileAvatars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvProfileAvatars.setAdapter(avatarAdapter);
    }

    private void setupMedals() {
        boolean medal1Unlocked = currentUser != null ? currentUser.isMedal1Unlocked() : prefsManager.isMedal1Unlocked();
        boolean medal2Unlocked = currentUser != null ? currentUser.isMedal2Unlocked() : prefsManager.isMedal2Unlocked();
        boolean medal3Unlocked = currentUser != null ? currentUser.isMedal3Unlocked() : prefsManager.isMedal3Unlocked();

        updateMedalView(ivMedal1, tvMedal1Status, medal1Unlocked);
        updateMedalView(ivMedal2, tvMedal2Status, medal2Unlocked);
        updateMedalView(ivMedal3, tvMedal3Status, medal3Unlocked);

        cardMedal1.setOnClickListener(v -> showMedalDetailDialog(1, medal1Unlocked));
        cardMedal2.setOnClickListener(v -> showMedalDetailDialog(2, medal2Unlocked));
        cardMedal3.setOnClickListener(v -> showMedalDetailDialog(3, medal3Unlocked));
    }

    private void setupBestiary() {
        int maxLevel = currentUser != null ? currentUser.getMaxLevel() : prefsManager.getMaxUnlockedLevel();
        String playerName = currentUser != null ? currentUser.getUsername() : prefsManager.getLastPlayerName();
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Jugador";
        }
        final String displayName = playerName;

        boolean g1 = maxLevel >= 1;
        boolean g2 = maxLevel >= 5;
        boolean g3 = maxLevel >= 9;
        boolean g4 = maxLevel >= 13;

        updateGuardianView(ivGuardian1, tvGuardian1Status, g1, 1);
        updateGuardianView(ivGuardian2, tvGuardian2Status, g2, 5);
        updateGuardianView(ivGuardian3, tvGuardian3Status, g3, 9);
        updateGuardianView(ivGuardian4, tvGuardian4Status, g4, 13);

        cardGuardian1.setOnClickListener(v -> showGuardianDialog(
                R.drawable.img_monster_rosa,
                getString(R.string.monster_start_name),
                getString(R.string.monster_start_zone),
                g1 ? getString(R.string.monster_start_msg, displayName) : getString(R.string.bestiary_locked_msg, 1),
                g1
        ));
        cardGuardian2.setOnClickListener(v -> showGuardianDialog(
                R.drawable.img_monster_celeste,
                getString(R.string.monster_river_name),
                getString(R.string.monster_river_zone),
                g2 ? getString(R.string.monster_lake_msg, displayName) : getString(R.string.bestiary_locked_msg, 5),
                g2
        ));
        cardGuardian3.setOnClickListener(v -> showGuardianDialog(
                R.drawable.img_monster_naranja,
                getString(R.string.monster_waterfall_name),
                getString(R.string.monster_waterfall_zone),
                g3 ? getString(R.string.monster_waterfall_msg, displayName) : getString(R.string.bestiary_locked_msg, 9),
                g3
        ));
        cardGuardian4.setOnClickListener(v -> showGuardianDialog(
                R.drawable.img_monster_violeta,
                getString(R.string.monster_mountain_name),
                getString(R.string.monster_mountain_zone),
                g4 ? getString(R.string.monster_mountain_msg, displayName) : getString(R.string.bestiary_locked_msg, 13),
                g4
        ));
    }

    private void updateGuardianView(ImageView iv, TextView tvStatus, boolean unlocked, int reqLevel) {
        if (unlocked) {
            iv.setColorFilter(null);
            iv.setAlpha(1.0f);
            tvStatus.setText(getString(R.string.bestiary_status_unlocked));
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
        } else {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            iv.setColorFilter(new ColorMatrixColorFilter(matrix));
            iv.setAlpha(0.42f);
            tvStatus.setText(getString(R.string.bestiary_status_locked, reqLevel));
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
        }
    }

    private void showGuardianDialog(int spriteRes, String name, String zone, String msg, boolean unlocked) {
        DialogMonsterInfoBinding dialogBinding = DialogMonsterInfoBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialogBinding.ivMonsterDialogSprite.setImageResource(spriteRes);
        if (!unlocked) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            dialogBinding.ivMonsterDialogSprite.setColorFilter(new ColorMatrixColorFilter(matrix));
            dialogBinding.ivMonsterDialogSprite.setAlpha(0.5f);
        }
        dialogBinding.tvMonsterDialogName.setText(name);
        dialogBinding.tvMonsterZoneBadge.setText(getString(R.string.monster_badge_format, zone));
        dialogBinding.tvMonsterDialogMsg.setText(msg);
        dialogBinding.btnMonsterOk.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.btnMonsterOpenGuide.setVisibility(View.GONE);
        dialog.show();
    }

    private void updateMedalView(ImageView imageView, TextView statusView, boolean isUnlocked) {
        if (isUnlocked) {
            imageView.setColorFilter(null);
            imageView.setAlpha(1.0f);
            statusView.setText(getString(R.string.medal_status_unlocked));
            statusView.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
        } else {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
            imageView.setAlpha(0.42f);
            statusView.setText(getString(R.string.medal_status_locked));
            statusView.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
        }
    }

    private void showMedalDetailDialog(int medalNum, boolean isUnlocked) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_medal_detail, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        int iconRes;
        String title;
        String desc;
        switch (medalNum) {
            case 1:
                iconRes = R.drawable.img_medalla1;
                title = getString(R.string.medal_1_title);
                desc = getString(R.string.medal_1_desc);
                break;
            case 2:
                iconRes = R.drawable.img_medalla2;
                title = getString(R.string.medal_2_title);
                desc = getString(R.string.medal_2_desc);
                break;
            default:
                iconRes = R.drawable.img_medalla3;
                title = getString(R.string.medal_3_title);
                desc = getString(R.string.medal_3_desc);
                break;
        }

        ImageView ivIcon = dialogView.findViewById(R.id.ivDetailMedalIcon);
        TextView tvTitle = dialogView.findViewById(R.id.tvDetailMedalTitle);
        TextView tvDesc = dialogView.findViewById(R.id.tvDetailMedalDesc);
        TextView tvState = dialogView.findViewById(R.id.tvDetailMedalState);
        TextView tvBadge = dialogView.findViewById(R.id.tvDetailMedalStatusBadge);
        Button btnClose = dialogView.findViewById(R.id.btnDetailMedalClose);

        if (ivIcon != null) ivIcon.setImageResource(iconRes);
        if (tvTitle != null) tvTitle.setText(title);
        if (tvDesc != null) tvDesc.setText(desc);

        if (isUnlocked) {
            if (ivIcon != null) {
                ivIcon.setColorFilter(null);
                ivIcon.setAlpha(1.0f);
            }
            if (tvState != null) {
                tvState.setText(getString(R.string.medal_detail_unlocked_text));
                tvState.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
            }
            if (tvBadge != null) tvBadge.setText(getString(R.string.medal_badge_unlocked));
        } else {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            if (ivIcon != null) {
                ivIcon.setColorFilter(new ColorMatrixColorFilter(matrix));
                ivIcon.setAlpha(0.42f);
            }
            if (tvState != null) {
                tvState.setText(getString(R.string.medal_detail_locked_text));
                tvState.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            }
            if (tvBadge != null) tvBadge.setText(getString(R.string.medal_badge_locked));
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void setupListeners() {
        btnProfileBack.setOnClickListener(v -> finish());

        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());

        btnManageUsers.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, UserManagerActivity.class)));

        btnManageQuestions.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, QuestionManagerActivity.class)));

        btnMatchHistory.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, MatchHistoryActivity.class)));

        btnOpenQuests.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, QuestsActivity.class)));

        btnReviewMistakes.setOnClickListener(v -> {
            if (currentUser == null || currentUser.getWrongQuestionIdsList().isEmpty()) {
                Toast.makeText(ProfileActivity.this, R.string.review_mistakes_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
            intent.putExtra("extra_is_quick_play", true);
            intent.putExtra(GameActivity.EXTRA_IS_REVIEW_MISTAKES, true);
            startActivity(intent);
        });

        btnOpenShop.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, ShopActivity.class)));

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefsManager.setSoundEnabled(isChecked));

        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefsManager.setVibrationEnabled(isChecked));

        btnLogout.setOnClickListener(v -> logoutToLogin());

        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());
    }

    private void saveProfileChanges() {
        String newName = etProfileName.getText() != null ? etProfileName.getText().toString().trim() : "";
        String newEmail = etProfileEmail.getText() != null ? etProfileEmail.getText().toString().trim() : "";
        String newPassword = etProfilePassword.getText() != null ? etProfilePassword.getText().toString() : "";

        boolean valid = true;
        if (newName.isEmpty()) {
            tilProfileName.setError(getString(R.string.profile_name_required));
            valid = false;
        } else {
            tilProfileName.setError(null);
        }

        if (newEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            tilProfileEmail.setError(getString(R.string.error_email_invalid));
            valid = false;
        } else {
            tilProfileEmail.setError(null);
        }

        if (newPassword.length() < 4) {
            tilProfilePassword.setError(getString(R.string.error_password_short));
            valid = false;
        } else {
            tilProfilePassword.setError(null);
        }

        if (!valid) {
            return;
        }

        Avatar selectedAvatar = avatarAdapter.getSelectedAvatar();
        if (currentUser == null) {
            prefsManager.updatePlayerProfile(newName, selectedAvatar.getId());
            Toast.makeText(this, getString(R.string.profile_updated_success, newName), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser.setUsername(newName);
        currentUser.setEmail(newEmail);
        currentUser.setPassword(newPassword);
        currentUser.setAvatarId(selectedAvatar.getId());
        btnSaveProfile.setEnabled(false);

        userRepository.updateUser(currentUser, new DbCallback<User>() {
            @Override
            public void onSuccess(User updated) {
                if (isFinishing()) {
                    return;
                }
                btnSaveProfile.setEnabled(true);
                currentUser = updated;
                prefsManager.syncFromUser(updated);
                Toast.makeText(ProfileActivity.this,
                        getString(R.string.profile_updated_success, updated.getUsername()),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                btnSaveProfile.setEnabled(true);
                if (error instanceof DuplicateRecordException) {
                    tilProfileName.setError(getString(R.string.error_username_exists));
                } else {
                    Toast.makeText(ProfileActivity.this, getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void confirmDeleteAccount() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            logoutToLogin();
            return;
        }
        String displayName = currentUser != null ? currentUser.getUsername() : prefsManager.getLastPlayerName();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_delete_account_title))
                .setMessage(getString(R.string.dialog_delete_account_msg, displayName))
                .setPositiveButton(getString(R.string.btn_confirm_delete), (dialog, which) ->
                        userRepository.deleteUser(userId, new DbCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean deleted) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(ProfileActivity.this,
                                        getString(R.string.msg_account_deleted), Toast.LENGTH_SHORT).show();
                                logoutToLogin();
                            }

                            @Override
                            public void onError(Exception error) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(ProfileActivity.this,
                                        getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                            }
                        }))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }

    private void logoutToLogin() {
        prefsManager.clearSession();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavSocial = findViewById(R.id.ivNavSocial);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavSocial = findViewById(R.id.btnNavSocial);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(inactiveColor);
        if (ivNavSocial != null) ivNavSocial.setColorFilter(inactiveColor);
        if (ivNavProfile != null) ivNavProfile.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> {
                startActivity(new Intent(ProfileActivity.this, RankingActivity.class));
                finish();
            });
        }
        if (btnNavSocial != null) {
            btnNavSocial.setOnClickListener(v -> {
                startActivity(new Intent(ProfileActivity.this, SocialActivity.class));
                finish();
            });
        }
    }

    private void applyRolePermissions() {
        boolean isAdmin = currentUser != null ? currentUser.isAdmin() : prefsManager.isCurrentUserAdmin();
        int visibility = isAdmin ? View.VISIBLE : View.GONE;
        btnManageUsers.setVisibility(visibility);
        btnManageQuestions.setVisibility(visibility);
    }
}
