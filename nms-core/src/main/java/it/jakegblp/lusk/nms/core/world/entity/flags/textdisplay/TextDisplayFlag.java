package it.jakegblp.lusk.nms.core.world.entity.flags.textdisplay;

import it.jakegblp.lusk.nms.core.world.entity.SemiBooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum TextDisplayFlag implements SemiBooleanFlag<Object> {
    HAS_SHADOW(0x01),
    SEE_THROUGH(0x02),
    DEFAULT_BACKGROUND(0x04),
    ALIGNMENT(0x08) {
        @Override
        @Nullable
        public TextDisplay.TextAlignment decode(int v) {
            return switch (v) {
                case 0 -> TextDisplay.TextAlignment.CENTER;
                case 1, 3 -> TextDisplay.TextAlignment.LEFT;
                case 2 -> TextDisplay.TextAlignment.RIGHT;
                default -> null;
            };
        }

        @Override
        public int encode(Object value) {
            return ((TextDisplay.TextAlignment) value).ordinal();
        }


        @Override
        public Class<Object> getValueClass() {
            return (Class<Object>)(Class<?>)TextDisplay.TextAlignment.class;
        }
    };

    private final int mask;
}
