package it.jakegblp.lusk.elements.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.command.EffectCommandEvent;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import it.jakegblp.lusk.utils.SkriptUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Skript - Effect Command String")
@Description({"The command used in an effect command event.\nThis doesn't include the prefix (usually `!`).\nCan be set, reset and deleted."})
@Examples({"set effect command to \"give me an apple\""})
@Since("1.3")
public class ExprSkriptEffectCommand extends EventValueExpression<String> {

	static {
		SkriptUtils.register(ExprSkriptEffectCommand.class, String.class, "[event-|the ][skript] effect command [string]");
	}

	public ExprSkriptEffectCommand() {
		super(String.class,true);
	}

	@Override
	public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return switch (mode) {
            case SET-> new Class[]{String.class};
			case DELETE, RESET -> new Class[0];
			case ADD, REMOVE, REMOVE_ALL -> null;
        };
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
		if (event instanceof EffectCommandEvent effectCommandEvent) {
			if (mode == Changer.ChangeMode.SET &&
					delta != null &&
					delta[0] instanceof String string) {
				effectCommandEvent.setCommand(string);
			} else if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
				effectCommandEvent.setCommand("");
			}
		}
	}
}