package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@AllArgsConstructor
@Getter
@Setter
public abstract class TeamPacket implements ClientboundPacket, Cloneable {

    @Contract(pure = true)
    public static @NotNull TeamPacket fromMethod(int method, String name, TeamParameters teamParameters, Set<String> members) {
        return switch (method) {
            case 0 -> new Create(name, teamParameters, members);
            case 1 -> new Delete(name);
            case 2 -> new UpdateInfo(name, teamParameters);
            case 3 -> new Add(name, members);
            case 4 -> new Remove(name, members);
            default -> throw new IllegalStateException("Unexpected method: " + method);
        };
    }

    protected @NotNull String name;

    @Override
    public TeamPacket clone() throws CloneNotSupportedException {
        return (TeamPacket) super.clone(); // fixme
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSSetPlayerTeamPacket(this);
    }

    public interface ChangeTeamInfo {
        @NotNull TeamParameters getParameters();
    }

    public interface WithMembers {
        @NotNull Set<String> getMembers();
    }

    public abstract int getMethod();

    @NullMarked
    @Getter
    @Setter
    public static class Create extends TeamPacket implements WithMembers, ChangeTeamInfo {
        protected TeamParameters parameters;
        protected Set<String> members;
        public Create(String name, TeamParameters parameters, Set<String> members) {
            super(name);
            this.parameters = parameters;
            this.members = members;
        }

        @Override
        public int getMethod() {
            return 0;
        }
    }

    @NullMarked
    public static class Delete extends TeamPacket {
        public Delete(String name) {
            super(name);
        }

        @Override
        public int getMethod() {
            return 1;
        }
    }

    @NullMarked
    @Getter
    @Setter
    public static class UpdateInfo extends TeamPacket implements ChangeTeamInfo {

        protected TeamParameters parameters;

        public UpdateInfo(String name, TeamParameters parameters) {
            super(name);
            this.parameters = parameters;
        }

        @Override
        public int getMethod() {
            return 2;
        }
    }

    @NullMarked
    @Getter
    @Setter
    public static class Add extends TeamPacket implements WithMembers {
        protected Set<String> members;
        public Add(String name, Set<String> members) {
            super(name);
            this.members = members;
        }

        @Override
        public int getMethod() {
            return 3;
        }
    }

    @NullMarked
    @Getter
    @Setter
    public static class Remove extends TeamPacket implements WithMembers {
        protected Set<String> members;
        public Remove(String name, Set<String> members) {
            super(name);
            this.members = members;
        }

        @Override
        public int getMethod() {
            return 4;
        }
    }

}
