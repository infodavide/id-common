package org.infodavid.commons.jdk;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.infodavid.commons.utility.NumberUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DiagnosticUtilities.
 */
@SuppressWarnings("static-method")
public final class DiagnosticUtilities {

    /** The singleton. */
    private static WeakReference<DiagnosticUtilities> instance = null;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticUtilities.class);

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized DiagnosticUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new DiagnosticUtilities());
        }

        return instance.get();
    }

    /**
     * Instantiates a new utilities.
     */
    private DiagnosticUtilities() {
    }

    /**
     * Builds the head dump.
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void buildHeadDump(final String path) throws IOException {
        final File executable = DiagnosticUtilities.getInstance().getJmapExecutable();

        if (!executable.exists()) {
            throw new UnsupportedOperationException("Executable not found: " + executable.getName());
        }

        final String pid = StringUtils.substringBefore(ManagementFactory.getRuntimeMXBean().getName(), "@");
        final File dump;

        if (StringUtils.isEmpty(path)) {
            dump = File.createTempFile("dump_" + pid + '_', ".hprof");
        }
        else {
            dump = new File(path);
        }

        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();

        try {
            if (CommandRunnerFactory.getInstance().run(output, error, new String[] {
                    executable.getAbsolutePath(), "-dump:live,format=b,file=" + dump.getAbsolutePath(), pid
            }) != 0 && StringUtils.isNotEmpty(error.toString())) {
                throw new IOException(error.toString());
            }
        }
        catch (final IOException e) {
            FileUtils.deleteQuietly(dump);

            throw e;
        }
    }

    /**
     * Collect diagnostics.
     * @param buffer the buffer
     */
    public void collectDiagnostics(final StringBuilder buffer) {
        buffer.append("Alive threads:\n");

        for (final ThreadEntry entry : getActiveLiveThreads(15).values()) {
            buffer.append('\t');
            buffer.append(entry.getName());
            buffer.append(", interrupted=");
            buffer.append(entry.isInterrupted());
            buffer.append(", daemon=");
            buffer.append(entry.isDaemon());
            buffer.append(", state=");
            buffer.append(entry.getState());
            buffer.append('\n');

            for (final String item : entry.getStackTrace()) {
                buffer.append('\t');
                buffer.append('\t');
                buffer.append(item);
                buffer.append('\n');
            }
        }

        buffer.append("Loaded objects:\n");

        for (final JMapEntry entry : getJavaHeapHistogram("org\\.infodavid\\..*", null).values()) {
            buffer.append('\t');
            buffer.append(entry.getClassName());
            buffer.append(':');
            buffer.append(entry.getInstances());
            buffer.append(':');
            buffer.append(NumberUtilities.getInstance().toHumanReadableByteCount(entry.getBytes(), true));
            buffer.append('\n');
        }
    }

    /**
     * This method guarantees that garbage collection is done unlike <code>{@link System#gc()}</code>.
     * @param reference the reference
     * @throws InterruptedException the interrupted exception
     */
    public void gc(final WeakReference<?> reference) throws InterruptedException {
        WeakReference<?> ref = reference;
        final long timeout = System.currentTimeMillis() + 5000;

        if (ref == null) {
            Object obj = new Object();
            ref = new WeakReference<>(obj);
            obj = null; // NOSONAR
        }

        while (ref.get() != null && System.currentTimeMillis() < timeout) {
            System.gc(); // NOSONAR
            Thread.sleep(100); // NOSONAR Only for testing
        }
    }

    /**
     * This method guarantees that garbage collection is done unlike <code>{@link System#gc()}</code>.
     * @throws InterruptedException the interrupted exception
     */
    public void gc() throws InterruptedException {
        gc(null);
    }

    /**
     * Collect active thread.
     * @param stackLength the length of the stacktrace
     * @return the map
     */
    public Map<String,ThreadEntry> getActiveLiveThreads(final int stackLength) {
        final Map<String,ThreadEntry> results = new HashMap<>();

        for (final Entry<Thread,StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            final Thread thread = entry.getKey();

            if (!thread.isAlive() || thread.getThreadGroup() != null && "system".equalsIgnoreCase(thread.getThreadGroup().getName()) || thread.getName().startsWith("nio") || thread.getName().startsWith("pool") || thread.getName().startsWith("http-") || thread == Thread.currentThread()) {
                continue;
            }

            final ThreadEntry threadEntry;

            if (thread.getThreadGroup() == null) {
                threadEntry = new ThreadEntry(thread.getName());
            }
            else {
                threadEntry = new ThreadEntry(thread.getThreadGroup().getName() + '.' + thread.getName());
            }

            results.put(threadEntry.getName(), threadEntry);
            threadEntry.setInterrupted(thread.isInterrupted());
            threadEntry.setDaemon(thread.isDaemon());
            threadEntry.setState(thread.getState());
            final StackTraceElement[] stackTraceElements = entry.getValue();
            final int length = stackLength < 1 ? stackTraceElements.length : Math.min(stackLength, stackTraceElements.length);
            final String[] stackTrace = new String[length];
            threadEntry.setStackTrace(stackTrace);

            for (int i = 0; i < length; i++) {
                final StackTraceElement element = stackTraceElements[i];

                stackTrace[i] = element.getClassName() + '.' + element.getMethodName();
            }
        }

        return results;
    }

    /**
     * Gets the java heap histogram.
     * @param includePattern the include regular expression
     * @param excludePattern the exclude regular expression
     * @return the java heap histogram
     */
    public Map<String,JMapEntry> getJavaHeapHistogram(final String includePattern, final String excludePattern) {
        final JMapCallable callable = new JMapCallable();
        callable.setIncludePattern(includePattern);
        callable.setExcludePattern(excludePattern);

        try {
            return callable.call();
        }
        catch (final Exception e) {
            LOGGER.warn("An error occured while collection Java heap histogram", e);

            return Collections.emptyMap();
        }
    }

    /**
     * Gets the jmap executable.
     * @return the jmap executable
     */
    protected File getJmapExecutable() {
        final File binDirectory = new File(org.apache.commons.lang3.SystemUtils.getJavaHome(), "bin");
        final String executableName;

        if (org.apache.commons.lang3.SystemUtils.IS_OS_UNIX || org.apache.commons.lang3.SystemUtils.IS_OS_LINUX) {
            executableName = "jmap";
        }
        else if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            executableName = "jmap.exe";
        }
        else {
            throw new UnsupportedOperationException("Operative system not supported: " + org.apache.commons.lang3.SystemUtils.OS_NAME);
        }

        File result = new File(binDirectory, executableName);

        if (!result.exists()) {
            result = new File(new File(org.apache.commons.lang3.SystemUtils.getJavaHome().getParentFile(), "bin"), executableName);
        }

        return result;
    }
}
