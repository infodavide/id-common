package org.infodavid.commons.jdk;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class JMapParserRunnable.
 */
class JMapParserRunnable implements Runnable {

    /** The error. */
    private Exception error = null;

    /** The exclude pattern. */
    private final Pattern excludePattern;

    /** The include pattern. */
    private final Pattern includePattern;

    /** The output. */
    private final StringBuilder output;

    /** The line pattern. */
    private final Pattern linePattern = Pattern.compile("^(\\d*):\\s+(\\d*)\\s+(\\d*)\\s+([0-9A-Z-a-z_\\.]*)$");

    /** The results. */
    private final Map<String,JMapEntry> results;

    /**
     * Instantiates a new parser runnable.
     * @param include the include pattern
     * @param exclude the exclude pattern
     * @param output the output
     * @param results the results
     */
    public JMapParserRunnable(final String include, final String exclude, final StringBuilder output, final Map<String,JMapEntry> results) {
        this.output = output;
        this.results = results;

        if (StringUtils.isNotEmpty(exclude)) {
            excludePattern = Pattern.compile(exclude);
        }
        else {
            excludePattern = null;
        }

        if (StringUtils.isNotEmpty(include)) {
            includePattern = Pattern.compile(include);
        }
        else {
            includePattern = null;
        }
    }

    /**
     * Gets the error.
     * @return the error
     */
    public Exception getError() {
        return error;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new StringReader(output.toString()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                final Matcher matcher = linePattern.matcher(line);

                if (matcher.matches()) {
                    final String className = matcher.group(4);
                    boolean add = includePattern == null;

                    if (!add) {
                        add = includePattern.matcher(className).matches();
                    }

                    if (add && excludePattern != null) {
                        add = !excludePattern.matcher(className).matches();
                    }

                    if (add) {
                        results.put(className, new JMapEntry(className, Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3))));
                    }
                }
            }
        }
        catch (final IOException e) {
            error = e;
        }
    }
}
