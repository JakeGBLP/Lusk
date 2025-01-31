package it.jakegblp.lusk.elements.minecraft.entities.irongolem.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.HAS_NMS;
import static it.jakegblp.lusk.utils.Constants.NMS;

@Name("NMS | Iron Golem - Offer Flower")
@Description("""
**THIS USES NMS, MEANING IT WON'T AUTOMATICALLY BE AVAILABLE WHEN A MINECRAFT UPDATE DROPS**

Makes the provided iron golems offer a flower, usually a poppy, will usually last for 400 ticks.
""")
@Examples("make target offer a flower")
@Since("1.4")
@RequiredPlugins("1.16.5 - 1.21.4")
public class EffNmsIronGolemOfferFlower extends Effect {

    static {
        if (HAS_NMS)
            Skript.registerEffect(EffNmsIronGolemOfferFlower.class, "make [iron[ |-]golem] %livingentities% [:not] offer [a] flower");
    }

    private Expression<LivingEntity> livingEntityExpression;
    private boolean shouldOffer;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) expressions[0];
        shouldOffer = !parseResult.hasTag("not");
        return true;
    }

    @Override
    protected void execute(Event event) {
        assert NMS != null;
        for (LivingEntity livingEntity : livingEntityExpression.getAll(event)) {
            if (livingEntity instanceof IronGolem ironGolem) {
                NMS.offerFlower(ironGolem, shouldOffer);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make " + livingEntityExpression.toString(event, debug) + (shouldOffer ? "" : " not") + " offer a flower";
    }
}
