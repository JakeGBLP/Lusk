package it.jakegblp.lusk.skript.modules.packets.expressions.constructors;

import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.protocol.packets.client.UpdateAttributesPacket;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.List;

public class ExprSecAttributePacket extends SectionExpression<UpdateAttributesPacket> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerExpression(syntaxRegistry, ExprSecAttributePacket.class, ExprSecAttributePacket::new, UpdateAttributesPacket.class,
                "[a] new [update] attribute[s] packet");
    }

    public static final EntryValidator VALIDATOR = EntryValidator.builder()
            .addEntryData(new ExpressionEntryData<>("entity id", null, false, Integer.class))
            .addEntryData(new ExpressionEntryData<>("attributes", null, false, AttributeSnapshot.class))
            .build();

    private Expression<Integer> entityIdExpression;
    private Expression<AttributeSnapshot> attributeSnapshotExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        entityIdExpression = (Expression<Integer>) container.getOptional("entity id", false);
        attributeSnapshotExpression = (Expression<AttributeSnapshot>) container.getOptional("attributes", false);
        return entityIdExpression != null && attributeSnapshotExpression != null;
    }

    @Override
    protected UpdateAttributesPacket @Nullable [] get(Event event) {
        var entityId = entityIdExpression.getSingle(event);
        if (entityId == null) return new UpdateAttributesPacket[0];
        var attributeSnapshots = attributeSnapshotExpression.getArray(event);
        if (attributeSnapshots == null) return new UpdateAttributesPacket[0];
        return new UpdateAttributesPacket[]{new UpdateAttributesPacket(entityId, attributeSnapshots)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends UpdateAttributesPacket> getReturnType() {
        return UpdateAttributesPacket.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "new attributes packet";
    }
}
