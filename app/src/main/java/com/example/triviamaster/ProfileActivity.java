package com.example.triviamaster;

import android.content.Intent;
<<<<<<< HEAD
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
=======
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.text.Editable;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.adapters.AvatarAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.models.Avatar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;
<<<<<<< HEAD

public final class ProfileActivity extends AppCompatActivity {
=======
import org.jetbrains.annotations.Nullable;

public class ProfileActivity extends AppCompatActivity {
    private static final class MedalInfo {
        private final int iconRes;
        private final String title;
        private final String description;

        private MedalInfo(int iconRes, String title, String description) {
            this.iconRes = iconRes;
            this.title = title;
            this.description = description;
        }
    }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

    private PreferencesManager prefsManager;
    private AvatarAdapter avatarAdapter;
    private TextInputEditText etProfileName;
    private TextInputLayout tilProfileName;
    private ImageView ivCurrentProfileAvatar;
    private TextView tvProfileTotalStars;
    private TextView tvProfileHighScore;
    private TextView tvProfileMaxLevel;
    private TextView tvPlayerTitleBadge;
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
    private View btnProfileBack;
    private Button btnSaveProfile;
<<<<<<< HEAD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prefsManager = new PreferencesManager(this);
=======
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.prefsManager = new PreferencesManager(this);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        initViews();
        setupStats();
        setupAvatarList();
        setupMedals();
        setupListeners();
        setupBottomNav();
    }

    private void initViews() {
<<<<<<< HEAD
        etProfileName = findViewById(R.id.etProfileName);
        tilProfileName = findViewById(R.id.tilProfileName);
        ivCurrentProfileAvatar = findViewById(R.id.ivCurrentProfileAvatar);
        tvProfileTotalStars = findViewById(R.id.tvProfileTotalStars);
        tvProfileHighScore = findViewById(R.id.tvProfileHighScore);
        tvProfileMaxLevel = findViewById(R.id.tvProfileMaxLevel);
        tvPlayerTitleBadge = findViewById(R.id.tvPlayerTitleBadge);
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
        btnProfileBack = findViewById(R.id.btnProfileBack);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void setupStats() {
        String lastPlayerName = prefsManager.getLastPlayerName();
        String currentName = (lastPlayerName == null || lastPlayerName.trim().isEmpty()) ? "Jugador" : lastPlayerName;
        etProfileName.setText(currentName);

        Avatar currentAvatar = prefsManager.getAvatarById(prefsManager.getLastAvatarId());
        ivCurrentProfileAvatar.setImageResource(currentAvatar.getIconResId());

        int totalStars = prefsManager.getTotalStars();
        tvProfileTotalStars.setText(getString(R.string.profile_stars_format, totalStars));
        tvProfileHighScore.setText(getString(R.string.profile_score_format, prefsManager.getHighScore()));
        tvProfileMaxLevel.setText(getString(R.string.profile_level_format, prefsManager.getMaxUnlockedLevel()));

        String title;
        if (prefsManager.isMedal3Unlocked()) {
            title = getString(R.string.profile_title_lord);
        } else if (prefsManager.isMedal2Unlocked()) {
            title = getString(R.string.profile_title_master);
        } else if (prefsManager.isMedal1Unlocked()) {
            title = getString(R.string.profile_title_initiate);
        } else {
            title = getString(R.string.profile_title_adventurer, currentName);
        }
        tvPlayerTitleBadge.setText(title);
    }

    private void setupAvatarList() {
        List<Avatar> avatars = PreferencesManager.getAvailableAvatars();
        int selectedId = prefsManager.getLastAvatarId();
        avatarAdapter = new AvatarAdapter(avatars, selectedId, avatar -> {
            prefsManager.setLastAvatarId(avatar.getId());
            setupStats();
        });
        rvProfileAvatars.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvProfileAvatars.setAdapter(avatarAdapter);
    }

    private void setupMedals() {
        boolean medal1Unlocked = prefsManager.isMedal1Unlocked();
        boolean medal2Unlocked = prefsManager.isMedal2Unlocked();
        boolean medal3Unlocked = prefsManager.isMedal3Unlocked();

        updateMedalView(ivMedal1, tvMedal1Status, medal1Unlocked);
        updateMedalView(ivMedal2, tvMedal2Status, medal2Unlocked);
        updateMedalView(ivMedal3, tvMedal3Status, medal3Unlocked);

        cardMedal1.setOnClickListener(v -> showMedalDetailDialog(1, medal1Unlocked));
        cardMedal2.setOnClickListener(v -> showMedalDetailDialog(2, medal2Unlocked));
        cardMedal3.setOnClickListener(v -> showMedalDetailDialog(3, medal3Unlocked));
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
=======
        this.etProfileName = findViewById(R.id.etProfileName);
        this.tilProfileName = findViewById(R.id.tilProfileName);
        this.ivCurrentProfileAvatar = findViewById(R.id.ivCurrentProfileAvatar);
        this.tvProfileTotalStars = findViewById(R.id.tvProfileTotalStars);
        this.tvProfileHighScore = findViewById(R.id.tvProfileHighScore);
        this.tvProfileMaxLevel = findViewById(R.id.tvProfileMaxLevel);
        this.tvPlayerTitleBadge = findViewById(R.id.tvPlayerTitleBadge);
        this.rvProfileAvatars = findViewById(R.id.rvProfileAvatars);
        this.ivMedal1 = findViewById(R.id.ivMedal1);
        this.tvMedal1Status = findViewById(R.id.tvMedal1Status);
        this.ivMedal2 = findViewById(R.id.ivMedal2);
        this.tvMedal2Status = findViewById(R.id.tvMedal2Status);
        this.ivMedal3 = findViewById(R.id.ivMedal3);
        this.tvMedal3Status = findViewById(R.id.tvMedal3Status);
        this.cardMedal1 = findViewById(R.id.cardMedal1);
        this.cardMedal2 = findViewById(R.id.cardMedal2);
        this.cardMedal3 = findViewById(R.id.cardMedal3);
        this.btnProfileBack = findViewById(R.id.btnProfileBack);
        this.btnSaveProfile = findViewById(R.id.btnSaveProfile);
    }

    private void setupStats() {
        String string;
        PreferencesManager preferencesManager = this.prefsManager;
        String lastPlayerName = preferencesManager.getLastPlayerName();
        String currentName = lastPlayerName == null || lastPlayerName.trim().isEmpty()
                ? "Jugador" : lastPlayerName;
        TextInputEditText textInputEditText = this.etProfileName;
        textInputEditText.setText(currentName);
        PreferencesManager preferencesManager2 = this.prefsManager;
        PreferencesManager preferencesManager3 = this.prefsManager;
        Avatar currentAvatar = preferencesManager2.getAvatarById(preferencesManager3.getLastAvatarId());
        ImageView imageView = this.ivCurrentProfileAvatar;
        imageView.setImageResource(currentAvatar.getIconResId());
        PreferencesManager preferencesManager4 = this.prefsManager;
        int totalStars = preferencesManager4.getTotalStars();
        TextView textView = this.tvProfileTotalStars;
        textView.setText(getString(R.string.profile_stars_format, totalStars));
        TextView textView2 = this.tvProfileHighScore;
        textView2.setText(getString(
                R.string.profile_score_format, this.prefsManager.getHighScore()));
        TextView textView3 = this.tvProfileMaxLevel;
        textView3.setText(getString(
                R.string.profile_level_format, this.prefsManager.getMaxUnlockedLevel()));
        TextView textView4 = this.tvPlayerTitleBadge;
        PreferencesManager preferencesManager7 = this.prefsManager;
        if (preferencesManager7.isMedal3Unlocked()) {
            string = getString(R.string.profile_title_lord);
        } else {
            PreferencesManager preferencesManager8 = this.prefsManager;
            if (preferencesManager8.isMedal2Unlocked()) {
                string = getString(R.string.profile_title_master);
            } else {
                PreferencesManager preferencesManager9 = this.prefsManager;
                string = preferencesManager9.isMedal1Unlocked() ? getString(R.string.profile_title_initiate) : getString(R.string.profile_title_adventurer, new Object[]{currentName});
            }
        }
        textView4.setText(string);
    }
    private void setupAvatarList() {
        List<Avatar> avatars = PreferencesManager.getAvailableAvatars();
        PreferencesManager preferencesManager = this.prefsManager;
        int selectedId = preferencesManager.getLastAvatarId();
        this.avatarAdapter = new AvatarAdapter(avatars, selectedId, avatar -> {
            PreferencesManager manager = this.prefsManager;
            if (manager == null) return;
            manager.setLastAvatarId(avatar.getId());
            setupStats();
        });
        RecyclerView recyclerView = this.rvProfileAvatars;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        );
        RecyclerView recyclerView2 = this.rvProfileAvatars;
        AvatarAdapter avatarAdapter = this.avatarAdapter;
        recyclerView2.setAdapter(avatarAdapter);
    }

    private void setupMedals() {
        PreferencesManager preferencesManager = this.prefsManager;
        boolean medal1Unlocked = preferencesManager.isMedal1Unlocked();
        PreferencesManager preferencesManager2 = this.prefsManager;
        boolean medal2Unlocked = preferencesManager2.isMedal2Unlocked();
        PreferencesManager preferencesManager3 = this.prefsManager;
        boolean medal3Unlocked = preferencesManager3.isMedal3Unlocked();
        ImageView imageView = this.ivMedal1;
        TextView textView = this.tvMedal1Status;
        updateMedalView(imageView, textView, medal1Unlocked);
        ImageView imageView2 = this.ivMedal2;
        TextView textView2 = this.tvMedal2Status;
        updateMedalView(imageView2, textView2, medal2Unlocked);
        ImageView imageView3 = this.ivMedal3;
        TextView textView3 = this.tvMedal3Status;
        updateMedalView(imageView3, textView3, medal3Unlocked);
        View view = this.cardMedal1;
        view.setOnClickListener(v -> showMedalDetailDialog(1, medal1Unlocked));
        View view2 = this.cardMedal2;
        view2.setOnClickListener(v -> showMedalDetailDialog(2, medal2Unlocked));
        View view3 = this.cardMedal3;
        view3.setOnClickListener(v -> showMedalDetailDialog(3, medal3Unlocked));
    }
    private void updateMedalView(ImageView imageView, TextView statusView, boolean isUnlocked) {
        if (isUnlocked) {
            imageView.setColorFilter((ColorFilter) null);
            imageView.setAlpha(1.0f);
            statusView.setText(getString(R.string.medal_status_unlocked));
            statusView.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
            return;
        }
        ColorMatrix grayscale = new ColorMatrix();
        grayscale.setSaturation(0.0f);
        imageView.setColorFilter(new ColorMatrixColorFilter(grayscale));
        imageView.setAlpha(0.42f);
        statusView.setText(getString(R.string.medal_status_locked));
        statusView.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
    }
    private void showMedalDetailDialog(int medalNum, boolean isUnlocked) {
        MedalInfo medal;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_medal_detail, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(dialogView).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
<<<<<<< HEAD

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

=======
        switch (medalNum) {
            case 1:
                medal = new MedalInfo(R.drawable.img_medalla1, getString(R.string.medal_1_title), getString(R.string.medal_1_desc));
                break;
            case 2:
                medal = new MedalInfo(R.drawable.img_medalla2, getString(R.string.medal_2_title), getString(R.string.medal_2_desc));
                break;
            default:
                medal = new MedalInfo(R.drawable.img_medalla3, getString(R.string.medal_3_title), getString(R.string.medal_3_desc));
                break;
        }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        ImageView ivIcon = dialogView.findViewById(R.id.ivDetailMedalIcon);
        TextView tvTitle = dialogView.findViewById(R.id.tvDetailMedalTitle);
        TextView tvDesc = dialogView.findViewById(R.id.tvDetailMedalDesc);
        TextView tvState = dialogView.findViewById(R.id.tvDetailMedalState);
        TextView tvBadge = dialogView.findViewById(R.id.tvDetailMedalStatusBadge);
        Button btnClose = dialogView.findViewById(R.id.btnDetailMedalClose);
<<<<<<< HEAD

        if (ivIcon != null) {
            ivIcon.setImageResource(iconRes);
        }
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (tvDesc != null) {
            tvDesc.setText(desc);
        }

        if (isUnlocked) {
            if (ivIcon != null) {
                ivIcon.setColorFilter(null);
=======
        if (ivIcon != null) {
            ivIcon.setImageResource(medal.iconRes);
        }
        if (tvTitle != null) {
            tvTitle.setText(medal.title);
        }
        if (tvDesc != null) {
            tvDesc.setText(medal.description);
        }
        if (isUnlocked) {
            if (ivIcon != null) {
                ivIcon.setColorFilter((ColorFilter) null);
            }
            if (ivIcon != null) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                ivIcon.setAlpha(1.0f);
            }
            if (tvState != null) {
                tvState.setText(getString(R.string.medal_detail_unlocked_text));
<<<<<<< HEAD
=======
            }
            if (tvState != null) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                tvState.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
            }
            if (tvBadge != null) {
                tvBadge.setText(getString(R.string.medal_badge_unlocked));
            }
        } else {
<<<<<<< HEAD
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0.0f);
            if (ivIcon != null) {
                ivIcon.setColorFilter(new ColorMatrixColorFilter(matrix));
=======
            ColorMatrix grayscale = new ColorMatrix();
            grayscale.setSaturation(0.0f);
            if (ivIcon != null) {
                ivIcon.setColorFilter(new ColorMatrixColorFilter(grayscale));
            }
            if (ivIcon != null) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                ivIcon.setAlpha(0.42f);
            }
            if (tvState != null) {
                tvState.setText(getString(R.string.medal_detail_locked_text));
<<<<<<< HEAD
=======
            }
            if (tvState != null) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                tvState.setTextColor(ContextCompat.getColor(this, R.color.wrong_red));
            }
            if (tvBadge != null) {
                tvBadge.setText(getString(R.string.medal_badge_locked));
            }
        }
<<<<<<< HEAD

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

=======
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        dialog.show();
    }

    private void setupListeners() {
<<<<<<< HEAD
        btnProfileBack.setOnClickListener(v -> finish());

        btnSaveProfile.setOnClickListener(v -> {
            String newName = etProfileName.getText() != null ? etProfileName.getText().toString().trim() : "";
            if (newName.isEmpty()) {
                tilProfileName.setError(getString(R.string.profile_name_required));
                return;
            }
            tilProfileName.setError(null);
            Avatar selectedAvatar = avatarAdapter.getSelectedAvatar();
            prefsManager.updatePlayerProfile(newName, selectedAvatar.getId());
            Toast.makeText(ProfileActivity.this, getString(R.string.profile_updated_success, newName), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);

=======
        View view = this.btnProfileBack;
        view.setOnClickListener(v -> finish());
        Button button = this.btnSaveProfile;
        button.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        TextInputEditText textInputEditText = etProfileName;
        Editable text = textInputEditText.getText();
        String newName = text == null ? "" : text.toString().trim();
        if (newName.length() == 0) {
            tilProfileName.setError(getString(R.string.profile_name_required));
            return;
        }
        tilProfileName.setError(null);
        AvatarAdapter avatarAdapter = this.avatarAdapter;
        Avatar selectedAvatar = avatarAdapter.getSelectedAvatar();
        PreferencesManager preferencesManager = this.prefsManager;
        preferencesManager.updatePlayerProfile(newName, selectedAvatar.getId());
        Toast.makeText(this, getString(R.string.profile_updated_success, newName), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
            ivNavMap.setColorFilter(inactiveColor);
        }
        if (ivNavLeaderboard != null) {
            ivNavLeaderboard.setColorFilter(inactiveColor);
        }
        if (ivNavProfile != null) {
            ivNavProfile.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        }
<<<<<<< HEAD

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, RankingActivity.class)));
        }
    }
=======
        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> openMap());
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> openRanking());
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> { });
        }
    }
    private void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    private void openRanking() {
        startActivity(new Intent(this, RankingActivity.class));
    }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
}
