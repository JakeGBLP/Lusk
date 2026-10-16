package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import io.papermc.paper.math.Rotations;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Name("Armor Stand - Rotations")
@Description("The rotation of each part of an armor stand.\nCan be set, added to, removed from, and reset.")
@Example("broadcast head rotation of target")
@Since("1.0.2, 1.3 (Degrees), 2.0.0 (Paper Rotations)")
public class ExprArmorStandRotations extends SimplerPropertyExpression<LivingEntity, Rotations> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprArmorStandRotations.class, ExprArmorStandRotations::new, Rotations.class)
                .propertyOf(Arrays.stream(BodyPart.values()).map(bodyPart ->
                                ":" + bodyPart.name().toLowerCase(Locale.ENGLISH).replace('_', ' '))
                        .collect(Collectors.joining("|", "(", ")")) + " rotation", "livingentities")
                .register(syntaxRegistry);
    }

    public enum BodyPart {
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG,
        HEAD,
        BODY
    }

    private BodyPart bodyPart;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        boolean init = super.init(expressions, matchedPattern, isDelayed, parseResult);
        bodyPart = BodyPart.valueOf(parseResult.tags.getFirst().toUpperCase(Locale.ENGLISH).replace(' ', '_'));
        return init;
    }

    @Override
    @Nullable
    public Rotations convert(LivingEntity from) {
        if (from instanceof ArmorStand armorStand)
            return switch (bodyPart) {
                case LEFT_ARM -> armorStand.getLeftArmRotations();
                case RIGHT_ARM -> armorStand.getRightArmRotations();
                case LEFT_LEG -> armorStand.getLeftLegRotations();
                case RIGHT_LEG -> armorStand.getRightLegRotations();
                case HEAD -> armorStand.getHeadRotations();
                case BODY -> armorStand.getBodyRotations();
            };
        else return null;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, ADD, REMOVE -> new Class[]{Rotations.class};
            case RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(LivingEntity from, @NotNull Rotations to) {
        if (from instanceof ArmorStand armorStand)
            switch (bodyPart) {
                case LEFT_ARM -> armorStand.setLeftArmRotations(to);
                case RIGHT_ARM -> armorStand.setRightArmRotations(to);
                case LEFT_LEG -> armorStand.setLeftLegRotations(to);
                case RIGHT_LEG -> armorStand.setRightLegRotations(to);
                case HEAD -> armorStand.setHeadRotations(to);
                case BODY -> armorStand.setBodyRotations(to);
            }
    }

    @Override
    public void add(LivingEntity from, Rotations to) {
        var value = convert(from);
        if (value != null)
            set(from, value.add(to.x(), to.y(), to.z()));
    }

    @Override
    public void remove(LivingEntity from, Rotations to) {
        var value = convert(from);
        if (value != null)
            set(from, value.subtract(to.x(), to.y(), to.z()));
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, Rotations.ZERO);
    }

    @Override
    public Class<? extends Rotations> getReturnType() {
        return Rotations.class;
    }

    @Override
    protected String getPropertyName() {
        return bodyPart.name().toLowerCase(Locale.ENGLISH).replace('_', ' ') + " rotation";
    }
}