package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.world.player.MutableProfileProperty;
import it.jakegblp.lusk.skript.elements.expressions.packets.properties.ExprBundlePackets;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExprPlayerProfileProperties extends PropertyExpression<PlayerProfile, MutableProfileProperty> {

    static {
        register(ExprBundlePackets.class, Packet.class, "[player] profile properties", "playerprofiles");
    }

    @Override
    protected MutableProfileProperty[] get(Event event, PlayerProfile[] source) {
        return CommonUtils.flatMap(MutableProfileProperty.class, source, PlayerProfile::getProperties, MutableProfileProperty::new);
    }

    @Override
    public Class<? extends MutableProfileProperty> getReturnType() {
        return MutableProfileProperty.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "player profile properties of "+getExpr().toString(event, debug);
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET -> new Class[]{MutableProfileProperty[].class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        boolean hasDelta = delta != null && delta.length > 0;
        if (mode == Changer.ChangeMode.DELETE) {
            for (PlayerProfile playerProfile : getExpr().getAll(event)) {
                playerProfile.clearProperties();
            }
        } else if (hasDelta) {
            List<ProfileProperty> properties = CommonUtils.filterMapToList(MutableProfileProperty.class, delta, MutableProfileProperty::asImmutable);
            for (PlayerProfile playerProfile : getExpr().getAll(event)) {
                switch (mode) {
                    case ADD -> playerProfile.setProperties(properties);
                    case REMOVE -> playerProfile.removeProperties(properties);
                    case SET -> {
                        playerProfile.clearProperties();
                        playerProfile.setProperties(properties);
                    }
                }
            }
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends PlayerProfile>) expressions[0]);
        return true;
    }
}

