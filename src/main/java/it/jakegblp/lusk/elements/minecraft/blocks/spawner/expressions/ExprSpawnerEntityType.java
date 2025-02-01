package it.jakegblp.lusk.elements.minecraft.blocks.spawner.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20;

@Name("Spawner/Trial Spawner - Entity Type")
@Description("""
Gets the spawner entity type of the provided spawners or trial spawners (1.21+).

**Works with items.**
**Works with Spawner Minecart (Requires 1.20+).**

Can be set, reset (sets it to pig), and deleted (requires Minecraft 1.20+).
""")
@Examples("set spawner entity type of {_block} to zombie")
@Since("1.3.4")
public class ExprSpawnerEntityType extends SimplerPropertyExpression<Object, EntityData> {

    static {
        register(ExprSpawnerEntityType.class, EntityData.class, "spawner (entity|creature) type", "itemtypes/blocks/blockstates" + (MINECRAFT_1_20 ? "/entities" : ""));
    }

    @Override
    public @Nullable EntityData convert(Object from) {
        return new BlockWrapper(from).getSpawnerEntityType();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return MINECRAFT_1_20;
    }

    @Override
    public void delete(Object from) {
        super.delete(from);
    }

    @Override
    public void set(Object from, EntityData to) {
        new BlockWrapper(from).setSpawnerEntityType(to);
    }

    @Override
    public void reset(Object from) {
        new BlockWrapper(from).setSpawnerEntityType(EntityType.PIG);
    }

    @Override
    protected String getPropertyName() {
        return "spawner entity type";
    }

    @Override
    public Class<? extends EntityData> getReturnType() {
        return EntityData.class;
    }
}
