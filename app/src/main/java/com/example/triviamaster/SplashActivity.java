package com.example.triviamaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.OvershootInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import com.example.triviamaster.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1400L;
    private ActivitySplashBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable navigateRunnable = () -> {
        if (!isFinishing() && !isDestroyed()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.triviamaster.data.PreferencesManager prefs =
                new com.example.triviamaster.data.PreferencesManager(this);
        if (prefs.getCurrentUserId() > 0) {
            com.example.triviamaster.utils.CountryHelper.applyLocaleForCountry(prefs.getUserCountry());
        }
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgSplashMonster.setScaleX(0.4f);
        binding.imgSplashMonster.setScaleY(0.4f);
        binding.imgSplashMonster.setAlpha(0f);
        binding.imgSplashMonster.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(650L)
                .setInterpolator(new OvershootInterpolator(1.4f))
                .start();

        binding.tvSplashTitle.setAlpha(0f);
        binding.tvSplashTitle.setTranslationY(24f);
        binding.tvSplashTitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(200L)
                .setDuration(500L)
                .start();

        binding.tvSplashSubtitle.setAlpha(0f);
        binding.tvSplashSubtitle.animate()
                .alpha(1f)
                .setStartDelay(350L)
                .setDuration(450L)
                .start();

        handler.postDelayed(navigateRunnable, SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(navigateRunnable);
        super.onDestroy();
    }
}
