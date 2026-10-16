package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.entity.EntityData;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import it.jakegblp.lusk.skript.utils.BlockStateHolder;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Spawner - Entity Type")
@Description("""
        Gets the spawner entity type of the provided spawners or trial spawners.

        Works with Items.
        Works with Spawner Minecarts

        Can be set and deleted.
        """)
@Example("set spawner entity type of {_block} to zombie")
@Keywords({"spawner", "creature", "minecart"})
@Since({"1.3.4", "2.0.0 (Removed Bad Trial Spawner Impl, Removed Reset Impl)"})
public class ExprSpawnerEntityType extends SimplerPropertyExpression<Object, EntityData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprSpawnerEntityType.class, ExprSpawnerEntityType::new, EntityData.class)
                .propertyOf("spawner (entity|creature) type", "blockstateholders/entities")
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable EntityData convert(Object from) {
        return switch (from) {
            case BlockStateHolder<?> holder ->
                    holder.getBlockState(CreatureSpawner.class).map(spawner -> EntityUtils.toSkriptEntityData(spawner.getSpawnedType())).orElse(null);
            case SpawnerMinecart spawnerMinecart -> EntityUtils.toSkriptEntityData(spawnerMinecart.getSpawnedType());
            default -> throw new IllegalStateException("Unexpected value: " + from);
        };
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[] {EntityData.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(Object from, EntityData to) {
        switch (from) {
            case BlockStateHolder<?> holder ->
                    holder.editBlockState(CreatureSpawner.class, creatureSpawner -> creatureSpawner.setSpawnedType(EntityUtils.toBukkitEntityType(to)));
            case SpawnerMinecart spawnerMinecart -> spawnerMinecart.setSpawnedType(EntityUtils.toBukkitEntityType(to));
            default -> throw new IllegalStateException("Unexpected value: " + from);
        }
    }

    @Override
    public void delete(Object from) {
        set(from, null);
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