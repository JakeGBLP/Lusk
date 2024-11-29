package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.api.wrappers.BlockWrapper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerbosePropertyExpression;

@Name("Entity - Waterlogged Property")
@Description("Returns whether or not one or more blocks, blockstates or blockdatas are waterlogged.\nCan be set.")
@Examples({"broadcast waterlogged property of block"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBlockWaterLogged extends SimplePropertyExpression<Object,Boolean> {

    static {
        registerVerbosePropertyExpression(ExprBlockWaterLogged.class, Boolean.class, "[block]","[is] water[ |-]log[ged]","blocks/blockstates/blockdatas");
    }

    @Override
    public @Nullable Boolean convert(Object from) {
        return new BlockWrapper(from).isWaterLogged();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[] {Boolean.class} : null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof Boolean bool) {
            getExpr().stream(event).map(BlockWrapper::new).forEach(blockWrapper -> blockWrapper.setWaterLogged(bool));
        }
    }

    @Override
    protected String getPropertyName() {
        return "block is waterlogged property";
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}