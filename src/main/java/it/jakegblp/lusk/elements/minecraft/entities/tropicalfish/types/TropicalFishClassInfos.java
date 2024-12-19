package it.jakegblp.lusk.elements.minecraft.entities.tropicalfish.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.TropicalFish;

@SuppressWarnings("unused")
public class TropicalFishClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.TropicalFish$Pattern") && Classes.getExactClassInfo(TropicalFish.Pattern.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(TropicalFish.Pattern.class, null, "tropical_fish_pattern")
                            .getClassInfo("tropicalfishpattern")
                            .user("tropical ?fish ?(variant|pattern|type)s?")
                            .description("All the Tropical Fish Patterns.")
                            .since("1.3"));
        }

    }
}
