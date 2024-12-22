package it.jakegblp.lusk.elements.minecraft.blocks.bell.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Bell - is Ringing")
@Description("Checks if a bell is ringing.")
@Examples({"on bell ring:\n\tif event-block is ringing:\n\t\tcancel event\n\t\t"})
@Since("1.0.3, 1.3 (Plural, BlockState)")
@SuppressWarnings("unused")
public class CondBellRinging extends PropertyCondition<Object> {
    static {
        register(CondBellRinging.class, "(ringing|shaking)", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isBellRinging();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "ringing";
    }
}