package it.jakegblp.lusk.elements.minecraft.persistence.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

@Name("Persistence - Persistent Data of X")
@Description("""
        Gets the custom tag container capable of storing tags on the provided holders.
        Note: the tags stored on this container are all stored under their own custom namespace therefore modifying default tags using this PersistentDataHolder is impossible.
        """)
public class ExprPersistentDataContainer extends SimplePropertyExpression<PersistentDataHolder, PersistentDataContainer> {

    static {
        register(ExprPersistentDataContainer.class, PersistentDataContainer.class, "persistent data [container]", "persistentdataholders");
    }

    @Override
    public PersistentDataContainer convert(PersistentDataHolder from) {
        return from.getPersistentDataContainer();
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
