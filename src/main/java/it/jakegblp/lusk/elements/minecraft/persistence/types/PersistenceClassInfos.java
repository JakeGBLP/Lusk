package it.jakegblp.lusk.elements.minecraft.persistence.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.enums.PersistentTagType;
import it.jakegblp.lusk.api.skript.EnumWrapper;
import it.jakegblp.lusk.utils.PersistenceUtils;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

public class PersistenceClassInfos {
    static {
        EnumWrapper<PersistentTagType> PERSISTENT_TAG_TYPE_ENUM = new EnumWrapper<>(PersistentTagType.class, "persistent", "tag");
        Classes.registerClass(PERSISTENT_TAG_TYPE_ENUM.getClassInfo("persistenttagtype")
                .user("persistent ?tag ?types?")
                .name("Persistence - Tag Type")
                .description("All the Persistent Tag Type.") // add example
                .since("1.4"));
        if (Classes.getExactClassInfo(PersistentDataHolder.class) == null) {
            Classes.registerClass(new ClassInfo<>(PersistentDataHolder.class, "persistentdataholder")
                    .user("(persistent ?)?data ?holders?")
                    .name("Persistent Data Holder")
                    .description("A Persistent Data Holder.") // add example
                    .since("1.4"));
        }
        if (Classes.getExactClassInfo(PersistentDataContainer.class) == null) {
            Classes.registerClass(new ClassInfo<>(PersistentDataContainer.class, "persistentdatacontainer")
                    .user("((persistent ?)?data ?container|persistent ?data)s?")
                    .name("Persistent Data Container")
                    .description("A Persistent Data Container.") // add example
                    .since("1.4")
                    .parser(new Parser<>() {
                        @Override
                        public @Nullable PersistentDataContainer parse(String s, ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public String toString(PersistentDataContainer o, int flags) {
                            return PersistenceUtils.asString(o);
                        }

                        @Override
                        public String toVariableNameString(PersistentDataContainer o) {
                            return "PersistentDataContainer [ " + PersistenceUtils.asString(o) + " ]";
                        }
                    }));
        }
    }
}
