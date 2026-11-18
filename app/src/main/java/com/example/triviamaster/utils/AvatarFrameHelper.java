package com.example.triviamaster.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public final class AvatarFrameHelper {

    public static final String FRAME_GOLD = "GOLD";
    public static final String FRAME_NEON = "NEON";
    public static final String FRAME_FIRE = "FIRE";

    private AvatarFrameHelper() {
    }

    public static void applyFrame(View container, String frameKey) {
        if (container == null) {
            return;
        }
        float density = container.getResources().getDisplayMetrics().density;
        if (FRAME_GOLD.equalsIgnoreCase(frameKey)) {
            GradientDrawable bg = createDrawable(density, "#FEF3C7", "#F59E0B");
            container.setBackground(bg);
            int pad = (int) (3 * density);
            container.setPadding(pad, pad, pad, pad);
        } else if (FRAME_NEON.equalsIgnoreCase(frameKey)) {
            GradientDrawable bg = createDrawable(density, "#CFFAFE", "#06B6D4");
            container.setBackground(bg);
            int pad = (int) (3 * density);
            container.setPadding(pad, pad, pad, pad);
        } else if (FRAME_FIRE.equalsIgnoreCase(frameKey)) {
            GradientDrawable bg = createDrawable(density, "#FEE2E2", "#EF4444");
            container.setBackground(bg);
            int pad = (int) (3 * density);
            container.setPadding(pad, pad, pad, pad);
        } else {
            container.setBackground(null);
            container.setPadding(0, 0, 0, 0);
        }
    }

    private static GradientDrawable createDrawable(float density, String fillHex, String strokeHex) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(14 * density);
        drawable.setColor(Color.parseColor(fillHex));
        drawable.setStroke((int) (3 * density), Color.parseColor(strokeHex));
        return drawable;
    }
}
