package org.infodavid.commons.checksum;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ChecksumGeneratorRegistry.
 */
public final class ChecksumGeneratorRegistry {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumGeneratorRegistry.class);

    /** The Constant SINGLETON. */
    private static final ChecksumGeneratorRegistry SINGLETON = new ChecksumGeneratorRegistry();

    /**
     * Gets the single instance.
     * @return single instance
     */
    public static ChecksumGeneratorRegistry getInstance() {
        return SINGLETON;
    }

    /** The generators. */
    private final Map<String, ChecksumGenerator> generators; // NOSONAR

    /**
     * Instantiates a new registry.
     */
    private ChecksumGeneratorRegistry() { // NOSONAR
        generators = new HashMap<>();
        final ServiceLoader<ChecksumGenerator> loader = ServiceLoader.load(ChecksumGenerator.class);

        loader.stream().sequential().forEach( i -> {
            final ChecksumGenerator generator = i.get();
            generators.put(generator.getAlgorithm().replace("-", "").toLowerCase(), generator);
        });

        if (generators.isEmpty()) {
            LOGGER.warn("No implementation found");
        }
    }

    /**
     * Gets the generator.
     * @param algorithm the algorithm
     * @return the generator
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public ChecksumGenerator getGenerator(final String algorithm) throws NoSuchAlgorithmException { // NOSONAR
        if (StringUtils.isEmpty(algorithm)) {
            throw new IllegalArgumentException("Algorithm is null or empty");
        }

        final String key = algorithm.toLowerCase().replace("-", "");
        final ChecksumGenerator result = generators.get(key);

        if (result == null) {
            throw new NoSuchAlgorithmException("Algorithm is not supported: " + algorithm);
        }

        return result;
    }

    /**
     * Gets the supported algorithms.
     * @return the supported algorithms
     */
    public String[] getSupportedAlgorithms() {
        return generators.keySet().toArray(new String[generators.size()]);
    }

    /**
     * Register a generator.
     * @param generator the generator
     */
    public void register(final ChecksumGenerator generator) {
        if (generator == null || StringUtils.isEmpty(generator.getAlgorithm())) {
            return;
        }

        generators.put(generator.getAlgorithm(), generator);
    }

    /**
     * Unregister a generator.
     * @param algorithm the algorithm
     * @return the checksum generator
     */
    public ChecksumGenerator unregister(final String algorithm) {
        if (StringUtils.isEmpty(algorithm)) {
            return null;
        }

        return generators.remove(algorithm);
    }
}
