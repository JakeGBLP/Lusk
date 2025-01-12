package it.jakegblp.lusk.elements.minecraft.blocks.block.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Fluid Level Change Event - new Block Data")
@Description({"Can be set, reset and deleted.\nWhen set, the new blockdata must be of the same material as the one you're replacing;\nwhen reset and deleted, an empty blockdata of that material will be used."})
@Examples({"set new fluid level to water[level=3]"})
@Since("1.3")
public class ExprFluidLevelChangeNewBlockData extends SimpleExpression<BlockData> {

	static {
		Skript.registerExpression(ExprFluidLevelChangeNewBlockData.class, BlockData.class, EVENT_OR_SIMPLE,
				"[the] [new] fluid level [block[ |-]]data");
	}

	@Override
	protected @Nullable BlockData[] get(Event event) {
		return new BlockData[] {((FluidLevelChangeEvent) event).getNewData()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends BlockData> getReturnType() {
		return BlockData.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "the new fluid level block data";
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return getParser().isCurrentEvent(FluidLevelChangeEvent.class);
	}

	@Override
	public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return switch (mode) {
            case SET-> new Class[]{BlockData.class};
			case DELETE, RESET -> new Class[0];
			case ADD, REMOVE, REMOVE_ALL -> null;
        };
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
		if (event instanceof FluidLevelChangeEvent fluidLevelChangeEvent) {
			if (mode == Changer.ChangeMode.SET
					&& delta != null && delta[0] instanceof BlockData blockData
					&& blockData.getMaterial().equals(fluidLevelChangeEvent.getNewData().getMaterial())) {
				fluidLevelChangeEvent.setNewData(blockData);
			} else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
				fluidLevelChangeEvent.setNewData(Material.AIR.createBlockData());
			}
		}
	}
}