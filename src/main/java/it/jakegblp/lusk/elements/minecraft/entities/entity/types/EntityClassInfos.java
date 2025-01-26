package it.jakegblp.lusk.elements.minecraft.entities.entity.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumWrapper;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;

import static it.jakegblp.lusk.utils.Constants.HAS_SPAWN_CATEGORY;

@SuppressWarnings("unused")
public class EntityClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumWrapper<Pose> POSE_ENUM = new EnumWrapper<>(Pose.class, null, "pose");
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Pose")
                    .description("All the Poses.") // add example
                    .documentationId("9099")
                    .since("1.0.2, 1.3.3 (Suffix)"));
        }
        if (HAS_SPAWN_CATEGORY && Classes.getExactClassInfo(SpawnCategory.class) == null) {
            EnumWrapper<SpawnCategory> SPAWNCATEGORY_ENUM = new EnumWrapper<>(SpawnCategory.class);
            Classes.registerClass(SPAWNCATEGORY_ENUM.getClassInfo("spawncategory")
                    .user("spawn ?categor(y|ies)")
                    .name("Spawn Category")
                    .description("All the Spawn Categories.") // add example
                    .documentationId("9100")
                    .since("1.0.2"));
        }
    }
}
