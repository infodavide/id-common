package org.infodavid.commons.system;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * The Class CommandRunnerFactory.
 */
public class CommandRunnerFactory {

    /** The instance. */
    private static CommandRunner instance;

    /**
     * Returns the instance.
     * @return the instance
     */
    public static synchronized CommandRunner getInstance() {
        if (instance != null) {
            return instance;
        }

        final ServiceLoader<CommandRunner> loader = ServiceLoader.load(CommandRunner.class);
        final Iterator<CommandRunner> ite = loader.iterator();

        if (ite.hasNext()) {
            instance = ite.next();
        } else {
            instance = new DefaultCommandExecutor();
        }

        return instance;
    }

    /**
     * Not allowed.
     */
    private CommandRunnerFactory() {
    }
}
