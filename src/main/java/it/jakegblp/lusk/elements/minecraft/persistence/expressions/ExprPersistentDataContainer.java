package it.jakegblp.lusk.elements.minecraft.persistence.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

@Name("Persistence - Persistent Data of X")
@Description("""
        Gets the custom tag container capable of storing tags on the provided holders.
        Note: the tags stored on this container are all stored under their own custom namespace therefore modifying default tags using this PersistentDataHolder is impossible.
        """)
@Examples("send persistent data of player")
@Since("1.4")
public class ExprPersistentDataContainer extends SimplerPropertyExpression<PersistentDataHolder, PersistentDataContainer> {

    static {
        register(ExprPersistentDataContainer.class, PersistentDataContainer.class, "persistent data [container]", "persistentdataholders");
    }

    @Override
    public PersistentDataContainer convert(PersistentDataHolder from) {
        return from.getPersistentDataContainer();
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void set(PersistentDataHolder from, PersistentDataContainer to) {
        delete(from);
        to.copyTo(from.getPersistentDataContainer(), false);
    }

    @Override
    public void delete(PersistentDataHolder from) {
        PersistentDataContainer container = from.getPersistentDataContainer();
        for (NamespacedKey key : container.getKeys()) {
            container.remove(key);
        }
    }

    @Override
    public void reset(PersistentDataHolder from) {
        delete(from);
    }

    @Override
    protected String getPropertyName() {
        return "persistent data";
    }

    @Override
    public Class<? extends PersistentDataContainer> getReturnType() {
        return PersistentDataContainer.class;
    }
}
