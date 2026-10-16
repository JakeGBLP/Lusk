package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.api.syntax.expression.SimplerPropertyExpression;
import it.jakegblp.lusk.skript.utils.BlockStateHolder;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Jukebox - Record/Disc")
@Description("Returns the music disc within a jukebox.\nCan be set, reset and deleted.")
@Examples("broadcast the music disc of {_j}")
@Since("1.0.3, 1.3 (Plural, Blockstate)")
public class ExprJukeboxRecord extends SimplerPropertyExpression<BlockStateHolder<?>, ItemType> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprJukeboxRecord.class, ExprJukeboxRecord::new, ItemType.class)
                .propertyOf("[jukebox] [music] (disc|record)", "blockstateholders")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public @Nullable ItemType convert(BlockStateHolder<?> from) {
        return from.getBlockState(Jukebox.class).map(jukebox -> new ItemType(jukebox.getRecord())).orElse(null);
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[] {ItemType.class};
            case DELETE -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void set(BlockStateHolder<?> from, ItemType to) {
        from.editBlockState(Jukebox.class, jukebox -> jukebox.setRecord(to.getRandom()));
    }

    @Override
    public void delete(BlockStateHolder<?> from) {
        set(from, new ItemType(Material.AIR));
    }

    @Override
    protected String getPropertyName() {
        return "jukebox music record";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}