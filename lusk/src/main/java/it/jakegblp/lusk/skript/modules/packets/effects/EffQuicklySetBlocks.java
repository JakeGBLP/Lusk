package it.jakegblp.lusk.skript.modules.packets.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffQuicklySetBlocks extends Effect {

    private Expression<Location> corner1Expression, corner2Expression;
    private Expression<BlockData> replacementExpression, toBeReplacedExpression;
    private boolean replace;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffQuicklySetBlocks.class, EffQuicklySetBlocks::new)
                .patterns("quickly set [all [[of] the]|the] blocks within %location% and %location% to %blockdata%",
                        "quickly replace [all] %blockdata% within %location% and %location% with %blockdata%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.replace = matchedPattern == 1;
        if (!replace) {
            corner1Expression = (Expression<Location>) expressions[0];
            corner2Expression = (Expression<Location>) expressions[1];
            replacementExpression = (Expression<BlockData>) expressions[2];
        } else {
            toBeReplacedExpression = (Expression<BlockData>) expressions[0];
            corner1Expression = (Expression<Location>) expressions[1];
            corner2Expression = (Expression<Location>) expressions[2];
            replacementExpression = (Expression<BlockData>) expressions[3];
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (!replace)
            AbstractNMS.NMS.setCubeFast(corner1Expression.getSingle(event), corner2Expression.getSingle(event), replacementExpression.getSingle(event));
        else
            AbstractNMS.NMS.replaceFast(corner1Expression.getSingle(event), corner2Expression.getSingle(event), toBeReplacedExpression.getSingle(event), replacementExpression.getSingle(event));
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        var builder = new SyntaxStringBuilder(event, debug).append("quickly");
        if (replace)
            builder.append("replace all", toBeReplacedExpression, "within", corner1Expression, "and", corner2Expression, "with", replacementExpression);
        else
            builder.append("set all of the blocks within", corner1Expression, "and", corner2Expression, "to", replacementExpression);
        return builder.toString();
    }
}
