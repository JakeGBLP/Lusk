package it.jakegblp.lusk.elements.minecraft.blocks.banner.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Banner Pattern - From Pattern Type and Color")
@Description("Created a Banner Pattern from a Pattern Type and a (dye) Color.")
@Examples("banner pattern skull pattern type with color blue")
@Since("1.3")
@SuppressWarnings("unused")
public class ExprBannerPattern extends SimpleExpression<Pattern> {

    static {// todo: not related to this expression in particular but make future banner stuff work on banner items
        Skript.registerExpression(ExprBannerPattern.class, Pattern.class, ExpressionType.COMBINED,
                "banner pattern %bannerpatterntype% with color %color%");
    }

    Expression<PatternType> patternTypeExpression;
    Expression<SkriptColor> colorExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        patternTypeExpression = (Expression<PatternType>) expressions[0];
        colorExpression = (Expression<SkriptColor>) expressions[1];
        return true;
    }
    @Nullable
    @Override
    protected Pattern[] get(Event event) {
        PatternType patternType = patternTypeExpression.getSingle(event);
        SkriptColor color = colorExpression.getSingle(event);
        if (patternType == null || color == null || color.asDyeColor() == null) return new Pattern[0];
        return new Pattern[]{new Pattern(color.asDyeColor(),patternType)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Pattern> getReturnType() {
        return Pattern.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "banner pattern "+patternTypeExpression.toString(event, debug) + " with color "+colorExpression.toString(event, debug);
    }
}
