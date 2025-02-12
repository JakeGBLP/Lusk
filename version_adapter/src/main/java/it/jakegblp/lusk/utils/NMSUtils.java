package it.jakegblp.lusk.utils;

import it.jakegblp.lusk.api.NMSAdapter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class NMSUtils {

    public static NMSAdapter NMS;

    public static final Random RANDOM = new Random();
    public static int getRandomID() {
        return RANDOM.nextInt();
    }

    public static int getIdOrRandom(@Nullable Integer integer) {
        return integer == null ? getRandomID() : integer;
    }

    //public static byte packDegrees(float degrees) {
    //    return (byte) Math.floor(degrees * 256.0F / 360.0F);
    //}

    @NotNull
    public static World getWorld(@Nullable World world) {
        return world != null ? world : Bukkit.getWorlds().get(0);
    }
}
