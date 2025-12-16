package it.jakegblp.lusk.nms.core.async;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ExecutionMode {
    ASYNCHRONOUS,
    SYNCHRONOUS,
    INHERITED;

    @NotNull
    public static ExecutionMode getPrioritizedMode(List<@NotNull ExecutionMode> modes) {
        if (modes == null || modes.isEmpty()) return INHERITED;
        ExecutionMode smallest = modes.get(0);
        for (int i = 1; i < modes.size(); i++) {
            ExecutionMode m = modes.get(i);
            if (m.compareTo(smallest) < 0)
                smallest = m;
        }
        return smallest;
    }

    @NotNull
    public static ExecutionMode getAsyncablePrioritizedMode(List<? extends Asyncable> asyncables) {
        if (asyncables == null || asyncables.isEmpty()) return INHERITED;
        ExecutionMode smallest = asyncables.get(0).getExecutionMode();
        for (int i = 1; i < asyncables.size(); i++) {
            ExecutionMode m = asyncables.get(i).getExecutionMode();
            if (m.compareTo(smallest) < 0)
                smallest = m;
        }
        return smallest;
    }


    @NotNull
    public static ExecutionMode fromBooleans(boolean async, boolean sync) {
        if (async) return ExecutionMode.ASYNCHRONOUS;
        else if (sync) return ExecutionMode.SYNCHRONOUS;
        else return ExecutionMode.INHERITED;
    }

    @Contract("false, false, false -> null")
    public static ExecutionMode fromBooleans(boolean async, boolean sync, boolean inherited) {
        if (async) return ExecutionMode.ASYNCHRONOUS;
        else if (sync) return ExecutionMode.SYNCHRONOUS;
        else if (inherited) return ExecutionMode.INHERITED;
        else return null;
    }

    public boolean isAsync() {
        return this == ASYNCHRONOUS;
    }

    public boolean isSync() {
        return this == SYNCHRONOUS;
    }

    public boolean isInherited() {
        return this == INHERITED;
    }

    public ExecutionMode highestPriority(ExecutionMode other) {
        return compareTo(other) > 0 ? other : this;
    }

    public boolean hasLessPriorityThan(ExecutionMode other) {
        return compareTo(other) > 0;
    }
}