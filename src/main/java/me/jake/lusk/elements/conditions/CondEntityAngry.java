package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Warden;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Angry")
@Description("Checks if an entity is angry.\n(Warden, PigZombie, Wolf)")
@Examples({"on damage of wolf:\n\tif victim is angry:\n\t\tcancel event"})
@Since("1.0.2")
public class CondEntityAngry extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityAngry.class, "angry", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof PigZombie pigZombie) {
            return pigZombie.isAngry();
        } else if (entity instanceof Wolf wolf) {
            return wolf.isAngry();
        } else if (entity instanceof Warden warden) {
            return warden.getAngerLevel() == Warden.AngerLevel.ANGRY;
        } else {
            Skript.error("You can only use this condition with Wardens, PigZombies and Wolves!");
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "angry";
    }
}