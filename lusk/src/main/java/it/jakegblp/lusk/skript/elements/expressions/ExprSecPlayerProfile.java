package it.jakegblp.lusk.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.expressions.base.SectionExpression;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.profile.PlayerProfile;
import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.world.player.CompletablePlayerProfile;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntax;
import it.jakegblp.lusk.skript.api.async.AsyncableSyntaxesWrapper;
import it.jakegblp.lusk.skript.api.expression.NoInitExpression;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.entry.EntryContainer;
import org.skriptlang.skript.lang.entry.EntryValidator;
import org.skriptlang.skript.lang.entry.util.ExpressionEntryData;

import java.util.List;
import java.util.UUID;

import static it.jakegblp.lusk.skript.utils.AddonUtils.*;

public class ExprSecPlayerProfile extends SectionExpression<CompletablePlayerProfile> implements AsyncableSyntaxesWrapper {

    public static final EntryValidator VALIDATOR;

    static {
        VALIDATOR = EntryValidator.builder()
                .addEntryData(new ExpressionEntryData<>("player", null, true, OfflinePlayer.class))
                .addEntryData(new ExpressionEntryData<>("uuid", null, true, UUID.class))
                .addEntryData(new ExpressionEntryData<>("name", null, true, String.class))
                .addEntryData(new ExpressionEntryData<>("complete", null, true, Boolean.class))
                .addEntryData(new ExpressionEntryData<>("complete sync", null, true, Boolean.class))
                .addEntryData(new ExpressionEntryData<>("complete async", null, true, Boolean.class))
                .build();
        Skript.registerExpression(ExprSecPlayerProfile.class, CompletablePlayerProfile.class, ExpressionType.COMBINED,
                "[a] new [player] profile");
    }

    @Getter
    public static class PlayerProfileExpression extends NoInitExpression<CompletablePlayerProfile> implements AsyncableSyntax {
        private final Expression<OfflinePlayer> playerExpression;
        private final Expression<UUID> uuidExpression;
        private final Expression<String> nameExpression;
        private Expression<Boolean> completeAsyncExpression, completeSyncExpression, completeExpression;
        private ExecutionMode initExecutionMode;
        private boolean error = false;

        @SuppressWarnings({"unchecked"})
        public PlayerProfileExpression(EntryContainer entryContainer) {
            playerExpression = (Expression<OfflinePlayer>) entryContainer.getOptional("player", false);
            uuidExpression = (Expression<UUID>) entryContainer.getOptional("uuid", false);
            nameExpression = (Expression<String>) entryContainer.getOptional("name", false);
            if (playerExpression != null) {
                if (uuidExpression != null || nameExpression != null) {
                    Skript.error("You've already provided a player.");
                    error = true;
                    return;
                }
            }
            completeExpression = (Expression<Boolean>) entryContainer.getOptional("complete", false);
            completeSyncExpression = (Expression<Boolean>) entryContainer.getOptional("complete sync", false);
            completeAsyncExpression = (Expression<Boolean>) entryContainer.getOptional("complete async", false);

            boolean hasComplete = completeExpression != null;
            boolean hasCompleteSync = completeSyncExpression != null;
            boolean hasCompleteAsync = completeAsyncExpression != null;
            int presentCount = (hasComplete ? 1 : 0) + (hasCompleteSync ? 1 : 0) + (hasCompleteAsync ? 1 : 0);

            if (presentCount > 1) {
                Skript.error("You cannot specify more than one 'completion' entry.");
                error = true;
                return;
            }
            boolean async = false, sync = false, inherited = false;

            if (completeAsyncExpression instanceof Literal<Boolean> literal)
                async = getLiteralValue(literal, false);
            else if (completeSyncExpression instanceof Literal<Boolean> literal)
                sync = getLiteralValue(literal, false);
            else if (completeExpression instanceof Literal<Boolean> literal)
                inherited = getLiteralValue(literal, false);
            initExecutionMode = ExecutionMode.fromBooleans(async, sync, inherited);
        }

        @Override
        public CompletablePlayerProfile[] get(Event event) {
            PlayerProfile profile;
            if (playerExpression != null) {
                var offlinePlayer = playerExpression.getSingle(event);
                if (offlinePlayer == null) return new CompletablePlayerProfile[0];
                profile = Bukkit.createProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
            } else if (uuidExpression != null) {
                UUID uuid = uuidExpression.getSingle(event);
                if (uuid == null) return new CompletablePlayerProfile[0];
                profile = Bukkit.createProfile(uuid);
            } else if (nameExpression != null) {
                profile = Bukkit.createProfile(getUnformattedString(nameExpression, event));
            } else return new CompletablePlayerProfile[0];
            boolean async = getSingleNullable(completeAsyncExpression, event, false);
            boolean sync = getSingleNullable(completeSyncExpression, event, false);
            boolean inherited = getSingleNullable(completeExpression, event, false);
            ExecutionMode finalExecutionMode;
            boolean shouldComplete;
            if (initExecutionMode != null) {
                shouldComplete = true;
                finalExecutionMode = initExecutionMode;
            } else if (async || sync || inherited) {
                shouldComplete = true;
                finalExecutionMode = ExecutionMode.fromBooleans(async, sync, inherited);
            } else {
                shouldComplete = false;
                finalExecutionMode = ExecutionMode.INHERITED;
            }
            return new CompletablePlayerProfile[]{new CompletablePlayerProfile(profile, shouldComplete, finalExecutionMode)};
        }
    }

    @Override
    public List<AsyncableSyntax> getAsyncableSyntaxes() {
        return List.of(playerProfileExpression);
    }

    @Getter
    private PlayerProfileExpression playerProfileExpression;

    @Override
    public boolean init(Expression<?>[] expressions, int pattern, Kleenean delayed, SkriptParser.ParseResult result, @Nullable SectionNode node, @Nullable List<TriggerItem> triggerItems) {
        EntryContainer container = VALIDATOR.validate(node);
        if (container == null) return false;
        playerProfileExpression = new PlayerProfileExpression(container);
        return !playerProfileExpression.isError();
    }

    @Override
    protected CompletablePlayerProfile @Nullable [] get(Event event) {
        return playerProfileExpression.get(event);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends CompletablePlayerProfile> getReturnType() {
        return CompletablePlayerProfile.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "a new player profile";
    }
}
