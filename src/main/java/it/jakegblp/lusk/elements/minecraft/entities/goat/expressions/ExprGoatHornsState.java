package it.jakegblp.lusk.elements.minecraft.entities.goat.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Goat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goat - has Left/Right Horn (Property)")
@Description("Returns whether or not a goat has either horn.\nCan be set and reset.")
@Examples({"broadcast goat has left horn state of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprGoatHornsState extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        register(ExprGoatHornsState.class, Boolean.class, "goat", "[has] (:left|right) horn", "livingentities");
    }

    private boolean left;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        left = parseResult.hasTag("left");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof Goat goat && (left ? goat.hasLeftHorn() : goat.hasRightHorn());
    }

    @Override
    protected String getPropertyName() {
        return "goat has "+(left ? "left" : "right")+ " horn";
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Goat goat) {
            if (left)
                goat.setLeftHorn(to);
            else
                goat.setRightHorn(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

}