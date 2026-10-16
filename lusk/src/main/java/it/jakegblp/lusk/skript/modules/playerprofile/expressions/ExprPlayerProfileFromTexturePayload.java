package it.jakegblp.lusk.skript.modules.playerprofile.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.profile.CompletablePlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.profile.TexturesPayload;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprPlayerProfileFromTexturePayload extends SimpleExpression<CompletablePlayerProfile> {

    private Expression<TexturesPayload> texturesPayloadExpression;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprPlayerProfileFromTexturePayload.class, ExprPlayerProfileFromTexturePayload::new, CompletablePlayerProfile.class)
                .patterns("player profile[s] from %texturepayloads%")
                .register(syntaxRegistry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        texturesPayloadExpression = (Expression<TexturesPayload>) expressions[0];
        return true;
    }

    @Override
    protected CompletablePlayerProfile @Nullable [] get(Event event) {
        return CommonUtils.map(CompletablePlayerProfile.class, texturesPayloadExpression.getArray(event), CompletablePlayerProfile::new);
    }

    @Override
    public boolean isSingle() {
        return texturesPayloadExpression.isSingle();
    }

    @Override
    public Class<? extends CompletablePlayerProfile> getReturnType() {
        return CompletablePlayerProfile.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "player profiles from " + texturesPayloadExpression.toString(event, debug);
    }
}
