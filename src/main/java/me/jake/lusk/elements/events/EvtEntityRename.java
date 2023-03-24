package me.jake.lusk.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
@SuppressWarnings("unused")
public class EvtEntityRename extends SkriptEvent {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerNameEntityEvent")) {
            Skript.registerEvent("Entity Rename", EvtEntityRename.class, PlayerNameEntityEvent.class, "entity rename [of %-entitydatas%]")
                    .description("This Event requires Paper.\n\nCalled when the player is attempting to rename a mob.")
                    .examples("""
                            on entity rename of pig:
                              broadcast entity's display name""")
                    .since("1.0.0");
        }
    }

    @Nullable
    private EntityData<?>[] types = new EntityData<?>[0];


    @Override
    public boolean init(Literal @NotNull [] args, int matchedPattern, @NotNull ParseResult parseResult) {
        if (args.length > 0) {
            types = args[0] == null ? null : (EntityData<?>[]) (args[0]).getAll();
        }
        return true;
    }

    @Override
    public boolean check(@NotNull Event e) {
        if (types == null) {
            return true;
        }
        final @NotNull EntityType entity = ((PlayerNameEntityEvent) e).getEntity().getType();
        for (final EntityData<?> type : types) {
            if (type != null) {
                if (entity == EntityUtils.toBukkitEntityType(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "entity rename" + (types == null ? "" : " " + Arrays.toString(types));
    }

}
