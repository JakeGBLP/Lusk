package me.jake.lusk.elements.expressions.aliases;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.jake.lusk.utils.Utils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
@Name("Sittables")
@Description("Returns all the sittable entities.")
@Examples({"broadcast all sittable entities"})
@Since("1.0.2")
public class ExprSittables extends SimpleExpression<EntityData> {
    static {
        Skript.registerExpression(ExprSittables.class, EntityData.class, ExpressionType.SIMPLE,
                "[all [[of] the]|the] sittable[ entitie]s");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        return true;
    }
    @Override
    protected EntityData @NotNull [] get(@NotNull Event e) {
        return Utils.getSittables().toArray(new EntityData[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends EntityData> getReturnType() {
        return EntityData.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "all of the Sittables entities";
    }
}
