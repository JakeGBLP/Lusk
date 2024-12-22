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

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_1;

@Name("Enderman - Randomly Teleport")
@Description("Attempts to teleport an enderman to a random nearby location.\nRequires Spigot 1.20.1+ or Paper, behavior might be slightly different.")
@Examples({"randomly teleport {_endermen::*}"})
@Since("1.0.2, 1.3 (Spigot)")
@RequiredPlugins("Paper or 1.20.1+")
@DocumentationId("9044")
@SuppressWarnings("unused")
public class EffEndermanRandomTeleport extends Effect {
    static {
        if (isPaper() || MINECRAFT_1_20_1)
            Skript.registerEffect(EffEndermanRandomTeleport.class, "[attempt to] randomly teleport %livingentities%");
    }

    private Expression<LivingEntity> entityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "attempt to randomly teleport " + entityExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (LivingEntity entity : entityExpression.getArray(event)) {
            if (entity instanceof Enderman enderman) {
                if (isPaper()) enderman.teleportRandomly();
                else if (MINECRAFT_1_20_1) enderman.teleport();
            }
        }
    }
}