package it.jakegblp.lusk.elements.minecraft.entities.endercrystal.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("End Crystal - Hide/Show Bottom Plate")
@Description("Hides or shows the bedrock slate underneath the provided end crystals.")
@Examples("show {_crystal}'s bottom plate")
@Since("1.3")
public class EffEndCrystalBottom extends Effect {

    static {
        Skript.registerEffect(EffEndCrystalBottom.class,
                "(hide|:show) end[er] crystal (bottom|bedrock) [plate|slate] of %entities%",
                "(hide|:show) %entities%'[s] end[er] crystal (bottom|bedrock) [plate|slate]"
        );
    }

    private boolean show;
    private Expression<Entity> entityExpression;

    @Override
    protected void execute(Event event) {
        for (Entity entity : entityExpression.getAll(event)) {
            if (entity instanceof EnderCrystal enderCrystal) {
                enderCrystal.setShowingBottom(show);
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return (show ? "show" : "hide") + " end crystal bedrock plate of " + entityExpression.toString(event,debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) expressions[0];
        show = parseResult.hasTag("show");
        return true;
    }
}
