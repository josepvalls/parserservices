package com.carrotsearch.hppc;

import java.util.Iterator;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.predicates.IntPredicate;
import com.carrotsearch.hppc.procedures.IntProcedure;

/**
 * A generic container holding <code>int</code>s. An overview of interface relationships
 * is given in the figure below:
 * 
 * <p><img src="doc-files/interfaces.png"
 *      alt="HPPC interfaces" /></p>
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: IntContainer.java") 
public interface IntContainer extends Iterable<IntCursor>
{
    /**
     * Returns an iterator to a cursor traversing the collection. The order of traversal
     * is not defined. More than one cursor may be active at a time. The behavior of
     * iterators is undefined if structural changes are made to the underlying collection.
     * 
     * <p>The iterator is implemented as a
     * cursor and it returns <b>the same cursor instance</b> on every call to
     * {@link Iterator#next()} (to avoid boxing of primitive types). To read the current
     * list's value (or index in the list) use the cursor's public fields. An example is
     * shown below.</p>
     * 
     * <pre>
     * for (IntCursor&lt;int&gt; c : container) {
     *   System.out.println("index=" + c.index + " value=" + c.value);
     * }
     * </pre>
     */
    public Iterator<IntCursor> iterator();

    /**
     * Lookup a given element in the container. This operation has no speed
     * guarantees (may be linear with respect to the size of this container).
     * 
     * @return Returns <code>true</code> if this container has an element
     * equal to <code>e</code>.
     */
    public boolean contains(int e);

    /**
     * Return the current number of elements in this container. The time for calculating
     * the container's size may take <code>O(n)</code> time, although implementing classes
     * should try to maintain the current size and return in constant time.
     */ 
    public int size();

    /**
     * Shortcut for <code>size() == 0</code>.
     */
    public boolean isEmpty();

    /**
     * Copies all elements from this container to an array of this container's component
     * type. The returned array is always a copy, regardless of the storage used by the container.
     */
        public int [] toArray();
        

    /*  */

    /**
     * Applies a <code>procedure</code> to all container elements. Returns the argument (any
     * subclass of {@link IntProcedure}. This lets the caller to call methods of the argument
     * by chaining the call (even if the argument is an anonymous type) to retrieve computed values,
     * for example (IntContainer):
     * <pre>
     * int count = container.forEach(new IntProcedure() {
     *      int count; // this is a field declaration in an anonymous class.
     *      public void apply(int value) { count++; }}).count;
     * </pre>
     */
    public <T extends IntProcedure> T forEach(T procedure);

    /**
     * Applies a <code>predicate</code> to container elements as long, as the predicate
     * returns <code>true</code>. The iteration is interrupted otherwise. 
     */
    public <T extends IntPredicate> T forEach(T predicate);
}
