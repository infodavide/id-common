package org.infodavid.commons.swing;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 * The Class SwingUtilities.
 */
@SuppressWarnings("static-method")
public final class SwingUtilities {

    /** The singleton. */
    private static WeakReference<SwingUtilities> instance = null;

    /** The toast singleton dialog. */
    private static JDialog toastSingletonDialog = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized SwingUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new SwingUtilities());
        }

        return instance.get();
    }

    /**
     * Style from message type.
     * @param messageType the message type
     * @return the integer
     */
    private static int styleFromMessageType(final int messageType) {
        switch (messageType) {
        case JOptionPane.ERROR_MESSAGE:
            return JRootPane.ERROR_DIALOG;
        case JOptionPane.QUESTION_MESSAGE:
            return JRootPane.QUESTION_DIALOG;
        case JOptionPane.WARNING_MESSAGE:
            return JRootPane.WARNING_DIALOG;
        case JOptionPane.INFORMATION_MESSAGE:
            return JRootPane.INFORMATION_DIALOG;
        case JOptionPane.PLAIN_MESSAGE:
        default:
            return JRootPane.PLAIN_DIALOG;
        }
    }

    /**
     * Instantiates a new utilities.
     */
    private SwingUtilities() {
    }

    /**
     * Apply.
     * @param component the component
     * @param function  the function
     * @param recursive the recursive
     */
    public void apply(final Component component, final UnaryOperator<Component> function, final boolean recursive) {
        final Container c = component instanceof final Container r ? r : null;

        if (c != null) {
            for (final Component comp : c.getComponents()) {
                function.apply(component);

                if (comp instanceof Container && recursive) {
                    apply(comp, function, recursive);
                }
            }
        }
    }

    /**
     * Center on screen.
     * @param frame  the frame
     * @param dialog the dialog
     */
    public void centerOnScreen(final JFrame frame, final JDialog dialog) {
        final Rectangle screen = frame.getGraphicsConfiguration().getBounds();
        dialog.setLocation(screen.x + (screen.width - dialog.getWidth()) / 2, screen.y + (screen.height - dialog.getHeight()) / 2);
    }

    /**
     * Center on screen.
     * @param window the window
     */
    public void centerOnScreen(final JWindow window) {
        final Rectangle screen = window.getGraphicsConfiguration().getBounds();
        window.setLocation(screen.x + (screen.width - window.getWidth()) / 2, screen.y + (screen.height - window.getHeight()) / 2);
    }

    /**
     * Creates the grid bag constraints.
     * @return the grid bag constraints
     */
    public GridBagConstraints createGridBagConstraints() {
        final GridBagConstraints result = new GridBagConstraints();
        result.fill = GridBagConstraints.HORIZONTAL;
        result.insets = new Insets(2, 2, 2, 2);
        result.weightx = 1;
        result.weighty = 1;
        result.gridx = 0;

        return result;
    }

    /**
     * Enable.
     * @param component the component
     * @param value     the value
     * @param recursive the recursive
     */
    public void enable(final Component component, final boolean value, final boolean recursive) {
        final Container c = component instanceof final Container r ? r : null;

        if (c != null) {
            for (final Component comp : c.getComponents()) {
                comp.setEnabled(value);

                if (comp instanceof Container && recursive) {
                    enable(comp, value, recursive);
                }
            }
        }
    }

    /**
     * Search for components into a specified container.
     * @param <T>       the generic type
     * @param container the container
     * @param type     the class
     * @return the matching components
     */
    public <T> Collection<T> findComponentsByClass(final Container container, final Class<T> type) {
        final Set<T> results = new LinkedHashSet<>();
        findComponentsByClass(container, type, new LinkedHashSet<>(), results);

        return results;
    }

    /**
     * Find components by class.
     * @param <T>       the generic type
     * @param container the container
     * @param type     the class
     * @param processed the processed
     * @param results   the results
     */
    @SuppressWarnings("unchecked")
    private <T> void findComponentsByClass(final Container container, final Class<T> type, final Set<Component> processed, final Set<T> results) {
        if (processed.contains(container)) {
            return;
        }

        processed.add(container);

        if (type.isAssignableFrom(container.getClass())) {
            results.add((T) container);

            return;
        }

        final Component[] components = getComponents(container);

        if (components == null) {
            return;
        }

        for (final Component c : components) {
            if (processed.contains(c)) {
                continue;
            }

            if (type.isAssignableFrom(c.getClass())) {
                results.add((T) c);
                processed.add(c);
            } else if (c instanceof final Container r) {
                findComponentsByClass(r, type, processed, results);
            }
        }
    }

    /**
     * Search for components into a specified container.
     * @param <T>       the generic type
     * @param container the container
     * @param name      the name
     * @return the matching components
     */
    public <T> Collection<T> findComponentsByName(final Container container, final String name) {
        final Set<T> results = new LinkedHashSet<>();
        findComponentsByName(container, name, new LinkedHashSet<>(), results);

        return results;
    }

    /**
     * Find components by name.
     * @param <T>       the generic type
     * @param container the container
     * @param name      the name
     * @param processed the processed
     * @param results   the results
     */
    @SuppressWarnings("unchecked")
    private <T> void findComponentsByName(final Container container, final String name, final Set<Component> processed, final Set<T> results) {
        if (processed.contains(container)) {
            return;
        }

        processed.add(container);

        if (name.equals(container.getName())) {
            results.add((T) container);

            return;
        }

        final Component[] components = getComponents(container);

        if (components == null) {
            return;
        }

        for (final Component c : components) {
            if (processed.contains(c)) {
                continue;
            }

            processed.add(c);

            if (name.equals(c.getName())) {
                results.add((T) c);
            } else if (c instanceof final Container r) {
                findComponentsByName(r, name, processed, results);
            }
        }
    }

    /**
     * Gets the component from list.
     * @param <T>      the generic type
     * @param clazz    the clazz
     * @param list     the list
     * @param property the property
     * @param value    the value
     * @return the component from list
     * @throws IllegalArgumentException the illegal argument exception
     */
    private <T extends JComponent> T getComponentFromList(final Class<T> clazz, final List<T> list, final String property, final Object value) throws IllegalArgumentException {
        final T retVal = null;
        Method method = null;

        try {
            method = clazz.getMethod("get" + property);
        } catch (@SuppressWarnings("unused") final NoSuchMethodException e) {
            try {
                method = clazz.getMethod("is" + property);
            } catch (@SuppressWarnings("unused") final NoSuchMethodException e1) {
                throw new IllegalArgumentException("Property " + property + " not found in class " + clazz.getName());
            }
        }

        try {
            for (final T t : list) {
                final Object testVal = method.invoke(t);

                if (Objects.equals(value, testVal)) {
                    return t;
                }
            }
        } catch (@SuppressWarnings("unused") final InvocationTargetException e) {
            throw new IllegalArgumentException("Error accessing property " + property + " in class " + clazz.getName());
        } catch (@SuppressWarnings("unused") final IllegalAccessException | SecurityException e) {
            throw new IllegalArgumentException("Property " + property + " cannot be accessed in class " + clazz.getName());
        }

        return retVal;
    }

    /**
     * Convenience method for mapping a container in the hierarchy to its contained components.<br/>
     * The keys are the containers, and the values are lists of contained components.<br/>
     * Implementation note: The returned value is a HashMap and the values are of type ArrayList.<br/>
     * This is subject to change, so callers should code against the interfaces Map and List.
     * @param container The JComponent to be mapped
     * @param nested    true to drill down to nested containers, false otherwise
     * @return the Map of the UI
     */
    public Map<JComponent, List<JComponent>> getComponentMap(final JComponent container, final boolean nested) {
        final HashMap<JComponent, List<JComponent>> retVal = new HashMap<>();

        for (final JComponent component : getDescendantsOfType(JComponent.class, container, false)) {
            final List<JComponent> components = retVal.computeIfAbsent(container, k -> new ArrayList<>());
            components.add(component);

            if (nested) {
                retVal.putAll(getComponentMap(component, nested));
            }
        }

        return retVal;
    }

    /**
     * Gets the components.
     * @param component the component
     * @return the components
     */
    public Component[] getComponents(final Component component) {
        Component[] result;

        if (component instanceof final Container r) {
            result = r.getComponents();
        } else {
            result = new Component[0];
        }

        return result;
    }

    /**
     * Convenience method that searches below <code>container</code> in the component hierarchy in a depth first manner and returns the first found component of class <code>clazz</code> having the bound property value.<br/>
     * Returns {@code null} if such component cannot be found.<br/>
     * This method invokes getDescendantOfClass(clazz, container, property, value, true)
     * @param <T>       the generic type
     * @param clazz     the class of component to be found.
     * @param container the container at which to begin the search
     * @param property  the className of the bound property, exactly as expressed in the accessor e.g. "Text" for getText(), "Value" for getValue(). This parameter is case sensitive.
     * @param value     the value of the bound property
     * @return the component, or null if no such component exists in the container's hierarchy.
     * @throws IllegalArgumentException           the illegal argument exception
     * @throws java.lang.IllegalArgumentException if the bound property does not exist for the class or cannot be accessed
     */
    public <T extends JComponent> T getDescendantOfClass(final Class<T> clazz, final Container container, final String property, final Object value) throws IllegalArgumentException {
        return getDescendantOfClass(clazz, container, property, value, true);
    }

    /**
     * Convenience method that searches below <code>container</code> in the component hierarchy in a depth first manner and returns the first found component of class <code>clazz</code> having the bound property value.<br/>
     * Returns {@code null} if such component cannot be found.
     * @param <T>       the generic type
     * @param clazz     the class of component to be found.
     * @param container the container at which to begin the search
     * @param property  the className of the bound property, exactly as expressed in the accessor e.g. "Text" for getText(), "Value" for getValue(). This parameter is case sensitive.
     * @param value     the value of the bound property
     * @param nested    true to include components nested within another listed component, false otherwise
     * @return the component, or null if no such component exists in the container's hierarchy
     * @throws IllegalArgumentException           the illegal argument exception
     * @throws java.lang.IllegalArgumentException if the bound property does not exist for the class or cannot be accessed
     */
    public <T extends JComponent> T getDescendantOfClass(final Class<T> clazz, final Container container, final String property, final Object value, final boolean nested) throws IllegalArgumentException {
        return getComponentFromList(clazz, getDescendantsOfClass(clazz, container, nested), property, value);
    }

    /**
     * Convenience method that searches below <code>container</code> in the component hierarchy and returns the first found component that is an instance of class <code>clazz</code> having the bound property value.<br/>
     * Returns {@code null} if such component cannot be found.<br/>
     * This method invokes getDescendantOfType(clazz, container, property, value, true)
     * @param <T>       the generic type
     * @param clazz     the class of component whose instance is to be found.
     * @param container the container at which to begin the search
     * @param property  the className of the bound property, exactly as expressed in the accessor e.g. "Text" for getText(), "Value" for getValue().
     * @param value     the value of the bound property
     * @return the component, or null if no such component exists in the container
     * @throws IllegalArgumentException           the illegal argument exception
     * @throws java.lang.IllegalArgumentException if the bound property does not exist for the class or cannot be accessed
     */
    public <T extends JComponent> T getDescendantOfType(final Class<T> clazz, final Container container, final String property, final Object value) throws IllegalArgumentException {
        return getDescendantOfType(clazz, container, property, value, true);
    }

    /**
     * Convenience method that searches below <code>container</code> in the component hierarchy and returns the first found component that is an instance of class <code>clazz</code> and has the bound property value.<br/>
     * Returns {@code null} if such component cannot be found.
     * @param <T>       the generic type
     * @param clazz     the class of component whose instance to be found.
     * @param container the container at which to begin the search
     * @param property  the className of the bound property, exactly as expressed in the accessor e.g. "Text" for getText(), "Value" for getValue().
     * @param value     the value of the bound property
     * @param nested    true to list components nested within another component which is also an instance of <code>clazz</code>, false otherwise
     * @return the component, or null if no such component exists in the container
     * @throws IllegalArgumentException           the illegal argument exception
     * @throws java.lang.IllegalArgumentException if the bound property does not exist for the class or cannot be accessed
     */
    public <T extends JComponent> T getDescendantOfType(final Class<T> clazz, final Container container, final String property, final Object value, final boolean nested) throws IllegalArgumentException {
        final List<T> list = getDescendantsOfType(clazz, container, nested);
        return getComponentFromList(clazz, list, property, value);
    }

    /**
     * Convenience method for searching below <code>container</code> in the component hierarchy and return nested components of class <code>clazz</code> it finds.<br/>
     * Returns an empty list if no such components exist in the container. This method invokes getDescendantsOfClass(clazz, container, true)
     * @param <T>       the generic type
     * @param clazz     the class of components to be found.
     * @param container the container at which to begin the search
     * @return the List of components
     */
    public <T extends JComponent> List<T> getDescendantsOfClass(final Class<T> clazz, final Container container) {
        return getDescendantsOfClass(clazz, container, true);
    }

    /**
     * Convenience method for searching below <code>container</code> in the component hierarchy and return nested components of class <code>clazz</code> it finds. Returns an empty list if no such components exist in the container.
     * @param <T>       the generic type
     * @param clazz     the class of components to be found.
     * @param container the container at which to begin the search
     * @param nested    true to list components nested within another listed component, false otherwise
     * @return the List of components
     */
    public <T extends JComponent> List<T> getDescendantsOfClass(final Class<T> clazz, final Container container, final boolean nested) {
        final List<T> results = new ArrayList<>();

        for (final Component component : getComponents(container)) {
            if (clazz.equals(component.getClass())) {
                results.add(clazz.cast(component));
            }

            if (nested || !clazz.equals(component.getClass())) {
                results.addAll(getDescendantsOfClass(clazz, (Container) component, nested));
            }
        }

        return results;
    }

    /**
     * Convenience method for searching below <code>container</code> in the component hierarchy and return nested components that are instances of class <code>clazz</code> it finds.<br/>
     * Returns an empty list if no such components exist in the container.<br/>
     * Invoking this method with a class parameter of JComponent.class will return all nested components. This method invokes getDescendantsOfType(clazz, container, true)
     * @param <T>       the generic type
     * @param clazz     the class of components whose instances are to be found.
     * @param container the container at which to begin the search
     * @return the List of components
     */
    public <T extends JComponent> List<T> getDescendantsOfType(final Class<T> clazz, final Container container) {
        return getDescendantsOfType(clazz, container, true);
    }

    /**
     * Convenience method for searching below <code>container</code> in the component hierarchy and return nested components that are instances of class <code>clazz</code> it finds.<br/>
     * Returns an empty list if no such components exist in the container.<br/>
     * Invoking this method with a class parameter of JComponent.class will return all nested components.
     * @param <T>       the generic type
     * @param clazz     the class of components whose instances are to be found.
     * @param container the container at which to begin the search
     * @param nested    true to list components nested within another listed component, false otherwise
     * @return the List of components
     */
    public <T extends JComponent> List<T> getDescendantsOfType(final Class<T> clazz, final Container container, final boolean nested) {
        final List<T> results = new ArrayList<>();

        for (final Component component : getComponents(container)) {
            if (clazz.isAssignableFrom(component.getClass())) {
                results.add(clazz.cast(component));
            }

            if (nested || !clazz.isAssignableFrom(component.getClass())) {
                results.addAll(getDescendantsOfType(clazz, (Container) component, nested));
            }
        }

        return results;
    }

    /**
     * Scroll to bottom.
     * @param scrollPane the scroll pane
     */
    public void scrollToBottom(final JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(final AdjustmentEvent e) {
                final Adjustable adjustable = e.getAdjustable();

                adjustable.setValue(adjustable.getMaximum());
                scrollPane.getVerticalScrollBar().removeAdjustmentListener(this);
            }
        });
    }

    /**
     * Sets the default font size.
     * @param size the new font size
     */
    public void setDefaultFontSize(final int size) {
        for (final Map.Entry<Object, Object> entry : javax.swing.UIManager.getDefaults().entrySet()) {
            final Object value = javax.swing.UIManager.get(entry.getKey());

            if (value instanceof final javax.swing.plaf.FontUIResource resource) {
                final javax.swing.plaf.FontUIResource clone = new javax.swing.plaf.FontUIResource(resource.getFamily(), resource.getStyle(), size);

                javax.swing.UIManager.put(entry.getKey(), clone);
            }
        }

    }

    /**
     * Show toast.
     * @param parent      the parent
     * @param message     the message
     * @param title       the title
     * @param messageType the message type
     * @throws HeadlessException the headless exception
     */
    public void showToast(final JFrame parent, final Object message, final String title, final int messageType) throws HeadlessException {
        showToast(parent, message, title, messageType, 4000, -1, -1);
    }

    /**
     * Show toast.
     * @param parent      the parent
     * @param message     the message
     * @param title       the title
     * @param messageType the message type
     * @param duration    the duration
     * @throws HeadlessException the headless exception
     */
    public void showToast(final JFrame parent, final Object message, final String title, final int messageType, final int duration) throws HeadlessException {
        showToast(parent, message, title, messageType, duration, -1, -1);
    }

    /**
     * Show toast.
     * @param parent      the parent
     * @param message     the message
     * @param title       the title
     * @param messageType the message type
     * @param duration    the duration
     * @param x           the x
     * @param y           the y
     * @throws HeadlessException the headless exception
     */
    public synchronized void showToast(final JFrame parent, final Object message, final String title, final int messageType, final int duration, final int x, final int y) throws HeadlessException {
        if (toastSingletonDialog != null && toastSingletonDialog.isVisible()) {
            toastSingletonDialog.setVisible(false);
            toastSingletonDialog.dispose();
        }

        final JOptionPane pane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION, null, null, null);
        final int style = styleFromMessageType(messageType);
        final Window window = parent.getOwner();
        final JDialog dialog;

        if (window instanceof final Frame f) {
            dialog = new JDialog(f, title, true);
        } else {
            dialog = new JDialog((Dialog) window, title, true);
        }

        toastSingletonDialog = dialog;
        pane.setInitialValue(null);
        pane.setComponentOrientation(parent.getComponentOrientation());
        dialog.setComponentOrientation(pane.getComponentOrientation());
        final Container contentPane = dialog.getContentPane();
        final TitledBorder border = BorderFactory.createTitledBorder(title);
        contentPane.setLayout(new BorderLayout());
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        border.setTitleJustification(TitledBorder.CENTER);
        pane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        contentPane.add(pane, BorderLayout.CENTER);

        for (final Component item : getComponents(pane)) {
            if ("OptionPane.buttonArea".equals(item.getName())) {
                pane.remove(item);
            }
        }

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setFocusableWindowState(false);
        dialog.setUndecorated(true);
        dialog.setMinimumSize(new Dimension(300, 50));
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            final boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.setUndecorated(true);
                pane.getRootPane().setWindowDecorationStyle(style);
            }
        }

        if (x < 0 || y < 0) {
            dialog.setLocationRelativeTo(parent);
        } else {
            final Point parentLocation = parent.getLocationOnScreen();

            dialog.setLocation(parentLocation.x + x, parentLocation.y + y);
        }

        dialog.pack();
        final WindowAdapter adapter = new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                dialog.getContentPane().removeAll();
            }
        };
        dialog.addWindowListener(adapter);
        dialog.addWindowFocusListener(adapter);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent ce) {
                // reset value to ensure closing works properly
                pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            }
        });
        pane.selectInitialValue();
        final Timer timer = new Timer(duration, e -> {
            if (dialog.isVisible()) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }
}
