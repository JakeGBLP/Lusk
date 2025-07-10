package it.jakegblp.lusk.elements.anvilgui.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.AnvilGuiWrapper;
import it.jakegblp.lusk.api.events.AnvilGuiClickEvent;
import it.jakegblp.lusk.api.events.AnvilGuiCloseEvent;
import it.jakegblp.lusk.api.events.AnvilGuiOpenEvent;
import it.jakegblp.lusk.api.listeners.AnvilGuiClickListener;
import it.jakegblp.lusk.api.listeners.AnvilGuiCloseListener;
import it.jakegblp.lusk.api.listeners.AnvilGuiOpenListener;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@Name("Anvil Gui - on Open/Close/Click Section")
@Description("""
        Runs the code inside of it when the provided anvil gui is opened, closed or clicked.

        Local Variables that are:
        - defined BEFORE this section CAN be used inside of it.
        - defined AFTER this section CANNOT be used inside of it.
        - defined INSIDE this section CANNOT be used outside of it.
        """)
@Examples(
        """
        set {_anvilGui} to a new anvil gui
        
        when {_anvilGui} gets opened:
            broadcast "%{_anvilGui}% has been opened!"
        
        when {_anvilGui} gets clicked:
            broadcast "%{_anvilGui}% has been clicked!"
        
        when {_anvilGui} gets closed:
            broadcast "%{_anvilGui}% has been closed!"
        """
)
@Since("1.3")
public class SecEvtAnvilGui extends Section {

    static {
        Skript.registerSection(SecEvtAnvilGui.class,
                "[execute|run] on anvil [gui|inventory] (:open|:close|click) of %~anvilguiinventory%",
                "[execute|run] when [[the] anvil [gui|inventory]] %~anvilguiinventory% ((get[s]|is) (open:opened|close:closed|clicked))");
    }

    private Expression<AnvilGuiWrapper> anvilGuiWrapperExpression;
    private int state;
    @Nullable
    private Trigger trigger;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, @NotNull Kleenean kleenean, SkriptParser.@NotNull ParseResult parseResult, @NotNull SectionNode sectionNode, @NotNull List<TriggerItem> list) {
        anvilGuiWrapperExpression = (Expression<AnvilGuiWrapper>) (expressions[0]);
        if (parseResult.hasTag("open")) {
            trigger = loadCode(sectionNode, "anvilguiopen", AnvilGuiOpenEvent.class);
            state = -1;
        } else if (parseResult.hasTag("close")) {
            trigger = loadCode(sectionNode, "anvilguiclose", AnvilGuiCloseEvent.class);
            state = 0;
        } else {
            trigger = loadCode(sectionNode, "anvilguiclick", AnvilGuiClickEvent.class);
            state = 1;
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(@NotNull Event event) {
        Object vars = Variables.copyLocalVariables(event);
        if (state == -1) {
            Consumer<AnvilGuiOpenEvent> consumer = trigger == null ? null : anvilGuiOpenEvent -> {
                Variables.setLocalVariables(anvilGuiOpenEvent, vars);
                TriggerItem.walk(trigger, anvilGuiOpenEvent);
                Variables.removeLocals(anvilGuiOpenEvent);
            };
            AnvilGuiOpenListener.log(consumer, anvilGuiWrapperExpression.getSingle(event));
        } else if (state == 0) {
            Consumer<AnvilGuiCloseEvent> consumer = trigger == null ? null : anvilGuiCloseEvent -> {
                Variables.setLocalVariables(anvilGuiCloseEvent, vars);
                TriggerItem.walk(trigger, anvilGuiCloseEvent);
                Variables.removeLocals(anvilGuiCloseEvent);
            };
            AnvilGuiCloseListener.log(consumer, anvilGuiWrapperExpression.getSingle(event));
        } else {
            Consumer<AnvilGuiClickEvent> consumer = trigger == null ? null : anvilGuiClickEvent -> {
                Variables.setLocalVariables(anvilGuiClickEvent, vars);
                TriggerItem.walk(trigger, anvilGuiClickEvent);
                Variables.removeLocals(anvilGuiClickEvent);
            };
            AnvilGuiClickListener.log(consumer, anvilGuiWrapperExpression.getSingle(event));
        }
        return super.walk(event, false);
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "when anvil gui " + anvilGuiWrapperExpression.toString(event, b) + " gets " + (state == -1 ? "opened" : state == 0 ? "closed" : "clicked");
    }
}