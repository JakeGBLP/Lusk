package it.jakegblp.lusk.elements.text.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Small Capital Case")
@Description("Returns the given string with the small font.\n'Strict' allows uppercase characters to be included.")
@Examples({"broadcast player's name in small caps"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class ExprCases extends SimpleExpression<String> {
    public static Character[] characters = {'ᴀ', 'ʙ', 'ᴄ', 'ᴅ', 'ᴇ', 'ғ', 'ɢ', 'ʜ', 'ɪ', 'ᴊ', 'ᴋ', 'ʟ', 'ᴍ', 'ɴ', 'ᴏ', 'ᴘ', 'ǫ', 'ʀ', 's', 'ᴛ', 'ᴜ', 'ᴠ', 'ᴡ', 'x', 'ʏ', 'ᴢ'};

    public static String toSmallFont(String string, boolean strict) {
        String regex = strict ? "[a-zA-Z]" : "[a-z]";
        for (String letter : string.split("")) {
            if (letter.matches(regex)) {
                string = string.replaceAll(letter, String.valueOf(characters[(Character.getNumericValue(letter.charAt(0)) - 10)]));
            }
        }
        return string;
    }

    static {
        Skript.registerExpression(ExprCases.class, String.class, ExpressionType.COMBINED,
                "%string% in [strict:(strict|fully)] small (font|[upper[ ]]case|cap(s|ital[ case]))",
                "[strict:(strict|fully)] small (font|[upper[ ]]case|cap(s|ital[ case])) %string%");
    }

    private Expression<String> string;

    private boolean strict;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        strict = parseResult.hasTag("strict");
        string = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        String s = string.getSingle(e);
        if (s != null) return new String[]{toSmallFont(s, strict)};
        return new String[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return (strict ? "strict " : "") + "small caps " + (e != null ? string.toString(e, debug) : "");
    }
}