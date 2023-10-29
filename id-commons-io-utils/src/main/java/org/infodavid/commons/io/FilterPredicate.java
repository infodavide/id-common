package org.infodavid.commons.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.yevdo.jwildcard.JWildcard;

/**
 * The Class FilterPredicate.
 */
class FilterPredicate implements Predicate<Path> {

    /** The exclude patterns. */
    private Collection<Pattern> excludePatterns;

    /** The include patterns. */
    private Collection<Pattern> includePatterns;

    /** The include hidden. */
    private final boolean includeHidden;

    /**
     * Instantiates a new filter predicate.
     * @param includes      the includes
     * @param excludes      the excludes
     * @param includeHidden the include hidden
     */
    public FilterPredicate(final Collection<String> includes, final Collection<String> excludes, final boolean includeHidden) {
        this.includeHidden = includeHidden;

        if (includes == null || includes.isEmpty()) {
            includePatterns = Collections.emptyList();
        } else {
            includePatterns = new ArrayList<>();

            for (final String item : includes) {
                includePatterns.add(Pattern.compile(JWildcard.wildcardToRegex(item)));
            }
        }

        if (excludes == null || excludes.isEmpty()) {
            excludePatterns = Collections.emptyList();
        } else {
            excludePatterns = new ArrayList<>();

            for (final String item : excludes) {
                excludePatterns.add(Pattern.compile(JWildcard.wildcardToRegex(item)));
            }
        }
    }

    /*
     * (non-javadoc)
     * @see java.util.function.Predicate#test(java.lang.Object)
     */
    @Override
    public boolean test(final Path path) { // NOSONAR No complexity
        if (path == null) {
            return false;
        }

        if (!includeHidden) {
            try {
                if (Files.isHidden(path)) {
                    return false;
                }
            } catch (@SuppressWarnings("unused") final Exception e) {
                // noop
            }
        }

        final String value = path.toAbsolutePath().toAbsolutePath().toString();

        if (!includePatterns.isEmpty()) {
            for (final Pattern pattern : includePatterns) {
                if (pattern.matcher(value).matches()) {
                    return true;
                }
            }
        }

        if (!excludePatterns.isEmpty()) {
            for (final Pattern pattern : excludePatterns) {
                if (pattern.matcher(value).matches()) {
                    return false;
                }
            }
        }

        return true;
    }
}
