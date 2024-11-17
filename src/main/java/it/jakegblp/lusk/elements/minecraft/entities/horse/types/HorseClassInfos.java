package it.jakegblp.lusk.elements.minecraft.entities.horse.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Horse;

@SuppressWarnings("unused")
public class HorseClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Horse$Color") && Classes.getExactClassInfo(Horse.Color.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Horse.Color.class, null, "horse color")
                            .getClassInfo("horsecolor")
                            .user("horse ?colou?rs?")
                            .description("All the Horse Colors.")
                            .since("1.3"));
        }
        if (Skript.classExists("org.bukkit.entity.Horse$Style") && Classes.getExactClassInfo(Horse.Style.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Horse.Style.class, null, "horse style")
                            .getClassInfo("horsestyle")
                            .user("horse ?styles?")
                            .description("All the Horse Styles.")
                            .since("1.3"));
        }
    }
}
