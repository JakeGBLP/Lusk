package it.jakegblp.lusk.elements.minecraft.entities.mushroomcow.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.MushroomCow;

@SuppressWarnings("unused")
public class MushroomCowClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.MushroomCow$Variant") && Classes.getExactClassInfo(MushroomCow.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(MushroomCow.Variant.class, null, "mushroom_cow_variant")
                            .getClassInfo("mushroomcowvariant")
                            .user("mushroom ?cow ?(variant|colou?r|type)s?")
                            .name("Mushroom Cow - Varian")
                            .description("All the Mushroom Cow Variants.")
                            .since("1.3")
                            .documentationId("MushroomCowVariant"));
        }

    }
}
