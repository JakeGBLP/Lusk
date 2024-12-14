package it.jakegblp.lusk.elements.other.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Run Silently")
@Description("""
Executes the effects within this section without making sounds, this currently only supports the changing of an item frame's item.

Note: this is not related to errors or warnings in the console.""")
@Examples("""
        silently:
          set item frame item of {_frame} to iron sword # won't play the sound
        """)
@Since("1.3")
public class SecSilent extends Section {

    static {
        Skript.registerSection(SecSilent.class, "[run|execute] (silent[ly]|without sounds)");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        loadCode(sectionNode);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        return walk(event,true);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "execute silently";
    }
}