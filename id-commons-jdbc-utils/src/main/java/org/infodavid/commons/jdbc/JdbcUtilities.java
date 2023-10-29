package org.infodavid.commons.jdbc;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class JdbcUtilities.
 */
@SuppressWarnings("static-method")
public final class JdbcUtilities {

    /** The singleton. */
    private static WeakReference<JdbcUtilities> instance = null;

    /** The Constant MAPPINGS. */
    @SuppressWarnings("rawtypes")
    private static final Map<Class,String> MAPPINGS;

    static {
        MAPPINGS = new LinkedHashMap<>();
        MAPPINGS.put(String.class, JDBCType.VARCHAR.name());
        MAPPINGS.put(BigDecimal.class, JDBCType.NUMERIC.name());
        MAPPINGS.put(boolean.class, JDBCType.BIT.name());
        MAPPINGS.put(byte.class, JDBCType.TINYINT.name());
        MAPPINGS.put(short.class, JDBCType.SMALLINT.name());
        MAPPINGS.put(int.class, JDBCType.INTEGER.name());
        MAPPINGS.put(long.class, JDBCType.BIGINT.name());
        MAPPINGS.put(float.class, JDBCType.REAL.name());
        MAPPINGS.put(double.class, JDBCType.DOUBLE.name());
        MAPPINGS.put(byte[].class, JDBCType.VARBINARY.name());
        MAPPINGS.put(Date.class, JDBCType.DATE.name());
        MAPPINGS.put(Time.class, JDBCType.TIME.name());
        MAPPINGS.put(Timestamp.class, JDBCType.TIMESTAMP.name());
    }

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized JdbcUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new JdbcUtilities());
        }

        return instance.get();
    }

    /**
     * Gets the mappings.
     * @return the classes
     */
    @SuppressWarnings("rawtypes")
    public static Map<Class,String> getMappings() {
        return MAPPINGS;
    }

    /**
     * Instantiates a new utilities.
     */
    private JdbcUtilities() {
    }

    /**
     * Gets the class.
     * @param jdbcType the JDBC type
     * @return the class
     * @throws SQLException the SQL exception
     */
    @SuppressWarnings("rawtypes")
    public Class getClass(final String jdbcType) throws SQLException {
        for (final Entry<Class,String> entry : MAPPINGS.entrySet()) {
            if (StringUtils.equalsIgnoreCase(entry.getValue(), jdbcType)) {
                return entry.getKey();
            }
        }

        throw new SQLException("Cannot map JDBC type: " + jdbcType + " to Java class");
    }

    /**
     * Gets the JDBC type associated to the given Java class.
     * @param type the Java class
     * @return the JDBC type
     * @throws SQLException the SQL exception
     */
    @SuppressWarnings({
        "rawtypes", "unchecked"
    })
    public String getJdbcType(final Class type) throws SQLException {
        for (final Entry<Class,String> entry : MAPPINGS.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }

        throw new SQLException("Cannot map Java class: " + type + " to JDBC type");
    }
}
