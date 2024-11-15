package it.jakegblp.lusk.elements.minecraft.entities.variants.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.enums.EmptyEnum;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Frog;

@SuppressWarnings("unused")
public class Types {
    static {
        // todo: split this into related packages then delete this class
        if (Skript.classExists("org.bukkit.entity.Axolotl$Variant") && Classes.getExactClassInfo(Axolotl.Variant.class) == null) {
            EnumWrapper<Axolotl.Variant> BLOCKFACE_ENUM = new EnumWrapper<>(Axolotl.Variant.class, null, "axolotl variant");
            Classes.registerClass(BLOCKFACE_ENUM.getClassInfo("axolotlvariant")
                    .user("axolotl ?(variant|colour?r)s?")
                    .name("Axolotl - Variant")
                    .description("All the Axolotl Variants.")
                    .since("1.3"));
        }

        if (Skript.classExists("org.bukkit.entity.Frog$Variant") && Classes.getExactClassInfo(Frog.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper<Frog.Variant, EmptyEnum>(Frog.Variant.class, null, "frog variant")
                            .getClassInfo("frogvariant")
                            .user("frog ?(variant|colou?r|temperature)s?")
                            .description("All the Frog Variants.")
                            .since("1.3"));
        }

    }
}
