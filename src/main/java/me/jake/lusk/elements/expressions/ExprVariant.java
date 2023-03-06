package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.jake.lusk.utils.Utils;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity Variant")
@Description("Returns the Variant of an Entity")
@Examples({"broadcast variant of event-entity"})
@Since("1.0.0")
public class ExprVariant extends SimplePropertyExpression<LivingEntity, String> {
    static {
        register(ExprVariant.class, String.class, "variant", "livingentity");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(LivingEntity e) {
        if (e != null) {
            if (e instanceof Axolotl) {
                return ((Axolotl) e).getVariant().toString();
            } else if (e instanceof Parrot) {
                return ((Parrot) e).getVariant().toString();
            } else if (e instanceof MushroomCow) {
                return ((MushroomCow) e).getVariant().toString();
            } else if ((Utils.serverVersion.getMain() == 1 && Utils.serverVersion.getMajor() > 19) || Utils.serverVersion.getMain() > 1) {
                if (e instanceof Frog) {
                    return ((Frog) e).getVariant().toString();
                }
            }
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity variant";
    }
}