package me.jake.lusk.elements.expressions;

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
import ch.njol.util.VectorMath;
import ch.njol.util.coll.CollectionUtils;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Armor Stand - Rotations")
@Description("Gets and sets a specific armor stand property.")
@Examples({"broadcast rotation of target", "set head rotation of target to vector(1,0,0)\n# sad armor stand :("})
@Since("1.0.2")
public class ExprArmorStandRotations extends SimpleExpression<Vector> {
    static {
        Skript.registerExpression(ExprArmorStandRotations.class, Vector.class, ExpressionType.COMBINED,
                "[the] (body|torso) rotation of %livingentity%",
                "[the] left arm rotation of %livingentity%",
                "[the] right arm rotation of %livingentity%",
                "[the] left leg rotation of %livingentity%",
                "[the] right leg rotation of %livingentity%",
                "[the] head rotation of %livingentity%",
                "[the] rotation of %livingentity%");

    }

    private Expression<LivingEntity> livingEntityExpression;
    private String property;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        property = switch (matchedPattern) {
            case 0 -> "body";
            case 1 -> "left arm";
            case 2 -> "right arm";
            case 3 -> "left leg";
            case 4 -> "right leg";
            case 5 -> "head";
            default -> "";
        };
        return true;
    }

    @Override
    protected Vector @NotNull [] get(@NotNull Event e) {
        LivingEntity entity = livingEntityExpression.getSingle(e);
        if (entity instanceof ArmorStand armorStand) {
            return switch (property) {
                case "left arm" -> new Vector[]{Utils.toBukkitVector(armorStand.getLeftArmPose())};
                case "right arm" -> new Vector[]{Utils.toBukkitVector(armorStand.getRightArmPose())};
                case "left leg" -> new Vector[]{Utils.toBukkitVector(armorStand.getLeftLegPose())};
                case "right leg" -> new Vector[]{Utils.toBukkitVector(armorStand.getRightLegPose())};
                case "head" -> new Vector[]{Utils.toBukkitVector(armorStand.getHeadPose())};
                case "body" -> new Vector[]{Utils.toBukkitVector(armorStand.getBodyPose())};
                case "" ->
                        new Vector[]{VectorMath.fromYawAndPitch(armorStand.getLocation().getYaw(), armorStand.getLocation().getPitch())};
                default -> new Vector[0];
            };
        }
        return new Vector[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Vector[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Vector vector = delta instanceof Vector[] ? ((Vector[]) delta)[0] : null;
        if (vector == null) return;
        LivingEntity entity = livingEntityExpression.getSingle(e);
        if (entity instanceof ArmorStand armorStand) {
            switch (property) {
                case "left arm" -> Utils.setLeftArmRotation(armorStand, vector);
                case "right arm" -> Utils.setRightArmRotation(armorStand, vector);
                case "left leg" -> Utils.setLeftLegRotation(armorStand, vector);
                case "right leg" -> Utils.setRightLegRotation(armorStand, vector);
                case "head" -> Utils.setHeadRotation(armorStand, vector);
                case "body" -> Utils.setBodyRotation(armorStand, vector);
                case "" -> Utils.setFullRotation(armorStand, vector);
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the" + (Objects.equals(property, "") ? "" : " ") + property + " rotation of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}