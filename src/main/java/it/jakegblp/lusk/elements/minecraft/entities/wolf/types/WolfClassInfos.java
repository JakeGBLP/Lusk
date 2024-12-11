package it.jakegblp.lusk.elements.minecraft.entities.wolf.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.data.DefaultChangers;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Wolf;

import static it.jakegblp.lusk.utils.Constants.HAS_WOLF_VARIANT;

@SuppressWarnings("unused")
public class WolfClassInfos {
    static {
        if (Classes.getExactClassInfo(Wolf.class) == null) {
            Classes.registerClass(new ClassInfo<>(Wolf.class, "wolf")
                    .user("wol(f|ves)")
                    .name("Wolf")
                    .description("A wolf.") // add example
                    .since("1.3")
                    .defaultExpression(new EventValueExpression<>(Wolf.class))
                    .changer(DefaultChangers.entityChanger));
        }
        if (HAS_WOLF_VARIANT && Classes.getExactClassInfo(Wolf.Variant.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Wolf.Variant.class, null, "wolf_variant")
                            .getClassInfo("wolfvariant")
                            .user("wolf ?(variant|type)s?")
                            .description("All the Wolf Variants.")
                            .since("1.3"));
        }
    }
}
