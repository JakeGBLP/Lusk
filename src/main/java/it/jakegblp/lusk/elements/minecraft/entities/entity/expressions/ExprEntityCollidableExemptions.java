package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.SimplerPluralPropertyExpression;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;
import java.util.stream.Stream;

import static it.jakegblp.lusk.utils.EntityUtils.getArrayAsUUIDList;

@Name("Entity - Collidable Exemptions")
@Description("""
Gets the list of entities (or their UUIDs) which are exempt from the provided entities' collidable rule and whose collision with this entity will behave the opposite of it.
Can be set, added to, removed from, and deleted.

For example if collidable is true and an entity is in the exemptions then it will not collide with it.
Similarly if collidable is false and an entity is in this set then it will still collide with it.

Note: **these exemptions are not (currently) persistent.**

Note: **the client may predict the collision between itself and another entity, resulting in those exemptions not being accurate for player collisions.**
 **This expression should therefore only be used to exempt non-player entities.**
 **To exempt collisions for a player, use the Team Collision Rule Option in combination with a Scoreboard and a Team.**
""")
@Examples("send collidable exemptions of {_entity}")
@Since("1.3")
public class ExprEntityCollidableExemptions extends SimplerPluralPropertyExpression<LivingEntity, Object> {

    static {
        register(ExprEntityCollidableExemptions.class,Object.class, "collidable exemptions [uuid:uuid[s]]", "livingentities");
    }

    private boolean uuid;

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        uuid = parseResult.hasTag("uuid");
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public Object[] get(LivingEntity livingEntity) {
        Stream<UUID> stream = livingEntity.getCollidableExemptions().stream();
        if (uuid) return stream.toArray();
        else return stream.map(Bukkit::getEntity).toArray(Entity[]::new);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Object[] to) {
        delete(from);
        add(from,to);
    }

    @Override
    public void add(LivingEntity from, Object[] to) {
        from.getCollidableExemptions().addAll(getArrayAsUUIDList(to));
    }

    @Override
    public void remove(LivingEntity from, Object[] to) {
        getArrayAsUUIDList(to).forEach(from.getCollidableExemptions()::remove);
    }

    @Override
    public void delete(LivingEntity from) {
        remove(from,from.getCollidableExemptions().toArray());
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    protected String getPropertyName() {
        return "collidable exemptions " + (uuid ? "uuid" : "");
    }
}