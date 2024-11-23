package it.jakegblp.lusk.elements.minecraft.entities.salmon.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Salmon;

@SuppressWarnings("unused")
public class SalmonClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Salmon$Variant") && Classes.getExactClassInfo(Salmon.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Salmon.Variant.class, null, "salmon_variant")
                            .getClassInfo("salmonvariant")
                            .user("salmon ?(variant|type)s?")
                            .description("All the Salmon Variants.")
                            .since("1.3"));
        }

    }
}
