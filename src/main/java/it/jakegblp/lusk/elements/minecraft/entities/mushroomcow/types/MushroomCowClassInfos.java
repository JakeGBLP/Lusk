package it.jakegblp.lusk.elements.minecraft.entities.mushroomcow.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.MushroomCow;

@SuppressWarnings("unused")
public class MushroomCowClassInfos {
    static {
        if (Classes.getExactClassInfo(MushroomCow.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(MushroomCow.Variant.class, null, "mushroom_cow_variant")
                            .getClassInfo("mushroomcowvariant")
                            .user("mushroom ?cow ?(variant|colou?r|type)s?")
                            .name("Mushroom Cow - Variant")
                            .description("All the Mushroom Cow Variants.")
                            .since("1.3")
                            .documentationId("MushroomCowVariant"));
        }

    }
}
