package it.jakegblp.lusk.elements.minecraft.blocks.bell.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import org.jetbrains.annotations.NotNull;

@Name("Bell - is Resonating")
@Description("Checks if a bell is resonating.")
@Examples({"on bell resonate:\n\tif event-block is resonating:\n\t\tcancel event\n\t\t"})
@Since("1.0.3, 1.3 (Plural, BlockState)")
@DocumentationId("11174")
@SuppressWarnings("unused")
public class CondBellResonating extends PropertyCondition<Object> {
    static {
        register(CondBellResonating.class, "resonating", "blocks/blockstates");
    }

    @Override
    public boolean check(Object o) {
        return new BlockWrapper(o).isBellResonating();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "resonating";
    }
}