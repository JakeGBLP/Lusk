package it.jakegblp.lusk.skript.modules.playerprofile.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.profile.MutableProfileProperty;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerProfilePropertyOf extends PropertyExpression<PlayerProfile, MutableProfileProperty> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprPlayerProfilePropertyOf.class, ExprPlayerProfilePropertyOf::new, MutableProfileProperty.class,
                "[player] profile property named %string%", "playerprofiles", true);
    }

    private Expression<String> nameExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        nameExpression = (Expression<String>) expressions[matchedPattern];
        setExpr((Expression<? extends PlayerProfile>) expressions[1 - matchedPattern]);
        return true;
    }

    @Override
    protected MutableProfileProperty[] get(Event event, PlayerProfile[] source) {
        String name = nameExpression.getSingle(event);
        if (name == null) return new MutableProfileProperty[0];
        return CommonUtils.map(source, playerProfile -> {
            if (playerProfile.hasProperty(name)) {
                ProfileProperty profileProperty = CommonUtils.findFirst(playerProfile.getProperties(), property -> property.getName().equals(name));
                if (profileProperty != null)
                    return new MutableProfileProperty(profileProperty);
            }
            return null;
        });
    }

    @Override
    public Class<? extends MutableProfileProperty> getReturnType() {
        return MutableProfileProperty.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "player profile property named " + nameExpression.toString(event, debug) + " of " + getExpr().toString(event, debug);
    }
}
