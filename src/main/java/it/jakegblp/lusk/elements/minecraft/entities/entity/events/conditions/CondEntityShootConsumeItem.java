package it.jakegblp.lusk.elements.minecraft.entities.entity.events.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity Shoot - Bow/Item")
@Description("Returns whether or not the consumable item should be consumed in the Entity Shoot Event.")
@Examples("on entity shoot:\n\tif the shot item should be consumed:\n\t\tbroadcast the consumed item")
@Since("1.1.1")
public class CondEntityShootConsumeItem extends Condition {
    static {
        Skript.registerCondition(CondEntityShootConsumeItem.class,
                "[the] (shot|launched) item should[not:( not|n't)] be consumed");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(EntityShootBowEvent.class)) {
            Skript.error("This expression can only be used in the Entity Shoot event!");
            return false;
        }
        setNegated(parser.hasTag("not"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the shot item should " + (isNegated() ? "not" : "") + " be consumed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((EntityShootBowEvent) event).shouldConsumeItem();
    }
}