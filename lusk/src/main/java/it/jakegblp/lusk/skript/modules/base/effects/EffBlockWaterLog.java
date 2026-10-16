package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.utils.BlockDataHolder;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Block - Water Log")
@Description("Changes whether one or more blocks, blockstates or waterloggable items are waterlogged.")
@Example("waterlog target block")
@Keywords({"waterlog", "water"})
@Since({"1.3", "2.0.0 (Polished)"})
public class EffBlockWaterLog extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffBlockWaterLog.class, EffBlockWaterLog::new)
                .patterns(
                        "[not:un]water[ |-]log %blockdataholders%",
                        "make %blockdataholders% [:not] water[ |-]logged")
                .register(syntaxRegistry);
    }

    protected Expression<BlockDataHolder<?>> blockDataHolderExpression;
    protected boolean waterlog;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockDataHolderExpression = (Expression<BlockDataHolder<?>>) expressions[0];
        waterlog = !parser.hasTag("not");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + blockDataHolderExpression.toString(event, debug) + (waterlog ? "" : " not") + " waterlogged";
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (BlockDataHolder<?> holder : blockDataHolderExpression.getArray(event)) {
            holder.editBlockData(Waterlogged.class, waterlogged -> waterlogged.setWaterlogged(waterlog));
        }
    }
}