package it.jakegblp.lusk.nms.core.logger;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class ConsoleLogInjector {

    private static volatile boolean injected = false;
    private static volatile Consumer<CancellableLogLine> handler = l -> {};
    private static volatile Filter rootFilter;

    private static final ThreadLocal<Boolean> IN_HANDLER = ThreadLocal.withInitial(() -> false);

    private static final long DECISION_TTL_NANOS = TimeUnit.MILLISECONDS.toNanos(250);
    private static final ConcurrentHashMap<Key, Decision> DECISIONS = new ConcurrentHashMap<>();

    private ConsoleLogInjector() {}

    public static synchronized void inject(Consumer<CancellableLogLine> onLine) {
        Objects.requireNonNull(onLine, "onLine");
        handler = onLine;

        if (injected) return;
        injected = true;

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig root = config.getRootLogger();

        rootFilter = new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (IN_HANDLER.get()) return Result.NEUTRAL;

                String msg = formatEventMessage(event);
                if (msg.isEmpty()) return Result.NEUTRAL;

                long now = System.nanoTime();
                cleanup(now);

                Key key = new Key(event, msg);

                // If Log4j calls filter twice for the same “logical line”, reuse the decision and don’t re-run handler
                Decision cached = DECISIONS.get(key);
                if (cached != null && cached.expiresAtNanos >= now) {
                    return cached.cancel ? Result.DENY : Result.NEUTRAL;
                }

                boolean cancel = false;
                IN_HANDLER.set(true);
                try {
                    CancellableLogLine line = new CancellableLogLine(event, msg);
                    handler.accept(line);
                    cancel = line.isCancelled();
                } catch (Throwable t) {
                    t.printStackTrace();
                    cancel = false;
                } finally {
                    IN_HANDLER.set(false);
                }

                DECISIONS.put(key, new Decision(cancel, now + DECISION_TTL_NANOS));
                return cancel ? Result.DENY : Result.NEUTRAL;
            }
        };

        root.addFilter(rootFilter);
        ctx.updateLoggers();
    }

    public static synchronized void uninject() {
        if (!injected) return;
        injected = false;

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        try {
            config.getRootLogger().removeFilter(rootFilter);
        } catch (Throwable ignored) {}

        rootFilter = null;
        handler = l -> {};
        IN_HANDLER.remove();
        DECISIONS.clear();

        ctx.updateLoggers();
    }

    private static void cleanup(long now) {
        // cheap cleanup to avoid unbounded growth
        if (DECISIONS.size() < 4096) return;
        for (Map.Entry<Key, Decision> e : DECISIONS.entrySet()) {
            if (e.getValue().expiresAtNanos < now) {
                DECISIONS.remove(e.getKey(), e.getValue());
            }
        }
    }

    private static String formatEventMessage(LogEvent event) {
        String msg = event.getMessage() == null ? "" : event.getMessage().getFormattedMessage();

        Throwable thrown = event.getThrown();
        if (thrown != null) {
            StringWriter sw = new StringWriter();
            thrown.printStackTrace(new PrintWriter(sw));
            msg = msg.isEmpty() ? sw.toString() : (msg + "\n" + sw);
        }

        return msg == null ? "" : msg;
    }

    private record Decision(boolean cancel, long expiresAtNanos) {}


    private static final class Key {
        private final long timeMillis;
        private final String loggerName;
        private final String level;
        private final String message;

        private Key(LogEvent e, String message) {
            this.timeMillis = e.getTimeMillis();
            this.loggerName = e.getLoggerName() == null ? "" : e.getLoggerName();
            this.level = e.getLevel() == null ? "" : e.getLevel().name();
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key k)) return false;
            return timeMillis == k.timeMillis
                    && loggerName.equals(k.loggerName)
                    && level.equals(k.level)
                    && message.equals(k.message);
        }

        @Override
        public int hashCode() {
            int h = Long.hashCode(timeMillis);
            h = 31 * h + loggerName.hashCode();
            h = 31 * h + level.hashCode();
            h = 31 * h + message.hashCode();
            return h;
        }
    }

    public static final class CancellableLogLine {
        private final LogEvent event;
        private final String message;

        @Getter
        private boolean cancelled;

        private CancellableLogLine(LogEvent event, String message) {
            this.event = event;
            this.message = message == null ? "" : message;
        }

        public LogEvent event() {
            return event;
        }

        public String message() {
            return message;
        }

        public String messageStrippedAnsi() {
            return AnsiStripper.strip(message);
        }

        public void cancel() {
            this.cancelled = true;
        }
    }

    public static final class AnsiStripper {
        private static final Pattern ANSI = Pattern.compile(
                "(?:\\u001B\\[[0-9;?]*[ -/]*[@-~])"
                        + "|(?:\\u001B[@-Z\\\\-_])"
                        + "|(?:\\u009B[0-9;?]*[ -/]*[@-~])"
        );

        private AnsiStripper() {}

        public static String strip(String s) {
            if (s == null || s.isEmpty()) return s;
            return ANSI.matcher(s).replaceAll("");
        }
    }
}
