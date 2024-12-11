package it.jakegblp.lusk.elements.minecraft.entities.arrow.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.data.DefaultChangers;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.registrations.Classes;
import org.bukkit.entity.AbstractArrow;

public class ArrowClassInfos {
    static {
        if (Classes.getExactClassInfo(AbstractArrow.class) == null) {
            Classes.registerClass(new ClassInfo<>(AbstractArrow.class, "abstractarrow")
                    .user("abstract ?arrows?")
                    .name("Abstract Arrow")
                    .description("An abstract arrow.") // add example
                    .since("1.3")
                    .defaultExpression(new EventValueExpression<>(AbstractArrow.class))
                    .changer(DefaultChangers.nonLivingEntityChanger));
        }
    }
}
