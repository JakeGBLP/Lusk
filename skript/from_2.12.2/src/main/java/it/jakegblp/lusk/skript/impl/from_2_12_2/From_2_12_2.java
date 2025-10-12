package it.jakegblp.lusk.skript.impl.from_2_12_2;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Section;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import it.jakegblp.lusk.skript.core.adapters.SkriptAdapter;

import java.util.List;
import java.util.function.Supplier;

public class From_2_12_2 implements SkriptAdapter {

    @Override
    public Section.SectionContext getSectionContext(Object parserInstance) {
        return ((ParserInstance) parserInstance).getData(Section.SectionContext.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T modifySectionContext(Object context, Object sectionNode, List<Object> triggerItems, Supplier<? extends T> supplier) {
        return ((Section.SectionContext)context).modify((SectionNode) sectionNode, (List<TriggerItem>) (List<?>) triggerItems, supplier);
    }

}