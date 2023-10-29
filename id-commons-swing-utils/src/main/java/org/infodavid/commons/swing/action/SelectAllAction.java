package org.infodavid.commons.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.FocusManager;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * The Class SelectAllAction.
 */
public class SelectAllAction extends TextAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 38030308887523532L;

    /**
     * Instantiates a new select all action.
     * @param name the name
     */
    public SelectAllAction(final String name) {
        super(name);
    }

    /*
     * (non-javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final Component component = FocusManager.getCurrentManager().getFocusOwner();

        if (component instanceof final JTextComponent c) {
            c.selectAll();
        }
        else if (component instanceof final JList<?> c) {
            final int end = c.getModel().getSize() - 1;

            if (end >= 0) {
                c.setSelectionInterval(0, end);
            }
        }
        else if (component instanceof final JTable c) {
            final int end = c.getModel().getRowCount() - 1;

            if (end >= 0) {
                c.setRowSelectionInterval(0, end);
            }
        }
    }
}
