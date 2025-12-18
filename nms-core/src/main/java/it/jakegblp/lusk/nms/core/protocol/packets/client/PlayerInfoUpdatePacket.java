package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.PseudoEnum;
import it.jakegblp.lusk.common.Validatable;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.async.AsyncablesWrapper;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
public class PlayerInfoUpdatePacket implements ClientboundPacket, AsyncablesWrapper {
    protected List<PlayerInfo> playerInfos;
    protected Set<Action<?>> actions;

    @Override
    public @NotNull List<? extends Asyncable> getAsyncables() {
        return playerInfos;
    }

    public PlayerInfoUpdatePacket() {
        this(new HashSet<>(), new ArrayList<>());
    }

    public PlayerInfoUpdatePacket(Set<Action<?>> actions, List<PlayerInfo> playerInfos) {
        this.actions = new HashSet<>(actions);
        this.playerInfos = new ArrayList<>(playerInfos);
    }

    public PlayerInfo getPlayerInfoFromUUID(UUID uuid) {
        for (PlayerInfo playerInfo : this.playerInfos)
            if (playerInfo.getUUID().equals(uuid))
                return playerInfo;
        return null;
    }

    public <T> void setAction(UUID uuid, Action<T> action, T value) {
        for (PlayerInfo playerInfo : this.playerInfos)
            if (playerInfo.getUUID().equals(uuid))
                playerInfo.set(action, value);
    }

    public <T> T getAction(UUID uuid, Action<T> action) {
        PlayerInfo info = getPlayerInfoFromUUID(uuid);
        return info != null ? info.get(action) : null;
    }

    public <T> boolean hasAction(Action<T> action) {
        return actions.contains(action);
    }

    public <T> void setActionForAll(Action<T> action, T value) {
        for (PlayerInfo playerInfo : this.playerInfos)
            playerInfo.set(action, value);
    }

    public <T> void removeActionForAll(Action<T> action) {
        for (PlayerInfo playerInfo : this.playerInfos)
            playerInfo.remove(action);
    }

    @Override
    public Object asNMS() {
        return NMS.toNMSPlayerInfoUpdatePacket(this);
    }

    @Getter
    public static final class Action<T> extends PseudoEnum implements Validatable<IllegalStateException>, NMSObject<Object> {

        // todo: move property names to the addon module
        public static final Action<CompletablePlayerProfile> ADD_PLAYER = new Action<>("ADD_PLAYER", "PROFILE", CompletablePlayerProfile.class);
        public static final Action<ChatSessionData> INITIALIZE_CHAT = new Action<>("INITIALIZE_CHAT", "CHAT_SESSION_DATA", ChatSessionData.class);
        public static final Action<GameMode> UPDATE_GAME_MODE = new Action<>("UPDATE_GAME_MODE", "GAMEMODE", GameMode.class);
        public static final Action<Boolean> UPDATE_LISTED = new Action<>("UPDATE_LISTED", "LISTED", Boolean.class);
        public static final Action<Integer> UPDATE_LATENCY = new Action<>("UPDATE_LATENCY", "PING", Integer.class);
        public static final Action<Component> UPDATE_DISPLAY_NAME = new Action<>("UPDATE_DISPLAY_NAME", "DISPLAY_NAME", Component.class);
        public static final @Availability(addedIn = "1.21.4") Action<Integer> UPDATE_LIST_ORDER = new Action<>("UPDATE_LIST_ORDER", "TAB_LIST_ORDER", Integer.class);
        public static final @Availability(addedIn = "1.21.4") Action<Boolean> UPDATE_HAT = new Action<>("UPDATE_HAT", "SHOW_HAT", Boolean.class);

        private static final Action<?>[] VALUES = (Action<?>[])values(Action.class);

        public static Action<?> fromPropertyName(String propertyName) {
            return CommonUtils.findFirst(values(), action -> action.propertyName.equals(propertyName));
        }

        @SuppressWarnings("unchecked")
        public static Action<?> valueOf(String name) {
            return (Action<?>) valueOf(Action.class, name);
        }

        public static @NotNull Action<?> @NotNull [] values() {
            return VALUES;
        }
        @NotNull
        private final Class<T> type;
        @NotNull
        private final String propertyName;

        Action(@NotNull String name, @NotNull String propertyName, @NotNull Class<T> type) {
            super(name);
            this.type = type;
            this.propertyName = propertyName;
        }

        @Override
        public boolean check() {
            return NMS.getVersion().isGreaterOrEqual(Version.of(1,21,4)) || (this != UPDATE_LIST_ORDER && this != UPDATE_HAT);
        }

        @Override
        public Supplier<IllegalStateException> getExceptionSupplier() {
            return () -> new IllegalStateException("This action was added in 1.21.4");
        }

        @Override
        public Object asNMS() {
            return NMS.toNMSPlayerInfoUpdatePacketAction(this);
        }
    }


}