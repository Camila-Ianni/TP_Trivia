package com.example.triviamaster.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import com.example.triviamaster.data.PreferencesManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RetroFeedbackManager {

    private static final int SAMPLE_RATE = 22050;

    private final PreferencesManager prefsManager;
    private final Vibrator vibrator;
    private final ExecutorService audioExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean released;

    public RetroFeedbackManager(Context context) {
        Context appContext = context.getApplicationContext();
        this.prefsManager = new PreferencesManager(appContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vm = (VibratorManager) appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            this.vibrator = vm != null ? vm.getDefaultVibrator() : null;
        } else {
            this.vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public void playClick() {
        playSequence(new int[]{660}, new int[]{40}, 0.25f);
        vibrateShort(25L);
    }

    public void playCorrect() {
        playSequence(new int[]{523, 659, 784}, new int[]{65, 65, 95}, 0.35f);
        vibrateShort(45L);
    }

    public void playWrong() {
        playSequence(new int[]{240, 170}, new int[]{110, 150}, 0.40f);
        vibrateShort(130L);
    }

    public void playPowerup() {
        playSequence(new int[]{440, 660, 880}, new int[]{50, 50, 75}, 0.35f);
        vibrateShort(50L);
    }

    public void playVictory() {
        playSequence(new int[]{523, 659, 784, 1046}, new int[]{80, 80, 80, 180}, 0.38f);
        vibrateShort(90L);
    }

    public void vibrateTest() {
        vibrateShort(80L);
    }

    private void playSequence(int[] freqsHz, int[] durationsMs, float gain) {
        if (released || !prefsManager.isSoundEnabled()) {
            return;
        }
        audioExecutor.execute(() -> {
            if (released) {
                return;
            }
            int totalSamples = 0;
            for (int d : durationsMs) {
                totalSamples += (SAMPLE_RATE * d) / 1000;
            }
            if (totalSamples <= 0) {
                return;
            }
            short[] pcm = new short[totalSamples];
            int offset = 0;
            for (int n = 0; n < freqsHz.length; n++) {
                int count = (SAMPLE_RATE * durationsMs[n]) / 1000;
                double freq = freqsHz[n];
                for (int i = 0; i < count && (offset + i) < totalSamples; i++) {
                    double t = (double) i / SAMPLE_RATE;
                    double env = 1.0 - ((double) i / count) * 0.35;
                    double wave = Math.sin(2.0 * Math.PI * freq * t);
                    double retro = (wave >= 0 ? 0.65 : -0.65) * 0.45 + wave * 0.55;
                    pcm[offset + i] = (short) (retro * env * gain * Short.MAX_VALUE);
                }
                offset += count;
            }

            AudioTrack track = null;
            try {
                int bufferBytes = pcm.length * 2;
                track = new AudioTrack.Builder()
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .build())
                        .setAudioFormat(new AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(SAMPLE_RATE)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build())
                        .setBufferSizeInBytes(bufferBytes)
                        .setTransferMode(AudioTrack.MODE_STATIC)
                        .build();
                track.write(pcm, 0, pcm.length);
                track.play();
                long sleepMs = 0;
                for (int d : durationsMs) {
                    sleepMs += d;
                }
                Thread.sleep(sleepMs + 30L);
            } catch (Exception ignored) {
            } finally {
                if (track != null) {
                    try {
                        track.stop();
                    } catch (Exception ignored) {
                    }
                    try {
                        track.release();
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void vibrateShort(long durationMs) {
        if (!prefsManager.isVibrationEnabled() || vibrator == null || !vibrator.hasVibrator()) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                VibrationEffect effect = VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE);
                VibrationAttributes attrs = new VibrationAttributes.Builder()
                        .setUsage(VibrationAttributes.USAGE_TOUCH)
                        .build();
                vibrator.vibrate(effect, attrs);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(durationMs);
            }
        } catch (RuntimeException ignored) {
        }
    }

    public void release() {
        released = true;
        audioExecutor.shutdownNow();
    }
}
