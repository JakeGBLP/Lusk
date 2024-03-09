package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Beacon Tier")
@Description("Returns beacon tier of a beacon.")
@Examples({"broadcast the beacon tier of {_beacon}"})
@Since("1.0.3")
public class ExprBeaconPyramidTier extends SimplePropertyExpression<Block, Integer> {
    static {
        register(ExprBeaconPyramidTier.class, Integer.class, "(beacon|pyramid) tier", "blocks");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    @Nullable
    public Integer convert(Block block) {
        return block.getState() instanceof Beacon beacon ? beacon.getTier() : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "beacon tier";
    }
}