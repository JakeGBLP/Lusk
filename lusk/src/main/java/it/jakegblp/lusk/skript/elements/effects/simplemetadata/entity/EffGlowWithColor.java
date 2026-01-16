package it.jakegblp.lusk.skript.elements.effects.simplemetadata.entity;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import it.jakegblp.lusk.nms.core.world.player.GlowMap;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Name("[NMS] Player - Fake Glow With Color")
@Description("""
        Makes a real entity or player glow.
        Player's will continue to glow until they log off unless turned off.
        
        Note: Using a color on a player will override any teams they are in, however it will copy the team options onto the new team.
        """)
@Examples({"""
        make target entity glow red for all players
        make {_targetPlayer} to glow red for player
        """
})
@Keywords({
        "packets", "packet", "protocol", "dispatch", "sync", "async", "glow"
})
@Since("2.0.0")
public class EffGlowWithColor extends Effect {

    static {
        Skript.registerEffect(EffGlowWithColor.class,
                "(make|fake) [entity] %entities%['s] glow [[color] %-color%] for %players%",
                "(make|fake) [entity] %entities%['s] not glow for %players%"
        );
    }

    private Expression<Entity> entityExpression;
    private Expression<Player> playerExpression;
    private Expression<Color> colorExpression;
    private int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) expressions[0];
        if (pattern == 0) {
            colorExpression = (Expression<Color>) expressions[1];
            playerExpression = (Expression<Player>) expressions[2];
        } else
            playerExpression = (Expression<Player>) expressions[1];
        this.pattern = pattern;
        return true;
    }

    private static final EntityMetadata packetDataTrue = EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.GLOWING, true));
    private static final EntityMetadata packetDataFalse = EntityMetadata.of(Map.of(MetadataKeys.EntityKeys.GLOWING, false));

    private static final Set<TeamPacket> deletionTeams = new HashSet<>();

    static {
        for (Field field : org.bukkit.Color.class.getFields()) {
            deletionTeams.add(TeamPacket.delete("Lusk-" + field.getName()));
        }
    }


    @SuppressWarnings("DataFlowIssue")
    @Override
    protected void execute(Event event) {
        EntityMetadata packetData = pattern == 0 ? packetDataTrue : packetDataFalse;

        Set<String> members = new HashSet<>();
        Set<Integer> entityIds = new HashSet<>();
        for (Entity entity : entityExpression.getArray(event)) {
            if (entity instanceof Player player) {
                members.add(player.getName());
                entityIds.add(player.getEntityId());
            } else
                members.add(entity.getUniqueId().toString());
        }

        final Player[] players = playerExpression.getArray(event);
        if (colorExpression != null && pattern == 0) {
            final NamedTextColor color = AddonUtils.toNamedTextColor(colorExpression.getSingle(event));
            final @NotNull Scoreboard scoreboardManager = Bukkit.getScoreboardManager().getMainScoreboard();
            for (Player player : players) {
                GlowMap.addGlow(player, entityIds);

                final Team playerTeam = scoreboardManager.getPlayerTeam(player);
                TeamParameters parameters;
                if (playerTeam != null) {
                    parameters = new TeamParameters(playerTeam.displayName(),
                            playerTeam.prefix(),
                            playerTeam.suffix(),
                            playerTeam.getOption(Team.Option.NAME_TAG_VISIBILITY),
                            playerTeam.getOption(Team.Option.COLLISION_RULE),
                            NamedTextColor.nearestTo(playerTeam.color()),
                            playerTeam.allowFriendlyFire(),
                            playerTeam.canSeeFriendlyInvisibles()
                    );
                    playerTeam.removePlayer(player); // stops client crashes on bungeecord (should work :D)
                } else {
                    parameters = new TeamParameters(Component.text("Lusk-" + color.toString()), Component.text(""), Component.text(""), Team.OptionStatus.ALWAYS, Team.OptionStatus.ALWAYS, color, true, false);
                }
                final TeamPacket[] create = new TeamPacket[]{TeamPacket.create("Lusk-" + color.toString(), parameters, members)};
                NMSApi.sendPackets(player, create, ExecutionMode.ASYNCHRONOUS);
            }
        } else if (pattern == 1) {
            NMSApi.sendPackets(players, deletionTeams.toArray(ClientboundPacket[]::new), ExecutionMode.ASYNCHRONOUS);
            for (Player player : players)
                GlowMap.removeGlow(player, entityIds);

        }
        AddonUtils.sendEasyMetadata(players, packetData, entityExpression.getArray(event)); // send glow or not glow
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "client glow with color";
    }


}
