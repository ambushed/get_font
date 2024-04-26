package com;

import com.jgoodies.binding.list.ObservableList;
import com.google.common.collect.Lists;
import org.jdesktop.observablecollections.ObservableListListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Elad Almighty
 * Date: 20/12/2008
 * Time: 01:31:21
 */
public class ObservableCopyOnWriteArrayList<E>
        extends CopyOnWriteArrayList<E>
        implements ObservableList<E>, org.jdesktop.observablecollections.ObservableList<E> {

    public static <E> ObservableCopyOnWriteArrayList<E> create(E... e) {
        final ObservableCopyOnWriteArrayList<E> list = new ObservableCopyOnWriteArrayList<E>();
        list.addAll(Lists.newArrayList(e));
        return list;
    }

    public static <E> ObservableCopyOnWriteArrayList<E> create(Iterable<E> e) {
        final ObservableCopyOnWriteArrayList<E> list = new ObservableCopyOnWriteArrayList<E>();
        list.addAll(Lists.newArrayList(e));
        return list;
    }

    // Overriding Superclass Behavior *****************************************

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index   index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * @throws IndexOutOfBoundsException if index is out of range
     *                                   <code>(index &lt; 0 || index &gt; size())</code>.
     */
    @Override
    public void add(int index, E element) {
        super.add(index, element);
        fireIntervalAdded(index, index);

        for (ObservableListListener listener : listeners) {
            listener.listElementsAdded(this, index, 1);
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list.
     * @return {@code true} (as per the general contract of Collection.add).
     */
    @Override
    public boolean add(E e) {
        int newIndex = size();
        boolean b = super.add(e);
        fireIntervalAdded(newIndex, newIndex);

        for (ObservableListListener listener : listeners) {
            listener.listElementsAdded(this, newIndex, 1);
        }

        return b;
    }

    /**
     * Inserts all of the elements in the specified Collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices). The new elements will appear
     * in the list in the order that they are returned by the
     * specified Collection's iterator.
     *
     * @param index index at which to insert first element
     *              from the specified collection.
     * @param c     elements to be inserted into this list.
     * @return {@code true} if this list changed as a result of the call.
     * @throws IndexOutOfBoundsException if index out of range <code>(index
     *                                   &lt; 0 || index &gt; size())</code>.
     * @throws NullPointerException      if the specified Collection is null.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = super.addAll(index, c);
        if (changed) {
            int lastIndex = index + c.size() - 1;
            fireIntervalAdded(index, lastIndex);

            for (ObservableListListener listener : listeners) {
                listener.listElementsAdded(this, index, c.size());
            }
        }
        return changed;
    }

    /**
     * Appends all of the elements in the specified Collection to the end of
     * this list, in the order that they are returned by the
     * specified Collection's Iterator. The behavior of this operation is
     * undefined if the specified Collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified Collection is this list, and this
     * list is nonempty.)
     *
     * @param c the elements to be inserted into this list.
     * @return {@code true} if this list changed as a result of the call.
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        int firstIndex = size();
        boolean changed = super.addAll(c);
        if (changed) {
            int lastIndex = firstIndex + c.size() - 1;
            fireIntervalAdded(firstIndex, lastIndex);

            for (ObservableListListener listener : listeners) {
                listener.listElementsAdded(this, firstIndex, c.size());
            }
        }
        return changed;
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     */
    @Override
    public void clear() {
        if (isEmpty())
            return;

        List<E> dup = new ArrayList<E>(this);
        int oldLastIndex = size() - 1;

        super.clear();

        fireIntervalRemoved(0, oldLastIndex);

        for (ObservableListListener listener : listeners) {
            listener.listElementsRemoved(this, 0, dup);
        }
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to removed.
     * @return the element that was removed from the list.
     * @throws IndexOutOfBoundsException if index out of range <code>(index
     *                                   &lt; 0 || index &gt;= size())</code>.
     */
    @Override
    public E remove(int index) {
        E removedElement = super.remove(index);
        fireIntervalRemoved(index, index);

        for (ObservableListListener listener : listeners) {
            listener.listElementsRemoved(this, index,
                    java.util.Collections.singletonList(removedElement));
        }
        return removedElement;
    }

    /**
     * Removes a single instance of the specified element from this
     * list, if it is present (optional operation).  More formally,
     * removes an element <tt>e</tt> such that <tt>(o==null ? e==null :
     * o.equals(e))</tt>, if the list contains one or more such
     * elements.  Returns <tt>true</tt> if the list contained the
     * specified element (or equivalently, if the list changed as a
     * result of the call).<p>
     * <p/>
     * This implementation looks for the index of the specified element.
     * If it finds the element, it removes the element at this index
     * by calling <code>#remove(int)</code> that fires a ListDataEvent.
     *
     * @param o element to be removed from this list, if present.
     * @return <tt>true</tt> if the list contained the specified element.
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        boolean contained = index != -1;
        if (contained) {
            remove(index);
        }
        return contained;
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index   index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if index out of range
     *                                   <code>(index &lt; 0 || index &gt;= size())</code>.
     */
    @Override
    public E set(int index, E element) {
        E previousElement = super.set(index, element);
        fireContentsChanged(index, index);
        for (ObservableListListener listener : listeners) {
            listener.listElementReplaced(this, index, previousElement);
        }
        return previousElement;
    }

    // ListModel Field ********************************************************

    /**
     * Holds the registered ListDataListeners. The list that holds these
     * listeners is initialized lazily in <code>#getEventListenerList</code>.
     *
     * @see #addListDataListener(javax.swing.event.ListDataListener)
     * @see #removeListDataListener(javax.swing.event.ListDataListener)
     */
    private EventListenerList listenerList;

    // ListModel Implementation ***********************************************

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    public void addListDataListener(ListDataListener l) {
        getEventListenerList().add(ListDataListener.class, l);
    }


    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    public void removeListDataListener(ListDataListener l) {
        getEventListenerList().remove(ListDataListener.class, l);
    }


    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    public Object getElementAt(int index) {
        return get(index);
    }


    /**
     * Returns the length of the list or 0 if there's no list.
     *
     * @return the length of the list or 0 if there's no list
     */
    public int getSize() {
        return size();
    }

    // Explicit Change Notification *******************************************

    /**
     * Notifies all registered <code>ListDataListeners</code> that the element
     * at the specified index has changed. Useful if there's a content change
     * without any structural change.<p>
     * <p/>
     * This method must be called <em>after</em> the element of the list changes.
     *
     * @param index the index of the element that has changed
     * @see EventListenerList
     */
    public void fireContentsChanged(final int index) {
        fireContentsChanged(index, index);

        for (final ObservableListListener listener : listeners) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.listElementPropertyChanged(ObservableCopyOnWriteArrayList.this, index);
                }
            });
        }
    }

    // ListModel Helper Code **************************************************

    /**
     * Returns an array of all the list data listeners
     * registered on this <code>ArrayListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s,
     *         or an empty array if no list data listeners
     *         are currently registered
     * @see #addListDataListener(ListDataListener)
     * @see #removeListDataListener(ListDataListener)
     */
    public ListDataListener[] getListDataListeners() {
        return getEventListenerList().getListeners(ListDataListener.class);
    }

    /**
     * This method must be called <em>after</em> one or more elements
     * of the list change.  The changed elements
     * are specified by the closed interval index0, index1 -- the end points
     * are included.  Note that index0 need not be less than or equal to index1.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     */
    private void fireContentsChanged(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this,
                            ListDataEvent.CONTENTS_CHANGED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }

    /**
     * This method must be called <em>after</em> one or more elements
     * are added to the model.  The new elements
     * are specified by a closed interval index0, index1 -- the end points
     * are included.  Note that index0 need not be less than or equal to index1.
     *
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     */
    private void fireIntervalAdded(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
        }
    }

    /**
     * This method must be called <em>after</em>  one or more elements
     * are removed from the model.
     * <code>index0</code> and <code>index1</code> are the end points
     * of the interval that's been removed.  Note that <code>index0</code>
     * need not be less than or equal to <code>index1</code>.
     *
     * @param index0 one end of the removed interval,
     *               including <code>index0</code>
     * @param index1 the other end of the removed interval,
     *               including <code>index1</code>
     * @see EventListenerList
     */
    private void fireIntervalRemoved(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }

    /**
     * Lazily initializes and returns the event listener list used
     * to notify registered listeners.
     *
     * @return the event listener list used to notify listeners
     */
    private EventListenerList getEventListenerList() {
        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        return listenerList;
    }

    // ****************************************************************** JDesktop Section *************************************************************************** //

    private final boolean supportsElementPropertyChanged = true;
    private List<ObservableListListener> listeners = new CopyOnWriteArrayList<ObservableListListener>();

    public void addObservableListListener(ObservableListListener listener) {
        listeners.add(listener);
    }

    public void removeObservableListListener(ObservableListListener listener) {
        listeners.remove(listener);
    }

    public boolean supportsElementPropertyChanged() {
        return supportsElementPropertyChanged;
    }

    public void fireAllElementsPropertyChanged() {
        for (ObservableListListener listener : listeners) {
            int index = 0;
            for (E element : this) {
                listener.listElementPropertyChanged(this, index);
                index++;
            }
        }
    }

    // ****************************************************************** JDesktop Section *************************************************************************** //
}
