package it.jakegblp.lusk.elements.minecraft.entities.enderman.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_1;


@Name("Enderman - Teleport Towards Entity")
@Description("Attempts to teleport an enderman to a random nearby location.\nRequires Spigot 1.20.1+.")
@Examples({"teleport {_endermen::*} towards {_entity}"})
@Since("1.3")
@RequiredPlugins("1.20.1")
@SuppressWarnings("unused")
public class EffEndermanTeleportTowards extends Effect {
    static {
        if (MINECRAFT_1_20_1)
            Skript.registerEffect(EffEndermanTeleportTowards.class, "teleport %livingentities% towards %entity%");
    }

    private Expression<LivingEntity> endermanExpression;
    private Expression<LivingEntity> entityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        endermanExpression = (Expression<LivingEntity>) expressions[0];
        entityExpression = (Expression<LivingEntity>) expressions[1];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "teleport " + endermanExpression.toString(event, debug) + " towards " + entityExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity entity = entityExpression.getSingle(event);
        if (entity == null) return;
        for (LivingEntity livingEntity : endermanExpression.getArray(event)) {
            if (entity instanceof Enderman enderman) {
                enderman.teleportTowards(livingEntity);
            }
        }
    }
}