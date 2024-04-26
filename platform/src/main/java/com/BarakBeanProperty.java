/*
 * User: Elad
 * Date: 11/03/2009
 * Time: 14:03:54
 */
package com;

import org.jdesktop.beansbinding.PropertyHelper;

import java.lang.reflect.Method;

public class BarakBeanProperty<P, E> extends PropertyHelper<E, P> {
    private final Class<E> beanClass;
    private final String propertyName;
    private final Class<P> propertyClass;

    private Method getMethod;
    private Method setMethod;

    public BarakBeanProperty(Class<E> beanClass, String propertyName, Class<P> propertyClass) {
        this.beanClass = beanClass;
        this.propertyName = propertyName;
        this.propertyClass = propertyClass;

        extractGetMethod(beanClass, propertyName, propertyClass);
        extractSetMethod(beanClass, propertyName, propertyClass);
    }

    public static <P, E> BarakBeanProperty<P, E> create(Class<E> beanClass, String propertyName, Class<P> propertyClass) {
        return (new BarakBeanProperty<P, E>(beanClass, propertyName, propertyClass));
    }

    public static <P, E> BarakBeanProperty<P, E> property(Class<E> beanClass, String propertyName, Class<P> propertyClass) {
        return (new BarakBeanProperty<P, E>(beanClass, propertyName, propertyClass));
    }

    public Class<E> getBeanClass() {
        return beanClass;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<P> getPropertyClass() {
        return propertyClass;
    }

    @Override public Class getWriteType(Object source) {
        return (getPropertyClass());
    }

    @SuppressWarnings({"unchecked"}) @Override public E getValue(Object source) {
        if (getMethod == null) {
            throw new UnsupportedOperationException("No get method for property : " + getPropertyName() + ", in class : " + getBeanClass().getName());

        } else {
            try {
                return (E) getMethod.invoke(source);

            } catch (Exception ex) {
                throw new UnsupportedOperationException("!");
            }
        }
    }

    @Override public void setValue(Object source, Object value) {
        if (setMethod == null) {
            throw new UnsupportedOperationException("No set method for property : " + getPropertyName() + ", in class : " + getBeanClass().getName());

        } else {
            try {
                setMethod.invoke(source, value);

            } catch (Exception ex) {
            }
        }
    }

    @Override public boolean isReadable(Object source) {
        return (getMethod != null);
    }

    @Override public boolean isWriteable(Object source) {
        return (setMethod != null);
    }

    private void extractGetMethod(Class<E> beanClass, String propertyName, Class<P> propertyClass) {
        extractGetMethodInner(beanClass, propertyName, "get");
        if (getMethod == null && propertyClass == Boolean.class) {
            extractGetMethodInner(beanClass, propertyName, "is");
        }
    }

    private void extractGetMethodInner(Class<E> beanClass, String propertyName, String inspectorMethodPrefix) {
        String methodName = inspectorMethodPrefix + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        getMethod = null;
        try { getMethod = beanClass.getMethod(methodName); } catch (NoSuchMethodException ex) { }
    }

    private void extractSetMethod(Class<E> beanClass, String propertyName, Class<P> propertyClass) {
        String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        setMethod = null;
        try {
            setMethod = beanClass.getMethod(methodName, propertyClass);
        } catch (NoSuchMethodException ex) {
        }
    }
}
