package it.jakegblp.lusk.nms.core.async;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Asyncable {

    default @NotNull ExecutionMode getExecutionMode() {
        return ExecutionMode.INHERITED;
    }

    default boolean isAsync() {
        return getExecutionMode().isAsync();
    }

    default boolean isSync() {
        return getExecutionMode().isSync();
    }

    default boolean isInherited() {
        return getExecutionMode().isInherited();
    }

    default void process() {
    }

    @ApiStatus.NonExtendable
    default CompletableFuture<Void> processAsync() {
        return CompletableFuture.runAsync(this::process);
    }

}