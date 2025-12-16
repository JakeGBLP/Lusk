package it.jakegblp.lusk.nms.core.world.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.async.Asyncable;
import it.jakegblp.lusk.nms.core.async.AsyncablesWrapper;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class PlayerInfo implements AsyncablesWrapper {
    @Setter
    protected @NotNull UUID UUID;
    protected CompletablePlayerProfile playerProfile;
    protected boolean listed;
    protected int latency;
    protected GameMode gameMode;
    protected Component displayName;
    protected boolean showHat;
    protected int listOrder;
    protected ChatSessionData chatSession;
    protected @NotNull Set<PlayerInfoUpdatePacket.@NotNull Action<?>> actions;

    public PlayerInfo(@NotNull UUID uuid, @Nullable CompletablePlayerProfile completablePlayerProfile, boolean listed, int latency, @Nullable GameMode gameMode, @Nullable Component displayName, boolean showHat, int listOrder, @Nullable ChatSessionData chatSession) {
        this.UUID = uuid;
        this.playerProfile = completablePlayerProfile;
        this.listed = listed;
        this.latency = latency;
        this.gameMode = gameMode;
        this.displayName = displayName;
        this.showHat = showHat;
        this.listOrder = listOrder;
        this.chatSession = chatSession;
        this.actions = CommonUtils.toSet(PlayerInfoUpdatePacket.Action.values());
    }

    public PlayerInfo(@NotNull UUID uuid, @Nullable PlayerProfile playerProfile, boolean listed, int latency, @Nullable GameMode gameMode, @Nullable Component displayName, boolean showHat, int listOrder, @Nullable ChatSessionData chatSession) {
        this(uuid, playerProfile != null ? new CompletablePlayerProfile(playerProfile) : null, listed, latency, gameMode, displayName, showHat, listOrder, chatSession);
    }

    public PlayerInfo(@NotNull Player player) {
        this(player.getUniqueId());
    }

    public PlayerInfo(@NotNull UUID uuid) {
        this.UUID = uuid;
        this.actions = new HashSet<>();
    }

    public void setActions(@NotNull Set<PlayerInfoUpdatePacket.@NotNull Action<?>> actions) {
        this.actions = actions;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
    }

    public void setPlayerProfile(@Nullable PlayerProfile playerProfile) {
        setPlayerProfile(playerProfile != null ? new CompletablePlayerProfile(playerProfile) : null);
    }

    public void setPlayerProfile(@Nullable CompletablePlayerProfile completablePlayerProfile) {
        this.playerProfile = completablePlayerProfile;
        this.actions.add(PlayerInfoUpdatePacket.Action.ADD_PLAYER);
    }

    public void setChatSession(@Nullable ChatSessionData chatSession) {
        this.chatSession = chatSession;
        this.actions.add(PlayerInfoUpdatePacket.Action.INITIALIZE_CHAT);
    }

    public void setGameMode(@Nullable GameMode gameMode) {
        this.gameMode = gameMode;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE);
    }

    public void setLatency(int latency) {
        this.latency = latency;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_LATENCY);
    }

    public void setListed(boolean listed) {
        this.listed = listed;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_LISTED);
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER);
    }

    public void setShowHat(boolean showHat) {
        this.showHat = showHat;
        this.actions.add(PlayerInfoUpdatePacket.Action.UPDATE_HAT);
    }

    public void removePlayerProfile() {
        this.playerProfile = null;
        this.actions.remove(PlayerInfoUpdatePacket.Action.ADD_PLAYER);
    }

    public void removeChatSession() {
        this.chatSession = null;
        this.actions.remove(PlayerInfoUpdatePacket.Action.INITIALIZE_CHAT);
    }

    public void removeGameMode() {
        this.gameMode = null;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE);
    }

    public void removeLatency() {
        this.latency = 0;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_LATENCY);
    }

    public void removeListed() {
        this.listed = false;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_LISTED);
    }

    public void removeListOrder() {
        this.listOrder = 0;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER);
    }

    public void removeShowHat() {
        this.showHat = false;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_HAT);
    }

    public void removeDisplayName() {
        this.displayName = null;
        this.actions.remove(PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
    }


    public <T> void set(PlayerInfoUpdatePacket.Action<T> action, T value) {
        action.validate();
        if (action == PlayerInfoUpdatePacket.Action.ADD_PLAYER)
            setPlayerProfile((CompletablePlayerProfile) value);
        else if (action == PlayerInfoUpdatePacket.Action.INITIALIZE_CHAT)
            setChatSession((ChatSessionData) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE)
            setGameMode((GameMode) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LISTED)
            setListed((boolean) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LATENCY)
            setLatency((int) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME)
            setDisplayName((Component) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER)
            setListOrder((int) value);
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_HAT)
            setShowHat((boolean) value);
        else
            throw new IllegalStateException("An unknown/unsupported action has been provided");
    }

    public <T> void remove(PlayerInfoUpdatePacket.Action<T> action) {
        action.validate();
        if (action == PlayerInfoUpdatePacket.Action.ADD_PLAYER)
            removePlayerProfile();
        else if (action == PlayerInfoUpdatePacket.Action.INITIALIZE_CHAT)
            removeChatSession();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE) {
            removeGameMode();
        } else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LISTED) {
            removeListed();
        } else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LATENCY) {
            removeLatency();
        } else if (action == PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME)
            removeDisplayName();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER) {
            removeListOrder();
        } else if (action == PlayerInfoUpdatePacket.Action.UPDATE_HAT) {
            removeShowHat();
        } else
            throw new IllegalStateException("An unknown/unsupported action has been provided");

    }

    @SuppressWarnings("unchecked")
    public <T> T get(PlayerInfoUpdatePacket.Action<T> action) {
        action.validate();
        Object value;
        if (action == PlayerInfoUpdatePacket.Action.ADD_PLAYER)
            value = getPlayerProfile();
        else if (action == PlayerInfoUpdatePacket.Action.INITIALIZE_CHAT)
            value = getChatSession();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE)
            value = getGameMode();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LISTED)
            value = isListed();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LATENCY)
            value = getLatency();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME)
            value = getDisplayName();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER)
            value = getListOrder();
        else if (action == PlayerInfoUpdatePacket.Action.UPDATE_HAT)
            value = isShowHat();
        else
            throw new IllegalStateException("An unknown/unsupported action has been provided");
        return (T) value;
    }


    @Override
    public @NotNull List<? extends Asyncable> getAsyncables() {
        return List.of(playerProfile);
    }
}