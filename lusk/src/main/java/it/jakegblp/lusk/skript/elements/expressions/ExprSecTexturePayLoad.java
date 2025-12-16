package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.player.TextureType;
import it.jakegblp.lusk.nms.core.world.player.TexturesPayload;
import it.jakegblp.lusk.skript.api.entry.DynamicEntryData;
import it.jakegblp.lusk.skript.api.expression.DefaultValueExpression;
import it.jakegblp.lusk.skript.api.expression.NoInitExpression;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ExprSecTexturePayLoad extends SectionExpression<TexturesPayload> {

    public static final EntryValidator VALIDATOR;
    public static final EntryValidator TEXTURE_VALIDATOR;

    static {
        TEXTURE_VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("url", null, false, String.class))
                .addEntryData(new ExpressionEntryData<>("slim", new DefaultValueExpression<>(Boolean.class, false), true, Boolean.class))
                .build();
        var builder = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("timestamp", new DefaultValueExpression<>(Long.class, 0L), true, Long.class, Date.class))
                .addEntryData(new ExpressionEntryData<>("uuid", null, true, UUID.class))
                .addEntryData(new ExpressionEntryData<>("player name", null, true, String.class))
                .addEntryData(new ExpressionEntryData<>("signature", null, true, String.class))
                .addEntryData(new ExpressionEntryData<>("is signature required", null, true, Boolean.class));
        for (TextureType textureType : TextureType.values()) {
            var key = textureType.name().toLowerCase();
            builder.addEntryData(textureType == TextureType.SKIN
                            ? new DynamicEntryData(key, String.class, TEXTURE_VALIDATOR, true) :
                            new ExpressionEntryData<>(key, null, false, String.class));
        }
        VALIDATOR = builder.build();
        Skript.registerExpression(ExprSecTexturePayLoad.class, TexturesPayload.class, ExpressionType.COMBINED,
                "[a] new [player] [profile] texture payload");
    }

    @Getter
    private Expression<String> playerNameExpression;
    @Getter
    private Expression<UUID> uuidExpression;
    private Expression<Object> timestampExpression;
    private Expression<String> signatureExpression;
    private Expression<Boolean> isSignatureRequiredExpression;
    private List<TextureEntryExpression> textureEntryExpressions;

    @Getter
    @AllArgsConstructor
    public static class TextureEntryExpression extends NoInitExpression<TexturesPayload.TextureEntry> {

        private TextureType textureType;
        private Expression<String> urlExpression;
        private Expression<Boolean> slimExpression;

        @SuppressWarnings("unchecked")
        public TextureEntryExpression(@NotNull TextureType textureType, @NotNull EntryContainer entryContainer) {
            this(textureType, (Expression<String>) entryContainer.getOptional("url", false), (Expression<Boolean>) entryContainer.getOptional("slim", false));
        }

        @Override
        public TexturesPayload.TextureEntry[] get(Event event) {
            var textureEntry = new TexturesPayload.TextureEntry(textureType, urlExpression.getSingle(event));
            if (textureType == TextureType.SKIN && slimExpression != null) {
                Boolean isSlim = slimExpression.getSingle(event);
                if (isSlim != null)
                    textureEntry.setMetadata(new TexturesPayload.TextureEntry.TextureMetadata(isSlim ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC));
            }
            return new TexturesPayload.TextureEntry[] {textureEntry};
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        playerNameExpression = (Expression<String>) container.get("player name", false);
        uuidExpression = (Expression<UUID>) container.get("uuid", false);
        if (playerNameExpression == null && uuidExpression == null) {
            Skript.error("You must provide at least a name or uuid.");
            return false;
        }
        signatureExpression = (Expression<String>) container.get("signature", false);
        if (!AddonUtils.initLiteralExpressions(literal -> CommonUtils.isBase64(literal.getSingle()), signatureExpression)) {
            Skript.error("You must provide a valid Base64 signature.");
            return false;
        }
        timestampExpression = (Expression<Object>) container.get("timestamp", true);
        isSignatureRequiredExpression = (Expression<Boolean>) container.get("is signature required", false);
        textureEntryExpressions = Arrays.stream(TextureType.values()).map(textureType -> {
            Object value = container.get(textureType.name().toLowerCase(), false);
            if (textureType == TextureType.SKIN && value instanceof SectionNode sectionNode) {
                EntryContainer textureContainer = TEXTURE_VALIDATOR.validate(sectionNode);
                if (textureContainer == null) return null;
                else return new TextureEntryExpression(textureType, textureContainer);
            } else {
                return new TextureEntryExpression(textureType, (Expression<String>) value, null);
            }
        }).toList();
        return true;
    }

    @Override
    protected TexturesPayload @Nullable [] get(Event event) {
        var timestampObject = timestampExpression.getSingle(event);
        assert timestampObject != null;
        long timestamp;
        if (timestampObject instanceof Long aLong)
            timestamp = aLong;
        else if (timestampObject instanceof Date date)
            timestamp = date.getTime();
        else {
            error("Provided invalid timestamp: " + timestampObject);
            return new TexturesPayload[0];
        }
        return new TexturesPayload[] {
                new TexturesPayload(
                        timestamp,
                        uuidExpression.getSingle(event),
                        playerNameExpression.getSingle(event),
                        signatureExpression.getSingle(event),
                        isSignatureRequiredExpression.getSingle(event),
                        CommonUtils.map(textureEntryExpressions, textureEntryExpression -> textureEntryExpression.getSingle(event))
                )
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends TexturesPayload> getReturnType() {
        return TexturesPayload.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player profile texture payload";
    }
}
