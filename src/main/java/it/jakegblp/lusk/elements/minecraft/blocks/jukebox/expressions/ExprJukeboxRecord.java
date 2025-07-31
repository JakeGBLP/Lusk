package it.jakegblp.lusk.elements.minecraft.blocks.jukebox.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.BlockWrapper;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.jetbrains.annotations.Nullable;

@Name("Jukebox - Record/Disc")
@Description("Returns the music disc within a jukebox.\nCan be set, reset and deleted.")
@Examples({"broadcast the music disc of {_j}"})
@Since("1.0.3, 1.3 (Plural, Blockstate)")
@DocumentationId("9162")
@SuppressWarnings("unused")
public class ExprJukeboxRecord extends SimplerPropertyExpression<Object,ItemType> {

    static {
        register(ExprJukeboxRecord.class, ItemType.class, "[jukebox] [music] (disc|record)", "blocks/blockstates/itemtypes");
    }

    @Override
    public @Nullable ItemType convert(Object from) {
        return new BlockWrapper(from).getJukeboxRecord();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(Object from, ItemType to) {
        new BlockWrapper(from, true).setJukeboxRecord(to);
    }

    @Override
    public void delete(Object from) {
        set(from, null);
    }

    @Override
    public void reset(Object from) {
        delete(from);
    }

    @Override
    protected String getPropertyName() {
        return "jukebox music record";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}