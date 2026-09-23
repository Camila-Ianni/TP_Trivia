package com.example.triviamaster;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.databinding.ActivityMainBinding;
import com.example.triviamaster.models.Avatar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferencesManager prefsManager;

    @NotNull
    public static String EXTRA_USER_NAME = "extra_user_name";

    @NotNull
    public static String EXTRA_AVATAR_ID = "extra_avatar_id";

    @NotNull
    public static String EXTRA_AVATAR_RES_ID = "extra_avatar_res_id";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        ActivityMainBinding activityMainBinding = this.binding;
        setContentView(activityMainBinding.getRoot());
        getWindow().setStatusBarColor(-1);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);
        this.prefsManager = new PreferencesManager(this);
        setupWelcomeInfo();
        setupListeners();
        startButtonPulseAnimation();
    }

    protected void onResume() {
        super.onResume();
        setupWelcomeInfo();
    }

    private void setupWelcomeInfo() {
        PreferencesManager preferencesManager = this.prefsManager;
        String savedName = preferencesManager.getLastPlayerName();
        if (savedName != null && !savedName.isBlank()) {
            binding.etPlayerName.setText(savedName);
            binding.tvWelcomeTitle.setText(getString(R.string.map_greeting_format, savedName));
            return;
        }
        binding.tvWelcomeTitle.setText(R.string.welcome_title);
    }

    private void startButtonPulseAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
                binding.btnStartGame, "scaleX", 1.0f, 1.03f, 1.0f);
        scaleX.setDuration(1500L);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.start();

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
                binding.btnStartGame, "scaleY", 1.0f, 1.03f, 1.0f);
        scaleY.setDuration(1500L);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.start();
    }

    private void setupListeners() {
        binding.btnStartGame.setOnClickListener(view -> startGame());
        binding.btnViewRanking.setOnClickListener(view ->
                startActivity(new Intent(this, RankingActivity.class)));
    }

    private void startGame() {
        String name = binding.etPlayerName.getText().toString().trim();
        if (name.isEmpty()) {
            binding.tilPlayerName.setError(getString(R.string.player_name_required));
            return;
        }
        binding.tilPlayerName.setError(null);
        prefsManager.setLastPlayerName(name);
        navigateToMap(name);
    }
    private void navigateToMap(String name) {
        PreferencesManager preferencesManager = this.prefsManager;
        PreferencesManager preferencesManager2 = this.prefsManager;
        Avatar avatar = preferencesManager.getAvatarById(preferencesManager2.getLastAvatarId());
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_USER_NAME, name);
        intent.putExtra(EXTRA_AVATAR_ID, avatar.getId());
        intent.putExtra(EXTRA_AVATAR_RES_ID, avatar.getIconResId());
        startActivity(intent);
    }
}
