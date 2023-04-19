package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

@Name("Llama - is In Caravan")
@Description("Checks if a llama is in a caravan.")
@Examples({"if target is in a caravan:"})
@Since("1.0.3")
public class CondLlamaInCaravan extends PropertyCondition<Entity> {
    static {
        register(CondLlamaInCaravan.class, "in [a] caravan", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        if (entity instanceof Llama llama) {
            return llama.inCaravan();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "in a caravan";
    }
}