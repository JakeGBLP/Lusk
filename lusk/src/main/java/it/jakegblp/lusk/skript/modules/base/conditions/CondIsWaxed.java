package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class CondIsWaxed extends PropertyCondition<BlockState> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondIsWaxed.class, CondIsWaxed::new)
                .property("blockstates", PropertyType.BE, "(waxed|:editable)")
                .register(syntaxRegistry);
    }

    private boolean editable;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        var result = super.init(expressions, matchedPattern, isDelayed, parseResult);
        editable = parseResult.hasTag("editable");
        setNegated(editable != isNegated());
        return result;
    }

    @Override
    public boolean check(BlockState value) {
        return value instanceof Sign sign && sign.isWaxed();
    }

    @Override
    protected String getPropertyName() {
        return editable ? "editable" : "waxed";
    }
}
