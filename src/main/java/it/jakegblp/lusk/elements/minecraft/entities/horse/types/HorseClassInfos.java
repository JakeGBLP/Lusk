package it.jakegblp.lusk.elements.minecraft.entities.horse.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Horse;

@SuppressWarnings("unused")
public class HorseClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Horse$Color") && Classes.getExactClassInfo(Horse.Color.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Horse.Color.class, null, "horse_color")
                            .getClassInfo("horsecolor")
                            .user("horse ?colou?rs?")
                            .name("Horse - Color")
                            .description("All the Horse Colors.")
                            .since("1.3")
                            .documentationId("HorseColor"));
        }
        if (Skript.classExists("org.bukkit.entity.Horse$Style") && Classes.getExactClassInfo(Horse.Style.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Horse.Style.class, null, "horse_style")
                            .getClassInfo("horsestyle")
                            .user("horse ?styles?")
                            .name("Horse - Style")
                            .description("All the Horse Styles.")
                            .since("1.3")
                            .documentationId("HorseStyle"));
        }
    }
}
