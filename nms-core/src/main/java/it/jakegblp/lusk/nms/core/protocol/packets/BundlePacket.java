package it.jakegblp.lusk.nms.core.protocol.packets;

import it.jakegblp.lusk.common.SimpleList;
import it.jakegblp.lusk.nms.core.async.AsyncablesWrapper;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@NullMarked
@EqualsAndHashCode
public abstract class BundlePacket<P extends Packet> implements Packet, SimpleList<P>, AsyncablesWrapper {
    protected final List<P> packets;

    public BundlePacket(P[] packets) {
        this(new ArrayList<>(Arrays.asList(packets)));
    }

    public BundlePacket(List<P> packets) {
        this();
        this.packets.addAll(packets);
    }

    public BundlePacket() {
        packets = new ArrayList<>();
    }

    public boolean isEmpty() {
        return packets.isEmpty();
    }

    @SafeVarargs
    public final void set(P... packets) {
        set(Arrays.asList(packets));
    }

    public void set(Collection<? extends P> packets) {
        this.packets.clear();
        this.packets.addAll(packets);
    }

    public final void add(P packet) {
        packets.add(packet);
    }

    @SafeVarargs
    public final void add(P... packets) {
        add(Arrays.asList(packets));
    }

    public void add(Collection<? extends P> packets) {
        this.packets.addAll(packets);
    }

    public final void remove(P packet) {
        packets.remove(packet);
    }

    @SafeVarargs
    public final void remove(P... packets) {
        remove(Arrays.asList(packets));
    }

    public void remove(Collection<? extends P> packets) {
        this.packets.removeAll(packets);
    }

    @Override
    public void clear() {
        this.packets.clear();
    }

    @Override
    public int size() {
        return packets.size();
    }

    @Override
    public List<P> get() {
        return packets;
    }

    @Override
    public void set(List<P> packets) {
        this.packets.clear();
        this.packets.addAll(packets);
    }

    @Override
    public void add(List<P> packets) {
        this.packets.addAll(packets);
    }

    @Override
    public void remove(List<P> packets) {
        this.packets.removeAll(packets);
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.getAsyncablePrioritizedMode(packets);
    }

    @Override
    public @NotNull List<P> getAsyncables() {
        return getPackets();
    }
}
