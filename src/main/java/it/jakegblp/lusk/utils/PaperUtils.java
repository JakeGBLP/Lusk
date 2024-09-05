package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import io.papermc.paper.event.block.PlayerShearBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;

public class PaperUtils {
    public static final boolean
            HAS_PLAYER_ARM_SWING_EVENT = Skript.classExists("io.papermc.paper.event.player.PlayerArmSwingEvent"),
            HAS_PLAYER_ARM_SWING_EVENT_HAND = HAS_PLAYER_ARM_SWING_EVENT && Skript.methodExists(PlayerArmSwingEvent.class, "getHand"),
            HAS_ENTITY_LOAD_CROSSBOW_EVENT = Skript.classExists("io.papermc.paper.event.entity.EntityLoadCrossbowEvent"),
            HAS_ENTITY_LOAD_CROSSBOW_EVENT_HAND = HAS_ENTITY_LOAD_CROSSBOW_EVENT && Skript.methodExists(EntityLoadCrossbowEvent.class, "getHand"),
            HAS_PLAYER_SHEAR_BLOCK_EVENT = Skript.classExists("io.papermc.paper.event.block.PlayerShearBlockEvent"),
            HAS_PLAYER_SHEAR_BLOCK_EVENT_HAND = HAS_PLAYER_SHEAR_BLOCK_EVENT && Skript.methodExists(PlayerShearBlockEvent.class, "getHand"),
            HAS_PLAYER_ELYTRA_BOOST_EVENT = Skript.classExists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"),
            HAS_PLAYER_ELYTRA_BOOST_EVENT_HAND = HAS_PLAYER_ELYTRA_BOOST_EVENT && Skript.methodExists(PlayerElytraBoostEvent.class, "getHand"),
            HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT = Skript.classExists("com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent"),
            HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT_HAND = HAS_PLAYER_USE_UNKNOWN_ENTITY_EVENT && Skript.methodExists(PlayerUseUnknownEntityEvent.class, "getHand"),
            HAS_PLAYER_JUMP_EVENT = Skript.classExists("com.destroystokyo.paper.event.player.PlayerJumpEvent"),
            HAS_ENTITY_JUMP_EVENT = Skript.classExists("com.destroystokyo.paper.event.entity.EntityJumpEvent"),
            HAS_PAPER_REGISTRY_ACCESS = Skript.classExists("io.papermc.paper.registry.RegistryAccess") && Skript.methodExists(RegistryAccess.class, "registryAccess"),
            HAS_PAPER_REGISTRY_KEY = HAS_PAPER_REGISTRY_ACCESS && Skript.classExists("io.papermc.paper.registry.RegistryKey") && Skript.methodExists(RegistryAccess.class, "getRegistry", RegistryKey.class);

    /**
     * Check if a PaperMC registry exists
     *
     * @param registry Registry to check for (Fully qualified name of registry)
     * @return True if registry exists else false
     */
    public static boolean registryExists(String registry) {
        return HAS_PAPER_REGISTRY_KEY && Skript.fieldExists(RegistryKey.class, registry);
    }

}
