package it.jakegblp.lusk.skript.api;

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
        super(key, null, false);
        this.expressionEntryData = new ExpressionEntryData<>(key, null, false, entryClass);
        this.containerEntryData = new ContainerEntryData(key, false, entryValidator);
    }

    public boolean isExpressionEntryData() {
        return expressionEntryData != null;
    }

    public boolean isContainerEntryData() {
        return containerEntryData != null;
    }

    @Override
    public @Nullable Object getValue(Node node) {
        return isContainerEntryData() ? containerEntryData.getValue(node) : isExpressionEntryData() ? expressionEntryData.getValue(node) : null;
    }

    @Override
    public boolean canCreateWith(Node node) {
        return (isContainerEntryData() && containerEntryData.canCreateWith(node)) || (isExpressionEntryData() && expressionEntryData.canCreateWith(node));
    }
}
