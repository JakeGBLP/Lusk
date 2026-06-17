package it.jakegblp.lusk.skript.modules.packets.expressions.properties;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprEntityMetadataEntries extends PropertyExpression<EntityMetadata, MetadataItem> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprEntityMetadataEntries.class, ExprEntityMetadataEntries::new, MetadataItem.class,
                "[entity] metadata entries", "entitymetadatas", true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<? extends EntityMetadata>) expressions[0]);
        return true;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected MetadataItem[] get(Event event, EntityMetadata[] source) {
        return CommonUtils.flatMap(MetadataItem.class, source, EntityMetadata::items);
    }

    // change modes?


    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class<? extends MetadataItem> getReturnType() {
        return MetadataItem.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "entity metadata entries of " + getExpr().toString(event, debug);
    }
}