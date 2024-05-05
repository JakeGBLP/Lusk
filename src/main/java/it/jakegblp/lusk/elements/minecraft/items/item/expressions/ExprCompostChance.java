package it.jakegblp.lusk.elements.minecraft.items.item.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.utils.Constants;
import it.jakegblp.lusk.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item - Compost Chance")
@Description("Returns an item's chance of successfully composting.")
@Examples({""})
@Since("1.0.1")
public class ExprCompostChance extends SimplePropertyExpression<ItemType, Integer> {
    static {
        register(ExprCompostChance.class, Integer.class, "compost[ing] chance", "itemtypes");
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    @Nullable
    public Integer convert(ItemType i) {
        if (Constants.compostables.contains(i.getMaterial())) return Utils.getCompostChance(i.getMaterial());
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "composting chance";
    }
}