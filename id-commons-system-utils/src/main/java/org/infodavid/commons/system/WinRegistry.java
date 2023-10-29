package org.infodavid.commons.system;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * The Class WinRegistry.
 */
class WinRegistry {

    /** The Constant HKEY. */
    private static final String HKEY = "hkey=";

    /** The Constant HKEY_CURRENT_USER. */
    public static final int HKEY_CURRENT_USER = 0x80000001;

    /** The Constant HKEY_LOCAL_MACHINE. */
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;

    /** The Constant KEY_ALL_ACCESS. */
    private static final int KEY_ALL_ACCESS = 0xf003f;

    /** The Constant KEY_READ. */
    private static final int KEY_READ = 0x20019;

    /** The Constant KEY2. */
    private static final String KEY2 = ", key=";

    /** The Constant REG_ACCESSDENIED. */
    public static final byte REG_ACCESSDENIED = 5;

    /** The Constant REG_NOTFOUND. */
    public static final byte REG_NOTFOUND = 2;

    /** The Constant REG_SUCCESS. */
    public static final byte REG_SUCCESS = 0;

    /** The Constant regCloseKey. */
    private static final Method regCloseKey;

    /** The Constant regCreateKeyEx. */
    private static final Method regCreateKeyEx;

    /** The Constant regDeleteKey. */
    private static final Method regDeleteKey;

    /** The Constant regDeleteValue. */
    private static final Method regDeleteValue;

    /** The Constant regEnumKeyEx. */
    private static final Method regEnumKeyEx;

    /** The Constant regEnumValue. */
    private static final Method regEnumValue;

    /** The Constant regOpenKey. */
    private static final Method regOpenKey;

    /** The Constant regQueryInfoKey. */
    private static final Method regQueryInfoKey;

    /** The Constant regQueryValueEx. */
    private static final Method regQueryValueEx;

    /** The Constant regSetValueEx. */
    private static final Method regSetValueEx;

    /** The Constant systemRoot. */
    private static final Preferences systemRoot = Preferences.systemRoot();

    /** The Constant userClass. */
    private static final Class<? extends Preferences> userClass = Preferences.userRoot().getClass();

    /** The Constant userRoot. */
    private static final Preferences userRoot = Preferences.userRoot();

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", int.class, byte[].class, int.class);
            regOpenKey.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", int.class);
            regCloseKey.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx", int.class, byte[].class);
            regQueryValueEx.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue", int.class, int.class, int.class);
            regEnumValue.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", int.class);
            regQueryInfoKey.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regEnumKeyEx = userClass.getDeclaredMethod("WindowsRegEnumKeyEx", int.class, int.class, int.class);
            regEnumKeyEx.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", int.class, byte[].class);
            regCreateKeyEx.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", int.class, byte[].class, byte[].class);
            regSetValueEx.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regDeleteValue = userClass.getDeclaredMethod("WindowsRegDeleteValue", int.class, byte[].class);
            regDeleteValue.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
            regDeleteKey = userClass.getDeclaredMethod("WindowsRegDeleteKey", int.class, byte[].class);
            regDeleteKey.setAccessible(true); // NOSONAR FIXME Fix it on Java 9 or later
        } catch (final Exception e) {
            throw new InstantiationError(e.getMessage());
        }
    }

    /**
     * Create a key.
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key  the key
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static void createKey(final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        int[] ret;

        if (hkey == HKEY_LOCAL_MACHINE) {
            ret = createKey(systemRoot, hkey, key);
            regCloseKey.invoke(systemRoot, Integer.valueOf(ret[0]));
        } else if (hkey == HKEY_CURRENT_USER) {
            ret = createKey(userRoot, hkey, key);
            regCloseKey.invoke(userRoot, Integer.valueOf(ret[0]));
        } else {
            throw new IllegalArgumentException(HKEY + hkey);
        }

        if (ret[1] != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + ret[1] + KEY2 + key);
        }
    }

    /**
     * Creates the key.
     * @param root the root
     * @param hkey the hkey
     * @param key  the key
     * @return the int[]
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static int[] createKey(final Preferences root, final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke(root, Integer.valueOf(hkey), toCstr(key));
    }

    /**
     * Delete a given key.
     * @param hkey the hkey
     * @param key  the key
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static void deleteKey(final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        int rc = -1;

        if (hkey == HKEY_LOCAL_MACHINE) {
            rc = deleteKey(systemRoot, hkey, key);
        } else if (hkey == HKEY_CURRENT_USER) {
            rc = deleteKey(userRoot, hkey, key);
        }

        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + KEY2 + key);
        }
    }

    /**
     * Delete key.
     * @param root the root
     * @param hkey the hkey
     * @param key  the key
     * @return the int
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static int deleteKey(final Preferences root, final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        return ((Integer) regDeleteKey.invoke(root, Integer.valueOf(hkey), toCstr(key))).intValue(); // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    }

    /**
     * delete a value from a given key/value name.
     * @param hkey  the hkey
     * @param key   the key
     * @param value the value
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static void deleteValue(final int hkey, final String key, final String value) throws IllegalAccessException, InvocationTargetException {
        int rc = -1;

        if (hkey == HKEY_LOCAL_MACHINE) {
            rc = deleteValue(systemRoot, hkey, key, value);
        } else if (hkey == HKEY_CURRENT_USER) {
            rc = deleteValue(userRoot, hkey, key, value);
        }

        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException("rc=" + rc + KEY2 + key + ", value=" + value);
        }
    }

    /**
     * Delete value.
     * @param root  the root
     * @param hkey  the hkey
     * @param key   the key
     * @param value the value
     * @return the int
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static int deleteValue(final Preferences root, final int hkey, final String key, final String value) throws IllegalAccessException, InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_ALL_ACCESS));

        if (handles[1] != REG_SUCCESS) {
            return handles[1]; // can be REG_NOTFOUND, REG_ACCESSDENIED
        }

        final int rc = ((Integer) regDeleteValue.invoke(root, Integer.valueOf(handles[0]), toCstr(value))).intValue();
        regCloseKey.invoke(root, Integer.valueOf(handles[0]));

        return rc;
    }

    /**
     * Read a value from key and value name.
     * @param hkey      HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key       the key
     * @param valueName the value name
     * @return the value
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static String readString(final int hkey, final String key, final String valueName) throws IllegalAccessException, InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readString(systemRoot, hkey, key, valueName);
        }

        if (hkey == HKEY_CURRENT_USER) {
            return readString(userRoot, hkey, key, valueName);
        }

        throw new IllegalArgumentException(HKEY + hkey);
    }

    /**
     * Read string.
     * @param root  the root
     * @param hkey  the hkey
     * @param key   the key
     * @param value the value
     * @return the string
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static String readString(final Preferences root, final int hkey, final String key, final String value) throws IllegalAccessException, InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_READ));

        if (handles[1] != REG_SUCCESS) {
            return null;
        }

        final byte[] valb = (byte[]) regQueryValueEx.invoke(root, Integer.valueOf(handles[0]), toCstr(value));
        regCloseKey.invoke(root, Integer.valueOf(handles[0]));

        return valb != null ? new String(valb).trim() : null;
    }

    /**
     * Read the value name(s) from a given key.
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key  the key
     * @return the value name(s)
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static List<String> readStringSubKeys(final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringSubKeys(systemRoot, hkey, key);
        }

        if (hkey == HKEY_CURRENT_USER) {
            return readStringSubKeys(userRoot, hkey, key);
        }

        throw new IllegalArgumentException(HKEY + hkey);
    }

    /**
     * Read string sub keys.
     * @param root the root
     * @param hkey the hkey
     * @param key  the key
     * @return the list
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static List<String> readStringSubKeys(final Preferences root, final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        final List<String> results = new ArrayList<>();
        final int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_READ));

        if (handles[1] != REG_SUCCESS) {
            return Collections.emptyList();
        }

        final int[] info = (int[]) regQueryInfoKey.invoke(root, Integer.valueOf(handles[0]));
        final int count = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
        final int maxlen = info[3]; // value length max

        for (int index = 0; index < count; index++) {
            final byte[] name = (byte[]) regEnumKeyEx.invoke(root, Integer.valueOf(handles[0]), Integer.valueOf(index), Integer.valueOf(maxlen + 1));
            results.add(new String(name).trim());
        }

        regCloseKey.invoke(root, Integer.valueOf(handles[0]));

        return results;
    }

    /**
     * Read value(s) and value name(s) form given key.
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key  the key
     * @return the value name(s) plus the value(s)
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static Map<String, String> readStringValues(final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringValues(systemRoot, hkey, key);
        }

        if (hkey == HKEY_CURRENT_USER) {
            return readStringValues(userRoot, hkey, key);
        }

        throw new IllegalArgumentException(HKEY + hkey);
    }

    /**
     * Read string values.
     * @param root the root
     * @param hkey the hkey
     * @param key  the key
     * @return the map
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static Map<String, String> readStringValues(final Preferences root, final int hkey, final String key) throws IllegalAccessException, InvocationTargetException {
        final HashMap<String, String> results = new HashMap<>();
        final int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_READ));

        if (handles[1] != REG_SUCCESS) {
            return Collections.emptyMap();
        }

        final int[] info = (int[]) regQueryInfoKey.invoke(root, Integer.valueOf(handles[0]));
        final int count = info[0]; // count
        final int maxlen = info[3]; // value length max

        for (int index = 0; index < count; index++) {
            final byte[] name = (byte[]) regEnumValue.invoke(root, Integer.valueOf(handles[0]), Integer.valueOf(index), Integer.valueOf(maxlen + 1));
            final String value = readString(hkey, key, new String(name));
            results.put(new String(name).trim(), value);
        }

        regCloseKey.invoke(root, Integer.valueOf(handles[0]));

        return results;
    }

    /**
     * To cstr.
     * @param str the str
     * @return the byte[]
     */
    // utility
    private static byte[] toCstr(final String str) {
        final byte[] result = new byte[str.length() + 1];

        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }

        result[str.length()] = 0;

        return result;
    }

    /**
     * Write a value in a given key/value name.
     * @param hkey      the hkey
     * @param key       the key
     * @param valueName the value name
     * @param value     the value
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    public static void writeStringValue(final int hkey, final String key, final String valueName, final String value) throws IllegalAccessException, InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            writeStringValue(systemRoot, hkey, key, valueName, value);
        } else if (hkey == HKEY_CURRENT_USER) {
            writeStringValue(userRoot, hkey, key, valueName, value);
        } else {
            throw new IllegalArgumentException(HKEY + hkey);
        }
    }

    /**
     * Write string value.
     * @param root      the root
     * @param hkey      the hkey
     * @param key       the key
     * @param valueName the value name
     * @param value     the value
     * @throws IllegalAccessException    the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     */
    private static void writeStringValue(final Preferences root, final int hkey, final String key, final String valueName, final String value) throws IllegalAccessException, InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke(root, Integer.valueOf(hkey), toCstr(key), Integer.valueOf(KEY_ALL_ACCESS));
        regSetValueEx.invoke(root, Integer.valueOf(handles[0]), toCstr(valueName), toCstr(value));
        regCloseKey.invoke(root, Integer.valueOf(handles[0]));
    }

    /**
     * Instantiates a new win registry.
     */
    private WinRegistry() {
    }
}
