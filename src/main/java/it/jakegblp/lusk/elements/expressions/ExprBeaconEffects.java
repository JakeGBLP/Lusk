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
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Effects")
@Description("Returns either potion effect of a beacon.\nCan be set.")
@Examples({"broadcast the primary effect of {_beacon}"})
@Since("1.0.3")
public class ExprBeaconEffects extends SimpleExpression<PotionEffect> {
    static {
        Skript.registerExpression(ExprBeaconEffects.class, PotionEffect.class, ExpressionType.SIMPLE,
                "[the] primary [potion] effect of %block%",
                "%block%'[s] primary [potion] effect",
                "[the] secondary [potion] effect of %block%",
                "%block%'[s] secondary [potion] effect");
    }

    private Expression<Block> blockExpression;
    private boolean primary;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        primary = matchedPattern == 0 || matchedPattern == 1;
        return true;
    }

    @Override
    protected PotionEffect @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null) {
            if (block.getState() instanceof Beacon beacon) {
                if (primary) {
                    return new PotionEffect[]{beacon.getPrimaryEffect()};
                } else {
                    return new PotionEffect[]{beacon.getSecondaryEffect()};
                }
            }
        }
        return new PotionEffect[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(PotionEffectType[].class);
        } else {
            return new Class[0];
        }
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        PotionEffectType potionEffectType = delta instanceof PotionEffectType[] ? ((PotionEffectType[]) delta)[0] : null;
        Block block = blockExpression.getSingle(e);
        if (potionEffectType == null || block == null) return;
        if (block.getState() instanceof Beacon beacon) {
            if (primary) {
                beacon.setPrimaryEffect(potionEffectType);
            } else {
                beacon.setSecondaryEffect(potionEffectType);
            }
            beacon.update();
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends PotionEffect> getReturnType() {
        return PotionEffect.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the " + (primary ? "primary" : "secondary") + " potion effect of " + (e == null ? "" : blockExpression.getSingle(e));
    }
}