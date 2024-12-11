package it.jakegblp.lusk.elements.minecraft.blocks.bell.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.SKRIPT_2_9;

@Name("Bell - is Resonating")
@Description("Checks if a bell is resonating.")
@Examples({"on bell resonate:\n\tif event-block is resonating:\n\t\tcancel event\n\t\t"})
@Since("1.0.3, 1.3 (Plural)")
@SuppressWarnings("unused")
public class CondBellResonating extends PropertyCondition<Object> {
    static {
        if (!SKRIPT_2_9) {
            register(CondBellResonating.class, "resonating", "blocks/blockstates");
        }
    }

    @Override
    public boolean check(Object o) {
        if (o instanceof Block block) {
            if (block.getState(false) instanceof Bell bell) {
                return bell.isResonating();
            }
        } else if (o instanceof Bell bell) {
            return bell.isResonating();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "resonating";
    }
}