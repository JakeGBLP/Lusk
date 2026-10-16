package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockState;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("BlockState - is Placed")
@Description("""
Checks whether the provided blockstates are placed in a world, if not, they are 'virtual' (e. g. on an item).

Some syntaxes will not work if the block state isn't placed in the world, an example can be made of the type of blockstate (or blockdata, it applies to both) that an object-altering effect takes: a provided blockstate (block, or item) will only be modified if the required type is a "blockstateholder".
""")
@Example("if {_blockState} is placed:")
@Since("1.3")
public class CondBlockStateIsPlaced extends PropertyCondition<BlockState> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondBlockStateIsPlaced.class, CondBlockStateIsPlaced::new)
                .property("blockstates", PropertyType.CAN, "placed")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(BlockState value) {
        return value.isPlaced();
    }

    @Override
    protected String getPropertyName() {
        return "placed";
    }
}