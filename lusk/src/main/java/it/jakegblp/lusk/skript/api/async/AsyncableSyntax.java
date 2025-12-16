package it.jakegblp.lusk.skript.api.async;

import ch.njol.skript.lang.SyntaxElement;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import org.skriptlang.skript.log.runtime.SyntaxRuntimeErrorProducer;

public interface AsyncableSyntax extends SyntaxElement, SyntaxRuntimeErrorProducer {

    ExecutionMode getInitExecutionMode();

}
