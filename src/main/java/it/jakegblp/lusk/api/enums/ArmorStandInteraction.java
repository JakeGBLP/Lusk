package it.jakegblp.lusk.api.enums;

public enum ArmorStandInteraction {
    CHANGE(0),
    SWAP(1),
    RETRIEVE(2),
    PLACE(3);
    public final int index;

    ArmorStandInteraction(int index) {
        this.index = index;
    }
}
