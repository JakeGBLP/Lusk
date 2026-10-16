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

@Name("Block Data - can Be Waterlogged")
@Description("Checks whether or not one or more blocks, blockstates or blockdatas can be waterlogged.")
@Example("if event-block can be waterlogged:\n\tbroadcast \"%event-block% can be waterlogged!\"")
@Since("1.3")
public class CondCanBeWaterLogged extends PropertyCondition<BlockData> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyCondition(CondCanBeWaterLogged.class, CondCanBeWaterLogged::new)
                .property("blockdatas", PropertyType.CAN, "be water[ |-]logged")
                .register(syntaxRegistry);
    }

    @Override
    public boolean check(BlockData blockData) {
        return blockData instanceof Waterlogged;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "be waterlogged";
    }
}