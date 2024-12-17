package it.jakegblp.lusk.elements.minecraft.entities.allay.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;

@Name("Allay - Can Be Duplicated")
@Description("Checks if an allay can be duplicated.")
@Examples({"if target can be duplicated:"})
@Since("1.0.2, 1.3 (Plural)")
@SuppressWarnings("unused")
public class CondAllayCanBeDuplicated extends PrefixedPropertyCondition<Entity> {

    static {
        register(CondAllayCanBeDuplicated.class,PropertyType.CAN,"[allay[s]]","be duplicated","entities");
    }

    @Override
    public boolean check(Entity value) {
        return value instanceof Allay allay && allay.canDuplicate();
    }

    @Override
    protected String getPropertyName() {
        return "be duplicated";
    }

    @Override
    public String getPrefix() {
        return "allays";
    }
}