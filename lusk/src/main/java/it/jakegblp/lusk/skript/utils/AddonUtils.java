package it.jakegblp.lusk.skript.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.nms.core.protocol.packets.client.EntityMetadataPacket;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.skript.api.section.SectionParseResult;
import it.jakegblp.lusk.skript.core.adapters.SkriptAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddonUtils {
    public static SkriptAdapter skriptAdapter;

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

    public static Object getSectionContext(ParserInstance parserInstance) {
        return skriptAdapter.getSectionContext(parserInstance);
    }

    public static <T> T modifySectionContext(Object sectionContext, SectionNode sectionNode, List<TriggerItem> triggerItems, Supplier<? extends T> supplier) {
        return skriptAdapter.modifySectionContext(sectionContext, sectionNode, List.copyOf(triggerItems), supplier);
    }

    public static <T> T modifySectionContext(ParserInstance parserInstance, SectionNode sectionNode, List<TriggerItem> triggerItems, Supplier<? extends T> supplier) {
        return modifySectionContext(getSectionContext(parserInstance), sectionNode, List.copyOf(triggerItems), supplier);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static SectionParseResult<?> parseSectionNodes(Section parentSection, SectionNode mainNode, Class<?> returnType, @Nullable BiConsumer<Node, String> failure, Class<?> @Nullable ... types) {
        Bukkit.getLogger().info("full parseSectionNodes(...) call");
        ParserInstance parserInstance = ParserInstance.get();
        List<TriggerSection> previousSections = parserInstance.getCurrentSections();
        List<TriggerSection> sections = new ArrayList<>(previousSections);
        sections.add(parentSection);
        parserInstance.setCurrentSections(sections);
        Object sectionContext = getSectionContext(parserInstance);
        List<Expression<?>> expressionList = new ArrayList<>();
        Class<?>[] possibleReturnTypes = (types == null || types.length == 0) ? new Class[]{returnType} : types;
        boolean error = false;
        Bukkit.getLogger().info("PRE NODE LOOP");
        for (Node subNode : mainNode) {
            String key = subNode.getKey();
            Bukkit.getLogger().info("node parse loop: "+key);
            if (key != null) {
                Bukkit.getLogger().info("key is not null");
                Supplier<Expression<?>> supplier = () -> new SkriptParser(key).parseExpression(possibleReturnTypes);
                Bukkit.getLogger().info("PRE SECTION CONTEXT MODIFICATION FOR "+key);
                Expression<?> expression = modifySectionContext(sectionContext, subNode instanceof SectionNode sectionNode ? sectionNode : null, List.of(), supplier);
                Bukkit.getLogger().info("POST SECTION CONTEXT MODIFICATION FOR "+key);
                if (expression != null) {
                    expressionList.add(expression);
                    Bukkit.getLogger().info("Success: "+expression);
                } else {
                    Bukkit.getLogger().info("Failure: "+key);
                    if (failure != null) {
                        failure.accept(subNode, key);
                    }
                    if (!error) {
                        error = true;
                    }
                }
            }
        }
        parserInstance.setCurrentSections(previousSections);
        return new SectionParseResult<>(new ExpressionList<>(expressionList.toArray(new Expression[0]), returnType, possibleReturnTypes, true), error);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> SectionParseResult<T> parseSectionNodesVerbose(Section parentSection, SectionNode mainNode, Class<T> returnType, Class<?> @Nullable ... types) {
        return (SectionParseResult<T>) parseSectionNodes(parentSection, mainNode, returnType, (node, key) -> errorForNode(node, "Unexpected expression: "+key), types);
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

    @SuppressWarnings("unchecked")
    public static @Nullable Map<String, Collection<Node>> getHandledNodes(EntryContainer container) {
        return ((Map<String, Collection<Node>>) new SimpleClass<>(EntryContainer.class).getField("handledNodes").get(container));
    }

    public static @Nullable Collection<Node> getHandledNodes(EntryContainer container, String key) {
        var handledNodes = getHandledNodes(container);
        return handledNodes == null ? null : handledNodes.get(key);
    }

    public static @Nullable Node getHandledNode(EntryContainer container, String key) {
        var handledNodes = getHandledNodes(container, key);
        if (handledNodes == null) return null;
        return handledNodes.iterator().next();
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
        if (expression instanceof VariableString variableString) return variableString.toUnformattedString(event);
        else if (expression != null) return expression.getSingle(event);
        else return null;
    }

    // todo: find a better name for this method
    @Contract("null, _, _, _ -> null")
    public static <T> T parseExpression(Expression<?> expression, @NotNull Event event, @NotNull Class<T> type, @NotNull Function<@NotNull String, T> function) {
        if (expression instanceof VariableString variableString) return function.apply(variableString.toUnformattedString(event));
        else if (expression != null) {
            Object value = expression.getSingle(event);
            if (type.isInstance(value))
                return type.cast(value);
        }
        return null;
    }

    public static boolean validateBase64(Expression<String> expression) {
        if (expression instanceof Literal<String> literal && !CommonUtils.isBase64(literal.getSingle())) {
            Skript.error("You must provide a valid Base64 string.");
            return false;
        }
        return true;
    }


    public static void sendEasyMetadata(Player[] players, EntityMetadata metadata, Object[] idsOrEntities){
        Set<EntityMetadataPacket> packets = Arrays.stream(idsOrEntities)
                .flatMap(o -> {
                    if (o instanceof Entity entity)
                        return Stream.of(new EntityMetadataPacket(entity.getEntityId(), metadata));
                    if (o instanceof Number number)
                        return Stream.of(new EntityMetadataPacket(number.intValue(), metadata));
                    return Stream.empty();
                })
                .collect(Collectors.toSet());

        NMSApi.sendPackets(players, packets, ExecutionMode.ASYNCHRONOUS);
    }








    /*
    public static <T> ExpressionList<T> asExpressionList(Expression<T> expression, Class<T> type) {
        if (expression instanceof ExpressionList<T> expressionList)
            return expressionList;
        return new ExpressionList<T>(new Expression[]{expression}, type, true);
    }
    */

}
