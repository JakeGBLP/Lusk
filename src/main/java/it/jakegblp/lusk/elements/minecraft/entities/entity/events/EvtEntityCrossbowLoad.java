package it.jakegblp.lusk.elements.minecraft.entities.entity.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerEventValue;

@SuppressWarnings("unused")
public class EvtEntityCrossbowLoad extends SkriptEvent {
    static {
        Skript.registerEvent("Crossbow - on Load", EvtEntityCrossbowLoad.class, EntityLoadCrossbowEvent.class, "[entity] (crossbow load[ed]|load [of] crossbow)")
                .description("Called when a LivingEntity loads a crossbow with a projectile.")
                .examples("")
                .since("1.0.1")
                .requiredPlugins("Paper");
        registerEventValue(EntityLoadCrossbowEvent.class, ItemStack.class, EntityLoadCrossbowEvent::getCrossbow, 0);
    }

    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity crossbow load";
    }

}
