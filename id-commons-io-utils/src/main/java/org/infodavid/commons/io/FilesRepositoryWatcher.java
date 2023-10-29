package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.infodavid.commons.io.FilesRepositoryWatcher.EventEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

/**
 * The Class FilesRepositoryWatcher.
 */
public class FilesRepositoryWatcher implements Runnable, RemovalListener<Path, EventEntry> {

    /**
     * The Class EventEntry.
     */
    public static class EventEntry {

        /** The kind. */
        Kind<?> kind;

        /** The path. */
        Path path;

        /**
         * Instantiates a new event entry.
         * @param path the path
         * @param kind the kind
         */
        public EventEntry(final Path path, final Kind<?> kind) {
            this.kind = StandardWatchEventKinds.ENTRY_CREATE.equals(kind) ? StandardWatchEventKinds.ENTRY_MODIFY : kind;
            this.path = path.toAbsolutePath();
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            final EventEntry other = (EventEntry) obj;

            if (!Objects.equals(kind, other.kind)) {
                return false;
            }

            return Objects.equals(path, other.path);
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (kind == null ? 0 : kind.name().hashCode());
            return prime * result + (path == null ? 0 : path.hashCode());
        }
    }

    /**
     * The Class EventEntryProcessor.<br>
     * The processor is used to process event in a different thread and keep event order by observer using an internal BlockingQueue.
     */
    private static class EventEntryProcessor implements Runnable {

        /** The observer. */
        private final FilesRepositoryObserver observer;

        /** The queue. */
        private final BlockingQueue<EventEntry> queue = new ArrayBlockingQueue<>(10);

        /** The running. */
        private final AtomicBoolean running = new AtomicBoolean(false);

        /**
         * Instantiates a new event entry processor.
         * @param observer the observer
         */
        public EventEntryProcessor(final FilesRepositoryObserver observer) {
            this.observer = observer;
        }

        /*
         * (non-javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            LOGGER.debug("Starting entry processor for observer: {}", observer);
            running.set(true);
            EventEntry entry;

            try {
                while (running.get()) {
                    entry = queue.poll(1000, TimeUnit.MILLISECONDS);

                    if (entry == null) {
                        continue;
                    }

                    if (StandardWatchEventKinds.ENTRY_CREATE.equals(entry.kind)) {
                        observer.fileCreated(entry.path);
                    } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(entry.kind)) {
                        observer.fileDeleted(entry.path);
                    } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(entry.kind)) {
                        observer.fileModfied(entry.path);
                    }
                }
            } catch (final InterruptedException e) { // NOSONAR Handled by utilities
                LOGGER.debug("Thread interrupted", e);
                Thread.currentThread().interrupt();
            } finally {
                LOGGER.debug("Entry processor stopped for observer: {}", observer);
            }
        }

        /**
         * Stop.
         */
        public void stop() {
            running.set(false);
        }
    }

    /** The Constant LOGGER. Keep it public. */
    public static final Logger LOGGER = LoggerFactory.getLogger(FilesRepositoryWatcher.class);

    /**
     * The cache.<br>
     * This cache is used to enqueue store events and avoid multiple dispatching when multiple similar events are detected.<br>
     * When cache entry expires, it indicates that no similar event has been detected and it can be dispatched.<br>
     * Order is normally kept by the expiration duration.
     */
    private final Cache<Path, EventEntry> cache;

    /** The enabled. */
    private final AtomicBoolean enabled = new AtomicBoolean(true);

    /** The executor. */
    private final ExecutorService executor;

    /** The observers. */
    private final Map<FilesRepositoryObserver, EventEntryProcessor> observers = new ConcurrentHashMap<>();

    /** The path. */
    private final Path path;

    /** The paused. */
    private final AtomicBoolean paused = new AtomicBoolean(false);

    /** The thread. */
    private Thread thread = null;

    /**
     * Instantiates a new watcher.
     * @param path     the path
     * @param executor the executor
     */
    public FilesRepositoryWatcher(final Path path, final ScheduledExecutorService executor) {
        cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).removalListener(this).build();
        this.executor = executor;
        this.path = path;
        executor.scheduleAtFixedRate(cache::cleanUp, 500, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds the observer.
     * @param observer the observer
     */
    public void addObserver(final FilesRepositoryObserver observer) {
        if (observer == null) {
            return;
        }

        final EventEntryProcessor processor = new EventEntryProcessor(observer);
        observers.put(observer, processor);
        executor.submit(processor);
    }

    /**
     * Pause.
     */
    public void pause() {
        LOGGER.debug("Pausing file monitor for directory: {}", path);
        paused.set(true);
    }

    /**
     * Removes the observer.
     * @param observer the observer
     */
    public void removeObserver(final FilesRepositoryObserver observer) {
        if (observer == null) {
            return;
        }

        final EventEntryProcessor processor = observers.remove(observer);
        processor.stop();
    }

    /**
     * Resume.
     */
    public void resume() {
        LOGGER.debug("Resuming file monitor for directory: {}", path);
        cache.invalidateAll();
        paused.set(false);
    }

    /*
     * (non-javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        thread = Thread.currentThread();
        LOGGER.debug("Initializing monitoring of directory: {}", path);

        try (FileSystem fs = FileSystems.getDefault(); final WatchService service = fs.newWatchService()) {
            path.register(service, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            final Collection<EventEntry> events = new LinkedList<>();
            WatchKey key;
            LOGGER.debug("Monitoring directory: {}", path);

            while (enabled.get()) {
                key = service.poll(1000, TimeUnit.MILLISECONDS);

                if (key == null) {
                    continue;
                }

                for (final WatchEvent<?> event : key.pollEvents()) { // NOSONAR Number of continue
                    if (StandardWatchEventKinds.OVERFLOW.equals(event.kind())) {
                        continue;
                    }

                    if (paused.get()) {
                        LOGGER.debug("Monitor is on pause, event on file: {} will not be dispatched to observers.", event.context());

                        continue;
                    }

                    if (enabled.get() && key.reset()) {
                        final EventEntry entry = new EventEntry(path.resolve((Path) event.context()), event.kind());
                        LOGGER.debug("File {}, event: {}", entry.path, entry.kind);
                        events.add(entry);

                    }
                }

                if (enabled.get() && key.reset()) {
                    events.forEach(e -> cache.put(e.path, e));
                }

                events.clear();
            }
        } catch (@SuppressWarnings("unused") final InterruptedException e) {
            LOGGER.warn("Monitor of directory: {} interrupted.", path);
            Thread.currentThread().interrupt();
        } catch (final IOException e) {
            LOGGER.error("Cannot monitor modifications on directory: " + path, e); // NOSONAR No format when using Throwable
        }

        LOGGER.debug("End of monitoring of directory: {}", path);
    }

    /**
     * Stop.
     * @throws InterruptedException the interrupted exception
     */
    public void stop() throws InterruptedException {
        enabled.set(false);
        thread.interrupt();
        observers.values().forEach(EventEntryProcessor::stop); // NOSONAR Lambda
    }

    /*
     * (non-javadoc)
     * @see com.github.benmanes.caffeine.cache.RemovalListener#onRemoval(java.lang.Object, java.lang.Object, com.github.benmanes.caffeine.cache.RemovalCause)
     */
    @Override
    public void onRemoval(final Path key, final EventEntry value, final RemovalCause cause) {
        if (key == null || value == null) {
            return;
        }

        LOGGER.debug("Dispatching file {}, event: {}", key, value.kind);
        observers.values().forEach(p -> p.queue.offer(value));
    }
}
