package it.jakegblp.lusk.elements.minecraft.entities.entity.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.ClickEventTracker;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.Lusk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Name("Click Section")
@Description("""
        Runs the code inside of it when the provided entity gets clicked.
        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """
)
@Examples(
        """
                command /a:
                  trigger:
                    spawn pig at player:
                      set display name of entity to "&dPig"
                      on click on entity:
                        broadcast "%entity% has been clicked!"
                """
)
@Since("1.1")
public class SecEvtClick extends Section {
    public static class ClickListener implements Listener {
        static {
            Lusk.getInstance().registerListener(new ClickListener());
        }

        private static final HashMap<Entity, Consumer<PlayerInteractEntityEvent>> map = new HashMap<>();

        private static void log(Consumer<PlayerInteractEntityEvent> consumer, Entity entity) {
            map.put(entity, consumer);
        }

        @EventHandler
        public static void onEntityClick(PlayerInteractEntityEvent event) {
            Entity entity = event.getRightClicked();
            if (map.containsKey(entity)) map.get(entity).accept(event);
        }
    }

    private static final ClickEventTracker entityInteractTracker = new ClickEventTracker(Lusk.getInstance());

    static {
        Skript.registerSection(SecEvtClick.class, "[execute|run] on click (on|of) %~entity%", "[execute|run] when %~entity% get[s] clicked");
    }

    private Expression<Entity> entityExpression;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        entityExpression = (Expression<Entity>) expressions[0];
        trigger = loadCode(sectionNode, "click", PlayerInteractEntityEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        if (event instanceof PlayerInteractEntityEvent clickEvent) {
            if (clickEvent instanceof PlayerInteractAtEntityEvent && !(clickEvent.getRightClicked() instanceof ArmorStand)) {
                return super.walk(event, false);
            } else if (!(clickEvent instanceof PlayerInteractAtEntityEvent) && !entityInteractTracker.checkEvent(clickEvent.getPlayer(), clickEvent, clickEvent.getHand())) {
                return super.walk(event, false);
            }
        }
        Object vars = Variables.copyLocalVariables(event);
        Consumer<PlayerInteractEntityEvent> consumer = trigger == null ? null : clickEvent -> {
            if (!entityInteractTracker.checkEvent(clickEvent.getPlayer(), clickEvent, clickEvent.getHand())) return;
            Variables.setLocalVariables(clickEvent, vars);
            TriggerItem.walk(trigger, clickEvent);
            Variables.removeLocals(clickEvent);

        };
        ClickListener.log(consumer, entityExpression.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + (event != null ? entityExpression.toString(event, b) : "") + " gets clicked";
    }
}