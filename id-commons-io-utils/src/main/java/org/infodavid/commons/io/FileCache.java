package org.infodavid.commons.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    private static class CacheWeigher implements Weigher<URI, Content> {

        @Override
        public int weigh(final URI key, final Content value) {
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
    private Cache<URI, Content> contentCache;

    /** The enabled. */
    private boolean enabled = true;

    /** The expiration. */
    private long expiration = DEFAULT_EXPIRATION;

    /** The locks cache. */
    private final Cache<URI, Lock> lockCache;

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
            if (k instanceof final URI u) {
                lockCache.invalidate(u);
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

        return getData(file.toURI());
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

        return getData(path.toUri());
    }

    /**
     * Gets the bytes.
     * @param uri the URI
     * @return the bytes
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] getData(final URI uri) throws IOException {
        if (uri == null) {
            throw new IOException("Specified URI is null");
        }

        final Lock lock = lockCache.get(uri, k -> new ReentrantLock());
        lock.lock();

        try {
            Content content = enabled ? contentCache.getIfPresent(uri) : null;

            if (content != null && content.getModificationDate() < getLastModificationDate(uri)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Content modified since : {}, reloading", new Date(content.getModificationDate()));
                }

                content = null;
            }

            if (content == null) {
                content = load(uri);

                if (enabled) {
                    contentCache.put(uri, content);
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
     * Gets the bytes.
     * @param url the URL
     * @return the bytes
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] getData(final URL url) throws IOException {
        if (url == null) {
            throw new IOException("Specified URL is null");
        }

        try {
            return getData(url.toURI());
        } catch (final URISyntaxException e) {
            throw new IOException(ExceptionUtils.getRootCause(e));
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

    /**
     * Load.
     * @param uri the URI
     * @return the content
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static Content load(final URI uri) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Loading data from URI: {}", uri);
        }

        if ("file".equalsIgnoreCase(uri.getScheme())) {
            final File file = new File(uri);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Modification date: {}", new Date(file.lastModified()));
            }

            return new Content(FileUtils.readFileToByteArray(file), file.lastModified());
        }

        final URLConnection connection = uri.toURL().openConnection();

        try (InputStream in = connection.getInputStream()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Modification date: {}", new Date(connection.getLastModified()));
            }

            return new Content(IOUtils.toByteArray(in), connection.getLastModified());
        } finally {
            if (connection instanceof final HttpURLConnection c) {
                c.disconnect();
            } else if (connection instanceof final Closeable c) {
                c.close();
            }
        }
    }

    /**
     * Gets the current modification date.
     * @param file the file
     * @return the current modification date
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected long getCurrentModificationDate(final File file) throws IOException {
        if (file == null) {
            throw new IOException("Specified file is null");
        }

        final Content content = contentCache.getIfPresent(file.toURI());

        return content == null ? -1 : content.getModificationDate();
    }

    /**
     * Gets the current modification date.
     * @param uri the URI
     * @return the current modification date
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected long getCurrentModificationDate(final URI uri) throws IOException {
        if (uri == null) {
            throw new IOException("Specified URI is null");
        }

        final Content content = contentCache.getIfPresent(uri);

        return content == null ? -1 : content.getModificationDate();
    }

    /**
     * Gets the last modification date.
     * @param file the file
     * @return the last modification date
     */
    @SuppressWarnings("static-method")
    protected long getLastModificationDate(final File file) {
        return file.lastModified();
    }

    /**
     * Gets the last modification date.
     * @param uri the URI
     * @return the last modification date
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("static-method")
    protected long getLastModificationDate(final URI uri) throws IOException {
        final URLConnection connection = uri.toURL().openConnection();

        try {
            return connection.getLastModified();
        } finally {
            if (connection instanceof final HttpURLConnection c) {
                c.disconnect();
            } else if (connection instanceof final Closeable c) {
                c.close();
            }
        }
    }
}
