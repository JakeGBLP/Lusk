package it.jakegblp.lusk.skript.core.adapters;

import java.util.List;
import java.util.function.Supplier;

public interface SkriptAdapter {

    Object getSectionContext(Object parserInstance);
    <T> T modifySectionContext(Object context, Object sectionNode, List<Object> triggerItems, Supplier<? extends T> supplier);

}