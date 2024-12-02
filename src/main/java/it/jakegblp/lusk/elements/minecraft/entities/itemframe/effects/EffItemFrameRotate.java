package it.jakegblp.lusk.elements.minecraft.entities.itemframe.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static it.jakegblp.lusk.utils.ItemFrameUtils.rotateItemFrame;
import static it.jakegblp.lusk.utils.ItemFrameUtils.sumRotations;

@Name("Item Frame - Rotate")
@Description("Rotates item frames counter clockwise and clockwise or by specific rotations and a set amount of times.")
@Examples("rotate {_itemFrame} by 45 degrees twice")
@Since("1.3")
public class EffItemFrameRotate extends Effect {

    static {
        Skript.registerEffect(EffItemFrameRotate.class,
                "rotate [item[ |-]frame[s]] %entities% [:counter] clockwise [%numbers%]",
                "rotate [item[ |-]frame[s]] %entities% by %rotations% [%numbers%]");
    }

    private Expression<Entity> entityExpression;
    private Expression<Rotation> rotationExpression;
    private Expression<Number> numberExpression;
    private boolean clockwise = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) expressions[0];
        if (matchedPattern == 0) {
            clockwise = !parseResult.hasTag("counter");
            rotationExpression = null;
            numberExpression = (Expression<Number>) expressions[1];
        } else {
            rotationExpression = (Expression<Rotation>) expressions[1];
            numberExpression = (Expression<Number>) expressions[2];
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        List<ItemFrame> itemFrameList = entityExpression.stream(event).filter(entity -> entity instanceof ItemFrame)
                .map(entity -> (ItemFrame) entity).toList();
        if (itemFrameList.isEmpty()) return;
        int times = Math.max(1,numberExpression.stream(event).mapToInt(Number::intValue).max().orElse(1));
        if (rotationExpression == null) {
            itemFrameList.forEach(itemFrame -> rotateItemFrame(itemFrame, clockwise, times));
        } else {
            itemFrameList.forEach(itemFrame -> itemFrame.setRotation(sumRotations(
                    rotationExpression.stream(event),
                    itemFrame.getRotation(),times)));
        }
    }

    public String toString(@Nullable Event event, boolean debug) {
        return "rotate item frames "+entityExpression.toString(event,debug)
                + (rotationExpression == null ? ((clockwise ? " " : " counter ")+"clockwise")
                : " by " + rotationExpression.toString(event,debug)) + " "+ numberExpression.toString(event,debug)+ " times";
    }

}
