package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Entity Shoot - Bow/Item")
@Description("The bow used to fire the arrow and the consumed arrow in the Entity Shoot Event.\nBoth can be null.")
@Examples("on entity shoot:\n\tbroadcast the used bow")
@Since("1.1.1")
@SuppressWarnings("unused")
public class ExprEntityShootItem extends SimpleExpression<ItemStack> {
    static {
        Skript.registerExpression(ExprEntityShootItem.class, ItemStack.class, EVENT_OR_SIMPLE,
                "[the |event-]used bow",
                "[the |event-](consumed|shot) item");
    }

    boolean bow;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EntityShootBowEvent.class)) {
            Skript.error("This expression can only be used in the Entity Shoot event!");
            return false;
        }
        bow = matchedPattern == 0;
        return true;
    }

    @Override
    protected ItemStack @NotNull [] get(@NotNull Event e) {
        EntityShootBowEvent shootBowEvent = (EntityShootBowEvent) e;
        return new ItemStack[]{bow ? shootBowEvent.getBow() : shootBowEvent.getConsumable()};
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return bow ? "the used bow" : "the shot item";
    }
}
