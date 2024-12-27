package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.SkriptParser;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Client Sided Custom Name Visibility (Property)")
@Description("Gets whether or not the entity's custom name is displayed client side.\n" +
        "This value has no effect on players, they will always display their name.")
@Examples({"broadcast custom name visibility of target"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprEntityCustomNameVisibility extends SimpleBooleanPropertyExpression<Entity> {
    static {
        register(ExprEntityCustomNameVisibility.class, Boolean.class,
                "[client[[-| ]side[d]]] custom[ |-]name [:in]visibility",
                "entities");
    }

    @Override
    public boolean setNegated(int matchedPattern, SkriptParser.ParseResult parseResult) {
        return parseResult.hasTag("in");
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Entity from, Boolean bool) {
        from.setCustomNameVisible(bool);
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from.isCustomNameVisible() ^ isNegated();
    }

    @Override
    protected String getPropertyName() {
        return "custom name " + (isNegated() ? "in" : "") + "visibility";
    }

}