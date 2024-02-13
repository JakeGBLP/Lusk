package it.jakegblp.lusk.utils;

import org.bukkit.entity.Entity;

public class CitizensUtils {
    public static boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }

}
