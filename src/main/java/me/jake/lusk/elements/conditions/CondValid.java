package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.classes.Version;
import org.jetbrains.annotations.NotNull;

@Name("is Valid Version")
@Description("Checks if the string is a valid version")
@Examples({"if \"1.19.2\" is a valid version:\n\tbroadcast version \"1.19.2\""})
@Since("1.0.0")
public class CondValid extends PropertyCondition<String> {

    static {
        register(CondValid.class, "[a] valid version", "string");
    }

    @Override
    public boolean check(String version) {
        if (version != null) {
            return Version.isValid(version);
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "valid version";
    }
}