package me.jake.lusk.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skbee.api.text.BeeComponent;
import me.jake.lusk.Lusk;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@Name("Text Component - On Component Click")
@Description("Allows you to run code when clicking on a text component.\nUsing this will override the already existing click event if any.\nWhen clicking on a component in a book the book will be closed, you can reopen it right after but you have to go back to that page manually.\nRequires SkBee")
@Examples({"set {_t} to text component from \"Click Me!\"\non click on {_t}:\n\tgive 15 diamonds to player"})
@Since("1.0.4")
public class SecTextToRun extends Section {
    static {
        if (Bukkit.getServer().getPluginManager().getPlugin("SkBee") != null) {
            Skript.registerSection(SecTextToRun.class, "[on] [component] click on %textcomponent%");
        }
    }

    private Expression<BeeComponent> componentExpression;
    private Trigger trigger;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        componentExpression = (Expression<BeeComponent>) exprs[0];
        trigger = loadCode(sectionNode, "component click", PlayerCommandPreprocessEvent.class);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        BeeComponent component = componentExpression.getSingle(e);
        if (component != null) {
            Object variables = Variables.copyLocalVariables(e);
            ClickEvent clickEvent;
            if (variables != null) {
                clickEvent = ClickEvent.runCommand(Lusk.getInstance().componentClickRun.createCommand(event -> {
                    Variables.setLocalVariables(event, variables);
                    trigger.execute(event);
                }));
            } else {
                clickEvent = ClickEvent.runCommand(Lusk.getInstance().componentClickRun.createCommand(event -> {
                    trigger.execute(event);
                }));
            }
            component.setClickEvent(clickEvent);
        }
        return walk(e, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "on component click";
    }
}
