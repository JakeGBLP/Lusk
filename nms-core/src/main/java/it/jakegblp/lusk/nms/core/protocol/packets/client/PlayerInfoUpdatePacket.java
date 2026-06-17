package it.jakegblp.lusk.nms.core.protocol.packets.client;

import it.jakegblp.lusk.common.*;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.async.AsyncablesWrapper;
import it.jakegblp.lusk.nms.core.event.client.PlayerInfoUpdatePacketEvent;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializer;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.nms.core.world.player.chat.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.profile.CompletablePlayerProfile;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@Setter
@NullMarked
public class PlayerInfoUpdatePacket implements BufferSerializableClientboundPacket<PlayerInfoUpdatePacketEvent>, AsyncablesWrapper {
    protected List<PlayerInfo> playerInfos;
    @SuppressWarnings("rawtypes")
    protected PseudoEnumSet<Action> actions;

    public PlayerInfoUpdatePacket(SimpleByteBuf buffer) {
        read(buffer);
    }

    public PlayerInfoUpdatePacket() {
        playerInfos = new ArrayList<>();
        actions = PseudoEnumSet.noneOf(Action.class);
    }

    @Override
    public List<? extends Asyncable> getAsyncables() {
        return playerInfos;
    }

    @SuppressWarnings("rawtypes")
    public <S extends Set<Action>> PlayerInfoUpdatePacket(S actions, List<PlayerInfo> playerInfos) {
        this.actions = PseudoEnumSet.of(actions);
        this.playerInfos = new ArrayList<>(playerInfos);
    }

    @Nullable
    public PlayerInfo getPlayerInfoFromUUID(UUID uuid) {
        for (PlayerInfo playerInfo : this.playerInfos)
            if (playerInfo.getUuid().equals(uuid))
                return playerInfo;
        return null;
    }

    public <T> void setAction(UUID uuid, Action<T> action, T value) {
        for (PlayerInfo playerInfo : this.playerInfos)
            if (playerInfo.getUuid().equals(uuid))
                playerInfo.set(action, value);
    }

    @Nullable
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
    public void write(SimpleByteBuf buffer) {
        buffer.writePseudoEnumSet(actions, Action.values());
        buffer.writeCollection(playerInfos, (simpleByteBuf, playerInfo) -> playerInfo.write(simpleByteBuf, actions));
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        actions = buffer.readPseudoEnumSet(Action.class);
        playerInfos = buffer.readList(simpleByteBuf -> new PlayerInfo(simpleByteBuf, actions));
    }

    @Override
    public PlayerInfoUpdatePacketEvent createEvent(Player player, boolean async) {
        return new PlayerInfoUpdatePacketEvent(this, player, async);
    }

    @Override
    public PlayerInfoUpdatePacket copy() {
        return new PlayerInfoUpdatePacket(actions.copy(), CommonUtils.map(playerInfos, PlayerInfo::copy));
    }

    @Getter
    @ApiStatus.NonExtendable
    public static abstract class Action<T> extends PseudoEnum implements Validatable<IllegalStateException>, BufferSerializer<PlayerInfo> {

        public static final Action<CompletablePlayerProfile> ADD_PLAYER = new Action<>("ADD_PLAYER", "PROFILE", CompletablePlayerProfile.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                var profile = value.getPlayerProfile();
                buffer.writeString(Objects.requireNonNull(profile.getName()), 16);
                buffer.writePlayerProfileProperties(profile.getProperties());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setPlayerProfile(new CompletablePlayerProfile(value.getUuid(), buffer.readString(16), buffer.readPlayerProfileProperties()));
            }
        };
        public static final Action<ChatSessionData> INITIALIZE_CHAT = new Action<>("INITIALIZE_CHAT", "CHAT_SESSION_DATA", ChatSessionData.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                var chatSessionData = value.getChatSession();
                if (chatSessionData != null && chatSessionData.getProfilePublicKey().hasExpired())
                    chatSessionData = null;
                buffer.writeNullable(chatSessionData);
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setChatSession(buffer.readNullable(SimpleByteBuf::readChatSessionData));
            }
        };
        public static final Action<GameMode> UPDATE_GAME_MODE = new Action<>("UPDATE_GAME_MODE", "GAMEMODE", GameMode.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeEnum(value.getGameMode());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setGameMode(buffer.readEnum(GameMode.class));
            }
        };
        public static final Action<Boolean> UPDATE_LISTED = new Action<>("UPDATE_LISTED", "LISTED", Boolean.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeBoolean(value.isListed());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setListed(buffer.readBoolean());
            }
        };
        public static final Action<Integer> UPDATE_LATENCY = new Action<>("UPDATE_LATENCY", "PING", Integer.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeVarInt(value.getLatency());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setLatency(buffer.readVarInt());
            }
        };
        public static final Action<Component> UPDATE_DISPLAY_NAME = new Action<>("UPDATE_DISPLAY_NAME", "DISPLAY_NAME", Component.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeNullable(value.getDisplayName(), SimpleByteBuf::writeComponent);
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setDisplayName(buffer.readNullable(SimpleByteBuf::readComponent));
            }
        };

        public static final @Availability(addedIn = "1.21.4") Action<Integer> UPDATE_LIST_ORDER = new Action<>("UPDATE_LIST_ORDER", "TAB_LIST_ORDER", Integer.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeVarInt(value.getListOrder());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setListOrder(buffer.readVarInt());
            }
        };
        public static final @Availability(addedIn = "1.21.4") Action<Boolean> UPDATE_HAT = new Action<>("UPDATE_HAT", "SHOW_HAT", Boolean.class) {
            @Override
            public void write(SimpleByteBuf buffer, PlayerInfo value) {
                buffer.writeBoolean(value.isShowHat());
            }

            @Override
            public void read(SimpleByteBuf buffer, PlayerInfo value) {
                value.setShowHat(buffer.readBoolean());
            }
        };

        @SuppressWarnings("unchecked")
        private static final Action<Object>[] VALUES = (Action<Object>[])values(Action.class);

        @Nullable
        public static Action<?> fromPropertyName(String propertyName) {
            return CommonUtils.findFirst(values(), action -> action.propertyName.equals(propertyName));
        }

        public static Action<?> valueOf(String name) {
            return (Action<?>) valueOf(Action.class, name);
        }

        public static Action<Object>[] values() {
            return VALUES;
        }

        private final Class<T> type;
        private final String propertyName;

        // todo: move property name to the addon module?
        Action(String name, String propertyName, Class<T> type) {
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

    }

}