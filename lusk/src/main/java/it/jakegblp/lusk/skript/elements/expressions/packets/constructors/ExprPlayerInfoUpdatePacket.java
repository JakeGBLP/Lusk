package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.client.PlayerInfoUpdatePacket;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.elements.expressions.ExprSecPlayerInfo;
import it.jakegblp.lusk.skript.elements.expressions.ExprSecPlayerProfile;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.Getter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.jakegblp.lusk.skript.elements.expressions.packets.properties.ExprPlayerInfoPacketEntry.PLAYER_INFO_PROPERTY_NAMES;
import static it.jakegblp.lusk.skript.utils.AddonUtils.errorForElement;

@Getter
public class ExprPlayerInfoUpdatePacket extends SimpleExpression<PlayerInfoUpdatePacket> implements AsyncableSyntaxesWrapper {

    static {
        Skript.registerExpression(ExprPlayerInfoUpdatePacket.class, PlayerInfoUpdatePacket.class, ExpressionType.COMBINED,
                "[a] new player info update packet (with|containing|from) %playerinfos%");
    }

    private Expression<? extends PlayerInfo> playerInfoExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        playerInfoExpression = (Expression<? extends PlayerInfo>) expressions[0];
        return playerInfoExpression != null && validatePlayerInfos(playerInfoExpression);
    }

    @Override
    protected PlayerInfoUpdatePacket @Nullable [] get(Event event) {
        var playerInfos = playerInfoExpression.getAll(event);
        var actions = new HashSet<>(CommonUtils.flatMapToList(playerInfos, PlayerInfo::getActions));
        boolean error = false;
        for (var action : actions)
            for (var info : playerInfos)
                if (!info.getActions().contains(action)) {
                    error("Player Info "+Classes.toString(info)+" does not have the action "+PLAYER_INFO_PROPERTY_NAMES.inverse().get(action));
                    if (!error)
                        error = true;
                }
        return error ? new PlayerInfoUpdatePacket[0] : new PlayerInfoUpdatePacket[] {new PlayerInfoUpdatePacket(actions, List.of(playerInfos))};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerInfoUpdatePacket> getReturnType() {
        return PlayerInfoUpdatePacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player info update packet with player infos " + playerInfoExpression.toString(event, debug);
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return AsyncableSyntaxesWrapper.filterAsyncableSyntaxes(playerInfoExpression);
    }

    public static boolean validatePlayerInfos(@NotNull Expression<? extends PlayerInfo> playerInfoExpressionList) {
        ExprSecPlayerInfo[] exprSecPlayerInfos = CommonUtils.filter(ExprSecPlayerInfo.class, AddonUtils.spread(playerInfoExpressionList));
        boolean justOne = exprSecPlayerInfos.length == 1;
        Set<PlayerInfoUpdatePacket.Action<?>> baselineSet = null;
        int baselineSize = 0;
        for (int i = 0; i < exprSecPlayerInfos.length; i++) {
            var exprPlayerInfo = exprSecPlayerInfos[i];
            if (exprPlayerInfo.getActionExpression(PlayerInfoUpdatePacket.Action.ADD_PLAYER) instanceof ExprSecPlayerProfile exprSecPlayerProfile) {
                if (exprSecPlayerProfile.getPlayerProfileExpression().isError())
                    return false;
            }
            if (i == 0) {
                baselineSet = exprPlayerInfo.getActions();
                baselineSize = baselineSet.size();
                continue;
            }
            var currentActions = exprPlayerInfo.getActions();
            int size = currentActions.size();
            if (size > baselineSize) {
                baselineSet = currentActions;
                baselineSize = size;
            }
        }

        if (!justOne) {
            List<ExprSecPlayerInfo> mismatching = new ArrayList<>();
            for (var info : exprSecPlayerInfos)
                if (!baselineSet.equals(info.getActions()))
                    mismatching.add(info);

            if (!mismatching.isEmpty()) {
                Skript.error("All entries must define the same properties.");
                var inverseMap = PLAYER_INFO_PROPERTY_NAMES.inverse();
                for (var info : mismatching) {
                    var actualSet = info.getActions();
                    var missing = new HashSet<>(baselineSet);
                    missing.removeAll(actualSet);
                    errorForElement(info, "This player info defines " +
                            Classes.toString(CommonUtils.mapToObjectArray(new ArrayList<>(actualSet), inverseMap::get), true) +
                            ", it must also define " +
                            Classes.toString(CommonUtils.mapToObjectArray(new ArrayList<>(missing), inverseMap::get), true) +
                            '.');
                }
                return false;
            }
        }
        return true;
    }
}
