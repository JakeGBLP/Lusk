package it.jakegblp.lusk.nms.core.world.entity.flags.textdisplay;

import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import org.bukkit.entity.TextDisplay;

public class TextDisplayFlags extends FlagByte<TextDisplayFlag, TextDisplay, Object> {

    public TextDisplayFlags() {
    }

    public TextDisplayFlags(byte value) {
        super(value);
    }

    @Override
    public Class<TextDisplayFlag> getBitFlagClass() {
        return TextDisplayFlag.class;
    }

    @Override
    public Class<TextDisplay> getFlagHolderClass() {
        return TextDisplay.class;
    }

    public boolean hasShadow() {
        return (boolean) get(TextDisplayFlag.HAS_SHADOW);
    }

    public void setShadow(boolean value) {
        set(TextDisplayFlag.HAS_SHADOW, value);
    }

    public boolean isSeeThrough() {
        return (boolean) get(TextDisplayFlag.SEE_THROUGH);
    }

    public void setSeeThrough(boolean value) {
        set(TextDisplayFlag.SEE_THROUGH, value);
    }

    public boolean hasDefaultBackground() {
        return (boolean) get(TextDisplayFlag.DEFAULT_BACKGROUND);
    }

    public void setDefaultBackground(boolean value) {
        set(TextDisplayFlag.DEFAULT_BACKGROUND, value);
    }

    public TextDisplay.TextAlignment getAlignment() {
        return (TextDisplay.TextAlignment) get(TextDisplayFlag.ALIGNMENT);
    }

    public void setAlignment(TextDisplay.TextAlignment value) {
        set(TextDisplayFlag.ALIGNMENT, value);
    }

    @Override
    public FlagByte<TextDisplayFlag, TextDisplay, Object> clone() {
        return new TextDisplayFlags(flags);
    }
}
