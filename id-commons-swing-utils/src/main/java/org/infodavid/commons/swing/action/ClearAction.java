package org.infodavid.commons.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.FocusManager;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * The Class ClearAction.
 */
public class ClearAction extends TextAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5738587776777636756L;

    /**
     * Instantiates a new clear action.
     * @param name the name
     */
    public ClearAction(final String name) {
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
            c.setText("");
        } else if (component instanceof final JList<?> c) {
            if (c.getModel() instanceof final DefaultListModel<?> m) {
                m.clear();
            }
        } else if (component instanceof final JTable c && c.getModel() instanceof final DefaultTableModel m) {
            for (int i = 0; i < m.getRowCount(); i++) {
                m.removeRow(i);
                c.revalidate();
            }
        }
    }
}
