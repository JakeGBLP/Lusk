package it.jakegblp.lusk.elements.minecraft.entities.chicken.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Chicken;

import static it.jakegblp.lusk.utils.Constants.HAS_CHICKEN_VARIANT;

@SuppressWarnings("unused")
public class ChickenClassInfos {
    static {
        if (HAS_CHICKEN_VARIANT && Classes.getExactClassInfo(Chicken.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Chicken.Variant.class, null, "chicken_variant")
                            .getClassInfo("chickenvariant")
                            .user("chicken ?variants?")
                            .name("Chicken - Variant")
                            .description("All the Chicken Variants.")
                            .since("1.3.5")
                            .documentationId("ChickenVariant"));
        }

    }
}
