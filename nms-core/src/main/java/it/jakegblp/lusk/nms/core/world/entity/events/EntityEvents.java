package it.jakegblp.lusk.nms.core.world.entity.events;

import lombok.Getter;

@Getter
public enum EntityEvents {

    // ---------- GENERIC ENTITY ----------
    ARROW_TIPPED_PARTICLES(0, Scope.ARROW),
    RABBIT_JUMP(1, Scope.RABBIT),
    LIVING_HURT(2, Scope.LIVING),
    LIVING_DEATH(3, Scope.LIVING),
    IRON_GOLEM_ATTACK(4, Scope.IRON_GOLEM),
    TAME_FAIL_SMOKE(6, Scope.TAMEABLE),
    TAME_SUCCESS_HEARTS(7, Scope.TAMEABLE),
    WOLF_SHAKE_START(8, Scope.WOLF),
    PLAYER_FINISH_ITEM_USE(9, Scope.PLAYER),
    SHEEP_EAT_GRASS(10, Scope.SHEEP),
    IRON_GOLEM_SHOW_POPPY(11, Scope.IRON_GOLEM),
    VILLAGER_MATING_HEARTS(12, Scope.VILLAGER),
    VILLAGER_ANGRY(13, Scope.VILLAGER),
    VILLAGER_HAPPY(14, Scope.VILLAGER),
    WITCH_MAGIC_PARTICLES(15, Scope.WITCH),
    ZOMBIE_VILLAGER_CURE_SOUND(16, Scope.ZOMBIE_VILLAGER),
    FIREWORK_EXPLODE(17, Scope.FIREWORK),
    LOVE_MODE_HEARTS(18, Scope.ANIMAL),
    SQUID_RESET_ROTATION(19, Scope.SQUID),
    MOB_SPAWNER_EXPLOSION(20, Scope.MOB),
    GUARDIAN_ATTACK_SOUND(21, Scope.GUARDIAN),
    PLAYER_DEBUG_REDUCED_ENABLE(22, Scope.PLAYER),
    PLAYER_DEBUG_REDUCED_DISABLE(23, Scope.PLAYER),
    PLAYER_OP_LEVEL_0(24, Scope.PLAYER),
    PLAYER_OP_LEVEL_1(25, Scope.PLAYER),
    PLAYER_OP_LEVEL_2(26, Scope.PLAYER),
    PLAYER_OP_LEVEL_3(27, Scope.PLAYER),
    PLAYER_OP_LEVEL_4(28, Scope.PLAYER),
    SHIELD_BLOCK_SOUND(29, Scope.LIVING),
    SHIELD_BREAK_SOUND(30, Scope.LIVING),
    FISHING_HOOK_PULL_PLAYER(31, Scope.FISHING_HOOK),
    ARMOR_STAND_HIT(32, Scope.ARMOR_STAND),
    IRON_GOLEM_HIDE_POPPY(34, Scope.IRON_GOLEM),
    TOTEM_OF_UNDYING(35, Scope.LIVING),
    DOLPHIN_HAPPY_PARTICLES(38, Scope.DOLPHIN),
    RAVAGER_STUNNED(39, Scope.RAVAGER),
    OCELOT_TAME_FAIL_SMOKE(40, Scope.OCELOT),
    OCELOT_TAME_SUCCESS_HEARTS(41, Scope.OCELOT),
    RAID_SPLASH_PARTICLES(42, Scope.VILLAGER),
    BAD_OMEN_TRIGGER_PARTICLES(43, Scope.PLAYER),
    FOX_CHEW_PARTICLES(45, Scope.FOX),
    PORTAL_TELEPORT_PARTICLES(46, Scope.LIVING),
    ITEM_BREAK_MAIN_HAND(47, Scope.LIVING),
    ITEM_BREAK_OFF_HAND(48, Scope.LIVING),
    ITEM_BREAK_HEAD(49, Scope.LIVING),
    ITEM_BREAK_CHEST(50, Scope.LIVING),
    ITEM_BREAK_LEGS(51, Scope.LIVING),
    ITEM_BREAK_FEET(52, Scope.LIVING),
    HONEY_BLOCK_SLIDE(53, Scope.ENTITY),
    HONEY_BLOCK_FALL(54, Scope.LIVING),
    SWAP_HAND_ITEMS(55, Scope.LIVING),
    WOLF_SHAKE_STOP(56, Scope.WOLF),
    GOAT_RAM_PREPARE(58, Scope.GOAT),
    GOAT_RAM_STOP(59, Scope.GOAT),
    DEATH_SMOKE_PARTICLES(60, Scope.LIVING),

    // ---------- MODERN (1.19+) ----------
    WARDEN_TENDRIL_SHAKE(61, Scope.WARDEN),
    WARDEN_SONIC_BOOM_ANIMATION(62, Scope.WARDEN),
    SNIFFER_DIG_SOUND(63, Scope.SNIFFER);

    private final int id;
    private final Scope scope;

    EntityEvents(int id, Scope scope) {
        this.id = id;
        this.scope = scope;
    }

    public static EntityEvents fromId(int id) {
        for (EntityEvents value : values()) {
            if (value.id == id) return value;
        }
        return null;
    }

    public enum Scope {
        ENTITY,
        LIVING,
        PLAYER,
        MOB,
        ANIMAL,
        TAMEABLE,
        WOLF,
        OCELOT,
        FOX,
        SHEEP,
        RABBIT,
        SQUID,
        DOLPHIN,
        GOAT,
        VILLAGER,
        IRON_GOLEM,
        WITCH,
        GUARDIAN,
        RAVAGER,
        ZOMBIE_VILLAGER,
        ARMOR_STAND,
        FISHING_HOOK,
        FIREWORK,
        ARROW,
        WARDEN,
        SNIFFER //todo ensure lower versions dont use this
    }
}
