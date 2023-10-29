package org.infodavid.commons.swing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * The Class Utf8Control.
 */
public class Utf8Control extends Control {

    /**
     * Instantiates a new control.
     */
    public Utf8Control() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see java.util.ResourceBundle.Control#newBundle(java.lang.String, java.util.Locale, java.lang.String, java.lang.ClassLoader, boolean)
     */
    @SuppressWarnings("resource")
    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        // The below is a copy of the default implementation.
        final String bundleName = toBundleName(baseName, locale);
        final String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle bundle = null;
        InputStream stream = null;

        if (reload) {
            final URL url = loader.getResource(resourceName);

            if (url != null) {
                final URLConnection connection = url.openConnection();

                if (connection != null) {
                    connection.setUseCaches(false);
                    stream = connection.getInputStream();
                }
            }
        } else {
            stream = loader.getResourceAsStream(resourceName); // NOSONAR handled below
        }

        if (stream != null) {
            try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                // Only this line is changed to make it to read properties files
                // as UTF-8.
                bundle = new PropertyResourceBundle(reader);
            }
        }

        return bundle;
    }
}
