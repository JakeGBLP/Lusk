package it.jakegblp.lusk.skript.api.async;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionList;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static it.jakegblp.lusk.nms.core.async.ExecutionMode.INHERITED;

public interface AsyncableSyntaxesWrapper extends AsyncableSyntax {

    static List<AsyncableSyntax> filterAsyncableSyntaxes(@NotNull ExpressionList<?> expressionList) {
        return CommonUtils.filterToList(AsyncableSyntax.class, expressionList.getExpressions());
    }

    static List<AsyncableSyntax> filterAsyncableSyntaxes(@NotNull Expression<?>... expressions) {
        return CommonUtils.filterToList(AsyncableSyntax.class, expressions);
    }

    static ExecutionMode getAsyncableSyntaxPrioritizedMode(List<? extends AsyncableSyntax> asyncableSyntaxes) {
        if (asyncableSyntaxes == null || asyncableSyntaxes.isEmpty()) return INHERITED;
        ExecutionMode smallest = asyncableSyntaxes.get(0).getInitExecutionMode();
        for (int i = 1; i < asyncableSyntaxes.size(); i++) {
            ExecutionMode m = asyncableSyntaxes.get(i).getInitExecutionMode();
            if (m.compareTo(smallest) < 0)
                smallest = m;
        }
        return smallest;
    }

    static ExecutionMode getExpressionListExecutionMode(ExpressionList<?> expressionList) {
        return getAsyncableSyntaxPrioritizedMode(filterAsyncableSyntaxes(expressionList));
    }

    List<AsyncableSyntax> getAsyncableSyntaxes();

    @Override
    default ExecutionMode getInitExecutionMode() {
        return ExecutionMode.getPrioritizedMode(CommonUtils.map(getAsyncableSyntaxes(), AsyncableSyntax::getInitExecutionMode));
    }

}
