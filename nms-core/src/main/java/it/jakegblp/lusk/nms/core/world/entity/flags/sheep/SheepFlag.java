package it.jakegblp.lusk.nms.core.world.entity.flags.sheep;

import it.jakegblp.lusk.nms.core.world.entity.SemiBooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.DyeColor;

@Getter
@AllArgsConstructor
public enum SheepFlag implements SemiBooleanFlag<Object> {
    WOOL_COLOR(0x0F, 0) {
        @Override
        public DyeColor decode(int bits) {
            return DyeColor.getByWoolData((byte) bits);
        }

        @Override
        public int encode(Object value) {
            return ((DyeColor) value).getWoolData();
        }

        @Override
        public Class<Object> getValueClass() {
            return (Class<Object>)(Class<?>)DyeColor.class;
        }
    },
    IS_SHEARED(0x10, 4);

    private final int mask, shift;

}