package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Block - is Waterlogged")
@Description("Checks whether or not one or more blocks, blockstates or blockdatas are waterlogged.")
@Example("if event-block is waterlogged:\n\tbroadcast \"%event-block% is waterlogged!\"")
@Since("1.3")
public class CondWaterLogged extends PropertyCondition<BlockData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondWaterLogged.class, CondWaterLogged::new)
                .property("blockdatas", PropertyType.BE, "water[ |-]logged")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(BlockData blockData) {
        return blockData instanceof Waterlogged waterlogged && waterlogged.isWaterlogged();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "waterlogged";
    }
}