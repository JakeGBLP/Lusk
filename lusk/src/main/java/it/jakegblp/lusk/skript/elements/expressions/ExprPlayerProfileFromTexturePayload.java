package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.TexturesPayload;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprPlayerProfileFromTexturePayload extends SimpleExpression<CompletablePlayerProfile> {

    static {
        Skript.registerExpression(ExprPlayerProfileFromTexturePayload.class, CompletablePlayerProfile.class, ExpressionType.COMBINED,
                "player profile[s] from %texturepayloads%");
    }

    private Expression<TexturesPayload> texturesPayloadExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        texturesPayloadExpression = (Expression<TexturesPayload>) expressions[0];
        return true;
    }

    @Override
    protected CompletablePlayerProfile @Nullable [] get(Event event) {
        return CommonUtils.map(texturesPayloadExpression.getAll(event), CompletablePlayerProfile::new);
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
