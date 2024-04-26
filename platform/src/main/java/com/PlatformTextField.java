/*
 * User: Gidi
 * Date: 07/07/2008
 * Time: 15:37:18
 */
package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;

public class PlatformTextField<T> extends JFormattedTextField {
    private T defaultValue;

    public PlatformTextField() {
        super();
        setMargin(new Insets(2,2,2,2));
    }

    @Override public T getValue() {
        T value = (T) super.getValue();
        if (value == null) {
            T defaultValue = getDefaultValue();
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return (T) super.getValue();
    }

    @Override @Deprecated
    public void setValue(Object value) {
        super.setValue(value);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        if (e.getID() == FocusEvent.FOCUS_GAINED)
            selectAll();
    }

}