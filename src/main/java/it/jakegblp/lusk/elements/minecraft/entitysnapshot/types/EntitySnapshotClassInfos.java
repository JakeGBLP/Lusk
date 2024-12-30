package it.jakegblp.lusk.elements.minecraft.entitysnapshot.types;

import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;

import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT;
import static it.jakegblp.lusk.utils.Constants.HAS_ENTITY_SNAPSHOT_GET_AS_STRING;

@SuppressWarnings("UnstableApiUsage")
public class EntitySnapshotClassInfos {
    static {
        if (HAS_ENTITY_SNAPSHOT && Classes.getExactClassInfo(EntitySnapshot.class) == null) {
            Classes.registerClass(new ClassInfo<>(EntitySnapshot.class, "entitysnapshot")
                    .user("entity ?snapshots?")
                    .name("Entity Snapshot")
                    .description("""
                            Represents an immutable copy of an entity's state. Can be used at any time to create an instance of the stored entity.
                            
                            Requires 1.20.5 to be stored and parsed.
                            """)
                    .since("1.3")
                    .requiredPlugins("1.20.2+, 1.20.5+ (for storing)")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public EntitySnapshot parse(final @NotNull String s, final @NotNull ParseContext context) {
                            try {
                                return Bukkit.getEntityFactory().createEntitySnapshot(s);
                            } catch (IllegalArgumentException ignored) {
                            }
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return HAS_ENTITY_SNAPSHOT_GET_AS_STRING;
                        }

                        @Override
                        public @NotNull String toString(final EntitySnapshot s, final int flags) {
                            return "entity snapshot of type "+ EntityUtils.toSkriptEntityData(s.getEntityType());
                        }

                        @Override
                        public @NotNull String toVariableNameString(final EntitySnapshot s) {
                            return s.getAsString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final EntitySnapshot s) {
                            return toString(s, 0) + " entity snapshot (" + s + ")";
                        }
                    })
                    .serializer(new Serializer<>() {
                        @Override
                        public Fields serialize(EntitySnapshot o) {
                            Fields fields = new Fields();
                            fields.putObject("asString", o.getAsString());
                            return fields;
                        }

                        @Override
                        public void deserialize(EntitySnapshot o, Fields f) {
                            assert false;
                        }

                        @Override
                        protected EntitySnapshot deserialize(Fields fields) throws StreamCorruptedException {
                            String asString = fields.getObject("asString", String.class);
                            if (asString != null)
                                return Bukkit.getEntityFactory().createEntitySnapshot(asString);
                            return null;
                        }

                        @Override
                        public boolean mustSyncDeserialization() {
                            return true;
                        }

                        @Override
                        protected boolean canBeInstantiated() {
                            return HAS_ENTITY_SNAPSHOT_GET_AS_STRING;
                        }
                    }));
        }
    }
}
