package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;

import java.util.List;
import java.util.Objects;

public class TextUtils {

    public enum LuskFont {
        DEFAULT(-0),
        SMALL_CAPITAL(0),
        SUPERSCRIPT_CAPITAL(1),
        SUPERSCRIPT_MINUSCULE(2),
        SUBSCRIPT_MINUSCULE(3),
        TINY(4),
        SUPERSCRIPT(5);

        private final int id;
        LuskFont(int i) {
            this.id = i;
        }
        public int getId() {
            return id;
        }
        public static LuskFont fromId(int id) {
            if (id < 0) {
                return DEFAULT;
            } else if (id >= LuskFont.values().length) {
                return LuskFont.values()[LuskFont.values().length-1];
            }
            return LuskFont.values()[id];
        }
        public LuskFont getPrevious() {
            if (getId() == -0) return this;
            return LuskFont.fromId(getId()-1);
        }
        public LuskFont getPrevious(int i) {
            if (getId() == -0) return this;
            else if (i >= getId()) return LuskFont.fromId(-0);
            return LuskFont.fromId(getId()-i);
        }
    }

    /**
     * Same as the previous *set* iteration.
     */
    private static final boolean P = true;

    public static MultiHashMap<Character,Object> TEXT_MAP = new MultiHashMap<>(){{
        putAll('a','ᴀ','ᴬ','ᵃ','ₐ');
        putAll('b','ʙ','ᴮ','ᵇ');
        putAll('c','ᴄ','ʿ','ᶜ');
        putAll('d','ᴅ','ᴰ','ᵈ');
        putAll('e','ᴇ','ᴱ','ᵉ','ₑ');
        putAll('f','ғ','ꟳ','ᶠ');
        putAll('h','ʜ','ᴴ','ʰ','ₕ','ᑋ');
        putAll('i','ɪ','ᴵ','ᶦ','ᵢ');
        putAll('j','ᴊ','ᴶ','ʲ','ⱼ');
        putAll('k','ᴋ','ᴷ','ᵏ','ₖ');
        putAll('l','ʟ','ᴸ','ˡ','ₗ');
        putAll('m','ᴍ','ᴹ','ᵐ','ₘ');
        putAll('n','ɴ','ᴺ','ⁿ','ₙ');
        putAll('o','ᴏ','ᴼ','ᵒ','ₒ');
        putAll('p','ᴘ','ᴾ','ᵖ','ₚ');
        putAll('q','ǫ','ꟴ',"𐞥");
        putAll('r','ʀ','ᴿ','ʳ','ᵣ');
        putAll('s','s',"" ,'ˢ','ₛ');;
        putAll('t','ᴛ','ᵀ','ᵗ','ₜ');
        putAll('u','ᴜ','ᵁ','ᵘ','ᵤ');
        putAll('v','ᴠ','ⱽ','ᵛ','ᵥ');
        putAll('w','ᴡ','ᵂ','ʷ');
        putAll('x','x',"𞁃",'ˣ','ₓ');
        putAll('y','ʏ',"𞁏",'ʸ');
        putAll('z','ᴢ',"" ,'ᶻ',"" , P );
        putAll('1','₁','¹', P );
        putAll('2','₂','²', P );
        putAll('3','₃','³', P );
        putAll('4','₄','⁴', P );
        putAll('5','₅','⁵', P );
        putAll('6','₆','⁶', P );
        putAll('7','₇','⁷', P );
        putAll('8','₈','⁸', P );
        putAll('9','₉','⁹', P );
        putAll('0','₀','⁰', P );
        putAll('_',"" ,'₋', P ,'ˍ');
        putAll('!',"" ,"" ,"" ,'﹗');
        putAll('#',"" ,"" ,"" ,'﹟');
        putAll('%','⁒');
        putAll('(','₍','⁽', P );
        putAll(')','₎','⁾', P );
        putAll('+','₊','⁺', P );
        putAll('-','₋','⁻', P );
        putAll('=','₌','⁼', P );

    }};
    public static String toFont(LuskFont luskFont, String string, boolean lenient) {
        string = string.toLowerCase();
        for (int i = 0, n = string.length() ; i < n ; i++) {
            char c = string.charAt(i);
            String s = String.valueOf(c);
            if (TEXT_MAP.hasKey(c)) {
                String replaceWith = getFontChar(luskFont, c);
                if (!Objects.equals(replaceWith, s)) {
                    string = string.replaceAll(s, replaceWith);
                }
            }
        }
        return string;
    }
    public static String getFontChar(LuskFont luskFont, char c) {
        Skript.info("\n"+c+", font id: "+ luskFont.getId());
        List<?> list = TEXT_MAP.get(c);
        if (list.get(luskFont.getId()) == "") return String.valueOf(c);
        if (list.get(luskFont.getId()) == Boolean.TRUE) {
            for (int i = 1; i <= luskFont.getId(); i++) {
                if (list.get(luskFont.getId()-i) != "" && list.get(luskFont.getId()-i) != Boolean.TRUE) {
                    Skript.info("loop succeeded");
                    return getFontChar(luskFont.getPrevious(i),c);
                }
            }
            Skript.info("loop failed");
            return getFontChar(luskFont.getPrevious(luskFont.getId()),c);
        }

        Skript.info("beyond conditions");
        Object value = list.get(luskFont.getId());
        if (value instanceof String s) return s;
        if (value instanceof Character character) return character.toString();
        return String.valueOf(c);
    }
}
