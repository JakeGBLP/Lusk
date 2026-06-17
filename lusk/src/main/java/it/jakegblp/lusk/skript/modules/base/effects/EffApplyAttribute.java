package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class EffApplyAttribute extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerEffect(syntaxRegistry, EffApplyAttribute.class, EffApplyAttribute::new,
                "apply (attribute[s]|attribute snapshot[s]) %attributesnapshots% to %livingentities%");
    }

    private Expression<AttributeSnapshot> attributeSnapshotExpression;
    private Expression<LivingEntity> livingEntityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.attributeSnapshotExpression = (Expression<AttributeSnapshot>) expressions[0];
        this.livingEntityExpression = (Expression<LivingEntity>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        var attributes = attributeSnapshotExpression.getArray(event);
        if (attributes == null || attributes.length == 0) return;
        var entities = livingEntityExpression.getArray(event);
        if (entities == null || entities.length == 0) return;
        for (AttributeSnapshot attribute : attributes) {
            for (LivingEntity livingEntity : entities) {
                var attributeInstance = livingEntity.getAttribute(attribute.getAttribute());
                if (attributeInstance != null)
                    for (AttributeModifier modifier : attribute.getModifiers())
                        attributeInstance.addModifier(modifier);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "apply attribute snapshots " + attributeSnapshotExpression.toString(event, debug) + " to " + livingEntityExpression.toString(event, debug);
    }
}