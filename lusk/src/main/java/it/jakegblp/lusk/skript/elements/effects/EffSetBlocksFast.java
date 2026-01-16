package it.jakegblp.lusk.skript.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("NMS - Set Blocks Fast")
public class EffSetBlocksFast extends Effect {

    static {
        Skript.registerEffect(EffSetBlocksFast.class,
                "set blocks [from] %location% to %location% (with|to) %blockdata% fast",
                "replace block %blockdata% (from|within) %location% to %location% (with|to) %blockdata% fast"
        );
    }

    private Expression<Location> corner1Expression;
    private Expression<Location> corner2Expression;
    private Expression<BlockData> blockDataExpression;
    private Expression<BlockData> blockDataForReplacementExpression;
    int pattern;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if(matchedPattern == 0) {
            corner1Expression = (Expression<Location>) expressions[0];
            corner2Expression = (Expression<Location>) expressions[1];
            blockDataExpression = (Expression<BlockData>) expressions[2];
        }
        else if (matchedPattern == 1){
            blockDataForReplacementExpression = (Expression<BlockData>) expressions[0];
            corner1Expression = (Expression<Location>) expressions[1];
            corner2Expression = (Expression<Location>) expressions[2];
            blockDataExpression = (Expression<BlockData>) expressions[3];
        }
        this.pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event event) {
        if (pattern == 0)
            AbstractNMS.NMS.setCubeFast(corner1Expression.getSingle(event), corner2Expression.getSingle(event), blockDataExpression.getSingle(event));
        else if (pattern == 1){
            AbstractNMS.NMS.replaceFast(corner1Expression.getSingle(event), corner2Expression.getSingle(event), blockDataForReplacementExpression.getSingle(event), blockDataExpression.getSingle(event));
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set or replace blocks fast";
    }
}
