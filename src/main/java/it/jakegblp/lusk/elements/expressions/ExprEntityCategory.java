package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Category")
@Description("A classification of entities which may behave differently than others or be affected uniquely by enchantments and potion effects among other things.")
@Examples({"broadcast entity category of target"})
@Since("1.0.2")
public class ExprEntityCategory extends SimplePropertyExpression<LivingEntity, EntityCategory> {
    static {
        register(ExprEntityCategory.class, EntityCategory.class, "[entity] category", "livingentity");
    }

    @Override
    public @NotNull Class<? extends EntityCategory> getReturnType() {
        return EntityCategory.class;
    }

    @Override
    @Nullable
    public EntityCategory convert(LivingEntity e) {
        if (e != null) {
            return e.getCategory();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity category";
    }
}