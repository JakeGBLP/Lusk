package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.api.entry.DynamicEntryData;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.*;

import static it.jakegblp.lusk.skript.elements.expressions.packets.properties.ExprPlayerInfoPacketEntry.PLAYER_INFO_PROPERTY_NAMES;

public class ExprSecPlayerInfo extends SectionExpression<PlayerInfo> implements AsyncableSyntaxesWrapper {

    public static final EntryValidator VALIDATOR;

    static {
        var builder = EntryValidator.builder().addEntryData(new ExpressionEntryData<>("player", null, false, UUID.class, OfflinePlayer.class));
        for (var entry : PLAYER_INFO_PROPERTY_NAMES.entrySet()) {
            var action = entry.getValue();
            if (action == PlayerInfoUpdatePacket.Action.ADD_PLAYER) {
                builder.addEntryData(new DynamicEntryData(entry.getKey(), action.getType(), ExprSecPlayerProfile.VALIDATOR, true));
                continue;
            }
            builder.addEntryData(new ExpressionEntryData<>(entry.getKey(), null, true, action.getType()));
        }
        VALIDATOR = builder.build();
        Skript.registerExpression(ExprSecPlayerInfo.class, PlayerInfo.class, ExpressionType.COMBINED,
                "[a] new player info");
    }

    private Expression<?> playerExpression;
    private Map<PlayerInfoUpdatePacket.Action<?>, Expression<?>> actionExpressionMap;

    @SuppressWarnings("unchecked")
    public <T> Expression<T> getActionExpression(PlayerInfoUpdatePacket.Action<T> action) {
        return (Expression<T>) actionExpressionMap.get(action);
    }

    @Unmodifiable
    public Set<PlayerInfoUpdatePacket.Action<?>> getActions() {
        return Set.copyOf(actionExpressionMap.keySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        playerExpression = (Expression<?>) container.getOptional("player", false);
        if (playerExpression == null) return false;
        for (var entry : PLAYER_INFO_PROPERTY_NAMES.entrySet()) {
            var key = entry.getKey();
            Object value = container.getOptional(key, false);
            var action = entry.getValue();
            if (actionExpressionMap == null) actionExpressionMap = new HashMap<>();
            if (action == PlayerInfoUpdatePacket.Action.ADD_PLAYER) {
                Expression<?> playerProfileExpression;
                if (value instanceof EntryContainer entryContainer) {
                    var playerProfileMultiExpression = new ExprSecPlayerProfile.PlayerProfileExpression(entryContainer);
                    if (playerProfileMultiExpression.isError()) {
                        return false;
                    }
                    playerProfileExpression = playerProfileMultiExpression;
                } else if (value instanceof Expression<?> expression)
                    playerProfileExpression = expression;
                else
                    continue;
                actionExpressionMap.put(action, playerProfileExpression);
            } else {
                Expression<Object> actionExpression = (Expression<Object>) container.getOptional(key, false);
                if (actionExpression != null) {
                    actionExpressionMap.put(action, actionExpression);
                }
            }
        }
        if (actionExpressionMap == null) {
            Skript.error("At least one property must be provided!");
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PlayerInfo @Nullable [] get(Event event) {
        Object playerOrUUID = playerExpression.getSingle(event);
        final UUID finalUUID;
        if (playerOrUUID instanceof OfflinePlayer offlinePlayer) finalUUID = offlinePlayer.getUniqueId();
        else if (playerOrUUID instanceof UUID uuid) finalUUID = uuid;
        else return new PlayerInfo[0];
        PlayerInfo playerInfo = new PlayerInfo(finalUUID);
        actionExpressionMap.forEach((key, expression) -> {
            Object value = expression.getSingle(event);
            if (value != null) {
                playerInfo.set((PlayerInfoUpdatePacket.Action<Object>)key, value);
            }
        });
        return new PlayerInfo[] {playerInfo};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerInfo> getReturnType() {
        return PlayerInfo.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player info";
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        List<AsyncableSyntax> syntaxes = new ArrayList<>(1);
        var playerProfileExpression = getActionExpression(PlayerInfoUpdatePacket.Action.ADD_PLAYER);
        if (playerProfileExpression instanceof AsyncableSyntax asyncableSyntax) {
            syntaxes.add(asyncableSyntax);
        }
        return syntaxes;
    }
}
