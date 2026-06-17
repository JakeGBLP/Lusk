package it.jakegblp.lusk.skript.modules.playerprofile.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.profile.TexturesPayload;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerProfile extends SimpleExpression<PlayerProfile> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprPlayerProfile.class, ExprPlayerProfile::new, PlayerProfile.class,
                "[a] [new] [player] profile from base64 %string% [and signature %-string%]");
    }

    private Expression<String> base64Expression, signatureExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result) {
        base64Expression = (Expression<String>) expressions[0];
        signatureExpression = (Expression<String>) expressions[1];
        return AddonUtils.initLiteralExpressions(literal -> CommonUtils.isBase64(literal.getSingle()), base64Expression, signatureExpression);
    }

    @Override
    protected PlayerProfile @Nullable [] get(Event event) {
        if (base64Expression != null) {
            String base64 = base64Expression.getSingle(event);
            if (base64 != null) {
                try {
                    TexturesPayload texturesPayload = TexturesPayload.fromBase64(base64, AddonUtils.getSingleDefaultOrNull(event, signatureExpression));
                    PlayerProfile profile = Bukkit.createProfileExact(texturesPayload.getProfileId(), texturesPayload.getProfileName());
                    profile.setProperty(new ProfileProperty("textures", base64, texturesPayload.getSignature()));
                    return new PlayerProfile[]{profile};
                } catch (IllegalArgumentException exception) {
                    error(exception.getMessage());
                }
            }
        }
        return new PlayerProfile[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PlayerProfile> getReturnType() {
        return PlayerProfile.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player profile from base64 " + base64Expression.toString(event, debug);
    }
}
