package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExprSecTeamCreatePacket extends SectionExpression<TeamPacket> {

    public static Set<String> processMembersExpression(@NotNull Expression<?> expression, Event event) {
        return expression.streamAll(event).map((Function<Object, String>) object -> {
            if (object instanceof String string) return string;
            else if (object instanceof UUID uuid) return uuid.toString();
            else if (object instanceof Player player) return player.getName();
            else if (object instanceof Entity entity) return entity.getUniqueId().toString();
            else return null;
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("name", null, false, String.class))
                .addEntryData(new ExpressionEntryData<>("members", null, false, Entity.class, UUID.class, String.class))
                .addEntryData(new ExpressionEntryData<>("display name", null, false, String.class, Component.class))
                .addEntryData(new ExpressionEntryData<>("prefix", null, false, String.class, Component.class))
                .addEntryData(new ExpressionEntryData<>("suffix", null, false, String.class, Component.class))
                .addEntryData(new ExpressionEntryData<>("nametag visibility", null, false, Team.OptionStatus.class))
                .addEntryData(new ExpressionEntryData<>("collision rule", null, false, Team.OptionStatus.class))
                .addEntryData(new ExpressionEntryData<>("style", null, false, Color.class))
                .addEntryData(new ExpressionEntryData<>("friendly fire", null, false, Boolean.class))
                .addEntryData(new ExpressionEntryData<>("can see friendly invisibles", null, false, Boolean.class))
                .build();
        Skript.registerExpression(ExprSecTeamCreatePacket.class, TeamPacket.class, ExpressionType.COMBINED,
                "[a] new team create packet"
        );
    }

    private Expression<String> nameExpression;
    private Expression<Object> membersExpression, displayNameExpression, prefixExpression, suffixExpression;
    private Expression<Team.OptionStatus> nameTagVisibilityExpression, collisionRuleExpression;
    private Expression<Color> styleExpression;
    private Expression<Boolean> allowFriendlyFireExpression, canSeeFriendlyInvisiblesExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        nameExpression = (Expression<String>) container.getOptional("name", false);
        membersExpression = (Expression<Object>) container.getOptional("members", false);
        displayNameExpression = (Expression<Object>) container.getOptional("display name", false);
        prefixExpression = (Expression<Object>) container.getOptional("prefix", false);
        suffixExpression = (Expression<Object>) container.getOptional("suffix", false);
        nameTagVisibilityExpression = (Expression<Team.OptionStatus>) container.getOptional("nametag visibility", false);
        collisionRuleExpression = (Expression<Team.OptionStatus>) container.getOptional("collision rule", false);
        styleExpression = (Expression<Color>) container.getOptional("style", false);
        if (styleExpression instanceof Literal<Color> literal && AddonUtils.toNamedTextColor(literal.getSingle()) == null) {
            Skript.error("Only chat colors are allowed.");
            return false;
        }
        allowFriendlyFireExpression = (Expression<Boolean>) container.getOptional("friendly fire", false);
        canSeeFriendlyInvisiblesExpression = (Expression<Boolean>) container.getOptional("can see friendly invisibles", false);
        return true;
    }

    @Override
    protected TeamPacket @Nullable [] get(Event event) {
        String name = nameExpression.getSingle(event);
        if (name == null) return new TeamPacket[0];
        Component displayName = AddonUtils.handleComponent(displayNameExpression, event);
        if (displayName == null) return new TeamPacket[0];
        Component prefix = AddonUtils.handleComponent(prefixExpression, event);
        if (prefix == null) return new TeamPacket[0];
        Component suffix = AddonUtils.handleComponent(suffixExpression, event);
        if (suffix == null) return new TeamPacket[0];
        Team.OptionStatus nameTagVisibility = nameTagVisibilityExpression.getSingle(event);
        if (nameTagVisibility == null) return new TeamPacket[0];
        Team.OptionStatus collisionRule = collisionRuleExpression.getSingle(event);
        if (collisionRule == null) return new TeamPacket[0];
        Color color = styleExpression.getSingle(event);
        if (color == null) return new TeamPacket[0];
        NamedTextColor style = AddonUtils.toNamedTextColor(color);
        if (style == null) return new TeamPacket[0];
        Boolean friendlyFire = allowFriendlyFireExpression.getSingle(event);
        if (friendlyFire == null) return new TeamPacket[0];
        Boolean canSeeFriendlyInvisible = canSeeFriendlyInvisiblesExpression.getSingle(event);
        if (canSeeFriendlyInvisible == null) return new TeamPacket[0];
        Set<String> members = processMembersExpression(membersExpression, event);
        return new TeamPacket[] {
                TeamPacket.create(
                        name,
                        new TeamParameters(
                                displayName,
                                prefix,
                                suffix,
                                nameTagVisibility,
                                collisionRule,
                                style,
                                friendlyFire,
                                canSeeFriendlyInvisible
                        ),
                        members
                )
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends TeamPacket> getReturnType() {
        return TeamPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new team create packet";
    }
}
