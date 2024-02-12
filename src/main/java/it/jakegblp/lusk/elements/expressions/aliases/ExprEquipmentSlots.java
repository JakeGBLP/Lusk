package it.jakegblp.lusk.elements.expressions.aliases;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Equipment Slots")
@Description("Returns all the equipment slots.")
@Examples({"broadcast all equipment slots"})
@Since("1.0.0")
public class ExprEquipmentSlots extends SimpleExpression<EquipmentSlot> {
    static {
        Skript.registerExpression(ExprEquipmentSlots.class, EquipmentSlot.class, ExpressionType.SIMPLE,
                "[all [[of] the]] equipment slots");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected EquipmentSlot @NotNull [] get(@NotNull Event e) {
        return EquipmentSlot.values();
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends EquipmentSlot> getReturnType() {
        return EquipmentSlot.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all of the equipment slots";
    }
}
