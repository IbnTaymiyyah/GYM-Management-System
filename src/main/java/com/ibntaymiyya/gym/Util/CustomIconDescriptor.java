package com.ibntaymiyya.gym.Util;

import io.github.palexdev.mfxresources.fonts.IconDescriptor;

public class CustomIconDescriptor implements IconDescriptor {
    private final String description;
    private final char code;

    public CustomIconDescriptor(String description, char code) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public char getCode() {
        return code;
    }
}
