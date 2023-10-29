package org.infodavid.commons.swing;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;

/**
 * The Class SwingAppender.
 */
public class SwingAppender extends AppenderBase<ILoggingEvent> {

    /** The Constant DATE_FORMATTER. */
    private static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    /** The Constant DEFAULT_LIMIT. */
    private static final short DEFAULT_LIMIT = 1000;

    /**
     * Creates the default style.
     * @param panel the panel
     * @param level the level
     * @param foreground the foreground
     * @return the style
     */
    private static Style createDefaultStyle(final JTextPane panel, final Level level, final Color foreground) {
        final Style result = panel.addStyle(level.levelStr + " style", null);
        StyleConstants.setForeground(result, foreground);

        return result;
    }

    /** The limit. */
    private short limit = DEFAULT_LIMIT;

    /** The lock. */
    private final Lock lock = new ReentrantLock();

    /** The pending events. */
    private List<Pair<WeakReference<SwingAppender>,ILoggingEvent>> pendingEvents = null;

    /** The scroll panel. */
    private WeakReference<JScrollPane> scrollPaneRef = null;

    /** The styles. */
    private final Map<Level,Style> styles = new HashMap<>();

    /** The text panel. */
    private WeakReference<JTextPane> textPaneRef = null;

    /**
     * Instantiates a new appender.
     */
    public SwingAppender() {
        // noop
    }

    /**
     * Gets the limit.
     * @return the limit
     */
    public short getLimit() {
        return limit;
    }

    /**
     * Gets the scroll panel.
     * @return the scroll panel
     */
    public JScrollPane getScrollPane() {
        return scrollPaneRef == null ? null : scrollPaneRef.get();
    }

    /**
     * Gets the text panel.
     * @return the text panel
     */
    public JTextPane getTextPane() {
        return textPaneRef == null ? null : textPaneRef.get();
    }

    /**
     * Sets the limit.
     * @param limit the limit to set
     */
    public void setLimit(final short limit) {
        this.limit = limit;
    }

    /**
     * Sets the limit.</br>
     * Used by Logback to set the property from the configuration file.
     * @param limit the new limit
     */
    public void setMaxSize(final String limit) {
        this.limit = Short.parseShort(limit);
    }

    /**
     * Sets the scroll panel.
     * @param scrollPane the panel to set
     * @param textPane the text pane
     */
    public void setup(final JScrollPane scrollPane, final JTextPane textPane) {
        scrollPaneRef = new WeakReference<>(scrollPane);
        lock.lock();

        try {
            textPaneRef = new WeakReference<>(textPane);
            styles.put(Level.TRACE, createDefaultStyle(textPane, Level.TRACE, Color.gray));
            styles.put(Level.DEBUG, createDefaultStyle(textPane, Level.DEBUG, Color.gray));
            styles.put(Level.INFO, createDefaultStyle(textPane, Level.INFO, Color.black));
            styles.put(Level.WARN, createDefaultStyle(textPane, Level.WARN, Color.red));
            styles.put(Level.ERROR, createDefaultStyle(textPane, Level.ERROR, Color.red));

            if (pendingEvents != null) {
                final Iterator<Pair<WeakReference<SwingAppender>,ILoggingEvent>> ite = pendingEvents.iterator();

                while (ite.hasNext()) {
                    final Pair<WeakReference<SwingAppender>,ILoggingEvent> pair = ite.next();
                    final SwingAppender appender = pair.getKey().get();

                    if (appender != null) {
                        appender.append(pair.getRight());
                    }

                    ite.remove();
                }

                pendingEvents = null;
            }
        }
        finally {
            lock.unlock();
        }
    }

    /*
     * (non-javadoc)
     * @see ch.qos.logback.core.AppenderBase#append(java.lang.Object)
     */
    @Override
    protected void append(final ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }

        lock.lock();

        try {
            final JTextPane textPane = textPaneRef == null ? null : textPaneRef.get();

            if (textPane == null) {
                if (pendingEvents == null) {
                    pendingEvents = new ArrayList<>();
                }

                pendingEvents.add(new ImmutablePair<>(new WeakReference<>(this), event));

                while (pendingEvents.size() > limit) {
                    pendingEvents.remove(0);
                }

                return;
            }

            final StyledDocument document = textPane.getStyledDocument();
            final Style style = styles.get(event.getLevel());
            final String line = DATE_FORMATTER.format(event.getTimeStamp()) + " - " + event.getThreadName() + " - " + event.getFormattedMessage() + '\n';

            try {
                document.insertString(document.getLength(), line, style);
                final IThrowableProxy throwableProxy = event.getThrowableProxy();

                if (throwableProxy != null) {
                    document.insertString(document.getLength(), throwableProxy.getClassName() + ": " + throwableProxy.getMessage() + '\n', style);

                    for (final StackTraceElementProxy item : throwableProxy.getStackTraceElementProxyArray()) {
                        document.insertString(document.getLength(), item.getSTEAsString() + '\n', style);
                    }
                }
            }
            catch (final BadLocationException e) {
                e.printStackTrace(); // NOSONAR Cannot log this error using a logger
            }

            final Element root = document.getDefaultRootElement();

            try {
                while (TextUtilities.getInstance().getWrappedLines(textPane) > limit) {
                    final Element first = root.getElement(0);
                    document.remove(first.getStartOffset(), first.getEndOffset());
                }
            }
            catch (@SuppressWarnings("unused") final BadLocationException e1) {
                // noop
            }

            final JScrollPane scrollPane = scrollPaneRef == null ? null : scrollPaneRef.get();

            if (scrollPane != null) {
                SwingUtilities.getInstance().scrollToBottom(scrollPane);
            }
        }
        finally {
            lock.unlock();
        }
    }
}
