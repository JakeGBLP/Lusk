package it.jakegblp.lusk.skript.api.section;

import ch.njol.skript.lang.ExpressionList;
import org.jetbrains.annotations.Nullable;

public record SectionParseResult<T>(@Nullable ExpressionList<T> expressionList, boolean error) {

    public boolean hasFailed() {
        return error || expressionList == null || expressionList.getExpressions().length == 0;
    }

}
