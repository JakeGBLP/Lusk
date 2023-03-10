package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.jake.lusk.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

@Name("Composter Level")
@Description("Returns a composter's level.\nCan be changed.")
@Examples("""
on compost:
  wait 1 tick
  set composter level of block to 5
""")
@Since("1.0.1")
public class ExprComposterLevel extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprComposterLevel.class, Integer.class, ExpressionType.COMBINED,
                "[the] composter level of %block%");
    }

    private Expression<Block> block;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) exprs[0];
        return true;
    }
    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Block b = block.getSingle(e);
        if (b != null) {
            if (b.getType() == Material.COMPOSTER) {
                return new Integer[]{(Integer) Utils.getBlockDataTag(b.getBlockData(),"level")};
            }
        }
        return new Integer[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case RESET, SET, ADD, REMOVE, DELETE -> CollectionUtils.array(Integer[].class);
            default -> new Class[0];
        };
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Block b = block.getSingle(e);
        if (b == null) return;
        String value;
        switch (mode) {
            case RESET, DELETE:
                value = "0";
                break;
            case SET:
                value = delta[0].toString();
                break;
            case REMOVE:
                value = String.valueOf(Integer.parseInt(Objects.requireNonNull(Utils.getBlockDataTag(b.getBlockData(), "level")).toString()) - (Integer) delta[0]);
                break;
            case ADD:
                value = String.valueOf(Integer.parseInt(Objects.requireNonNull(Utils.getBlockDataTag(b.getBlockData(), "level")).toString()) + (Integer) delta[0]);
                break;
            default:
                return;
        }
        BlockData oldBlockData = b.getBlockData();
        String newBlockData = b.getType().getKey() + "[" + "level="+value+"]";
        try {
            BlockData blockData = Bukkit.createBlockData(newBlockData);
            blockData = oldBlockData.merge(blockData);
            b.setBlockData(blockData);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the composter level of" + (e == null ? "" : " " +block.getSingle(e));
    }
}
