package it.jakegblp.lusk.elements.expressions;

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
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Armor Stand - Properties")
@Description("Gets and sets a specific armor stand property.")
@Examples({"broadcast arms of target", "set marker of target to true"})
@Since("1.0.2")
public class ExprArmorStandProperties extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprArmorStandProperties.class, Boolean.class, ExpressionType.COMBINED,
                "[the] [is] small [state] of %livingentity%",
                "[the] [has] arms [visibility|state] of %livingentity%",
                "[the] [has] base plate [visibility [state]|state] of %livingentity%",
                "[the] [is] marker [state] of %livingentity%",
                "[the] [can] tick stat(us|e) of %livingentity%",
                "[the] [can] move stat(us|e) of %livingentity%");

    }

    private Expression<LivingEntity> livingEntityExpression;
    private String property;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        property = matchedPattern == 0 ? "small" : (matchedPattern == 1 ? "arms" : (matchedPattern == 2 ? "base plate" : (matchedPattern == 3 ? "marker" : (matchedPattern == 4 ? "can tick status" : "can move status"))));
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        LivingEntity entity = livingEntityExpression.getSingle(e);
        if (entity instanceof ArmorStand armorStand) {
            return switch (property) {
                case "small" -> new Boolean[]{armorStand.isSmall()};
                case "arms" -> new Boolean[]{armorStand.hasArms()};
                case "base plate" -> new Boolean[]{armorStand.hasBasePlate()};
                case "marker" -> new Boolean[]{armorStand.isMarker()};
                case "can tick status" -> new Boolean[]{armorStand.canTick()};
                case "can move status" -> new Boolean[]{armorStand.canMove()};
                default -> new Boolean[0];
            };
        }
        return new Boolean[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Boolean bool = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (bool == null) return;
        LivingEntity entity = livingEntityExpression.getSingle(e);
        if (entity instanceof ArmorStand armorStand) {
            switch (property) {
                case "small" -> armorStand.setSmall(bool);
                case "arms" -> armorStand.setArms(bool);
                case "base plate" -> armorStand.setBasePlate(bool);
                case "marker" -> armorStand.setMarker(bool);
                case "can tick status" -> armorStand.setCanTick(bool);
                case "can move status" -> armorStand.setCanMove(bool);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return property + " of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}