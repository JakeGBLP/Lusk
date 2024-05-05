package it.jakegblp.lusk.elements.minecraft.blocks.sign.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;

@Name("Sign - is Editable")
@Description("Checks if a sign can be edited.")
@Examples({"if target block is editable:"})
@Since("1.0.3")
public class CondSignEditable extends PropertyCondition<Block> {

    static {
        register(CondSignEditable.class, "editable", "block");
    }

    @Override
    public boolean check(Block block) {
        return block.getState() instanceof Sign sign && !sign.isWaxed();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "editable";
    }

}