package it.jakegblp.lusk.nms.core.world.entity.flags.player;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Player;


public class PlayerFlags extends FlagByte<PlayerFlag, Player, Boolean> {

    public PlayerFlags() {
        super();
    }

    public PlayerFlags(byte flags) {
        super(flags);
    }

    @Override
    public Class<PlayerFlag> getBitFlagClass() {
        return PlayerFlag.class;
    }

    @Override
    public Class<Player> getFlagHolderClass() {
        return Player.class;
    }

    @Override
    public PlayerFlags clone() {
        return new PlayerFlags(flags);
    }

    public boolean hasCape() {
        return get(PlayerFlag.CAPE);
    }

    public void setCape(boolean enabled) {
        set(PlayerFlag.CAPE, enabled);
    }

    public boolean hasJacket() {
        return get(PlayerFlag.JACKET);
    }

    public void setJacket(boolean enabled) {
        set(PlayerFlag.JACKET, enabled);
    }

    public boolean hasLeftSleeve() {
        return get(PlayerFlag.LEFT_SLEEVE);
    }

    public void setLeftSleeve(boolean enabled) {
        set(PlayerFlag.LEFT_SLEEVE, enabled);
    }

    public boolean hasRightSleeve() {
        return get(PlayerFlag.RIGHT_SLEEVE);
    }

    public void setRightSleeve(boolean enabled) {
        set(PlayerFlag.RIGHT_SLEEVE, enabled);
    }

    public boolean hasLeftPants() {
        return get(PlayerFlag.LEFT_PANTS);
    }

    public void setLeftPants(boolean enabled) {
        set(PlayerFlag.LEFT_PANTS, enabled);
    }

    public boolean hasRightPants() {
        return get(PlayerFlag.RIGHT_PANTS);
    }

    public void setRightPants(boolean enabled) {
        set(PlayerFlag.RIGHT_PANTS, enabled);
    }

    public boolean hasHat() {
        return get(PlayerFlag.HAT);
    }

    public void setHat(boolean enabled) {
        set(PlayerFlag.HAT, enabled);
    }

    public boolean hasUnused() {
        return get(PlayerFlag.UNUSED);
    }

    public void setUnused(boolean enabled) {
        set(PlayerFlag.UNUSED, enabled);
    }
}