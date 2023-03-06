package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import me.jake.lusk.classes.Version;
import org.jetbrains.annotations.NotNull;

@Name("is Faceplanted")
@Description("Checks if the fox is faceplanted.")
@Examples({"on damage of fox:\n\tif victim is faceplanted:\n\t\tcancel event"})
@Since("1.0.0")
public class CondValid extends PropertyCondition<String> {

    static {
        register(CondValid.class, "valid", "string");
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