package it.jakegblp.lusk.skript.api.entry;

import ch.njol.skript.config.Node;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.ContainerEntryData;
import org.skriptlang.skript.lang.entry.EntryData;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

public class DynamicEntryData extends EntryData<Object> {
    private final ExpressionEntryData<?> expressionEntryData;
    private final ContainerEntryData containerEntryData;

    public DynamicEntryData(String key, Class<?> entryClass, EntryValidator entryValidator) {
        this(key, entryClass, entryValidator, false);
    }
    public DynamicEntryData(String key, Class<?> entryClass, EntryValidator entryValidator, boolean optional) {
        super(key, null, optional);
        this.expressionEntryData = new ExpressionEntryData<>(key, null, optional, entryClass);
        this.containerEntryData = new ContainerEntryData(key, optional, entryValidator);
    }

    public boolean isExpressionEntryData() {
        return expressionEntryData != null;
    }

    public boolean isContainerEntryData() {
        return containerEntryData != null;
    }

    @Override
    public @Nullable Object getValue(Node node) {
        if (isContainerEntryData()) {
            var entryContainer = containerEntryData.getValue(node);
            if (entryContainer != null)
                return entryContainer;
        }
        if (isExpressionEntryData())
            return expressionEntryData.getValue(node);
        return null;
    }

    @Override
    public boolean canCreateWith(Node node) {
        return (isContainerEntryData() && containerEntryData.canCreateWith(node)) || (isExpressionEntryData() && expressionEntryData.canCreateWith(node));
    }
}
