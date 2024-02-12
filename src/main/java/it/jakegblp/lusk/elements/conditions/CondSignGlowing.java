package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;

@Name("Sign - is Glowing")
@Description("Checks if a sign has glowing text.")
@Examples({"if target block is glowing:"})
@Since("1.0.3")
public class CondSignGlowing extends PropertyCondition<Block> {

    static {
        register(CondSignGlowing.class, "(glowing|[a] glowing sign)", "block");
    }

    @Override
    public boolean check(Block block) {
        if (block.getState() instanceof Sign sign) {
            return sign.isGlowingText();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "a glowing sign";
    }

}