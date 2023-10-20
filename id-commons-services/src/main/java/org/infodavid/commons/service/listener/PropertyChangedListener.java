package org.infodavid.commons.service.listener;

import org.infodavid.commons.model.Property;

/**
 * The listener interface for receiving propertyChanged events. The class that is interested in processing a propertyChanged event implements this interface, and the object created with that class is registered with a component using the component's <code>addPropertyChangedListener<code> method. When the propertyChanged event occurs, that object's appropriate method is invoked.
 * @see PropertyChangedEvent
 */
public interface PropertyChangedListener {

    /**
     * Property have changed.
     * @param property the property
     * @throws InterruptedException the interrupted exception
     */
    void propertyChanged(Property property) throws InterruptedException;
}
