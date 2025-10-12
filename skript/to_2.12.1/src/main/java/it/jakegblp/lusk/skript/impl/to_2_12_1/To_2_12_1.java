package it.jakegblp.lusk.skript.impl.to_2_12_1;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.parser.ParserInstance;
import it.jakegblp.lusk.skript.core.adapters.SkriptAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

public class To_2_12_1 implements SkriptAdapter {

    public final Class<ParserInstance.Data> SECTION_CONTEXT_CLASS;
    public final Method SECTION_CONTEXT_MODIFY_METHOD;

    @SuppressWarnings("unchecked")
    public To_2_12_1() {
        try {
            SECTION_CONTEXT_CLASS = (Class<ParserInstance.Data>) Class.forName("ch.njol.skript.lang.Section$SectionContext");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't get SectionContext class.", e);
        }
        try {
            SECTION_CONTEXT_MODIFY_METHOD = SECTION_CONTEXT_CLASS.getDeclaredMethod("modify", SectionNode.class, List.class, Supplier.class);
            SECTION_CONTEXT_MODIFY_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Couldn't get SectionContext 'modify' method.", e);
        }
    }

    @Override
    public ParserInstance.Data getSectionContext(Object parserInstance) {
        return ((ParserInstance) parserInstance).getData(SECTION_CONTEXT_CLASS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T modifySectionContext(Object context, Object sectionNode, List<Object> triggerItems, Supplier<? extends T> supplier) {
        try {
            return (T) SECTION_CONTEXT_MODIFY_METHOD.invoke(context, (SectionNode) sectionNode, triggerItems, supplier);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't modify section context.", e);
        }
    }
}
