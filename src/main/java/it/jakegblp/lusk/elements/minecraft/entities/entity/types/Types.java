package it.jakegblp.lusk.elements.minecraft.entities.entity.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumWrapper<Pose> POSE_ENUM = new EnumWrapper<>(Pose.class);
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.2"));
        }
        if (Skript.classExists("org.bukkit.entity.SpawnCategory") && Classes.getExactClassInfo(SpawnCategory.class) == null) {
            EnumWrapper<SpawnCategory> SPAWNCATEGORY_ENUM = new EnumWrapper<>(SpawnCategory.class);
            Classes.registerClass(SPAWNCATEGORY_ENUM.getClassInfo("spawncategory")
                    .user("spawn ?categor(y|ies)")
                    .name("Spawn Category")
                    .description("All the Spawn Categories.") // add example
                    .since("1.0.2"));
        }
    }
}
