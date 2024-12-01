package it.jakegblp.lusk.api.enums;

import org.bukkit.Axis;

public enum Axis4D {
    X(Axis.X),Y(Axis.Y),Z(Axis.Z),W(null);

    private final Axis axis;

    Axis4D(Axis axis) {
        this.axis = axis;
    }

    public Axis asBukkitAxis() {
        return axis;
    }
}
