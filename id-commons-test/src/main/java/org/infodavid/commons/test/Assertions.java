package org.infodavid.commons.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;

/**
 * The Class Assertions.
 */
@SuppressWarnings("boxing")
public class Assertions {

    /**
     * Assert equals with normalized new lines.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void assertEqualsWithNormalizedNewLines(final String message, final String expected, final String actual) throws IOException {
        assertEquals(message, expected.replace("\r\n", "\n"), actual.replace("\r\n", "\n"));
    }

    /**
     * Assert greater than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertGreaterThan(final String message, final byte expected, final byte actual) {
        if (expected > actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert greater than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertGreaterThan(final String message, final int expected, final int actual) {
        if (expected > actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert greater than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertGreaterThan(final String message, final long expected, final long actual) {
        if (expected > actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert greater than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertGreaterThan(final String message, final short expected, final short actual) {
        if (expected > actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert lower than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertLowerThan(final String message, final byte expected, final byte actual) {
        if (expected < actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert lower than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertLowerThan(final String message, final int expected, final int actual) {
        if (expected < actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert lower than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertLowerThan(final String message, final long expected, final long actual) {
        if (expected < actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Assert lower than.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     */
    public static void assertLowerThan(final String message, final short expected, final short actual) {
        if (expected < actual) {
            Assert.fail(format(message, expected, actual));
        }
    }

    /**
     * Format class and value.
     * @param value       the value
     * @param valueString the value string
     * @return the string
     */
    private static String formatClassAndValue(final Object value, final String valueString) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(value == null ? "null" : value.getClass().getName());
        buffer.append('<');
        buffer.append(valueString);
        buffer.append('>');

        return buffer.toString();
    }

    /**
     * Format.
     * @param message  the message
     * @param expected the expected
     * @param actual   the actual
     * @return the string
     */
    static String format(final String message, final Object expected, final Object actual) {
        final StringBuilder buffer = new StringBuilder();

        if (message != null && !"".equals(message)) {
            buffer.insert(0, message);
            buffer.append(' ');
        }

        final String expectedString = String.valueOf(expected);
        final String actualString = String.valueOf(actual);
        buffer.append("expected");

        if (expectedString.equals(actualString)) {
            buffer.append(": ");
            buffer.append(formatClassAndValue(expected, expectedString));
            buffer.append(" but was: ");
            buffer.append(formatClassAndValue(actual, actualString));
        } else {
            buffer.append(":<");
            buffer.append(expectedString);
            buffer.append("> but was:<");
            buffer.append(actualString);
            buffer.append('>');
        }

        return buffer.toString();
    }

    /**
     * Instantiates a new assertions.
     */
    private Assertions() {
        // noop
    }
}
