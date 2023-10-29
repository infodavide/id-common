package org.infodavid.commons.swing;

import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;

import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import javax.swing.text.View;

/**
 * The Class TextUtilities.
 */
@SuppressWarnings("static-method")
public final class TextUtilities {

    /** The singleton. */
    private static WeakReference<TextUtilities> instance = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized TextUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new TextUtilities());
        }

        return instance.get();
    }

    /**
     * Instantiates a new util.
     */
    private TextUtilities() {
    }

    /**
     * Attempt to center the line containing the caret at the center of the scroll pane.
     * @param component the text component in the sroll pane
     */
    public void centerLineInScrollPane(final JTextComponent component) {
        final Container container = SwingUtilities.getAncestorOfClass(JViewport.class, component);

        if (container == null) {
            return;
        }

        try {
            final Rectangle2D r = component.modelToView2D(component.getCaretPosition());
            final JViewport viewport = (JViewport)container;
            final int extentHeight = viewport.getExtentSize().height;
            final int viewHeight = viewport.getViewSize().height;
            int y = (int) Math.round(Math.max(0, r.getY() - (extentHeight - r.getHeight()) / 2));
            y = Math.min(y, viewHeight - extentHeight);
            viewport.setViewPosition(new Point(0, y));
        }

        catch (@SuppressWarnings("unused") final BadLocationException e) {
            // noop
        }
    }

    /**
     * Return the column number at the Caret position. The column returned will only make sense when using a Monospaced font.
     * @param component the component
     * @return the column at caret
     */
    public int getColumnAtCaret(final JTextComponent component) {
        // Since we assume a monospaced font we can use the width of a single
        // character to represent the width of each character
        final FontMetrics fm = component.getFontMetrics(component.getFont());
        final int characterWidth = fm.stringWidth("0");
        int column = 0;

        try {
            final Rectangle2D r = component.modelToView2D(component.getCaretPosition());
            final int width = (int) (r.getX() - component.getInsets().left);
            column = width / characterWidth;
        }
        catch (@SuppressWarnings("unused") final BadLocationException e) {
            // noop
        }

        return column + 1;
    }

    /**
     * Return the line number at the Caret position.
     * @param component the component
     * @return the line at caret
     */
    public int getLineAtCaret(final JTextComponent component) {
        final int caretPosition = component.getCaretPosition();
        final Element root = component.getDocument().getDefaultRootElement();

        return root.getElementIndex(caretPosition) + 1;
    }

    /**
     * Return the number of lines of text in the Document.
     * @param component the component
     * @return the lines
     */
    public int getLines(final JTextComponent component) {
        final Element root = component.getDocument().getDefaultRootElement();

        return root.getElementCount();
    }

    /**
     * Return the number of lines of text, including wrapped lines.
     * @param component the component
     * @return the wrapped lines
     */
    public int getWrappedLines(final JTextArea component) {
        final View view = component.getUI().getRootView(component).getView(0);
        final int preferredHeight = (int)view.getPreferredSpan(View.Y_AXIS);
        final int lineHeight = component.getFontMetrics(component.getFont()).getHeight();

        return preferredHeight / lineHeight;
    }

    /**
     * Return the number of lines of text, including wrapped lines.
     * @param component the component
     * @return the wrapped lines
     */
    public int getWrappedLines(final JTextComponent component) {
        int lines = 0;
        final View view = component.getUI().getRootView(component).getView(0);
        final int paragraphs = view.getViewCount();

        for (int i = 0; i < paragraphs; i++) {
            lines += view.getView(i).getViewCount();
        }

        return lines;
    }

    /**
     * Position the caret on the first word of a line.
     * @param component the component
     * @param line the line
     */
    public void gotoFirstWordOnLine(final JTextComponent component, final int line) {
        gotoStartOfLine(component, line);

        // The following will position the caret at the start of the first word
        try {
            final int position = component.getCaretPosition();
            final String first = component.getDocument().getText(position, 1);

            if (Character.isWhitespace(first.charAt(0))) {
                component.setCaretPosition(Utilities.getNextWord(component, position));
            }
        }
        catch (@SuppressWarnings("unused") final Exception e) {
            // noop
        }
    }

    /**
     * Position the caret at the start of a line.
     * @param component the component
     * @param line the line
     */
    public void gotoStartOfLine(final JTextComponent component, final int line) {
        final Element root = component.getDocument().getDefaultRootElement();
        int l = Math.max(line, 1);
        l = Math.min(l, root.getElementCount());
        final int startOfLineOffset = root.getElement(l - 1).getStartOffset();
        component.setCaretPosition(startOfLineOffset);
    }
}
