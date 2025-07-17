package it.jakegblp.lusk.elements.minecraft.entities.pig.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Pig;

import static it.jakegblp.lusk.utils.Constants.HAS_PIG_VARIANT;

@SuppressWarnings("unused")
public class PigClassInfos {
    static {
        if (HAS_PIG_VARIANT && Classes.getExactClassInfo(Pig.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Pig.Variant.class, null, "pig_variant")
                            .getClassInfo("pigvariant")
                            .user("pig ?variants?")
                            .name("Pig - Variant")
                            .description("All the Pig Variants.")
                            .since("1.3.6")
                            .documentationId("PigVariant"));
        }

    }
}
