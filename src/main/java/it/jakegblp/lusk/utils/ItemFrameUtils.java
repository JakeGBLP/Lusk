package it.jakegblp.lusk.utils;

import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.stream.Stream;


public class ItemFrameUtils {

    public static Rotation sumRotations(Stream<? extends Rotation> rotations, Rotation rotation, int times) {
        int total = rotations.mapToInt(Rotation::ordinal).sum()*times+rotation.ordinal();
        return Rotation.values()[total % Rotation.values().length];
    }

    public static Rotation subtractRotations(Rotation... rotations) {
        int total = Arrays.stream(rotations).mapToInt(Rotation::ordinal).reduce(0, (a, b) -> a - b);
        return Rotation.values()[(total % Rotation.values().length + Rotation.values().length) % Rotation.values().length];
    }

    public static void rotateItemFrame(Entity entity, boolean clockwise, @Range(from = 1, to = Integer.MAX_VALUE) int times) {
        if (entity instanceof ItemFrame itemFrame) {
            Rotation rotation = itemFrame.getRotation();
            for (int i = 0; i < times; i++) {
                rotation = clockwise ? rotation.rotateClockwise() : rotation.rotateCounterClockwise();
            }
            itemFrame.setRotation(rotation);
        }
    }
}
