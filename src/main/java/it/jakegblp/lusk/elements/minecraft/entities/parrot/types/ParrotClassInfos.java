package it.jakegblp.lusk.elements.minecraft.entities.parrot.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Parrot;

@SuppressWarnings("unused")
public class ParrotClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Parrot$Variant") && Classes.getExactClassInfo(Parrot.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Parrot.Variant.class, null, "parrot_variant")
                            .getClassInfo("parrotvariant")
                            .user("parrot ?(variant|colou?r|type)s?")
                            .description("All the Parrot Variants.")
                            .since("1.3"));
        }

    }
}
