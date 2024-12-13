package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Name("Fox - Trusts Players")
@Description("Checks if the provided foxes trust the provided offline players.")
@Examples({"if {_fox} trusts player:"})
@Since("1.3")
public class CondFoxTrusts extends Condition {

    static {
        Skript.registerCondition(CondFoxTrusts.class,
                "[fox[es]] %livingentities% trust[s] %offlineplayers%",
                "[fox[es]] %livingentities% do[es](n't| not) trust %offlineplayers%");
    }

    private Expression<LivingEntity> livingEntityExpression;
    private Expression<OfflinePlayer> offlinePlayerExpression;

    @Override
    public boolean check(Event event) {
        return livingEntityExpression.check(event,livingEntity -> {
            if (livingEntity instanceof Fox fox) {
                return offlinePlayerExpression.check(event,offlinePlayer -> {
                    if (fox.getFirstTrustedPlayer() == null) return false;
                    UUID uuid = offlinePlayer.getUniqueId();
                    if (fox.getFirstTrustedPlayer().getUniqueId() == uuid) return true;
                    return fox.getSecondTrustedPlayer() != null && fox.getSecondTrustedPlayer().getUniqueId() == uuid;
                });
            }
            return false;
        },isNegated());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "foxes "+livingEntityExpression.toString(event, debug)+(isNegated() ? " does not" : "")+" trust "+offlinePlayerExpression.toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) expressions[0];
        offlinePlayerExpression = (Expression<OfflinePlayer>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }
}
