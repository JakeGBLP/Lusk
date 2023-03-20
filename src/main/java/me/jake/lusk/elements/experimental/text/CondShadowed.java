package me.jake.lusk.elements.experimental.text;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

@Name("Display Text is Shadowed")
@Description("Checks if a display text is shadowed.")
@Examples("")
@Since("1.0.2")
public class CondShadowed extends PropertyCondition<Entity> {

    static {
        register(CondShadowed.class, "shadowed", "entity");
    }

    @Override
    public boolean check(Entity e) {
        if (e != null) {
            if (e instanceof TextDisplay display) {
                return display.isShadowed();
            }
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "shadowed";
    }
}
