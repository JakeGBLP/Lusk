package it.jakegblp.lusk.skript.modules.base.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.Classes;
import io.papermc.paper.event.player.PlayerLecternPageChangeEvent;
import io.papermc.paper.event.player.PlayerLoomPatternSelectEvent;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EvtLoomPatternSelect extends SkriptEvent {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.event(EvtLoomPatternSelect.class, PlayerLoomPatternSelectEvent.class)
                .patterns("[loom] pattern select [of %-bannerpatterntypes%]")
                .name("Pattern Select")
                .description("Called when a player selects a banner pattern in a loom inventory.")
                .since("1.0.0")
                .keywords("loom", "banner", "pattern")
                .examples("""
                        on pattern select:
                          broadcast event-patterntype""")
                .register(syntaxRegistry);
        Registrations.registerEventValue(PlayerLoomPatternSelectEvent.class, PatternType.class, PlayerLoomPatternSelectEvent::getPatternType, PlayerLoomPatternSelectEvent::setPatternType);
        Registrations.registerEventValue(PlayerLoomPatternSelectEvent.class, Inventory.class, PlayerLoomPatternSelectEvent::getLoomInventory);
        Registrations.registerEventValue(PlayerLecternPageChangeEvent.class, Block.class, e -> e.getLectern().getBlock());
    }

    @Nullable
    private PatternType[] types;

    @Override
    public boolean init(Literal<?> @NotNull [] args, int matchedPattern, @NotNull SkriptParser.ParseResult parseResult) {
        if (args.length > 0)
            types = args[0] == null ? null : (PatternType[]) (args[0]).getArray();
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (types == null) return true;
        PatternType patternType = ((PlayerLoomPatternSelectEvent) e).getPatternType();
        for (final PatternType type : types)
            if (type == patternType) return true;
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "loom pattern select" + (types == null ? "" : " of " + Classes.toString(types));
    }
}