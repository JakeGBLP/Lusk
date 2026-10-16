package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockFace;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Blockface - is Cartesian")
@Description("Returns true if the provided blockfaces are aligned with one of the unit axes in 3D Cartesian space (NORTH, SOUTH, EAST, WEST, UP, DOWN).")
@Examples("if {_blockface} is cartesian:")
@Since("1.2")
public class CondCartesian extends PropertyCondition<BlockFace> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondCartesian.class, CondCartesian::new)
                .property("blockfaces", PropertyType.BE, "cartesian")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(BlockFace blockFace) {
        return blockFace.isCartesian();
    }

    @Override
    protected String getPropertyName() {
        return "cartesian";
    }
}
