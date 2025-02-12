package it.jakegblp.lusk.elements.minecraft.namespacedkey.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import it.jakegblp.lusk.utils.LuskUtils;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;

public class NamespacedKeyClassInfos {
    static {
        if (Classes.getExactClassInfo(NamespacedKey.class) == null) {
            Classes.registerClass(new ClassInfo<>(NamespacedKey.class, "namespacedkey")
                    .user("namespaced ?keys?")
                    .name("Namespaced Key")
                    .description("""
                            A String based key which consists of a namespace and a key; used to declare and specify game objects in Minecraft without without potential ambiguity or conflicts.
                            
                            Namespaces may only contain lowercase alphanumeric characters, periods, underscores, and hyphens.
                            Keys may only contain lowercase alphanumeric characters, periods, underscores, hyphens, and forward slashes.
                            
                            More Info: [**Resource Location**](https://minecraft.wiki/w/Resource_location)
                            """)
                    .since("1.4")
                    .parser(new Parser<>() {
                        @Override
                        public @Nullable NamespacedKey parse(String s, ParseContext context) {
                            return LuskUtils.getNamespacedKey(s);
                        }

                        @Override
                        public String toString(NamespacedKey o, int flags) {
                            return o.toString();
                        }

                        @Override
                        public String toVariableNameString(NamespacedKey o) {
                            return o.toString();
                        }
                    })
                    .serializer(new Serializer<>() {
                        @Override
                        public @NotNull Fields serialize(NamespacedKey namespacedKey) {
                            Fields fields = new Fields();
                            fields.putObject("key", namespacedKey.toString());
                            return fields;
                        }

                        @Override
                        public void deserialize(NamespacedKey o, Fields f) {
                        }

                        @Override
                        protected NamespacedKey deserialize(Fields fields) throws StreamCorruptedException {
                            String key = fields.getObject("key", String.class);
                            if (key == null) {
                                throw new StreamCorruptedException("NamespacedKey string is null");
                            }
                            return NamespacedKey.fromString(key);
                        }

                        @Override
                        public boolean mustSyncDeserialization() {
                            return true;
                        }

                        @Override
                        protected boolean canBeInstantiated() {
                            return false;
                        }
                    }));
        }
    }
}