package org.infodavid.commons.utility;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The Class StringUtilities.
 */
@SuppressWarnings("static-method")
public final class StringUtilities {

    /** The Constant CR. */
    public static final char CR = '\n';

    /** The Constant CR. */
    public static final String CRLF = "\r\n";

    /** The Constant DOT. */
    public static final String DOT = ".";

    /** The Constant DOT_CHAR. */
    public static final char DOT_CHAR = '.';

    /** The Constant EQ. */
    public static final char EQ = '=';

    /** The Constant SPACE_CHAR. */
    public static final char SPACE_CHAR = ' ';

    /** The Constant TAB. */
    public static final char TAB = '\t';

    /** The singleton. */
    private static WeakReference<StringUtilities> instance = null;

    /** The rand. */
    private static Random rand = new Random(System.currentTimeMillis());

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized StringUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new StringUtilities());
        }

        return instance.get();
    }

    /** The source. */
    private final byte[] source = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(StandardCharsets.UTF_8); // NOSONAR Allow unallocation

    /** The target. */
    private final byte[] target = "Qa5Ab8zWZcS0XtdEsDyCr6eRFqViT f9hGpBoYg4jnHkmNlU3Ju2MIv1KO7LwPx".getBytes(StandardCharsets.UTF_8); // NOSONAR Allow unallocation

    /**
     * Instantiates a new utilities.
     */
    private StringUtilities() {
    }

    /**
     * Compare version.
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the integer
     */
    public int compareVersion(final String v1, final String v2) {
        if (v1 == v2) { // NOSONAR
            return 0;
        }
        if (v1 == null) {
            return -1;
        }

        if (v2 == null) {
            return 1;
        }

        if (v1.equals(v2)) {
            return 0;
        }

        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        if (parts1.length < parts2.length) {
            parts1 = ArrayUtils.add(parts1, "0");
        } else if (parts2.length < parts1.length) {
            parts2 = ArrayUtils.add(parts2, "0");
        }

        for (int i = 0; i < parts1.length; i++) {
            final int part1 = Integer.parseInt(parts1[i]);
            final int part2 = Integer.parseInt(parts2[i]);

            if (part1 < part2) {
                return -1;
            }

            if (part1 > part2) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Decode.
     * @param bytes the bytes
     * @return the string
     */
    public String decode(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        if (bytes.length == 0) {
            return "";
        }

        final byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            final int index = ArrayUtils.indexOf(target, bytes[i]);

            if (index >= 0) {
                result[i] = source[index];
            } else {
                result[i] = bytes[i];
            }
        }

        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Decode.
     * @param text the text to decode
     * @return the string
     */
    public String decode(final String text) {
        if (text == null) {
            return null;
        }

        if (text.length() == 0) {
            return "";
        }

        final byte[] result = text.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < result.length; i++) {
            final int index = ArrayUtils.indexOf(target, result[i]);

            if (index >= 0) {
                result[i] = source[index];
            }
        }

        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Encode.
     * @param text the text to encode
     * @return the byte[]
     */
    public byte[] encode(final String text) {
        if (text == null || text.length() == 0) {
            return new byte[0];
        }

        final byte[] result = text.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < result.length; i++) {
            final int index = ArrayUtils.indexOf(source, result[i]);

            if (index >= 0) {
                result[i] = target[index];
            }
        }

        return result;
    }

    /**
     * Equals removing white spaces.
     * @param s1 the s 1
     * @param s2 the s 2
     * @return true, if successful
     */
    public boolean equalsRemovingWhiteSpaces(final String s1, final String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return removeWhiteSpaces(s1).equals(removeWhiteSpaces(s2));
    }

    /**
     * Generate random string.
     * @param characters the characters
     * @param length     the length
     * @return the string
     */
    public String generateRandomString(final String characters, final int length) {
        final StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            final int pos = rand.nextInt(characters.length());
            sb.append(characters.charAt(pos));
        }

        return sb.toString();
    }

    /**
     * Gets the lines.
     * @param s the s
     * @return the lines
     */
    public String[] getLines(final String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return new String[0];
        }

        return s.split("\\r?\\n");
    }

    /**
     * Checks if is value is a JSON content.
     * @param value the value
     * @return true, if it is JSON
     */
    public boolean isJson(final String value) {
        if (value == null) {
            return false;
        }

        final String trimmed = value.trim();

        if (trimmed.isEmpty()) {
            return false;
        }

        final char c = trimmed.charAt(0);

        return c == '[' || c == '{';
    }

    /**
     * Matches.
     * @param regex the regular expression
     * @param value the value
     * @return true, if successful
     */
    public boolean matchesOneOf(final Collection<String> regex, final String value) {
        if (regex == null || regex.isEmpty()) {
            return false;
        }

        for (final String item : regex) {
            if (matches(item, value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Matches.
     * @param regex the regular expression
     * @param value the value
     * @return true, if successful
     */
    public boolean matches(final String regex, final String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(regex) && org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return true;
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(regex) || org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return false;
        }

        try {
            if (Pattern.matches(regex, value)) {
                return true;
            }
        } catch (@SuppressWarnings("unused") final PatternSyntaxException e) {
            if (regex.equals(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Null less.
     * @param s the s
     * @return the string
     */
    public String nullLess(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Removes the white spaces.
     * @param s the s
     * @return the string
     */
    public String removeWhiteSpaces(final String s) {
        return s.replaceAll("\\s+", "");
    }

    /**
     * Replace all no regular expression.
     * @param s        the s
     * @param oldValue the old value
     * @param newValue the new value
     * @return the string
     */
    public String replaceAllNoRegex(final String s, final String oldValue, final String newValue) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(oldValue)) {
            return s;
        }

        if (newValue == null) {
            return s;
        }

        String result = s;

        while (result.indexOf(oldValue) >= 0) {
            result = result.replace(oldValue, newValue);
        }

        return result;
    }

    /**
     * Replace empty string by null.
     * @param s the s
     * @return the string
     */
    public String replaceEmptyStringByNull(final String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return null;
        }

        return s;
    }

    /**
     * Replace the string at the specified position.
     * @param value       the value
     * @param replacement the replacement
     * @param index       the index
     * @return the string
     */
    public String setAt(final String value, final String replacement, final int index) {
        if (value == null || index < 0 || index > value.length() - 1 || replacement == null || replacement.length() == 0) {
            return value;
        }

        final char[] array1 = value.toCharArray();
        final char[] array2 = replacement.toCharArray();
        int length = array2.length;

        if (index + length > array1.length) {
            length = array1.length - index;
        }

        System.arraycopy(array2, 0, array1, index, length);

        return new String(array1);
    }

    /**
     * Sets the char at the specified position.
     * @param value       the value
     * @param replacement the replacement
     * @param index       the index
     * @return the string
     */
    public String setCharAt(final String value, final char replacement, final int index) {
        if (value == null || index < 0 || index > value.length() - 1) {
            return value;
        }

        final char[] array = value.toCharArray();
        array[index] = replacement;

        return new String(array);
    }

    /**
     * To string.
     * @param collection the value
     * @param buffer     the buffer
     */
    @SuppressWarnings({ "rawtypes" })
    public void toString(final Collection collection, final StringBuilder buffer) {
        final Iterator ite = collection.iterator();

        buffer.append('[');

        if (ite.hasNext()) {
            toString(ite.next(), buffer);

            while (ite.hasNext()) {
                buffer.append(',');
                toString(ite.next(), buffer);
            }
        }

        buffer.append(']');
    }

    /**
     * To string.
     * @param iterable the iterable
     * @return the string
     */
    public String toString(final Iterable<Object> iterable) {
        return org.apache.commons.lang3.StringUtils.join(iterable, ",");
    }

    /**
     * To string.
     * @param map    the value
     * @param buffer the buffer
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void toString(final Map map, final StringBuilder buffer) {
        final Iterator<Entry> ite = map.entrySet().iterator();
        buffer.append('{');

        if (ite.hasNext()) {
            Map.Entry entry = ite.next();
            toString(entry.getKey(), buffer);
            buffer.append('=');
            toString(entry.getValue(), buffer);

            while (ite.hasNext()) {
                entry = ite.next();
                buffer.append(',');
                toString(entry.getKey(), buffer);
                buffer.append('=');
                toString(entry.getValue(), buffer);
            }
        }

        buffer.append('}');
    }

    /**
     * To string.
     * @param object the object
     * @param buffer the buffer
     */
    public void toString(final Object object, final StringBuilder buffer) {
        if (object == null) {
            buffer.append("null");
        } else if (object instanceof final Throwable throwable) {
            buffer.append(throwable.getClass().getSimpleName());
            buffer.append(": ");
            buffer.append(throwable.getMessage());
        } else if (object.getClass().isArray()) {
            if (object.getClass().getComponentType() == boolean.class) {
                buffer.append(Arrays.toString((boolean[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == byte.class) {
                buffer.append(Arrays.toString((byte[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == short.class) {
                buffer.append(Arrays.toString((short[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == int.class) {
                buffer.append(Arrays.toString((int[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == long.class) {
                buffer.append(Arrays.toString((long[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == float.class) {
                buffer.append(Arrays.toString((float[]) object).replace(", ", ","));
            } else if (object.getClass().getComponentType() == double.class) {
                buffer.append(Arrays.toString((double[]) object).replace(", ", ","));
            } else {
                toString((Object[]) object, buffer);
            }
        } else if (object instanceof final Collection<?> c) {
            toString(c, buffer);
        } else if (object instanceof final Map<?, ?> m) {
            toString(m, buffer);
        } else {
            buffer.append(object);
        }
    }

    /**
     * To string.
     * @param array  the array
     * @param buffer the buffer
     */
    public void toString(final Object[] array, final StringBuilder buffer) {
        buffer.append(Arrays.toString(array).replace(", ", ","));
    }
}
