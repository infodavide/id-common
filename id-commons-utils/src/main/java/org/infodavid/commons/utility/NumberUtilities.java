package org.infodavid.commons.utility;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.mutable.MutableByte;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.mutable.MutableShort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class NumberUtilities.
 */
@SuppressWarnings("static-method")
public final class NumberUtilities {

    /** The Constant BINARY_PREFIX. */
    public static final String BINARY_PREFIX = "0b";

    /** The Constant HEX_PREFIX. */
    public static final String HEX_PREFIX = "0x";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberUtilities.class);

    /** The singleton. */
    private static WeakReference<NumberUtilities> instance = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized NumberUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new NumberUtilities());
        }

        return instance.get();
    }

    /** The Constant powers. */
    private final long[] powers = new long[] { // NOSONAR Allow unallocation
        1,
        10,
        100,
        1000,
        10000,
        100000,
        1000000,
        10000000,
        100000000,
        1000000000
    };

    /**
     * Instantiates a new utilities.
     */
    private NumberUtilities() {
    }

    /**
     * Creates the number.
     * @param text the text
     * @return the number
     */
    public Number createNumber(final String text) {
        return org.apache.commons.lang3.math.NumberUtils.createNumber(text);
    }

    /**
     * Equals.
     * @param left  the left
     * @param right the right
     * @return true, if successful
     */
    public boolean equals(final Object left, final Object right) {
        if (left instanceof Number) {
            if (!(right instanceof Number)) {
                return false;
            }

            return ((Number) left).doubleValue() == ((Number) right).doubleValue();
        }

        return Objects.equals(left, right);
    }

    /**
     * Gets the max value.
     * @param clazz the class to get the maximum value
     * @return the max value
     */
    public double getMaximumValue(final Class<?> clazz) { // NOSONAR No complexity
        double result = -1;

        if (Number.class.isAssignableFrom(clazz)) {
            if (BigDecimal.class.equals(clazz)) {
                result = Double.MAX_VALUE;
            } else if (Double.class.equals(clazz) || double.class.equals(clazz) || MutableDouble.class.equals(clazz)) {
                result = Double.MAX_VALUE;
            } else if (Float.class.equals(clazz) || float.class.equals(clazz) || MutableFloat.class.equals(clazz)) {
                result = Float.MAX_VALUE;
            } else if (BigInteger.class.equals(clazz)) {
                result = Long.MAX_VALUE;
            } else if (Long.class.equals(clazz) || AtomicLong.class.equals(clazz) || long.class.equals(clazz) || MutableLong.class.equals(clazz)) {
                result = Long.MAX_VALUE;
            } else if (Integer.class.equals(clazz) || AtomicInteger.class.equals(clazz) || int.class.equals(clazz) || MutableInt.class.equals(clazz)) {
                result = Integer.MAX_VALUE;
            } else if (Short.class.equals(clazz) || short.class.equals(clazz) || MutableShort.class.equals(clazz)) {
                result = Short.MAX_VALUE;
            } else if (Byte.class.equals(clazz) || byte.class.equals(clazz) || MutableByte.class.equals(clazz)) {
                result = Byte.MAX_VALUE;
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Maximum value of {} is {}", clazz, String.valueOf(result));
        }

        return result;
    }

    /**
     * Checks if is creatable.
     * @param text the text
     * @return true, if is creatable
     */
    public boolean isCreatable(final String text) {
        return org.apache.commons.lang3.math.NumberUtils.isCreatable(text);
    }

    /**
     * Round.
     * @param value    the value
     * @param decimals the number of decimals
     * @return the double
     */
    public double round(final double value, final int decimals) {
        return Math.round(value * powers[decimals]) / (double) powers[decimals];
    }

    /**
     * To binary.
     * @param bits the bits
     * @return the string
     */
    public String toBinary(final BitSet bits) {
        if (bits == null) {
            return toBinary(0);
        }

        if (bits.length() <= Integer.BYTES * 4) {
            return toBinary(toInt(bits));
        }

        return toBinary(toLong(bits));
    }

    /**
     * To binary.
     * @param bits the bits
     * @return the string
     */
    public String toBinary(final boolean[] bits) {
        if (bits == null) {
            return toBinary(0);
        }

        if (bits.length <= Integer.BYTES * 4) {
            return toBinary(toInt(bits));
        }

        return toBinary(toLong(bits));
    }

    /**
     * To binary.
     * @param bits the bits
     * @return the string
     */
    public String toBinary(final int bits) {
        return BINARY_PREFIX + Integer.toBinaryString(bits);
    }

    /**
     * To binary.
     * @param bits the bits
     * @return the string
     */
    public String toBinary(final long bits) {
        return BINARY_PREFIX + Long.toBinaryString(bits);
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public boolean[] toBits(final byte value) {
        final boolean[] bits = new boolean[Byte.SIZE];
        Arrays.fill(bits, false);

        for (int i = 0; i < Byte.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits[i] = true;
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public boolean[] toBits(final int value) {
        final boolean[] bits = new boolean[Integer.SIZE];
        Arrays.fill(bits, false);

        for (int i = 0; i < Integer.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits[i] = true;
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public boolean[] toBits(final long value) {
        final boolean[] bits = new boolean[Long.SIZE];
        Arrays.fill(bits, false);

        for (int i = 0; i < Long.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits[i] = true;
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public boolean[] toBits(final short value) {
        final boolean[] bits = new boolean[Short.SIZE];
        Arrays.fill(bits, false);

        for (int i = 0; i < Short.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits[i] = true;
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public BitSet toBitSet(final byte value) {
        final BitSet bits = new BitSet();

        for (int i = 0; i < Byte.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits.set(i);
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public BitSet toBitSet(final int value) {
        final BitSet bits = new BitSet();

        for (int i = 0; i < Integer.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits.set(i);
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public BitSet toBitSet(final short value) {
        final BitSet bits = new BitSet();

        for (int i = 0; i < Short.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits.set(i);
            }
        }

        return bits;
    }

    /**
     * Convert.
     * @param value the value
     * @return the bit set
     */
    public BitSet toBitsSet(final long value) {
        final BitSet bits = new BitSet();

        for (int i = 0; i < Long.SIZE; i++) {
            if ((value >> i & 1) == 1) {
                bits.set(i);
            }
        }

        return bits;
    }

    /**
     * To byte.
     * @param bits the bits
     * @return the byte
     */
    public byte toByte(final BitSet bits) {
        byte value = 0;

        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? 1 << i : 0;
        }

        return value;
    }

    /**
     * To byte.
     * @param bits the bits
     * @return the byte
     */
    public byte toByte(final boolean[] bits) {
        byte value = 0;

        for (int i = 0; i < bits.length; ++i) {
            value += bits[i] ? 1 << i : 0;
        }

        return value;
    }

    /**
     * Double to byte array.
     * @param value the value
     * @return the byte[]
     */
    public byte[] toBytes(final double value) {
        final byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);

        return bytes;
    }

    /**
     * Float to byte array.
     * @param value the value
     * @return the byte[]
     */
    public byte[] toBytes(final float value) {
        final byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putFloat(value);

        return bytes;
    }

    /**
     * To byte array.
     * @param value the value
     * @return the byte[]
     */
    public byte[] toBytes(final int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    /**
     * To byte array.
     * @param value the value
     * @return the byte[]
     */
    public byte[] toBytes(final long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    /**
     * To byte array.
     * @param value the value
     * @return the byte[]
     */
    public byte[] toBytes(final short value) {
        return ByteBuffer.allocate(2).putShort(value).array();
    }

    /**
     * To byte array.
     * @param hex the hex
     * @return the byte[]
     */
    public byte[] toBytes(final String hex) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(hex)) {
            return new byte[0];
        }

        final int len = hex.length();
        final byte[] result = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }

        return result;
    }

    /**
     * To double.
     * @param bytes the bytes
     * @return the double
     */
    public double toDouble(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    /**
     * To double.
     * @param text the text
     * @return the double
     */
    public double toDouble(final String text) {
        return org.apache.commons.lang3.math.NumberUtils.toDouble(text);
    }

    /**
     * To float.
     * @param bytes the bytes
     * @return the float
     */
    public float toFloat(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    /**
     * To hex.
     * @param bits the bits
     * @return the string
     */
    public String toHex(final BitSet bits) {
        if (bits == null) {
            return toHex(0);
        }

        if (bits.length() <= Integer.BYTES * 4) {
            return toHex(toInt(bits));
        }

        return toHex(toLong(bits));
    }

    /**
     * To hex.
     * @param bits the bits
     * @return the string
     */
    public String toHex(final boolean[] bits) {
        if (bits == null) {
            return toHex(0);
        }

        if (bits.length <= Integer.BYTES * 4) {
            return toHex(toInt(bits));
        }

        return toHex(toLong(bits));
    }

    /**
     * To hex.
     * @param bits the bits
     * @return the string
     */
    public String toHex(final int bits) {
        return HEX_PREFIX + Integer.toHexString(bits);
    }

    /**
     * To hex.
     * @param bits the bits
     * @return the string
     */
    public String toHex(final long bits) {
        return HEX_PREFIX + Long.toHexString(bits);
    }

    /**
     * Human readable bytes count.
     * @param bytes the bytes
     * @param si    the si
     * @return the string
     */
    @SuppressWarnings("boxing")
    public String toHumanReadableByteCount(final long bytes, final boolean si) {
        final int unit = si ? 1000 : 1024;

        if (bytes < unit) {
            return bytes + " B";
        }

        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * To integer.
     * @param bits the bits
     * @return the integer
     */
    public int toInt(final BitSet bits) {
        int value = 0;

        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? 1 << i : 0;
        }

        return value;
    }

    /**
     * To integer.
     * @param bits the bits
     * @return the integer
     */
    public int toInt(final boolean[] bits) {
        int value = 0;

        for (int i = 0; i < bits.length; ++i) {
            value += bits[i] ? 1 << i : 0;
        }

        return value;
    }

    /**
     * To integer.
     * @param bytes the bytes
     * @return the integer
     */
    public int toInt(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * To long.
     * @param bits the bits
     * @return the long
     */
    public long toLong(final BitSet bits) {
        long value = 0L;

        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? 1L << i : 0L;
        }

        return value;
    }

    /**
     * To long.
     * @param bits the bits
     * @return the long
     */
    public long toLong(final boolean[] bits) {
        long value = 0L;

        for (int i = 0; i < bits.length; ++i) {
            value += bits[i] ? 1L << i : 0L;
        }

        return value;
    }

    /**
     * To long.
     * @param bytes the bytes
     * @return the long
     */
    public long toLong(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    /**
     * To long.
     * @param text the text
     * @return the long
     */
    public long toLong(final String text) {
        return org.apache.commons.lang3.math.NumberUtils.toLong(text);
    }

    /**
     * To short.
     * @param bits the bits
     * @return the short
     */
    public short toShort(final BitSet bits) {
        short value = 0;

        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? 1 << i : 0;
        }

        return value;
    }

    /**
     * To short.
     * @param bits the bits
     * @return the short
     */
    public short toShort(final boolean[] bits) {
        short value = 0;

        for (int i = 0; i < bits.length; ++i) {
            value += bits[i] ? 1 << i : 0;
        }

        return value;
    }

    /**
     * To short.
     * @param bytes the bytes
     * @return the short
     */
    public short toShort(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    /**
     * To unsigned integer..
     * @param value the byte
     * @return the integer
     */
    public short toUnsigned(final byte value) {
        return (short) (value & 0xFF);
    }

    /**
     * To unsigned long.
     * @param bytes the bytes
     * @return the big integer
     */
    public BigInteger toUnsignedBigInteger(final byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    /**
     * To unsigned long.
     * @param bytes the bytes
     * @return the long
     */
    public long toUnsignedInt(final byte[] bytes) {
        return toInt(bytes) & 0xFFFFFFFFL;
    }

    /**
     * To unsigned integer.
     * @param bytes the bytes
     * @return the integer
     */
    public int toUnsignedShort(final byte[] bytes) {
        return toShort(bytes) & 0xFFFF;
    }

    /**
     * Unsigned long to bytes.
     * @param value the value
     * @return the byte[]
     */
    public byte[] unsignedBigIntegerToBytes(final BigInteger value) {
        return value.toByteArray();
    }

    /**
     * Unsigned byte to byte.
     * @param value the value
     * @return the byte
     */
    public byte unsignedByteToByte(final short value) {
        return (byte) value;
    }

    /**
     * Unsigned integer to bytes.
     * @param value the value
     * @return the byte[]
     */
    public byte[] unsignedIntToBytes(final long value) {
        return toBytes((int) value);
    }

    /**
     * Unsigned short to bytes.
     * @param value the value
     * @return the byte[]
     */
    public byte[] unsignedShortToBytes(final int value) {
        return toBytes((short) value);
    }
}
