package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class TeamPacket implements BufferSerializableClientboundPacket {

    @Contract("_, _, _ -> new")
    public static @NotNull TeamPacket create(String name, TeamParameters params, Set<String> members) {
        return new TeamPacket(0, name, params, members);
    }

    @Contract("_ -> new")
    public static @NotNull TeamPacket delete(String name) {
        return new TeamPacket(1, name, null, null);
    }

    @Contract("_, _ -> new")
    public static @NotNull TeamPacket updateInfo(String name, TeamParameters params) {
        return new TeamPacket(2, name, params, null);
    }

    @Contract("_, _ -> new")
    public static @NotNull TeamPacket add(String name, Set<String> members) {
        return new TeamPacket(3, name, null, members);
    }

    @Contract("_, _ -> new")
    public static @NotNull TeamPacket remove(String name, Set<String> members) {
        return new TeamPacket(4, name, null, members);
    }

    @Override
    public TeamPacket copy() {
        return new TeamPacket(method, name, parameters.copy(), new HashSet<>(members));
    }

    public enum Type {
        CREATE, DELETE, UPDATE_INFO, ADD, REMOVE;

        public static @Nullable Type fromId(int id) {
            return id >= 0 && id < values().length ? values()[id] : null;
        }
    }

    private Type method;
    private @NotNull String name;
    private TeamParameters parameters;
    private Set<String> members;

    public TeamPacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public TeamPacket(int id, String name, TeamParameters params, Set<String> members) {
        this(Type.fromId(id), name, params, members);
    }

    public boolean hasParameters() {
        return method == Type.CREATE || method == Type.UPDATE_INFO;
    }

    public boolean hasMembers() {
        return method == Type.CREATE || method == Type.ADD || method == Type.REMOVE;
    }

    @Override
    public void write(@NotNull SimpleByteBuf buffer) {
        buffer.writeString(name);
        buffer.writeByte(method.ordinal());
        if (hasMembers()) buffer.writeCollection(members, SimpleByteBuf::writeString);
        if (hasParameters()) parameters.write(buffer);
    }

    @Override
    public void read(@NotNull SimpleByteBuf buffer) {
        name = buffer.readString();
        method = Type.fromId(buffer.readByte());
        if (hasMembers()) members = buffer.readSet(SimpleByteBuf::readString);
        if (hasParameters()) parameters = new TeamParameters(buffer);
    }
}
