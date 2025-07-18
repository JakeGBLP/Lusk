package it.jakegblp.lusk.elements.minecraft.blocks.block.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.listeners.BlockBreakListener;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static it.jakegblp.lusk.utils.EventUtils.willItemsDrop;

@Name("Block - on Break Section")
@Description("""
        Runs the code inside of it when the provided block gets broken.
        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples("")
@Since("1.2")
public class SecEvtBreak extends Section {

    static {
        Skript.registerSection(SecEvtBreak.class, "[execute|run] on (break[ing]|mine:min(e|ing)) of %~block%", "[execute|run] when %~block% get[s] (broken|mine:mined)");
    }

    private Expression<Block> blockExpression;
    @Nullable
    private Trigger trigger;

    private boolean mine = false;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        blockExpression = (Expression<Block>) (expressions[0]);
        trigger = loadCode(sectionNode, "break", BlockBreakEvent.class);
        mine = parseResult.hasTag("mine");
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Object vars = Variables.copyLocalVariables(event);
        Consumer<BlockBreakEvent> consumer = trigger == null ? null : breakEvent -> {
            if (!mine || willItemsDrop(breakEvent)) {
                Variables.setLocalVariables(breakEvent, vars);
                TriggerItem.walk(trigger, breakEvent);
                Variables.removeLocals(breakEvent);
            }
        };
        BlockBreakListener.log(consumer, blockExpression.getSingle(event));
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when " + blockExpression.toString(event, b) + " gets " + (mine ? "mined" : "broken");
    }
}