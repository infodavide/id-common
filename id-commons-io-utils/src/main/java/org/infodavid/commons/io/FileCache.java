package org.infodavid.commons.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Weigher;

/**
 * The Class FileCache.
 */
public class FileCache {

    /**
     * The Class CacheWeigher.
     */
    private static class CacheWeigher implements Weigher<Path, Content> {

        @Override
        public int weigh(final Path key, final Content value) {
            if (value == null || value.getData() == null) {
                return 0;
            }

            return value.getData().length;
        }
    }

    /** The Constant DEFAULT_EXPIRATION. */
    private static final byte DEFAULT_EXPIRATION = 15;

    /** The Constant DEFAULT_MAXIMUM_WEIGHT. */
    private static final long DEFAULT_MAXIMUM_WEIGHT = 52428800;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCache.class);

    /** The contents cache. */
    private Cache<Path, Content> contentCache;

    /** The enabled. */
    private boolean enabled = true;

    /** The expiration. */
    private long expiration = DEFAULT_EXPIRATION;

    /** The locks cache. */
    private final Cache<Path, Lock> lockCache;

    /** The maximum weight. */
    private long maximumWeight = DEFAULT_MAXIMUM_WEIGHT;

    /**
     * Instantiates a new cache.
     */
    protected FileCache() {
        this(true, DEFAULT_EXPIRATION, TimeUnit.MINUTES, DEFAULT_MAXIMUM_WEIGHT);
    }

    /**
     * Instantiates a new cache.
     */
    protected FileCache(final boolean accessExpiration, final long expiration, final TimeUnit expirationUnit, final long maximumWeight) {
        lockCache = Caffeine.newBuilder().build();
        final Caffeine<Object, Object> builder = Caffeine.newBuilder();

        if (accessExpiration) {
            builder.expireAfterAccess(expiration, expirationUnit);
        } else {
            builder.expireAfterWrite(expiration, expirationUnit);
        }

        if (maximumWeight > 0) {
            builder.weigher(new CacheWeigher()).maximumWeight(maximumWeight);
        }

        builder.removalListener((k, v, c) -> {
            if (k instanceof final Path p) {
                lockCache.invalidate(p);
            }
        });

        contentCache = builder.build();
    }

    /**
     * Gets the bytes.
     * @param file the file
     * @return the bytes
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] getData(final File file) throws IOException {
        if (file == null) {
            throw new IOException("Specified file is null");
        }

        return getData(file.toPath());
    }

    /**
     * Gets the bytes.
     * @param path the path
     * @return the bytes
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] getData(final Path path) throws IOException {
        if (path == null) {
            throw new IOException("Specified path is null");
        }

        final Lock lock = lockCache.get(path, k -> new ReentrantLock());
        lock.lock();

        try {
            Content content = enabled ? contentCache.getIfPresent(path) : null;

            if (content != null && content.getModificationDate() < Files.getLastModifiedTime(path).toMillis()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Content modified since : {}, reloading", new Date(content.getModificationDate()));
                }

                content = null;
            }

            if (content == null) {
                content = new Content(Files.readAllBytes(path), Files.getLastModifiedTime(path).toMillis());

                if (enabled) {
                    contentCache.put(path, content);
                }
            }

            return content.getData();
        } finally {
            lock.unlock();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Cache size: {} ({})", String.valueOf(contentCache.estimatedSize()), String.valueOf(lockCache.estimatedSize()));
            }
        }
    }

    /**
     * Gets the expiration.
     * @return the expiration
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Gets the maximum weight.
     * @return the maximumWeight
     */
    public long getMaximumWeight() {
        return maximumWeight;
    }

    /**
     * Gets the size.
     * @return the size
     */
    public long getSize() {
        return contentCache.estimatedSize();
    }

    /**
     * Invalidate.
     */
    public void invalidate() {
        contentCache.invalidateAll();
    }

    /**
     * Checks if is enabled.
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled.
     * @param enabled the enabled to set
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;

        if (!enabled) {
            contentCache.invalidateAll();
        }
    }

    /**
     * Sets the expiration.
     * @param minutes the new expiration
     */
    public void setExpiration(final long minutes) {
        contentCache.invalidateAll();
        expiration = minutes;
        contentCache = Caffeine.newBuilder().expireAfterAccess(minutes, TimeUnit.MINUTES).weigher(new CacheWeigher()).maximumWeight(maximumWeight).removalListener((k, v, c) -> {
            if (k == null) {
                return;
            }

            lockCache.invalidate(k);
        }).build();
    }

    /**
     * Sets the maximum weight.
     * @param weight the new maximum weight
     */
    public void setMaximumWeight(final long weight) {
        contentCache.invalidateAll();
        maximumWeight = weight;
        contentCache = Caffeine.newBuilder().expireAfterAccess(expiration, TimeUnit.MINUTES).weigher(new CacheWeigher()).maximumWeight(maximumWeight).removalListener((k, v, c) -> {
            if (k == null) {
                return;
            }

            lockCache.invalidate(k);
        }).build();
    }
}
