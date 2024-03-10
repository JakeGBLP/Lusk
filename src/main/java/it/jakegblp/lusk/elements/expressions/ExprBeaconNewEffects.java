package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
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
import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - New Effects")
@Description("Returns the potion effect types being applied to the beacon in the Beacon Effect Change Event.\nThis expression can be set.")
@Examples({"on beacon effect change:\n\tbroadcast the primary effect"})
@Since("1.0.3")
public class ExprBeaconNewEffects extends SimpleExpression<PotionEffectType> {
    static {
        Skript.registerExpression(ExprBeaconNewEffects.class, PotionEffectType.class, ExpressionType.SIMPLE,
                "[the] [new] (:primary|secondary) [beacon] [potion] effect [type]",
                "[the] [new] secondary [beacon] [potion] effect [type]");
    }

    private boolean primary;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(PlayerChangeBeaconEffectEvent.class)) {
            Skript.error("This expression can only be used in the Beacon Effect Change Event!");
            return false;
        }
        primary = parseResult.hasTag("primary");
        return true;
    }

    @Override
    protected PotionEffectType @NotNull [] get(@NotNull Event e) {
        PlayerChangeBeaconEffectEvent event = ((PlayerChangeBeaconEffectEvent) e);
        PotionEffectType potionEffectType = primary ? event.getPrimary() : event.getSecondary();
        return new PotionEffectType[]{potionEffectType};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{PotionEffectType.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof PotionEffectType potionEffectType) {
            PlayerChangeBeaconEffectEvent event = ((PlayerChangeBeaconEffectEvent) e);
            if (primary) event.setPrimary(potionEffectType);
            else event.setSecondary(potionEffectType);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends PotionEffectType> getReturnType() {
        return PotionEffectType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the new " + (primary ? "primary" : "secondary") + " beacon potion effect type";
    }
}
