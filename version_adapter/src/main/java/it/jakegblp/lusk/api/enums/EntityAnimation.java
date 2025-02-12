package it.jakegblp.lusk.api.enums;

public enum EntityAnimation {
    SWING_MAIN_HAND(0),
    WAKE_UP(2),
    SWING_OFF_HAND(3),
    CRITICAL_HIT(4),
    MAGIC_CRITICAL_HIT(5);

    private final int id;

    EntityAnimation(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }
}
