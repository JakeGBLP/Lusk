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
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Beacon - Applied Effect")
@Description("Returns the potion effect being applied in the Beacon Effect Applied Event.\nThis expression can be set.")
@Examples({"on beacon effect applied:\n\tbroadcast the applied effect"})
@Since("1.0.3")
public class ExprBeaconAppliedEffect extends SimpleExpression<PotionEffect> {
    static {
        Skript.registerExpression(ExprBeaconAppliedEffect.class, PotionEffect.class, ExpressionType.SIMPLE,
                "[the] applied [beacon] [potion] effect");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(BeaconEffectEvent.class)) {
            Skript.error("This expression can only be used in the Beacon Effect Applied Event!");
            return false;
        }
        return true;
    }

    @Override
    protected PotionEffect @NotNull [] get(@NotNull Event e) {
        return new PotionEffect[]{((BeaconEffectEvent) e).getEffect()};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(PotionEffect[].class);
        } else {
            return new Class[0];
        }
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        PotionEffect potionEffect = delta instanceof PotionEffect[] ? ((PotionEffect[]) delta)[0] : null;
        if (potionEffect == null) return;
        ((BeaconEffectEvent) e).setEffect(potionEffect);
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
        return "the applied beacon effect";
    }
}
