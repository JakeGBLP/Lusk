package it.jakegblp.lusk.skript.elements.guardianbeam;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.nms.guardian.GuardianBeam;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;


@Name("All Guardian Beams")
@Description("Returns all active guardian beams")
@Examples({"""
        send all guardian beams to player
        
        remove guardian beam with id (all guardian beams)
        """})
@Keywords({"packets", "packet", "protocol", "dispatch", "sync", "async", "guardian", "beam"})
@Since("1.0.0")
public class ExprAllBeams extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprAllBeams.class, String.class, ExpressionType.SIMPLE,
                "all [guardian] beams [id]['s]");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected String @Nullable [] get(Event event) {
        return GuardianBeam.beams.keySet().toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "all guardian beams";
    }


}
