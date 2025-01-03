package it.jakegblp.lusk.elements.minecraft.entities.allay.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Allay - Jukebox")
@Description("Gets the jukebox the allay is set to dance to.")
@Examples({"broadcast jukebox of target"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprAllayJukebox extends SimplePropertyExpression<Entity, Block> {
    static {
        register(ExprAllayJukebox.class, Block.class, "jukebox", "entities");
    }

    @Override
    public @NotNull Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    @Nullable
    public Block convert(Entity e) {
        return (e instanceof Allay allay && allay.getJukebox() != null) ? allay.getJukebox().getBlock() : null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "jukebox";
    }
}