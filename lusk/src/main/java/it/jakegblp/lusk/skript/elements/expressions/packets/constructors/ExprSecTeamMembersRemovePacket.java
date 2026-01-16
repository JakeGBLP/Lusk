package it.jakegblp.lusk.skript.elements.expressions.packets.constructors;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.TeamPacket;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static it.jakegblp.lusk.skript.elements.expressions.packets.constructors.ExprSecTeamCreatePacket.processMembersExpression;

public class ExprSecTeamMembersRemovePacket extends SectionExpression<TeamPacket> {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("name", null, false, String.class))
                .addEntryData(new ExpressionEntryData<>("members", null, false, Entity.class, UUID.class, String.class))
                .build();
        Skript.registerExpression(ExprSecTeamMembersRemovePacket.class, TeamPacket.class, ExpressionType.COMBINED,
                "[a] new team members remove packet"
        );
    }

    private Expression<String> nameExpression;
    private Expression<Object> membersExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        nameExpression = (Expression<String>) container.getOptional("name", false);
        membersExpression = (Expression<Object>) container.getOptional("members", false);
        return true;
    }

    @Override
    protected TeamPacket @Nullable [] get(Event event) {
        String name = nameExpression.getSingle(event);
        if (name == null) return new TeamPacket[0];
        Set<String> members = processMembersExpression(membersExpression, event);
        return new TeamPacket[] {TeamPacket.remove(name, members)};
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
        return "a new team members remove packet";
    }
}
