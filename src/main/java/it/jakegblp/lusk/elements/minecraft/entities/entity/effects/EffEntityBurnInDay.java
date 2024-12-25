package it.jakegblp.lusk.elements.minecraft.entities.entity.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.setShouldBurnDuringTheDay;

@Name("Entity - Make Burn Under the Sun")
@Description("Makes the provided entities burn (or not) when under the sun.")
@Examples("make target burn during the day")
@Since("1.3")
@RequiredPlugins("Paper")
public class EffEntityBurnInDay extends Effect {

    static {
        Skript.registerEffect(EffEntityBurnInDay.class,
                "make %livingentities% [:not] burn ((during|in) [the] day|(in|under) [the] (sun[light]|daylight))",
                "make %livingentities% [:not] immune to [the] (sun[light]|daylight)");
    }

    private Expression<LivingEntity> entity;
    private boolean shouldBurn;

    @Override
    protected void execute(Event event) {
        for (LivingEntity livingEntity : entity.getAll(event)) {
            setShouldBurnDuringTheDay(livingEntity,shouldBurn);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make " + entity.toString(event, debug) + (shouldBurn ? " " : " not ") + "burn during the day";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) expressions[0];
        shouldBurn = matchedPattern == 0 ^ parseResult.hasTag("not");
        return true;
    }
}
