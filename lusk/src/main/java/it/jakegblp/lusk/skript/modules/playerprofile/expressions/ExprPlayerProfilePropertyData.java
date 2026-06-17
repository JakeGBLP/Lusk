package it.jakegblp.lusk.skript.modules.playerprofile.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.nms.core.world.player.profile.MutableProfileProperty;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerProfilePropertyData extends SimplePropertyExpression<Object, String> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprPlayerProfilePropertyData.class, ExprPlayerProfilePropertyData::new, String.class,
                "[player] profile property (:name|:value|signature)", "playerproperty/mutableplayerproperty", true);
    }

    private Kleenean data;
    
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        data = AddonUtils.getKleenean(parseResult.hasTag("name"), parseResult.hasTag("value"));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable String convert(Object from) {
        ProfileProperty property;
        if (from instanceof ProfileProperty profileProperty)
            property = profileProperty;
        else if (from instanceof MutableProfileProperty mutableProfileProperty)
            property = mutableProfileProperty.immutable();
        else return null;
        return switch (data) {
            case TRUE -> property.getName();
            case FALSE -> property.getValue();
            case UNKNOWN -> property.getSignature();
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        if ((delta != null && delta.length > 0 && mode == Changer.ChangeMode.SET) || (mode == Changer.ChangeMode.DELETE && data.isUnknown())) {
            for (Object object : getExpr().getArray(event)) {
                if (object instanceof MutableProfileProperty mutableProfileProperty) {
                    switch (mode) {
                        case SET -> {
                            String string = (String) delta[0];
                            switch (data) {
                                case TRUE -> mutableProfileProperty.setName(string);
                                case FALSE -> mutableProfileProperty.setValue(string);
                                case UNKNOWN -> mutableProfileProperty.setSignature(string);
                            }
                        }
                        case DELETE -> mutableProfileProperty.setSignature(null);
                    }
                } else
                    error("Tried modifying an immutable profile property.");
            }
        }
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE && data.isUnknown())
            return new Class[0];
        else if (mode == Changer.ChangeMode.SET)
            return new Class[]{String.class};
        else return null;
    }

    @Override
    protected String getPropertyName() {
        return "player profile property " + switch (data) {
            case TRUE -> "name";
            case FALSE -> "value";
            case UNKNOWN -> "signature";
        };
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
