package org.infodavid.commons.swing.component;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The Class ToggleButton.
 */
public class ToggleButton extends AbstractButton {

    /**
     * The Class ActionListenerWrapper.
     */
    private static class ActionListenerWrapper implements ActionListener {

        /** The delegate. */
        private final ActionListener delegate;

        /** The parent. */
        private final ToggleButton parent;

        /**
         * Instantiates a new action listener wrapper.
         * @param parent   the parent
         * @param delegate the delegate
         */
        public ActionListenerWrapper(final ToggleButton parent, final ActionListener delegate) {
            this.delegate = delegate;
            this.parent = parent;
        }

        /*
         * (non-javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            delegate.actionPerformed(new ActionEvent(parent, ActionEvent.ACTION_PERFORMED, parent.getLeftButton().getActionCommand(), e.getWhen(), e.getModifiers()));
        }
    }

    /**
     * The Class ChangeListenerWrapper.
     */
    private static class ChangeListenerWrapper implements ChangeListener {

        /** The delegate. */
        private final ChangeListener delegate;

        /** The parent. */
        private final ToggleButton parent;

        /**
         * Instantiates a new change listener wrapper.
         * @param parent   the parent
         * @param delegate the delegate
         */
        public ChangeListenerWrapper(final ToggleButton parent, final ChangeListener delegate) {
            this.delegate = delegate;
            this.parent = parent;
        }

        /*
         * (non-javadoc)
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        @Override
        public void stateChanged(final ChangeEvent e) {
            delegate.stateChanged(new ChangeEvent(parent));
        }
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1575155977045348269L;

    /** The button group. */
    private final ButtonGroup buttonGroup;

    /** The component resized flag. */
    private final AtomicBoolean componentResized;

    /** The left. */
    private final JToggleButton left;

    /** The left component listener. */
    private final transient ComponentListener leftComponentListener = new ComponentAdapter() {
        @Override
        public void componentResized(final ComponentEvent e) {
            if (componentResized.get()) {
                return;
            }

            componentResized.set(true);
            final int w = Math.max(e.getComponent().getWidth(), getRightButton().getWidth()) * 2;
            final int h = Math.max(e.getComponent().getHeight(), getRightButton().getHeight());

            if (w != getWidth() || h != getHeight()) {
                setSize(w, h);
            }

            componentResized.set(false);
        }
    };

    /** The right. */
    private final JToggleButton right;

    /** The right component listener. */
    private final transient ComponentListener rightComponentListener = new ComponentAdapter() {
        @Override
        public void componentResized(final ComponentEvent e) {
            if (componentResized.get()) {
                return;
            }

            componentResized.set(true);
            final int w = Math.max(e.getComponent().getWidth(), getLeftButton().getWidth()) * 2;
            final int h = Math.max(e.getComponent().getHeight(), getLeftButton().getHeight());

            if (w != getWidth() || h != getHeight()) {
                setSize(w, h);
            }

            componentResized.set(false);
        }
    };

    /**
     * Instantiates a new extended toggle button.
     */
    public ToggleButton() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        componentResized = new AtomicBoolean(false);
        buttonGroup = new ButtonGroup();
        right = new JToggleButton("");
        left = new JToggleButton("");
        buttonGroup.add(right);
        buttonGroup.add(left);
        left.addComponentListener(leftComponentListener);
        left.setMaximumSize(new Dimension(Byte.MAX_VALUE, Byte.MAX_VALUE));
        right.addComponentListener(rightComponentListener);
        right.setMaximumSize(new Dimension(Byte.MAX_VALUE, Byte.MAX_VALUE));
        super.setModel(left.getModel());
        add(left);
        add(right);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param a the a
     */
    public ToggleButton(final Action a) {
        this();
        left.setAction(a);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftIcon  the left icon
     * @param rightIcon the right icon
     */
    public ToggleButton(final Icon leftIcon, final Icon rightIcon) {
        this();
        left.setIcon(leftIcon);
        right.setIcon(rightIcon);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftIcon  the left icon
     * @param rightIcon the right icon
     * @param selected  the selected
     */
    public ToggleButton(final Icon leftIcon, final Icon rightIcon, final boolean selected) {
        this();
        left.setIcon(leftIcon);
        right.setIcon(rightIcon);
        setSelected(selected);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftText  the left text
     * @param rightText the right text
     */
    public ToggleButton(final String leftText, final String rightText) {
        this();
        left.setText(leftText);
        right.setText(rightText);
        updateSizes();
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftText  the left text
     * @param rightText the right text
     * @param selected  the selected
     */
    public ToggleButton(final String leftText, final String rightText, final boolean selected) {
        this();
        left.setText(leftText);
        right.setText(rightText);
        setSelected(selected);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftText  the left text
     * @param rightText the right text
     * @param leftIcon  the left icon
     * @param rightIcon the right icon
     */
    public ToggleButton(final String leftText, final String rightText, final Icon leftIcon, final Icon rightIcon) {
        this();
        left.setText(leftText);
        left.setIcon(leftIcon);
        right.setText(rightText);
        right.setIcon(rightIcon);
    }

    /**
     * Instantiates a new extended toggle button.
     * @param leftText  the left text
     * @param rightText the right text
     * @param leftIcon  the left icon
     * @param rightIcon the right icon
     * @param selected  the selected
     */
    public ToggleButton(final String leftText, final String rightText, final Icon leftIcon, final Icon rightIcon, final boolean selected) {
        this();
        left.setText(leftText);
        left.setIcon(leftIcon);
        right.setText(rightText);
        right.setIcon(rightIcon);
        setSelected(selected);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#addActionListener(java.awt.event.ActionListener)
     */
    @Override
    public void addActionListener(final ActionListener l) {
        left.addActionListener(new ActionListenerWrapper(this, l));
        right.addActionListener(new ActionListenerWrapper(this, l));
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#addChangeListener(javax.swing.event.ChangeListener)
     */
    @Override
    public void addChangeListener(final ChangeListener l) {
        left.addChangeListener(new ChangeListenerWrapper(this, l));
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#getActionListeners()
     */
    @Override
    public ActionListener[] getActionListeners() {
        final List<ActionListener> result = new ArrayList<>();
        final ActionListener[] listeners = left.getActionListeners();

        if (listeners != null) {
            for (final ActionListener listener : listeners) {
                if (listener instanceof final ActionListenerWrapper w) {
                    result.add(w.delegate);
                }
            }
        }

        return result.toArray(new ActionListener[result.size()]);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#getChangeListeners()
     */
    @Override
    public ChangeListener[] getChangeListeners() {
        final List<ChangeListener> result = new ArrayList<>();
        final ChangeListener[] listeners = left.getChangeListeners();

        if (listeners != null) {
            for (final ChangeListener listener : listeners) {
                if (listener instanceof final ChangeListenerWrapper w) {
                    result.add(w.delegate);
                }
            }
        }

        return result.toArray(new ChangeListener[result.size()]);
    }

    /*
     * (non-javadoc)
     * @see java.awt.Component#getComponentListeners()
     */
    @Override
    public synchronized ComponentListener[] getComponentListeners() {
        ComponentListener[] result = left.getComponentListeners();

        if (result != null) {
            result = result.clone();
            result = ArrayUtils.removeAllOccurrences(result, leftComponentListener);
        }

        return result;
    }

    /**
     * Gets the left button.
     * @return the left button
     */
    public JToggleButton getLeftButton() {
        return left;
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#getModel()
     */
    @Override
    public ButtonModel getModel() {
        return right.getModel();
    }

    /**
     * Gets the right button.
     * @return the right button
     */
    public JToggleButton getRightButton() {
        return right;
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#isSelected()
     */
    @Override
    public boolean isSelected() {
        return left.isSelected();
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#removeActionListener(java.awt.event.ActionListener)
     */
    @Override
    public void removeActionListener(final ActionListener l) {
        ActionListener[] listeners = left.getActionListeners();

        if (listeners != null) {
            for (final ActionListener listener : listeners) {
                if (listener instanceof final ActionListenerWrapper w && w.delegate == l) {
                    left.removeActionListener(listener);
                }
            }
        }

        listeners = right.getActionListeners();

        if (listeners != null) {
            for (final ActionListener listener : listeners) {
                if (listener instanceof final ActionListenerWrapper  w && w.delegate == l) {
                    right.removeActionListener(listener);
                }
            }
        }
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#removeChangeListener(javax.swing.event.ChangeListener)
     */
    @Override
    public void removeChangeListener(final ChangeListener l) {
        final ChangeListener[] listeners = left.getChangeListeners();

        if (listeners != null) {
            for (final ChangeListener listener : listeners) {
                if (listener instanceof final ChangeListenerWrapper w && w.delegate == l) {
                    left.removeChangeListener(listener);
                }
            }
        }
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setAlignmentX(float)
     */
    @Override
    public void setAlignmentX(final float value) {
        left.setAlignmentX(value);
        right.setAlignmentX(value);
        super.setAlignmentX(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setAlignmentY(float)
     */
    @Override
    public void setAlignmentY(final float value) {
        left.setAlignmentY(value);
        right.setAlignmentY(value);
        super.setAlignmentY(value);
    }

    /*
     * (non-javadoc)
     * @see java.awt.Component#setComponentOrientation(java.awt.ComponentOrientation)
     */
    @Override
    public void setComponentOrientation(final ComponentOrientation value) {
        left.setComponentOrientation(value);
        right.setComponentOrientation(value);
        super.setComponentOrientation(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setContentAreaFilled(boolean)
     */
    @Override
    public void setContentAreaFilled(final boolean value) {
        left.setContentAreaFilled(value);
        right.setContentAreaFilled(value);
        super.setContentAreaFilled(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setDisabledIcon(javax.swing.Icon)
     */
    @Override
    public void setDisabledIcon(final Icon icon) {
        left.setDisabledIcon(icon);
        super.setDisabledIcon(icon);
    }

    /**
     * Sets the disabled leftt icon.
     * @param icon the new disabled leftt icon
     */
    public void setDisabledLefttIcon(final Icon icon) {
        setDisabledIcon(icon);
    }

    /**
     * Sets the disabled right icon.
     * @param icon the new disabled right icon
     */
    public void setDisabledRightIcon(final Icon icon) {
        right.setDisabledIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setDisabledSelectedIcon(javax.swing.Icon)
     */
    @Override
    public void setDisabledSelectedIcon(final Icon icon) {
        left.setDisabledSelectedIcon(icon);
        super.setDisabledSelectedIcon(icon);
    }

    /**
     * Sets the disabled selected left icon.
     * @param icon the new disabled selected left icon
     */
    public void setDisabledSelectedLeftIcon(final Icon icon) {
        setDisabledSelectedIcon(icon);
    }

    /**
     * Sets the disabled selected right icon.
     * @param icon the new disabled selected right icon
     */
    public void setDisabledSelectedRightIcon(final Icon icon) {
        right.setDisabledSelectedIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setEnabled(boolean)
     */
    @Override
    public void setEnabled(final boolean b) {
        left.setEnabled(b);
        right.setEnabled(b);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setFocusPainted(boolean)
     */
    @Override
    public void setFocusPainted(final boolean value) {
        left.setFocusPainted(value);
        right.setFocusPainted(value);
        super.setFocusPainted(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setFont(java.awt.Font)
     */
    @Override
    public void setFont(final Font font) {
        left.setFont(font);
        right.setFont(font);
        super.setFont(font);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setHideActionText(boolean)
     */
    @Override
    public void setHideActionText(final boolean value) {
        left.setHideActionText(value);
        right.setHideActionText(value);
        super.setHideActionText(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setHorizontalAlignment(int)
     */
    @Override
    public void setHorizontalAlignment(final int value) {
        left.setHorizontalAlignment(value);
        right.setHorizontalAlignment(value);
        super.setHorizontalAlignment(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setHorizontalTextPosition(int)
     */
    @Override
    public void setHorizontalTextPosition(final int value) {
        left.setHorizontalTextPosition(value);
        right.setHorizontalTextPosition(value);
        super.setHorizontalTextPosition(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setIcon(javax.swing.Icon)
     */
    @Override
    public void setIcon(final Icon icon) {
        left.setIcon(icon);
        super.setIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setIconTextGap(int)
     */
    @Override
    public void setIconTextGap(final int value) {
        left.setIconTextGap(value);
        right.setIconTextGap(value);
        super.setIconTextGap(value);
    }

    /**
     * Sets the left icon.
     * @param icon the new left icon
     */
    public void setLeftIcon(final Icon icon) {
        setIcon(icon);
    }

    /**
     * Sets the left text.
     * @param value the new left text
     */
    public void setLeftText(final String value) {
        setText(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setMaximumSize(java.awt.Dimension)
     */
    @Override
    public void setMaximumSize(final Dimension maximumSize) {
        final Dimension dimension = new Dimension(maximumSize.width / 2, maximumSize.height);
        left.setMaximumSize(dimension);
        right.setMaximumSize(dimension);
        super.setMaximumSize(maximumSize);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setMinimumSize(java.awt.Dimension)
     */
    @Override
    public void setMinimumSize(final Dimension minimumSize) {
        final Dimension dimension = new Dimension(minimumSize.width / 2, minimumSize.height);
        left.setMinimumSize(dimension);
        right.setMinimumSize(dimension);
        super.setMinimumSize(minimumSize);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setModel(javax.swing.ButtonModel)
     */
    @Override
    public void setModel(final ButtonModel newModel) {
        left.setModel(newModel);
        super.setModel(newModel);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setMultiClickThreshhold(long)
     */
    @Override
    public void setMultiClickThreshhold(final long value) {
        left.setMultiClickThreshhold(value);
        right.setMultiClickThreshhold(value);
        super.setMultiClickThreshhold(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
     */
    @Override
    public void setPreferredSize(final Dimension preferredSize) {
        final Dimension dimension = new Dimension(preferredSize.width / 2, preferredSize.height);
        left.setPreferredSize(dimension);
        right.setPreferredSize(dimension);
        super.setPreferredSize(preferredSize);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setPressedIcon(javax.swing.Icon)
     */
    @Override
    public void setPressedIcon(final Icon value) {
        left.setPressedIcon(value);
        super.setPressedIcon(value);
    }

    /**
     * Sets the pressed left icon.
     * @param value the new pressed left icon
     */
    public void setPressedLeftIcon(final Icon value) {
        setPressedIcon(value);
    }

    /**
     * Sets the pressed right icon.
     * @param value the new pressed right icon
     */
    public void setPressedRightIcon(final Icon value) {
        right.setPressedIcon(value);
    }

    /**
     * Sets the right text.
     * @param value the new right text
     */
    public void setRightText(final String value) {
        right.setText(value);
    }

    /**
     * Sets the rigth icon.
     * @param icon the new rigth icon
     */
    public void setRigthIcon(final Icon icon) {
        right.setIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setRolloverEnabled(boolean)
     */
    @Override
    public void setRolloverEnabled(final boolean value) {
        left.setRolloverEnabled(value);
        right.setRolloverEnabled(value);
        super.setRolloverEnabled(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setRolloverIcon(javax.swing.Icon)
     */
    @Override
    public void setRolloverIcon(final Icon icon) {
        left.setRolloverIcon(icon);
        right.setRolloverIcon(icon);
        super.setRolloverIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setRolloverSelectedIcon(javax.swing.Icon)
     */
    @Override
    public void setRolloverSelectedIcon(final Icon icon) {
        left.setRolloverSelectedIcon(icon);
        right.setRolloverSelectedIcon(icon);
        super.setRolloverSelectedIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setSelected(boolean)
     */
    @Override
    public void setSelected(final boolean value) {
        left.setSelected(value);
        right.setSelected(!value);
        super.setSelected(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setSelectedIcon(javax.swing.Icon)
     */
    @Override
    public void setSelectedIcon(final Icon icon) {
        left.setSelectedIcon(icon);
        right.setSelectedIcon(icon);
        super.setSelectedIcon(icon);
    }

    /*
     * (non-javadoc)
     * @see java.awt.Component#setSize(int, int)
     */
    @Override
    public void setSize(final int width, final int height) {
        final Dimension dimension = new Dimension(width / 2, height);
        left.setSize(dimension);
        right.setSize(dimension);
        super.setSize(width, height);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setText(java.lang.String)
     */
    @Override
    public void setText(final String value) {
        left.setText(value);
        super.setText(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setUI(javax.swing.plaf.ButtonUI)
     */
    @Override
    public void setUI(final ButtonUI ui) {
        left.setUI(ui);
        right.setUI(ui);
        super.setUI(ui);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setVerticalAlignment(int)
     */
    @Override
    public void setVerticalAlignment(final int value) {
        left.setVerticalAlignment(value);
        right.setVerticalAlignment(value);
        super.setVerticalAlignment(value);
    }

    /*
     * (non-javadoc)
     * @see javax.swing.AbstractButton#setVerticalTextPosition(int)
     */
    @Override
    public void setVerticalTextPosition(final int value) {
        left.setVerticalTextPosition(value);
        right.setVerticalTextPosition(value);
        super.setVerticalTextPosition(value);
    }

    /**
     * Update sizes.
     */
    private void updateSizes() {
        int w = Math.max(left.getMinimumSize().width, right.getMinimumSize().width);
        int h = Math.max(left.getMinimumSize().height, right.getMinimumSize().height);
        super.setMinimumSize(new Dimension(w * 2, h));
        w = Math.max(left.getPreferredSize().width, right.getPreferredSize().width);
        h = Math.max(left.getPreferredSize().height, right.getPreferredSize().height);
        super.setPreferredSize(new Dimension(w * 2, h));
    }
}
