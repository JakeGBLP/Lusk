package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.util.Color;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.ItemUtils.getArmorColor;
import static it.jakegblp.lusk.utils.ItemUtils.setArmorColor;

@Name("Armor - Color")
@Description("Gets the color of the provided armor items.\nThis works for leather armor, horse leather armor and wolf armor.\nCan be set.")
@Examples("if chestplate of player is dyed:")
@Since("1.3.3")
public class ExprArmorColor extends SimplerPropertyExpression<ItemType, Color> {

    static {
        register(ExprArmorColor.class, Color.class, "armor color", "itemtypes");
    }

    @Override
    public @Nullable Color convert(ItemType from) {
        return getArmorColor(from);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(ItemType from, Color to) {
        setArmorColor(from, to);
    }

    @Override
    protected String getPropertyName() {
        return "armor color";
    }

    @Override
    public Class<? extends Color> getReturnType() {
        return Color.class;
    }
}
