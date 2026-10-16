package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.utils.BlockStateHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Sign - Lines")
@Description("Gets the lines of a sign on either side, by line index or just all of them.\nAllows changing.\nWorks with items.")
@Example("set sign lines 1, 2, 4 of target block to \"First\", \"Second\", \"Fourth\"")
@Example("set sign line 1 of tool to \"Hi!\"")
@Example("set rear sign content of target block to \"Cool\", \"Multi\", \"Line\", \"Text\"")
@Since("2.0.0")
@Keywords({"sign", "signs", "text", "content"})
public class ExprSignContent extends SimpleExpression<Component> {
    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprSignContent.class, ExprSignContent::new, Component.class)
                .patterns(
                        "[the] [front|:back|back:rear] [sign] line[s] %integers% of %blockstateholders%",
                        "%blockstateholders%'[s] [front|:back|back:rear] [sign] line[s] %integers%",
                        "[the] [front|:back|back:rear] sign (lines|content) of %blockstateholders%",
                        "%blockstateholders%'[s] [front|:back|back:rear] sign (lines|content)"
                ).register(syntaxRegistry);
    }

    private Side side;
    private @Nullable Expression<Integer> linesExpression;
    private Expression<BlockStateHolder<?>> blockStateHoldersExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        side = parseResult.hasTag("back") ? Side.BACK : Side.FRONT;
        if (matchedPattern == 0) {
            linesExpression = (Expression<Integer>) expressions[0];
            blockStateHoldersExpression = (Expression<BlockStateHolder<?>>) expressions[1];
        } else {
            blockStateHoldersExpression = (Expression<BlockStateHolder<?>>) expressions[0];
            if (matchedPattern == 1)
                linesExpression = (Expression<Integer>) expressions[1];
        }
        return true;
    }

    @Override
    protected Component @Nullable [] get(Event event) {
        var blockStates = blockStateHoldersExpression.getArray(event);
        if (blockStates.length == 0) return new Component[0];
        var components = new ArrayList<Component>();
        if (linesExpression == null) {
            for (BlockStateHolder<?> holder : blockStates)
                holder.getBlockState(Sign.class).ifPresent(sign -> components.addAll(sign.getSide(side).lines()));
        } else {
            var lines = Arrays.stream(linesExpression.getArray(event))
                    .filter(i -> i >= 1 && i <= 4)
                    .distinct()
                    .map(integer -> integer-1)
                    .toArray(Integer[]::new);
            if (lines.length == 0) return new Component[0];
            for (BlockStateHolder<?> holder : blockStates)
                holder.getBlockState(Sign.class).ifPresent(sign -> {
                    for (Integer line : lines)
                        components.add(sign.getSide(side).line(line));
                });
        }
        return components.toArray(new Component[0]);
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class<?>[]{Component[].class};
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        Integer[] lines = linesExpression == null ? new Integer[] {0,1,2,3} : Arrays.stream(linesExpression.getArray(event))
                .filter(i -> i >= 1 && i <= 4)
                .distinct()
                .map(integer -> integer-1)
                .toArray(Integer[]::new);
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            for (BlockStateHolder<?> holder : blockStateHoldersExpression.getArray(event)) {
                holder.editBlockState(Sign.class, sign -> {
                    var signSide = sign.getSide(side);
                    for (Integer line : lines)
                        signSide.line(line, Component.empty());
                });
            }
        } else {
            if (delta == null || delta.length == 0) return;
            List<Component> components = new ArrayList<>();
            for (Object o : delta)
                if (o instanceof Component component)
                    components.add(component);
            for (BlockStateHolder<?> holder : blockStateHoldersExpression.getArray(event)) {
                holder.editBlockState(Sign.class, sign -> {
                    var signSide = sign.getSide(side);
                    for (int i = 0; i < lines.length; i++) {
                        if (components.size() > i)
                            signSide.line(lines[i], components.get(i));
                    }
                });
            }
        }
    }

    @Override
    public boolean isSingle() {
        return linesExpression != null && linesExpression.isSingle();
    }

    @Override
    public Class<? extends Component> getReturnType() {
        return Component.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "the " + side.name().toLowerCase() + " sign lines" +
                (linesExpression == null ? "" : " " + linesExpression.toString(event, debug)) + " of " +
                blockStateHoldersExpression.toString(event, debug);
    }
}
