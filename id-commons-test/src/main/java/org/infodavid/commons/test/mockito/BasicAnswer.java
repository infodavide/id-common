package org.infodavid.commons.test.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * The Class BasicAnswer.
 * @param <T> the generic type
 */
public class BasicAnswer<T> implements Answer<T> {

    /** The value. */
    private T value;

    /**
     * Instantiates a new answer.
     */
    public BasicAnswer() {
    }

    /**
     * Instantiates a new answer.
     * @param value the value
     */
    public BasicAnswer(final T value) {
        this.value = value;
    }

    /*
     * (non-javadoc)
     * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
     */
    @Override
    public T answer(final InvocationOnMock invocation) throws Throwable {
        synchronized (this) {
            return value;
        }
    }

    /**
     * Gets the value.
     * @return the value
     */
    public T getValue() {
        synchronized (this) {
            return value;
        }
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final T value) {
        synchronized (this) {
            this.value = value;
        }
    }
}
