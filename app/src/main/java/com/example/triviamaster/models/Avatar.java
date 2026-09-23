package com.example.triviamaster.models;

import androidx.annotation.DrawableRes;

public class Avatar {
<<<<<<< HEAD
    private final int id;
    private final String name;
    private final int iconResId;
    private final String backgroundColorHex;
=======
    private int id;
    private String name;
    private int iconResId;
    private String backgroundColorHex;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

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
