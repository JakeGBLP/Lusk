package it.jakegblp.lusk.elements.minecraft.mixed.conditions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_18_2;

@Name("Entity/Block/BlockState/Item - is Collidable")
@Description("""
        Checks whether the provided living entities, blocks, blockstates and items are collidable.
        
        Info:
        - LivingEntity: Some entities might be exempted from the collidable rule of this entity. Use the "Entity - Collidable Exemptions" expression to get these. Please note that this method returns only the custom collidable state, not whether the entity is non-collidable for other reasons such as being dead.
        """)
@Examples("")
@Since("1.3")
public class CondIsCollidable extends PropertyCondition<Object> {

    static {
        register(CondIsCollidable.class,
                "collidable [with]", "livingentities"+((MINECRAFT_1_18_2 && isPaper()) ? "/blocks/blockstates/itemtypes" : ""));
    }

    @Override
    public boolean check(Object value) {
        if (value instanceof LivingEntity livingEntity) {
            return livingEntity.isCollidable();
        }
        if (!MINECRAFT_1_18_2 || !isPaper()) return false;
        if (value instanceof BlockState blockState) {
            return blockState.isCollidable();
        } else if (value instanceof Block block) {
            return block.isCollidable();
        } else if (value instanceof ItemType itemType && itemType.getMaterial().isBlock()) {
            return itemType.getMaterial().isCollidable();
        }
        return false;
    }

    @Override
    protected String getPropertyName() {
        return "placed";
    }
}
