package it.jakegblp.lusk.elements.sections;

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
    private static class ClickEventData {
        public Event event;
        public Object object;
        public ClickEventData(Event event, Object object) {
            this.object = object;
            this.event = event;
        }
    }
    public static class ClickListener implements Listener {
        private static final HashMap<Object, Consumer<ClickEventData>> map = new HashMap<>();
        private static void log(Consumer<ClickEventData> consumer, Object object) {
            if (object instanceof Entity entity) {
                String uuid = entity.getUniqueId().toString();
                if (!map.containsKey(uuid)) {
                    map.put(uuid,consumer);
                }
            }
        }
        @EventHandler
        public static void onEntityClick(PlayerInteractEntityEvent event) {
            Entity entity = event.getRightClicked();
            String uuid = entity.getUniqueId().toString();
            if (map.containsKey(uuid))
                map.get(uuid).accept(new ClickEventData(event,entity));
        }
    }

    private static final ClickEventTracker entityInteractTracker = new ClickEventTracker(Lusk.getInstance());


    static {
        Lusk.getInstance().registerListener(new ClickListener());
        Skript.registerSection(SecEvtClick.class,"[on] click on %entity%","when %entity% get[s] clicked");
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
            Entity entity = clickEvent.getRightClicked();
            if (entity instanceof PlayerInteractAtEntityEvent) {
                if (!(entity instanceof ArmorStand)) {
                    return super.walk(event, false);
                }
            } else {
                if (!entityInteractTracker.checkEvent(clickEvent.getPlayer(),clickEvent,clickEvent.getHand())) {
                    return super.walk(event,false);
                }
            }

        }
        Consumer<ClickEventData> consumer;
        if (trigger != null) {
            consumer = o -> {
                PlayerInteractEntityEvent clickEvent = (PlayerInteractEntityEvent)o.event;
                if (entityInteractTracker.checkEvent(clickEvent.getPlayer(),clickEvent,clickEvent.getHand())) {
                    Variables.setLocalVariables(clickEvent, Variables.copyLocalVariables(event));
                    TriggerItem.walk(trigger, clickEvent);
                }
            };
        } else {
            consumer = null;
        }
        ClickListener.log(consumer, entityExpression.getSingle(event));
        return super.walk(event,false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when "+(event != null ? entityExpression.toString(event,b) : "") + " gets clicked";
    }
}