package it.jakegblp.lusk.nms.core.world.entity.flags.player;

import com.destroystokyo.paper.SkinParts;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.Player;

import java.util.Collection;


public class PlayerFlags extends FlagByte<PlayerFlag, Player, Boolean> implements SkinParts {

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

    @Override
    public boolean hasCapeEnabled() {
        return get(PlayerFlag.CAPE);
    }

    public void setCapeEnabled(boolean enabled) {
        set(PlayerFlag.CAPE, enabled);
    }

    @Override
    public boolean hasJacketEnabled() {
        return get(PlayerFlag.JACKET);
    }

    public void setJacketEnabled(boolean enabled) {
        set(PlayerFlag.JACKET, enabled);
    }

    @Override
    public boolean hasLeftSleeveEnabled() {
        return get(PlayerFlag.LEFT_SLEEVE);
    }

    public void setLeftSleeveEnabled(boolean enabled) {
        set(PlayerFlag.LEFT_SLEEVE, enabled);
    }

    @Override
    public boolean hasRightSleeveEnabled() {
        return get(PlayerFlag.RIGHT_SLEEVE);
    }

    public void setRightSleeveEnabled(boolean enabled) {
        set(PlayerFlag.RIGHT_SLEEVE, enabled);
    }

    @Override
    public boolean hasLeftPantsEnabled() {
        return get(PlayerFlag.LEFT_PANTS);
    }

    public void setLeftPantsEnabled(boolean enabled) {
        set(PlayerFlag.LEFT_PANTS, enabled);
    }

    @Override
    public boolean hasRightPantsEnabled() {
        return get(PlayerFlag.RIGHT_PANTS);
    }

    public void setRightPantsEnabled(boolean enabled) {
        set(PlayerFlag.RIGHT_PANTS, enabled);
    }

    @Override
    public boolean hasHatsEnabled() {
        return get(PlayerFlag.HAT);
    }

    public void setHatsEnabled(boolean enabled) {
        set(PlayerFlag.HAT, enabled);
    }

    public boolean hasUnused() {
        return get(PlayerFlag.UNUSED);
    }

    public void setUnused(boolean enabled) {
        set(PlayerFlag.UNUSED, enabled);
    }

    @Override
    public int getRaw() {
        return flags;
    }
}