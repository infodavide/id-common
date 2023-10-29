package org.infodavid.commons.swing.component;

import javax.swing.JFormattedTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * The Class FormattedTextFieldLimit.
 */
public class FormattedTextFieldLimit extends JFormattedTextField { // NOSONAR Inheritance

    /**
     * The Class LimitDocument.
     */
    private class LimitDocument extends PlainDocument {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1276422359285756347L;

        /**
         * Insert string.
         * @param offset the offset
         * @param str the text
         * @param attr the attributes
         * @throws BadLocationException the bad location exception
         */
        @Override
        public void insertString(final int offset, final String str, final AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            if (getLength() + str.length() <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -766338953063304836L;

    /** The limit. */
    private final int limit;

    /**
     * Instantiates a new field.
     * @param limit the limit
     */
    public FormattedTextFieldLimit(final int limit) {
        this.limit = limit;
    }

    /*
     * (non-javadoc)
     * @see javax.swing.JTextField#createDefaultModel()
     */
    @Override
    protected Document createDefaultModel() {
        return new LimitDocument();
    }
}