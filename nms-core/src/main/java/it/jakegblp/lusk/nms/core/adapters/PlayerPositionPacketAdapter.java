package it.jakegblp.lusk.nms.core.adapters;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerPositionPacket;
import it.jakegblp.lusk.nms.core.world.entity.flags.entity.RelativeFlag;

import java.util.HashSet;
import java.util.Set;

public interface PlayerPositionPacketAdapter<
        NMSRelativeFlag extends Enum<NMSRelativeFlag>,
        NMSPlayerPositionPacket
        > {

    default NMSRelativeFlag asNMSRelativeFlag(RelativeFlag relativeFlag) {
        return NMSRelativeFlag.valueOf(getNMSRelativeFlagClass(), relativeFlag.name());
    }

    default RelativeFlag fromNMSRelativeFlag(NMSRelativeFlag nmsMovementFlag) {
        return RelativeFlag.valueOf(nmsMovementFlag.name());
    }

    @SuppressWarnings("unchecked")
    default Set<NMSRelativeFlag> asNMSRelativeFlags(Set<RelativeFlag> relativeFlags) {
        return (Set<NMSRelativeFlag>) new HashSet<>(CommonUtils.map(relativeFlags, RelativeFlag::asNMS));
    }

    default Set<RelativeFlag> fromNMSRelativeFlag(Set<NMSRelativeFlag> nmsRelativeFlags) {
        return new HashSet<>(CommonUtils.map(nmsRelativeFlags, this::fromNMSRelativeFlag));
    }

    Class<NMSRelativeFlag> getNMSRelativeFlagClass();

    default boolean isNMSRelativeFlag(Object object) {
        return getNMSRelativeFlagClass().isInstance(object);
    }

    NMSPlayerPositionPacket asNMSPlayerPositionPacket(PlayerPositionPacket packet);

    PlayerPositionPacket fromNMSPlayerPositionPacket(NMSPlayerPositionPacket packet);

    Class<NMSPlayerPositionPacket> getNMSPlayerPositionPacketClass();

    default boolean isNMSPlayerPositionPacket(Object object) {
        return getNMSPlayerPositionPacketClass().isInstance(object);
    }

}
