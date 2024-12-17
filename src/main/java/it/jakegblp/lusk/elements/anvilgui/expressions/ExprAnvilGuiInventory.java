package it.jakegblp.lusk.elements.anvilgui.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.ANVIL_GUI_PREFIX;

@Name("Anvil GUI - Inventory")
@Description("Gets the inventory of one or more anvil guis, this shouldn't be used to edit slots or in non-anvil-gui events.")
@Examples({"set {_anvil} to a new anvil gui"})
@Since("1.3")
public class ExprAnvilGuiInventory extends SimplePropertyExpression<AnvilGuiWrapper, Inventory> {
    static {
        register(ExprAnvilGuiInventory.class, Inventory.class, ANVIL_GUI_PREFIX + " inventory", "anvilguiinventories");
    }

    @Override
    public @NotNull Class<? extends Inventory> getReturnType() {
        return Inventory.class;
    }

    @Override
    @Nullable
    public Inventory convert(AnvilGuiWrapper e) {
        return e.getAnvilGUI().getInventory();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "anvil gui inventory";
    }
}