package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("JukeBox - Record")
@Description("Returns the music disc within a jukebox.\nCan be set, reset and deleted.")
@Examples({"broadcast the music disc of {_j}"})
@Since("1.0.3")
public class ExprJukeBoxRecord extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprJukeBoxRecord.class, ItemType.class, ExpressionType.COMBINED,
                "[the] ([music] disc|record) (inside [of]|of|[with]in) %block%",
                "%block%'[s] ([music] disc|record)");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }
    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block.getState() instanceof Jukebox jukebox) {
            return new ItemType[]{new ItemType(jukebox.getRecord())};
        }
        return new ItemType[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
            return CollectionUtils.array(ItemStack[].class);
        } else {
            return new Class[0];
        }
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Block block = blockExpression.getSingle(e);
        if (block.getState() instanceof Jukebox jukebox) {
            if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
                jukebox.setRecord(new ItemStack(Material.AIR));
            } else {
                ItemStack itemStack = delta instanceof ItemStack[] ? ((ItemStack[]) delta)[0] : null;
                if (itemStack != null) jukebox.setRecord(itemStack);
            }
            jukebox.update();
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the music disc of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}
