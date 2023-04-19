package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.Location;
import org.bukkit.block.Bell;
import org.jetbrains.annotations.NotNull;

@Name("Bell - is Ringing")
@Description("Checks if a bell is ringing.")
@Examples({"on bell ring:\n\tif event-block is ringing:\n\t\tcancel event\n\t\t"})
@Since("1.0.3")
public class CondBellResonating extends PropertyCondition<Object> {
    static {
        register(CondBellResonating.class, "resonating", "block/location");
    }

    @Override
    public boolean check(Object o) {
        if (o instanceof Location location) {
            if (location.getBlock() instanceof Bell bell) {
                return bell.isResonating();
            }
        } else if (o instanceof Bell bell) {
            return bell.isResonating();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "resonating";
    }
}