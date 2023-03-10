package me.jake.lusk.elements.conditions;

/*
Credits: Sovde
*/


import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

@Name("is Left Handed")
@Description("Checks if an entity is left handed")
@Examples({"on damage of player:\n\tif victim is left handed:\n\t\tbroadcast \"A left handed man has been attacked!\""})
@Since("1.0.0")
public class CondLeftHanded extends PropertyCondition<LivingEntity> {

    private static final boolean CAN_USE_ENTITIES = Skript.methodExists(Mob.class, "isLeftHanded");

    static {
        if (CAN_USE_ENTITIES) {
            register(CondLeftHanded.class, PropertyType.BE, "(:left|right)[ |-]handed", "livingentities");
        } else {
            register(CondLeftHanded.class, PropertyType.BE, "(:left|right)[ |-]handed", "players");
        }
    }

    private boolean leftHand = true;

    @Override
    public boolean init(Expression @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, ParseResult parseResult) {
        leftHand = parseResult.hasTag("left");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public boolean check(LivingEntity o) {
        if (CAN_USE_ENTITIES && o instanceof Mob)
            return ((Mob) o).isLeftHanded() == leftHand;
        if (o instanceof HumanEntity)
            return ((HumanEntity) o).getMainHand() == (leftHand ? org.bukkit.inventory.MainHand.LEFT : org.bukkit.inventory.MainHand.RIGHT);
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return (leftHand ? "left" : "right") +" handed";
    }
}