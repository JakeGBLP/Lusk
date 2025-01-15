package it.jakegblp.lusk.elements.minecraft.entities.wolf.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Wolf;

import static it.jakegblp.lusk.utils.Constants.HAS_WOLF_VARIANT;

@SuppressWarnings("unused")
public class WolfClassInfos {
    static {
        if (HAS_WOLF_VARIANT && Classes.getExactClassInfo(Wolf.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Wolf.Variant.class, null, "wolf_variant")
                            .getClassInfo("wolfvariant")
                            .user("wolf ?(variant|type)s?")
                            .name("Wolf - Variant")
                            .description("All the Wolf Variants.")
                            .since("1.3")
                            .documentationId("WolfVariant"));
        }
    }
}
