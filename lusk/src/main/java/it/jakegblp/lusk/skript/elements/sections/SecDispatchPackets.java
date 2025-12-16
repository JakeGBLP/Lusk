package it.jakegblp.lusk.skript.elements.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.nms.api.NMSApi;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.ClientboundPacket;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.api.expression.DefaultValueExpression;
import it.jakegblp.lusk.skript.elements.effects.EffDispatchPacket;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SecDispatchPackets extends Section implements AsyncableSyntaxesWrapper {

    private static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("recipients", null, false, Player.class))
                .addEntryData(new ExpressionEntryData<>("packets", null, false, ClientboundPacket.class)) // todo: this does not allow section expressions directly, this is a skript limitation, figure out a way to make it somehow work, if not do the same thing ExprSecList.class does
                .addEntryData(new ExpressionEntryData<>("bundled", new DefaultValueExpression<>(Boolean.class, false), true, Boolean.class))
                .addSection("then run", true)
                .build();
        Skript.registerSection(SecDispatchPackets.class,
                "[sync:[async:a]sync[hronous]] packet dispatch");
    }

    private Expression<ClientboundPacket> clientboundPacketExpression;
    private Expression<Player> playerExpression;
    private Expression<Boolean> bundledExpression;
    @Getter
    private ExecutionMode initExecutionMode;
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
        EntryContainer entryContainer = VALIDATOR.validate(sectionNode);
        if (entryContainer == null) return false;
        clientboundPacketExpression = (Expression<ClientboundPacket>) entryContainer.getOptional("packets", false);
        playerExpression = (Expression<Player>) entryContainer.getOptional("recipients", false);
        bundledExpression = (Expression<Boolean>) entryContainer.getOptional("bundled", true);
        if (entryContainer.getOptional("then run", false) instanceof SectionNode postDispatchSectionNode)
            trigger = loadCode(postDispatchSectionNode, "post_packet_dispatch", null, null, CollectionUtils.array());
        initExecutionMode = ExecutionMode.fromBooleans(parseResult.hasTag("async"), parseResult.hasTag("sync"));
        EffDispatchPacket.scanPackets(this);
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        var players = playerExpression.getAll(event);
        var packets = clientboundPacketExpression.getAll(event);
        var bundled = AddonUtils.getSingleNullable(bundledExpression, event, false);
        CompletableFuture<Void> future;
        if (bundled)
            future = NMSApi.sendBundledPackets(players, packets, initExecutionMode);
        else
            future = NMSApi.sendPackets(players, packets, initExecutionMode);
        if (trigger != null)
            future.thenRun(() -> TriggerItem.walk(trigger, event));
        return super.walk(event, false);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (initExecutionMode.isInherited() ? "" : initExecutionMode.name().toLowerCase() + " ") + "packet dispatch";
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return AsyncableSyntaxesWrapper.filterAsyncableSyntaxes(clientboundPacketExpression);
    }
}
