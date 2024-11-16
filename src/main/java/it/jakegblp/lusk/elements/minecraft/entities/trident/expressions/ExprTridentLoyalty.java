package it.jakegblp.lusk.elements.minecraft.entities.trident.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Trident - Loyalty")
@Description("""
        Returns loyalty level of a thrown trident.
        
        Allows: Setting, Removing, Adding, Deleting and Resetting.
        
        This value will always be between 0 and 127.
        
        Changing this value won't influence the item itself, just the throw trident.""")
@Examples({"broadcast loyalty of {_trident}"})
@Since("1.2")
@SuppressWarnings("unused")
public class ExprTridentLoyalty extends SimpleExpression<Integer> {
    static {
        // TODO: PROPERTY EXPR
        Skript.registerExpression(ExprTridentLoyalty.class, Integer.class, ExpressionType.PROPERTY,
                "[the] [trident] loyalty [level] of %projectile%",
                "%projectile%'[s] [trident] loyalty [level]");
    }

    private Expression<Projectile> projectileExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        projectileExpression = (Expression<Projectile>) exprs[0];
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Projectile projectile = projectileExpression.getSingle(e);
        if (projectile instanceof Trident trident) {
            return new Integer[]{trident.getLoyaltyLevel()};
        }
        return new Integer[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET, RESET, DELETE -> new Class[]{Integer.class};
            case REMOVE_ALL -> null;
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.REMOVE_ALL) return;
        if (projectileExpression.getSingle(e) instanceof Trident trident) {
            if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE)
                trident.setLoyaltyLevel(0);
            else if (delta[0] instanceof Integer integer) {
                if (mode == Changer.ChangeMode.ADD)
                    integer = trident.getLoyaltyLevel() + integer;
                else if (mode == Changer.ChangeMode.REMOVE)
                    integer = trident.getLoyaltyLevel() - integer;
                trident.setLoyaltyLevel(Math.clamp(integer, 0, 127));
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the trident loyalty level of " + (e == null ? "" : projectileExpression.toString(e, debug));
    }
}