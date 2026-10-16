package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Collection;

@Name("Block - is Full Block")
@Description("Checks if a block is a full block (like dirt, stone, endstone).\nEssentially checks if a block is 1x1x1 and only has one mesh.")
@Example("if event-block is a full block:")
@Since("1.2.1, 1.3 (BlockStates)")
public class CondFullBlock extends PropertyCondition<Block> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondFullBlock.class, CondFullBlock::new)
                .property("blocks", PropertyType.BE, "[a] full block[s]")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(Block block) {
        Collection<BoundingBox> boxes = block.getCollisionShape().getBoundingBoxes();
        if (boxes.size() != 1) return false;
        BoundingBox box = boxes.iterator().next();
        return box.getWidthX() == 1 && box.getHeight() == 1 && box.getWidthZ() == 1;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "full block";
    }
}