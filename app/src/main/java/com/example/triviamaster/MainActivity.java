package com.example.triviamaster;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityMainBinding;
import com.example.triviamaster.models.Avatar;
import com.example.triviamaster.models.User;

public final class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_AVATAR_ID = "extra_avatar_id";
    public static final String EXTRA_AVATAR_RES_ID = "extra_avatar_res_id";

    private ActivityMainBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(-1);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        setupWelcomeInfo();
        setupListeners();
        startButtonPulseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupWelcomeInfo();
    }

    private void setupWelcomeInfo() {
        String savedName = prefsManager.getLastPlayerName();
        if (savedName != null && !savedName.trim().isEmpty()) {
            binding.etPlayerName.setText(savedName);
        }
        binding.tvWelcomeTitle.setText(getString(R.string.login_title));
    }

    private void startButtonPulseAnimation() {
        ObjectAnimator animX = ObjectAnimator.ofFloat(binding.btnStartGame, "scaleX", 1.0f, 1.03f, 1.0f);
        animX.setDuration(1500L);
        animX.setRepeatCount(ObjectAnimator.INFINITE);
        animX.setRepeatMode(ObjectAnimator.REVERSE);
        animX.setInterpolator(new AccelerateDecelerateInterpolator());
        animX.start();

        ObjectAnimator animY = ObjectAnimator.ofFloat(binding.btnStartGame, "scaleY", 1.0f, 1.03f, 1.0f);
        animY.setDuration(1500L);
        animY.setRepeatCount(ObjectAnimator.INFINITE);
        animY.setRepeatMode(ObjectAnimator.REVERSE);
        animY.setInterpolator(new AccelerateDecelerateInterpolator());
        animY.start();
    }

    private void setupListeners() {
        binding.btnStartGame.setOnClickListener(v -> attemptLogin());

        binding.btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String username = binding.etPlayerName.getText() != null
                ? binding.etPlayerName.getText().toString().trim() : "";
        String password = binding.etLoginPassword.getText() != null
                ? binding.etLoginPassword.getText().toString() : "";

        boolean valid = true;
        if (username.isEmpty()) {
            binding.tilPlayerName.setError(getString(R.string.error_username_required));
            valid = false;
        } else {
            binding.tilPlayerName.setError(null);
        }

        if (password.isEmpty()) {
            binding.tilLoginPassword.setError(getString(R.string.error_password_required));
            valid = false;
        } else {
            binding.tilLoginPassword.setError(null);
        }

        if (!valid) {
            return;
        }

        setLoading(true);
        userRepository.login(username, password, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing()) {
                    return;
                }
                setLoading(false);
                if (user == null) {
                    binding.tilLoginPassword.setError(getString(R.string.error_invalid_credentials));
                    return;
                }
                prefsManager.syncFromUser(user);
                com.example.triviamaster.utils.CountryHelper.applyLocaleForCountry(user.getCountry());
                navigateToMap(user);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                setLoading(false);
                Toast.makeText(MainActivity.this, getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        binding.progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnStartGame.setEnabled(!loading);
        binding.btnGoRegister.setEnabled(!loading);
    }

    private void navigateToMap(User user) {
        Avatar avatar = prefsManager.getAvatarById(user.getAvatarId());
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_USER_NAME, user.getUsername());
        intent.putExtra(EXTRA_AVATAR_ID, avatar.getId());
        intent.putExtra(EXTRA_AVATAR_RES_ID, avatar.getIconResId());
        startActivity(intent);
    }
}
