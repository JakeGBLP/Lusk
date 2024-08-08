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
import it.jakegblp.lusk.utils.TextUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.TextUtils.toFont;

@Name("Small Capital Case")
@Description("Returns the given string with the small font.\n'Strict' allows uppercase SMALL to be included.")
@Examples({"broadcast player's name in small caps"})
@Since("1.0.0")
public class ExprCases extends SimpleExpression<String> {                                                                                                               //
    /*
    https://en.wikipedia.org/wiki/Modifier_letter_left_half_ring
    https://rupertshepherd.info/resource_pages/superscript-letters-in-unicode
    https://lingojam.com/SuperscriptGenerator
    > https://en.wikipedia.org/wiki/Unicode_subscripts_and_superscripts
    https://www.compart.com/en/unicode/block/U+2070


     */

                                                                                                                                                                        //abcdefghijklmnopqrstuvwxyz1234567890_!#%)(+-/\<>:;"'[]?
    public static Object[] NORMAL                =    {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0','_','!','#','%','(',')','+','-','=','/','<','>',':',';','"','\'','[',']','?'};
    public static Object[] SMALL                 =    {'ᴀ','ʙ','ᴄ','ᴅ','ᴇ','ғ','ɢ','ʜ','ɪ','ᴊ','ᴋ','ʟ','ᴍ','ɴ','ᴏ','ᴘ','ǫ','ʀ','s','ᴛ','ᴜ','ᴠ','ᴡ','x','ʏ','ᴢ','₁','₂','₃','₄','₅','₆','₇','₈','₉','₀','ˍ',' ',' ','⁒','₍','₎' ,'₊','₋','₌'};
    public static Object[] SUBSCRIPT_MINUSCULE   =    {'ᵃ','ᵇ','ᶜ','ᵈ','ᵉ','ᶠ' ,'ᵍ','ʰ','ᶦ','ʲ','ᵏ','ˡ','ᵐ','ⁿ' ,'ᵒ','ᵖ',"𐞥",'ʳ','ˢ','ᵗ','ᵘ','ᵛ','ʷ','ˣ','ʸ','ᶻ'};
    public static Object[] SUPERSCRIPT_CAPITAL   =    {'ᴬ','ᴮ','ʿ','ᴰ','ᴱ','ꟳ','ᴳ','ᴴ','ᴵ','ᴶ','ᴷ','ᴸ','ᴹ','ᴺ','ᴼ','ᴾ','ꟴ','ᴿ','꟱','ᵀ','ᵁ','ⱽ','ᵂ'};
    public static Object[] SUPERSCRIPT_MINUSCULE =    {'ₐ',             'ₑ',        'ₕ','ᵢ','ⱼ' ,'ₖ','ₗ','¹','²','³','⁴','⁵','⁶','⁷','⁸','⁹','⁰','⁽','⁾','⁺','⁻','⁼'};
    public static Object[] TINY                  =    {'ᶻ','ˍ','﹗','﹟'};

    public static String toSmallFont(String string, boolean strict) {
        String regex = strict ? "[a-zA-Z]" : "[a-z]";
        for (String letter : string.split("")) {
            if (letter.matches(regex)) {
                string = string.replaceAll(letter, String.valueOf(SMALL[(Character.getNumericValue(letter.charAt(0)) - 10)]));
            }
        }
        return string;
    }

    static {
        Skript.registerExpression(ExprCases.class, String.class, ExpressionType.SIMPLE,
                "%string% in [strict:(strict|fully)] small (font|[upper[ ]]case|cap(s|ital[ case]))",
                "[strict:(strict|fully)] small (font|[upper[ ]]case|cap(s|ital[ case])) %string%");
    }
    "%strings% in [strict|:lenient] small (caps|capital case)",
    "%strings% in [:upper|:lower|:title] super[ ]script case",
    "%strings% in [strict|:lenient] sub[ ]script case",


    DEFAULT(-0),
    SMALL_CAPITAL(0),
    SUPERSCRIPT_CAPITAL(1),
    SUPERSCRIPT_MINUSCULE(2),
    SUBSCRIPT_MINUSCULE(3),
    TINY(4),
    SUPERSCRIPT(5);
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
        if (s != null) return new String[]{toFont(TextUtils.LuskFont.SMALL_CAPITAL, s,false)};
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
