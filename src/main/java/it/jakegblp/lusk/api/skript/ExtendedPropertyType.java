package it.jakegblp.lusk.api.skript;

import ch.njol.skript.conditions.base.PropertyCondition.PropertyType;

/**
 * An extended version of {@link PropertyType PropertyType}.
 */
public enum ExtendedPropertyType {
    BE("(is|are)","(isn't|is not|aren't|are not)"),
    CAN("can","(can't|cannot|can not)"),
    HAVE("(has|have)","(doesn't|does not|do not|don't) have"),
    WILL("will","(will (not|neither)|won't)"),
    WOULD("would","(would (not|neither)|wouldn't)"),
    COULD("could","(could (not|neither)|couldn't)"),
    SHOULD("should","(should (not|neither)|shouldn't)"),;

    private final String normal, negated;

    ExtendedPropertyType(String normal, String negated) {
        this.normal = normal;
        this.negated = negated;
    }

    public String getPattern(boolean isNegated) {
        return isNegated ? negated : normal;
    }
}
