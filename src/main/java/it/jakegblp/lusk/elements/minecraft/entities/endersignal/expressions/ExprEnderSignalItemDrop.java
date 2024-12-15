package it.jakegblp.lusk.elements.minecraft.entities.endersignal.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Ender Signal - Item")
@Description("""
Gets the item the provided ender signals will display and drop on death.
""")
@Examples("send ender signal will drop property of {_enderSignal}")
@Since("1.3")
public class ExprEnderSignalItemDrop extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprEnderSignalItemDrop.class, Boolean.class, "ender (signal|eye)", "[will|is going to] (drop|:shatter)", "entities");
    }

    @Override
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return parseResult.hasTag("shatter");
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from instanceof EnderSignal enderSignal && enderSignal.getDropItem() ^ isNegated();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Entity from, Boolean to) {
        if (from instanceof EnderSignal enderSignal) {
            enderSignal.setDropItem(to);
        }
    }

    @Override
    protected String getPropertyName() {
        return "ender signal will "+ (isNegated() ? "shatter" : "drop");
    }
}
