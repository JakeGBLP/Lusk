package it.jakegblp.lusk.skript.api;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class OptionallySectionEffect extends EffectSection implements OptionalSection {

    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        if (hasSection()) {
            return initSection(expressions, pattern, delayed, result, node, triggerItems);
        } else if (node != null) {
            Skript.error("This element should not be treated as a section if the items are provided inline.");
            return false;
        }
        return initNormal(expressions, pattern, delayed, result);
    }
}
