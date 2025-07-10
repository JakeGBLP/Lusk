package it.jakegblp.lusk.elements.minecraft.entities.cow.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Cow;

import static it.jakegblp.lusk.utils.Constants.HAS_COW_VARIANT;

@SuppressWarnings("unused")
public class CowClassInfos {
    static {
        if (HAS_COW_VARIANT && Classes.getExactClassInfo(Cow.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Cow.Variant.class, null, "cow_variant")
                            .getClassInfo("cowvariant")
                            .user("cow ?variants?")
                            .name("Cow - Variant")
                            .description("All the Cow Variants.")
                            .since("1.3.5")
                            .documentationId("CowVariant"));
        }

    }
}
