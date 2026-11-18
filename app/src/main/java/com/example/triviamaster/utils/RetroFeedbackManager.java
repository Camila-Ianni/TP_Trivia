package com.example.triviamaster.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.example.triviamaster.data.PreferencesManager;

public final class RetroFeedbackManager {

    private final PreferencesManager prefsManager;
    private final Vibrator vibrator;
    private ToneGenerator toneGenerator;

    public RetroFeedbackManager(Context context) {
        Context appContext = context.getApplicationContext();
        this.prefsManager = new PreferencesManager(appContext);
        this.vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            this.toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 65);
        } catch (RuntimeException ignored) {
            this.toneGenerator = null;
        }
    }

    public void playCorrect() {
        playTone(ToneGenerator.TONE_PROP_ACK, 130);
    }

    public void playWrong() {
        playTone(ToneGenerator.TONE_PROP_NACK, 200);
        vibrateShort(110L);
    }

    public void playPowerup() {
        playTone(ToneGenerator.TONE_PROP_BEEP2, 110);
    }

    public void playVictory() {
        playTone(ToneGenerator.TONE_PROP_PROMPT, 220);
    }

    private void playTone(int toneType, int durationMs) {
        if (!prefsManager.isSoundEnabled() || toneGenerator == null) {
            return;
        }
        try {
            toneGenerator.startTone(toneType, durationMs);
        } catch (RuntimeException ignored) {
        }
    }

    @SuppressWarnings("deprecation")
    private void vibrateShort(long durationMs) {
        if (!prefsManager.isVibrationEnabled() || vibrator == null || !vibrator.hasVibrator()) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(durationMs);
            }
        } catch (RuntimeException ignored) {
        }
    }

    public void release() {
        if (toneGenerator != null) {
            try {
                toneGenerator.release();
            } catch (RuntimeException ignored) {
            }
            toneGenerator = null;
        }
    }
}
