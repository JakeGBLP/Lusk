package me.jake.lusk.elements.experimental;

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
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Display Entity")
@Description("A display entity.")
@Examples({"broadcast display text"})
@Since("1.0.2")
public class ExprDisplayEntity extends SimpleExpression<EntityType> {
    static {
        Skript.registerExpression(ExprDisplayEntity.class, EntityType.class, ExpressionType.SIMPLE,
                "display text","display block","display item");
    }

    private int type;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        type = matchedPattern;
        return true;
    }
    @Override
    protected EntityType @NotNull [] get(@NotNull Event e) {
        if (type == 0) {
            return new EntityType[]{EntityType.TEXT_DISPLAY};
        } else if (type == 1) {
            return new EntityType[]{EntityType.BLOCK_DISPLAY};
        } else {
            return new EntityType[]{EntityType.ITEM_DISPLAY};
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends EntityType> getReturnType() {
        return EntityType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "display entity";
    }
}
