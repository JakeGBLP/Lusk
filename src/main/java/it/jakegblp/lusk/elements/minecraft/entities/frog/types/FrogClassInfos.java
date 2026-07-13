package it.jakegblp.lusk.elements.minecraft.entities.frog.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Frog;

@SuppressWarnings("unused")
public class FrogClassInfos {
    static {
        if (Classes.getExactClassInfo(Frog.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Frog.Variant.class, null, "frog_variant")
                            .getClassInfo("frogvariant")
                            .user("frog ?(variant|colou?r|temperature)s?")
                            .name("Frog - Variant")
                            .description("All the Frog Variants.")
                            .since("1.3")
                            .documentationId("FrogVariant"));
        }

    }
}
