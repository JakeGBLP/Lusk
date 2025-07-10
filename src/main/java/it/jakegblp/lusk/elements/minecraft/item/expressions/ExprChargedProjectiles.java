package it.jakegblp.lusk.elements.minecraft.item.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import it.jakegblp.lusk.utils.ItemUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Name("Crossbow - Charged Projectiles")
@Description("Returns the Projectile Items charged in the provided crossbows.\nCan be set, added to, removed from, reset and deleted.")
@Examples({"broadcast charged projectiles of tool"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprChargedProjectiles extends PropertyExpression<ItemType, ItemType> {

    static {
        Skript.registerExpression(ExprChargedProjectiles.class, ItemType.class, ExpressionType.PROPERTY,
                "[the] charged projectiles (in|of) %itemtypes%",
                "[the] projectiles charged in %itemtypes%",
                "%itemtypes%'[s] charged projectiles");
    }

    @Override
    protected ItemType[] get(Event event, ItemType[] source) {
        return Arrays.stream(source).flatMap(itemType -> Arrays.stream(ItemUtils.getChargedProjectiles(itemType))).toArray(ItemType[]::new);
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "charged projectiles of " + getExpr().toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        setExpr((Expression<ItemType>) expressions[0]);
        return true;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case ADD, REMOVE, SET -> CollectionUtils.array(ItemType[].class);
            case DELETE, RESET -> CollectionUtils.array();
            case REMOVE_ALL -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemType[] source = getExpr().getArray(event);

        ItemType[] projectiles;
        if ((mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) && delta == null) {
            projectiles = null;
        } else {
            projectiles = (ItemType[]) delta;
        }

        switch (mode) {
            case ADD:
                for (ItemType item : source)
                    ItemUtils.addChargedProjectiles(item, projectiles);
                break;
            case REMOVE:
                for (ItemType item : source)
                    ItemUtils.removeChargedProjectiles(item, projectiles);
                break;
            case SET, DELETE, RESET:
                for (ItemType item : source)
                    ItemUtils.setChargedProjectiles(item, projectiles);
                break;
        }
    }
}