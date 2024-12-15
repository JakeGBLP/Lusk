package it.jakegblp.lusk.elements.minecraft.entities.endersignal.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Ender Signal - Item")
@Description("""
Gets the item the provided ender signals will display and drop on death.
""")
@Examples("set ender signal item of {_signal} to iron sword")
@Since("1.3")
public class ExprEnderSignalItem extends SimplerPropertyExpression<Entity, ItemType> {

    static {
        register(ExprEnderSignalItem.class, ItemType.class, "ender (signal|eye) item", "entities");
    }

    @Override
    public @Nullable ItemType convert(Entity from) {
        return from instanceof EnderSignal enderSignal ? new ItemType(enderSignal.getItem()) : null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Entity from, ItemType to) {
        if (from instanceof EnderSignal enderSignal) {
            enderSignal.setItem(to.getRandom());
        }
    }

    @Override
    protected String getPropertyName() {
        return "ender signal item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}
