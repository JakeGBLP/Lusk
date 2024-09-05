package it.jakegblp.lusk.api.enums;

import org.jetbrains.annotations.Nullable;

public enum XYZ {
    X(false),
    Y(null),
    Z(true);

    @Nullable
    private final Boolean value;

    XYZ(@Nullable Boolean bool) {
        this.value = bool;
    }

    @Nullable
    public Boolean getValue() {
        return value;
    }

    public boolean isX() {
        return Boolean.FALSE.equals(value);
    }

    public boolean isY() {
        return value == null;
    }

    public boolean isZ() {
        return Boolean.TRUE.equals(value);
    }
}
