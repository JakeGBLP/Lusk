package it.jakegblp.lusk.nms.core.async;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AsyncablesWrapper extends Asyncable {

    @NotNull
    List<? extends Asyncable> getAsyncables();

    @Override
    @NotNull
    default ExecutionMode getExecutionMode() {
        return ExecutionMode.getAsyncablePrioritizedMode(getAsyncables());
    }

    @Override
    default void process() {
        for (Asyncable asyncable : getAsyncables()) {
            asyncable.process();
        }
    }

}
