package it.jakegblp.lusk.elements.version.types;

import com.vdurmont.semver4j.Semver;
import it.jakegblp.lusk.api.enums.GenericRelation;

import static it.jakegblp.lusk.utils.SkriptUtils.registerComparator;

public class VersionComparators {

    static {
        registerComparator(Semver.class, Semver.class, (version1, version2) ->
                version1.isGreaterThan(version2) ? GenericRelation.GREATER :
                        version2.isGreaterThan(version1) ? GenericRelation.SMALLER : GenericRelation.EQUAL);
    }

    public VersionComparators() {
    }
}
