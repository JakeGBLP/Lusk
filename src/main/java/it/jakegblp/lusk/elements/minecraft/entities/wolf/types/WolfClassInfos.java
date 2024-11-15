package it.jakegblp.lusk.elements.minecraft.entities.wolf.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.enums.EmptyEnum;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Wolf;

@SuppressWarnings("unused")
public class WolfClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Wolf$Variant") && Classes.getExactClassInfo(Wolf.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper<Wolf.Variant, EmptyEnum>(Wolf.Variant.class, null, "wolf variant")
                            .getClassInfo("wolfvariant")
                            .user("wolf ?variants?")
                            .description("All the Wolf Variants.")
                            .since("1.3"));
        }
    }
}
