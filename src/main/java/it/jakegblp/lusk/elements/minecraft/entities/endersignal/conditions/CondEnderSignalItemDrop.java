package it.jakegblp.lusk.elements.minecraft.entities.endersignal.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;

import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Ender Signal - is Going to Drop/Shatter")
@Description("Checks if the provided ender signals should drop an item on death. If true, they will drop an item. If false, they will shatter.")
@Examples("if ender signal {_enderSingle} is going to drop:")
@Since("1.3")
public class CondEnderSignalItemDrop extends PropertyCondition<Entity> {

    static {
        registerPrefixedPropertyCondition(CondEnderSignalItemDrop.class,
                "[ender (signal|eye)]", "going to (drop|:shatter)", "entities");
    }

    private boolean shatter;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        shatter = parseResult.hasTag("shatter");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof EnderSignal enderSignal && enderSignal.getDropItem() ^ shatter;
    }

    @Override
    protected String getPropertyName() {
        return "going to " + (shatter ? "shatter" : "drop");
    }
}
