package it.jakegblp.lusk.nms.core.logger;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@NullMarked
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConsoleLogInjector {

    private static final Injector INJECTOR = new Injector();

    public static void inject(Consumer<CancellableLogLine> handler) {
        INJECTOR.inject(handler);
    }

    public static void uninject() {
        INJECTOR.uninject();
    }

    private static final class Injector {

        private static final long DECISION_TTL_NANOS = TimeUnit.MILLISECONDS.toNanos(250);

        private static final Consumer<CancellableLogLine> NO_OP = _ -> {};

        private final ThreadLocal<Boolean> handling = ThreadLocal.withInitial(() -> false);

        private final ConcurrentHashMap<Key, CachedDecision> decisions = new ConcurrentHashMap<>();

        private Consumer<CancellableLogLine> handler = NO_OP;

        @Nullable
        private Filter filter;
        private boolean injected;

        synchronized void inject(Consumer<CancellableLogLine> handler) {
            this.handler = Objects.requireNonNull(handler, "handler");
            if (injected) return;

            LoggerContext context = context();
            LoggerConfig root = context.getConfiguration().getRootLogger();

            filter = new InjectorFilter();
            root.addFilter(filter);

            injected = true;
            context.updateLoggers();
        }

        synchronized void uninject() {
            if (!injected) return;

            LoggerContext context = context();
            LoggerConfig root = context.getConfiguration().getRootLogger();

            try {
                root.removeFilter(filter);
            } catch (Throwable _) {}

            filter = null;
            handler = NO_OP;
            decisions.clear();
            handling.remove();
            injected = false;

            context.updateLoggers();
        }

        private LoggerContext context() {
            return (LoggerContext) LogManager.getContext(false);
        }

        private final class InjectorFilter extends AbstractFilter {

            @Override
            public Result filter(LogEvent event) {
                if (handling.get())
                    return Result.NEUTRAL;

                String message = format(event);
                if (message.isEmpty())
                    return Result.NEUTRAL;

                long now = System.nanoTime();
                Key key = new Key(event, message);

                CachedDecision cached = decisions.get(key);

                if (cached != null) {
                    if (cached.expiresAtNanos > now)
                        return cached.decision.result();

                    decisions.remove(key, cached);
                }

                CachedDecision evaluated = new CachedDecision(evaluate(event, message), now + DECISION_TTL_NANOS);

                CachedDecision existing = decisions.putIfAbsent(key, evaluated);

                if (existing != null && existing.expiresAtNanos > now)
                    return existing.decision.result();

                cleanup(now);

                return evaluated.decision.result();
            }
        }

        private Decision evaluate(LogEvent event, String message) {
            CancellableLogLine line = new CancellableLogLine(event, message);

            handling.set(true);
            try {
                handler.accept(line);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                handling.set(false);
            }

            return new Decision(line.isCancelled());
        }

        private void cleanup(long now) {
            if (decisions.size() < 4096) return;

            for (Map.Entry<Key, CachedDecision> entry : decisions.entrySet())
                if (entry.getValue().expiresAtNanos <= now)
                    decisions.remove(entry.getKey(), entry.getValue());
        }
    }

    private static String format(LogEvent event) {
        Message logMessage = event.getMessage();
        String message = logMessage == null ? "" : logMessage.getFormattedMessage();
        Throwable throwable = event.getThrown();
        if (throwable == null) return message;
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return message.isEmpty() ? writer.toString() : message + '\n' + writer;
    }

    private record Decision(boolean cancelled) {
        private Filter.Result result() {
            return cancelled ? Filter.Result.DENY : Filter.Result.NEUTRAL;
        }
    }

    private record CachedDecision(Decision decision, long expiresAtNanos) { }

    private record Key(long timeMillis, String loggerName, String level, String message) {
        private Key(LogEvent event, String message) {
            this(
                    event.getTimeMillis(),
                    Objects.requireNonNullElse(event.getLoggerName(), ""),
                    event.getLevel() == null ? "" : event.getLevel().name(),
                    message
            );
        }
    }

    @Getter
    public static final class CancellableLogLine {

        private final LogEvent event;
        private final String message;

        private boolean cancelled;

        private CancellableLogLine(LogEvent event, String message) {
            this.event = event;
            this.message = message;
        }

        public String getMessageStrippedAnsi() {
            return AnsiStripper.strip(message);
        }

        public void cancel() {
            cancelled = true;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class AnsiStripper {
        private static final Pattern ANSI = Pattern.compile("\\u001B\\[[0-9;?]*[ -/]*[@-~]|\\u001B[@-Z\\\\-_]|\\u009B[0-9;?]*[ -/]*[@-~]");

        private static String strip(String message) {
            return message.isEmpty() ? message : ANSI.matcher(message).replaceAll("");
        }
    }
}