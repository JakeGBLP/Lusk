package it.jakegblp.lusk.elements.version.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.vdurmont.semver4j.SemverException;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Utils.Version;

@Name("Version - is Valid")
@Description("Checks if the string is a valid version")
@Examples({"if \"1.19.2\" is a valid version:\n\tbroadcast version \"1.19.2\""})
@Since("1.0.0")
public class CondVersionValid extends PropertyCondition<String> {

    static {
        register(CondVersionValid.class, "[a] valid version[s]", "strings");
    }

    @Override
    public boolean check(String version) {
        try {
            Version(version);
        } catch (SemverException e) {
            return false;
        }
        return true;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "valid version";
    }
}