package it.jakegblp.lusk.utils;

import org.bukkit.entity.Entity;

// I am well aware this class is very empty
public class CitizensUtils {
    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }
}
