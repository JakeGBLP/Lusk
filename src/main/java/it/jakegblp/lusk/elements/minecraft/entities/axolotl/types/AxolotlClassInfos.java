package it.jakegblp.lusk.elements.minecraft.entities.axolotl.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Axolotl;

@SuppressWarnings("unused")
public class AxolotlClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Axolotl$Variant") && Classes.getExactClassInfo(Axolotl.Variant.class) == null) {
            EnumRegistryWrapper AXOLOTL_VARIANT = new EnumRegistryWrapper(Axolotl.Variant.class, null, "axolotl variant");
            Classes.registerClass(AXOLOTL_VARIANT.getClassInfo("axolotlvariant")
                    .user("axolotl ?(variant|colour?r)s?")
                    .name("Axolotl - Variant")
                    .description("All the Axolotl Variants.")
                    .since("1.3"));
        }

    }
}
