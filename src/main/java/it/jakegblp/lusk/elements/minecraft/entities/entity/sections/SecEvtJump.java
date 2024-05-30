package it.jakegblp.lusk.elements.minecraft.entities.entity.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.classes.events.GenericEntityJumpEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.HorseJumpEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Name("Jump Section")
@Description("""
        Runs the code inside of it when the provided entity jumps.
        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples("""
        """)
@Since("1.2")
public class SecEvtJump extends Section {
    
    public static class JumpListener implements Listener {
        static {
            Lusk.getInstance().registerListener(new JumpListener());
        }

        private static final HashMap<Entity, Consumer<GenericEntityJumpEvent>> map = new HashMap<>();

        private static void log(Consumer<GenericEntityJumpEvent> consumer, Entity entity) {
            map.put(entity, consumer);
        }

        @EventHandler
        public void onEntityJump(EntityJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getEntity()));
        }
        @EventHandler
        public static void onHorseJump(HorseJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getEntity()));
        }
        @EventHandler
        public static void onPlayerJump(PlayerJumpEvent event) {
            Lusk.callEvent(new GenericEntityJumpEvent(event.getPlayer()));
        }
        @EventHandler
        public static void onGenericEntityJump(GenericEntityJumpEvent event) {
            Entity entity = event.getEntity();
            if (map.containsKey(entity)) map.get(entity).accept(event);
        }
    }

    static {
        Skript.registerSection(SecEvtJump.class, "[execute|run] on %~entity% jump[ing]", "[execute|run] when %~entity% jump[s]");
    }

    private Expression<Entity> entity;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        entity = (Expression<Entity>) (expressions[0]);
        trigger = loadCode(sectionNode, "jump", GenericEntityJumpEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Object vars = Variables.copyLocalVariables(event);
        Consumer<GenericEntityJumpEvent> consumer = trigger == null ? null : entityJumpEvent -> {
            Variables.setLocalVariables(entityJumpEvent, vars);
            TriggerItem.walk(trigger, entityJumpEvent);
            Variables.removeLocals(entityJumpEvent);
        };
        JumpListener.log(consumer, entity.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? entity.toString(event, b) : "") + " jumps";
    }
}