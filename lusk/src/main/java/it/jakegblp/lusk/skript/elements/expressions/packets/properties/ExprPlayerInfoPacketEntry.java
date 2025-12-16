package it.jakegblp.lusk.skript.elements.expressions.packets.properties;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.arithmetic.Arithmetics;
import org.skriptlang.skript.lang.arithmetic.Operator;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExprPlayerInfoPacketEntry extends PropertyExpression<Object, Object> {

    public static final BiMap<@NotNull String, PlayerInfoUpdatePacket.@NotNull Action<?>> PLAYER_INFO_PROPERTY_NAMES =
            Arrays.stream(PlayerInfoUpdatePacket.Action.values())
                    .collect(ImmutableBiMap.toImmutableBiMap(
                            action -> action.getPropertyName().toLowerCase(Locale.ROOT).replace('_', ' '),
                            Function.identity()
                    ));
    public static final String PLAYER_INFO_PROPERTY_CHOICE = PLAYER_INFO_PROPERTY_NAMES.keySet().stream()
            .map(s -> ":" + s)
            .collect(Collectors.joining("|", "(", ")"));

    static {
        register(ExprPlayerInfoPacketEntry.class, Object.class,
                PLAYER_INFO_PROPERTY_CHOICE + " player (info[rmation] [property]|property)", "playerinfos/playerinfopackets");
    }

    private PlayerInfoUpdatePacket.Action<? super Object> action;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        action = (PlayerInfoUpdatePacket.Action<? super Object>) PLAYER_INFO_PROPERTY_NAMES.get(parseResult.tags.get(0));
        if (!LiteralUtils.hasUnparsedLiteral(expressions[0])) {
            setExpr(expressions[0]);
        } else {
            setExpr(LiteralUtils.defendExpression(expressions[0]));
            return LiteralUtils.canInitSafely(getExpr());
        }
        return true;
    }

    @Override
    protected Object[] get(Event event, Object[] source) {
        return CommonUtils.map(getPlayerInfos(source), playerInfo -> playerInfo.get(action));
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        var type = action.getType();
        return switch (mode) {
            case ADD, REMOVE -> {
                if (Number.class.isAssignableFrom(type))
                    yield new Class[] {type};
                else
                    yield null;
            }
            case SET -> new Class[]{action.getType()};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public boolean isSingle() {
        return super.isSingle() || getReturnType() == PlayerInfo.class;
    }

    public static PlayerInfo[] getPlayerInfos(Object[] source) {
        return CommonUtils.flatMap(PlayerInfo.class, source, object -> {
            if (object instanceof PlayerInfoUpdatePacket packet)
                return packet.getPlayerInfos();
            else if (object instanceof PlayerInfo playerInfo)
                return List.of(playerInfo);
            else
                return null;
        });
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        PlayerInfo[] playerInfos;
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {
            playerInfos = getPlayerInfos(getExpr().getAll(event));
        } else return;
        if (playerInfos.length == 0) return;
        if (mode == Changer.ChangeMode.DELETE) {
            for (PlayerInfo playerInfo : playerInfos)
                playerInfo.remove(action);
            return;
        }
        if (delta == null || delta.length == 0) return;
        Object value = delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (PlayerInfo playerInfo : playerInfos)
                playerInfo.set(action, value);
        } else {
            var type = action.getType();
            Operator operator = mode == Changer.ChangeMode.ADD ? Operator.ADDITION : Operator.SUBTRACTION;
            for (PlayerInfo playerInfo : playerInfos)
                playerInfo.set(action, Arithmetics.calculate(
                        operator,
                        playerInfo.get(action),
                        value,
                        type));
        }
    }

    @Override
    public Class<?> getReturnType() {
        return getExpr().getReturnType();
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return PLAYER_INFO_PROPERTY_NAMES.inverse().get(action) + " player info of " + getExpr().toString(event, debug);
    }
}