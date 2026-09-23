package com.example.triviamaster.models;

import androidx.annotation.DrawableRes;

public class Avatar {
    private int id;
    private String name;
    private int iconResId;
    private String backgroundColorHex;

    public Avatar(int id, String name, @DrawableRes int iconResId, String backgroundColorHex) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
        this.backgroundColorHex = backgroundColorHex;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
    public String getBackgroundColorHex() { return backgroundColorHex; }
}
