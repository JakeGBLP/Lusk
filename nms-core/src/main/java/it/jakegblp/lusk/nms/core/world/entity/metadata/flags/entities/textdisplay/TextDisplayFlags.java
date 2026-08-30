package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entities.textdisplay;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BooleanFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.EnumFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import lombok.NoArgsConstructor;
import org.bukkit.entity.TextDisplay;
import org.jspecify.annotations.NullMarked;

@NoArgsConstructor
@NullMarked
public class TextDisplayFlags extends FlagByte<TextDisplay> implements Copyable<TextDisplayFlags> {
    public static BooleanFlag<TextDisplay> HAS_SHADOW = Flag.booleanFlag("HAS_SHADOW", TextDisplay::isShadowed, TextDisplay::setShadowed);
    public static BooleanFlag<TextDisplay> SEE_THROUGH = Flag.booleanFlag("SEE_THROUGH", TextDisplay::isSeeThrough, TextDisplay::setSeeThrough);
    public static BooleanFlag<TextDisplay> DEFAULT_BACKGROUND = Flag.booleanFlag("DEFAULT_BACKGROUND", TextDisplay::isDefaultBackground, TextDisplay::setDefaultBackground);
    public static EnumFlag<TextDisplay, TextDisplay.TextAlignment> ALIGNMENT = Flag.enumFlag("ALIGNMENT", TextDisplay.TextAlignment.class, TextDisplay::getAlignment, TextDisplay::setAlignment);

    @SuppressWarnings("unchecked")
    private static final Flag<TextDisplay, ?, ?>[] FLAGS = new Flag[]{HAS_SHADOW, SEE_THROUGH, DEFAULT_BACKGROUND, ALIGNMENT};

    public static Flag<TextDisplay, ?, ?>[] getFlags() {
        return FLAGS.clone();
    }

    public TextDisplayFlags(TextDisplay entity) {
        super(entity);
    }

    public TextDisplayFlags(byte value) {
        super(value);
    }

    @Override
    public Class<TextDisplay> getFlagHolderClass() {
        return TextDisplay.class;
    }

    @Override
    public Flag<TextDisplay, ?, ?>[] getConstants() {
        return getFlags();
    }

    public boolean hasShadow() {
        return get(HAS_SHADOW);
    }

    public void setShadow(boolean value) {
        set(HAS_SHADOW, value);
    }

    public boolean isSeeThrough() {
        return get(SEE_THROUGH);
    }

    public void setSeeThrough(boolean value) {
        set(SEE_THROUGH, value);
    }

    public boolean hasDefaultBackground() {
        return get(DEFAULT_BACKGROUND);
    }

    public void setDefaultBackground(boolean value) {
        set(DEFAULT_BACKGROUND, value);
    }

    public TextDisplay.TextAlignment getAlignment() {
        return get(ALIGNMENT);
    }

    public void setAlignment(TextDisplay.TextAlignment value) {
        set(ALIGNMENT, value);
    }

    @Override
    public TextDisplayFlags copy() {
        return new TextDisplayFlags(flags);
    }
}
