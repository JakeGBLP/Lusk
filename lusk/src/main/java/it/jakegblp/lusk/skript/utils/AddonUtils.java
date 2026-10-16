package it.jakegblp.lusk.skript.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataEntry;
import it.jakegblp.lusk.skript.api.syntax.section.SectionParseResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class AddonUtils {
    public static final Pattern REGEX_NUMBER_WITH_DECIMAL = Pattern.compile("(\\d+.\\d+)");

    /**
     * Replaces numbers in a string and truncates the number of decimals to the one specified in Skript's own config.
     * (Number Accuracy)
     *
     * @param string The string to replace numbers in.
     * @return The provided string with all numbers replaced with themselves limited to Skript's number accuracy from its config file.
     */
    public static String withSkriptNumberAccuracy(String string) {
        return REGEX_NUMBER_WITH_DECIMAL.matcher(string).replaceAll(result -> Skript.toString(Double.parseDouble(result.group())));
    }

    /**
     * Note: '*' indicates that there is no skript type (classinfo) for this class.<br>
     * While one should be added, converters can handle going from a more skript friendly type to the missing one and the other way around
     */
    public static @NotNull String toExactClassInfoStringOrSimpleClassName(@NotNull Class<?> clazz) {
        var exactClassInfo = Classes.getExactClassInfo(clazz);
        return exactClassInfo == null ? "*" + CommonUtils.toSentence(clazz.getSimpleName()) : Classes.toString(exactClassInfo);
    }

    public static NamedTextColor toNamedTextColor(Color color) {
        DyeColor dyeColor = color.asDyeColor();
        if (dyeColor == null) return null;
        return switch (dyeColor) {
            case BLACK -> NamedTextColor.BLACK;
            case GRAY -> NamedTextColor.DARK_GRAY;
            case LIGHT_GRAY -> NamedTextColor.GRAY;
            case WHITE -> NamedTextColor.WHITE;
            case ORANGE -> NamedTextColor.GOLD;
            case MAGENTA -> NamedTextColor.LIGHT_PURPLE;
            case LIGHT_BLUE -> NamedTextColor.AQUA;
            case YELLOW -> NamedTextColor.YELLOW;
            case LIME -> NamedTextColor.GREEN;
            case PINK -> NamedTextColor.RED;
            case CYAN -> NamedTextColor.DARK_AQUA;
            case PURPLE -> NamedTextColor.DARK_PURPLE;
            case BLUE -> NamedTextColor.DARK_BLUE;
            case BROWN -> NamedTextColor.BLUE;
            case GREEN -> NamedTextColor.DARK_GREEN;
            case RED -> NamedTextColor.DARK_RED;
        };
    }

    public static @Nullable Component handleComponent(@Nullable Expression<?> expression, Event event) {
        if (expression == null) return null;
        Object object = expression.getSingle(event);
        if (object instanceof Component component) return component;
        else if (object instanceof String string) return Component.text(string);
        else return null;
    }

    @Nullable
    public static Vector getVectorFromExpression(@Nullable Expression<?> expression, Event event) {
        if (expression == null) return null;
        Object object = expression.getSingle(event);
        if (object instanceof Vector vector)
            return vector;
        else if (object instanceof Location location)
            return location.toVector();
        return null;
    }

    public static <T> T getSingleNullable(@Nullable Expression<T> expression, Event event) {
        return expression == null ? null : expression.getSingle(event);
    }

    public static <T> T getSingleNullable(@Nullable Expression<T> expression, Event event, T defaultValue) {
        return expression == null ? defaultValue : expression.getOptionalSingle(event).orElse(defaultValue);
    }

    public static <T> T getLiteralValue(@Nullable Literal<T> literal, T defaultValue) {
        if (literal == null) return defaultValue;
        T t = literal.getSingle();
        if (t == null) return defaultValue;
        return t;
    }

    @SafeVarargs
    public static <T> boolean initLiteralExpressions(Predicate<Literal<T>> predicate, Expression<T>... expressions) {
        for (Expression<T> expression : expressions)
            if (expression instanceof Literal<T> literal && !predicate.test(literal))
                return false;
        return true;
    }


    @SuppressWarnings("unchecked")
    @NotNull
    public static SectionParseResult<?> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<?> returnType, @Nullable BiConsumer<Node, String> failure, Class<?> @Nullable ... types) {
        ParserInstance parserInstance = ParserInstance.get();
        List<TriggerSection> previousSections = parserInstance.getCurrentSections();
        List<TriggerSection> sections = new ArrayList<>(previousSections);
        sections.add(parentSection);
        parserInstance.setCurrentSections(sections);
        var sectionContext = parserInstance.getData(Section.SectionContext.class);
        List<Expression<?>> expressionList = new ArrayList<>();
        Class<?>[] possibleReturnTypes;
        if (types == null || types.length == 0)
            possibleReturnTypes = new Class[]{returnType};
        else {
            possibleReturnTypes = types;
            returnType = possibleReturnTypes[0];
        }
        boolean error = false;
        Skript.debug("Lusk - parse section nodes: node loop start");
        for (Node subNode : mainNode) {
            String key = subNode.getKey();
            Skript.debug("Lusk -- sub node key: " + key);
            if (key != null) {
                Skript.debug("Lusk --- key is not null");
                Supplier<Expression<?>> supplier = () -> new SkriptParser(key).parseExpression(possibleReturnTypes);
                Skript.debug("Lusk --- pre section context modification for key: " + key + " and sub node: " + subNode.getClass().getSimpleName());
                Expression<?> expression = sectionContext.modify(subNode instanceof SectionNode sectionNode ? sectionNode : null, List.of(), supplier);
                Skript.debug("Lusk --- post section context modification for key: " + key + " and sub node: " + subNode.getClass().getSimpleName());
                if (expression != null) {
                    expressionList.add(expression);
                    Skript.debug("Lusk ---- modified section successfully");
                } else {
                    Skript.debug("Lusk ---- could not modify section");
                    if (failure != null)
                        failure.accept(subNode, key);
                    if (!error)
                        error = true;
                }
            }
        }
        parserInstance.setCurrentSections(previousSections);
        return new SectionParseResult<>(new ExpressionList<>(expressionList.toArray(new Expression[0]), returnType, possibleReturnTypes, true), error);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> SectionParseResult<T> parseSectionNodesVerbose(Section parentSection, SectionNode mainNode, Class<T> returnType, Class<?> @Nullable ... types) {
        return (SectionParseResult<T>) parseSectionNodes(parentSection, mainNode, returnType, (node, key) -> errorForNode(node, "Unexpected expression: " + key), types);
    }

    @NotNull
    public static SectionParseResult<?> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<?> returnType, Class<?> @Nullable ... types) {
        return parseSectionNodes(parentSection, mainNode, returnType, null, types);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> SectionParseResult<T> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<T> returnType) {
        return (SectionParseResult<T>) parseSectionNodes(parentSection, mainNode, returnType, null, (Class<?>[]) null);
    }

    public static <T> Expression<? extends T>[] spread(Expression<T> expression) {
        if (expression instanceof ExpressionList<T> expressionList)
            return expressionList.getExpressions();
        return CollectionUtils.array(expression);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> List<Literal<? extends T>> getLiterals(Expression<T> expression) {
        if (expression instanceof LiteralList<T> literalList)
            return Arrays.asList(literalList.getExpressions());
        else if (expression instanceof Literal<T> literal)
            return List.of(literal);
        else if (expression instanceof ExpressionList<T> expressionList)
            return (List<Literal<? extends T>>) (List<?>) CommonUtils.filterToList(Literal.class, expressionList.getExpressions());
        return List.of();
    }

    public static <T> @Nullable T getSingleDefaultOrNull(Event event, @Nullable Expression<T> expression, T defaultValue) {
        return expression == null ? defaultValue : expression.getOptionalSingle(event).orElse(null);
    }

    public static <T> @Nullable T getSingleDefaultOrNull(Event event, @Nullable Expression<T> expression) {
        return getSingleDefaultOrNull(event, expression, null);
    }

    public static void errorForElement(@NotNull SyntaxRuntimeErrorProducer element, String message) {
        errorForNode(element.getNode(), message);
    }

    public static void errorForNode(Node node, String message) {
        var parserInstance = ParserInstance.get();
        Node originalNode = parserInstance.getNode();
        parserInstance.setNode(node);
        Skript.error(message);
        parserInstance.setNode(originalNode);
    }

    public static void warningForElement(@NotNull SyntaxRuntimeErrorProducer element, String message) {
        warningForNode(element.getNode(), message);
    }

    public static void warningForNode(Node node, String message) {
        var parserInstance = ParserInstance.get();
        Node originalNode = parserInstance.getNode();
        parserInstance.setNode(node);
        Skript.warning(message);
        parserInstance.setNode(originalNode);
    }

    /**
     * Useful for when a and b can't both be true, and a fallback value is required.
     *
     * @return a ? TRUE : b ? FALSE : UNKNOWN
     */
    @NotNull
    public static Kleenean getKleenean(boolean a, boolean b) {
        if (a) return Kleenean.TRUE;
        else if (b) return Kleenean.FALSE;
        else return Kleenean.UNKNOWN;
    }

    @Contract("null, _ -> null")
    public static String getUnformattedString(Expression<String> expression, @NotNull Event event) {
        if (expression instanceof VariableString variableString) return variableString.toString(event);
        else if (expression != null) return expression.getSingle(event);
        else return null;
    }

    public static void sendEasyMetadata(Player[] players, EntityMetadata metadata, ProtocolEntityReference[] entities) {
        var packets = CommonUtils.map(ClientboundPacket.class, entities, reference -> new EntityMetadataPacket(reference.getId(), metadata));
        NMSApi.sendPackets(players, packets, ExecutionMode.ASYNCHRONOUS);
    }

    public static void sendEasyMetadata(Event event, Expression<Player> playerExpression, Expression<ProtocolEntityReference> protocolEntityReferenceExpression, MetadataEntry<?, ?>... items) {
        sendEasyMetadata(playerExpression.getArray(event), EntityMetadata.of(items), protocolEntityReferenceExpression.getArray(event));
    }
}
