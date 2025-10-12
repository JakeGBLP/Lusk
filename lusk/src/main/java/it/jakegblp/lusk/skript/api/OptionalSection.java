package it.jakegblp.lusk.skript.api;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface OptionalSection {
    boolean initNormal(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result);
    boolean initSection(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems);
}
