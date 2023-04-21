package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import org.jetbrains.annotations.NotNull;

@Name("Version - is Valid")
@Description("Checks if the string is a valid version")
@Examples({"if \"1.19.2\" is a valid version:\n\tbroadcast version \"1.19.2\""})
@Since("1.0.0")
public class CondVersionValid extends PropertyCondition<String> {

    static {
        register(CondVersionValid.class, "[a] valid version", "string");
    }

    @Override
    public boolean check(String version) {
        try {
            new Semver(version, Semver.SemverType.LOOSE);
        } catch (SemverException e) {
            return false;
        }
        return true;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "a valid version";
    }
}