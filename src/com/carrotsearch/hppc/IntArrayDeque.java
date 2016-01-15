package com.carrotsearch.hppc;

import java.util.*;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.predicates.IntPredicate;
import com.carrotsearch.hppc.procedures.IntProcedure;

import static com.carrotsearch.hppc.Internals.*;

/**
 * An array-backed deque (doubly linked queue) of ints. A single array is used to store and 
 * manipulate all elements. Reallocations are governed by a {@link ArraySizingStrategy}
 * and may be expensive if they move around really large chunks of memory.
 *
 * <p>See {@link ObjectArrayDeque} class for API similarities and differences against Java
 * Collections.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: IntArrayDeque.java") 
public class IntArrayDeque 
    extends AbstractIntCollection implements IntDeque, Cloneable
{
    /**
     * Default capacity if no other capacity is given in the constructor.
     */
    public final static int DEFAULT_CAPACITY = 5;

    /**
     * Internal array for storing elements.
     * 
     */
    public int [] buffer;

    /**
     * The index of the element at the head of the deque or an
     * arbitrary number equal to tail if the deque is empty.
     */
    public int head;

    /**
     * The index at which the next element would be added to the tail
     * of the deque.
     */
    public int tail;

    /**
     * Buffer resizing strategy.
     */
    protected final ArraySizingStrategy resizer;

    /**
     * Create with default sizing strategy and initial capacity for storing 
     * {@value #DEFAULT_CAPACITY} elements.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public IntArrayDeque()
    {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Create with default sizing strategy and the given initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public IntArrayDeque(int initialCapacity)
    {
        this(initialCapacity, new BoundedProportionalArraySizingStrategy());
    }

    /**
     * Create with a custom buffer resizing strategy.
     */
    public IntArrayDeque(int initialCapacity, ArraySizingStrategy resizer)
    {
        assert initialCapacity >= 0 : "initialCapacity must be >= 0: " + initialCapacity;
        assert resizer != null;

        this.resizer = resizer;
        initialCapacity = resizer.round(initialCapacity);
        buffer = new int [initialCapacity];
    }

    /**
     * Creates a new deque from elements of another container, appending them
     * at the end of this deque. 
     */
    public IntArrayDeque(IntContainer container)
    {
        this(container.size());
        addLast(container);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFirst(int e1)
    {
        int h = oneLeft(head, buffer.length);
        if (h == tail)
        {
            ensureBufferSpace(1);
            h = oneLeft(head, buffer.length);
        }
        buffer[head = h] = e1;
    }

    /**
     * Vararg-signature method for adding elements at the front of this deque.
     * 
     * <p><b>This method is handy, but costly if used in tight loops (anonymous 
     * array passing)</b></p>
     */
    public void addFirst(int... elements)
    {
        ensureBufferSpace(elements.length);

        // For now, naive loop.
        for (int i = 0; i < elements.length; i++)
            addFirst(elements[i]);
    }

    /**
     * Inserts all elements from the given container to the front of this deque.
     * 
     * @return Returns the number of elements actually added as a result of this
     * call.
     */
    public int addFirst(IntContainer container)
    {
        int size = container.size();
        ensureBufferSpace(size);

        for (IntCursor cursor : container)
        {
            addFirst(cursor.value);
        }

        return size;
    }

    /**
     * Inserts all elements from the given iterable to the front of this deque.
     * 
     * @return Returns the number of elements actually added as a result of this call.
     */
    public int addFirst(Iterable<? extends IntCursor> iterable)
    {
        int size = 0;
        for (IntCursor cursor : iterable)
        {
            addFirst(cursor.value);
            size++;
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLast(int e1)
    {
        int t = oneRight(tail, buffer.length);
        if (head == t)
        {
            ensureBufferSpace(1);
            t = oneRight(tail, buffer.length);
        }
        buffer[tail] = e1;
        tail = t;
    }
    
    /**
     * Vararg-signature method for adding elements at the end of this deque.
     * 
     * <p><b>This method is handy, but costly if used in tight loops (anonymous 
     * array passing)</b></p>
     */
    public void addLast(int... elements)
    {
        ensureBufferSpace(1);

        // For now, naive loop.
        for (int i = 0; i < elements.length; i++)
            addLast(elements[i]);
    }

    /**
     * Inserts all elements from the given container to the end of this deque.
     * 
     * @return Returns the number of elements actually added as a result of this
     * call.
     */
    public int addLast(IntContainer container)
    {
        int size = container.size();
        ensureBufferSpace(size);

        for (IntCursor cursor : container)
        {
            addLast(cursor.value);
        }

        return size;
    }
    
    /**
     * Inserts all elements from the given iterable to the end of this deque.
     * 
     * @return Returns the number of elements actually added as a result of this call.
     */
    public int addLast(Iterable<? extends IntCursor> iterable)
    {
        int size = 0;
        for (IntCursor cursor : iterable)
        {
            addLast(cursor.value);
            size++;
        }
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeFirst()
    {
        assert size() > 0 : "The deque is empty.";

        final int result = buffer[head];
        buffer[head] = ((int) 0);
        head = oneRight(head, buffer.length); 
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeLast()
    {
        assert size() > 0 : "The deque is empty.";

        tail = oneLeft(tail, buffer.length); 
        final int result = buffer[tail];
        buffer[tail] = ((int) 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFirst()
    {
        assert size() > 0 : "The deque is empty.";

        return buffer[head];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLast()
    {
        assert size() > 0 : "The deque is empty.";

        return buffer[oneLeft(tail, buffer.length)];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeFirstOccurrence(int e1)
    {
        final int index = bufferIndexOf(e1);
        if (index >= 0) removeAtBufferIndex(index);
        return index;
    }

    /**
     * Return the index of the first (counting from head) element equal to
     * <code>e1</code>. The index points to the {@link #buffer} array.
     *   
     * @param e1 The element to look for.
     * @return Returns the index of the first element equal to <code>e1</code>
     * or <code>-1</code> if not found.
     */
    public int bufferIndexOf(int e1)
    {
        final int last = tail;
        final int bufLen = buffer.length;
        for (int i = head; i != last; i = oneRight(i, bufLen))
        {
            if (((e1) == (buffer[i])))
                return i;
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeLastOccurrence(int e1)
    {
        final int index = lastBufferIndexOf(e1);
        if (index >= 0) removeAtBufferIndex(index);
        return index;
    }

    /**
     * Return the index of the last (counting from tail) element equal to
     * <code>e1</code>. The index points to the {@link #buffer} array.
     *   
     * @param e1 The element to look for.
     * @return Returns the index of the first element equal to <code>e1</code>
     * or <code>-1</code> if not found.
     */
    public int lastBufferIndexOf(int e1)
    {
        final int bufLen = buffer.length;
        final int last = oneLeft(head, bufLen);
        for (int i = oneLeft(tail, bufLen); i != last; i = oneLeft(i, bufLen))
        {
            if (((e1) == (buffer[i])))
                return i;
        }

        return -1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int removeAllOccurrences(int e1)
    {
        int removed = 0;
        final int last = tail;
        final int bufLen = buffer.length;
        int from, to;
        for (from = to = head; from != last; from = oneRight(from, bufLen))
        {
            if (((e1) == (buffer[from])))
            {
                buffer[from] = ((int) 0);
                removed++;
                continue;
            }
    
            if (to != from)
            {
                buffer[to] = buffer[from];
                buffer[from] = ((int) 0);
            }
    
            to = oneRight(to, bufLen);
        }
    
        tail = to;
        return removed;
    }

    /**
     * Removes the element at <code>index</code> in the internal
     * {#link {@link #buffer}} array, returning its value.
     * 
     * @param index Index of the element to remove. The index must be located between
     * {@link #head} and {@link #tail} in modulo {@link #buffer} arithmetic. 
     */
    public void removeAtBufferIndex(int index)
    {
        assert (head <= tail 
            ? index >= head && index < tail
            : index >= head || index < tail) : "Index out of range (head=" 
                + head + ", tail=" + tail + ", index=" + index + ").";

        // Cache fields in locals (hopefully moved to registers).
        final int [] b = this.buffer;
        final int bufLen = b.length;
        final int lastIndex = bufLen - 1;
        final int head = this.head;
        final int tail = this.tail;

        final int leftChunk = Math.abs(index - head) % bufLen;
        final int rightChunk = Math.abs(tail - index) % bufLen;

        if (leftChunk < rightChunk)
        {
            if (index >= head)
            {
                System.arraycopy(b, head, b, head + 1, leftChunk);
            }
            else
            {
                System.arraycopy(b, 0, b, 1, index);
                b[0] = b[lastIndex];
                System.arraycopy(b, head, b, head + 1, lastIndex - head);
            }
            b[head] = ((int) 0);
            this.head = oneRight(head, bufLen);
        }
        else
        {
            if (index < tail)
            {
                System.arraycopy(b, index + 1, b, index, rightChunk);
            }
            else
            {
                System.arraycopy(b, index + 1, b, index, lastIndex - index);
                b[lastIndex] = b[0];
                System.arraycopy(b, 1, b, 0, tail);
            }
            b[tail] = ((int) 0);
            this.tail = oneLeft(tail, bufLen);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        if (head <= tail)
            return tail - head;
        else
            return (tail - head + buffer.length);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>The internal array buffers are not released as a result of this call.</p>
     * 
     * @see #release()
     */
    @Override
    public void clear()
    {
        if (head < tail)
        {
            Arrays.fill(buffer, head, tail, ((int) 0));
        }
        else
        {
            Arrays.fill(buffer, 0, tail, ((int) 0));
            Arrays.fill(buffer, head, buffer.length, ((int) 0));
        }
        this.head = tail = 0;
    }

    /**
     * Release internal buffers of this deque and reallocate the smallest buffer possible.
     */
    public void release()
    {
        this.head = tail = 0;
        buffer = new int [resizer.round(DEFAULT_CAPACITY)];
    }

    /**
     * Ensures the internal buffer has enough free slots to store
     * <code>expectedAdditions</code>. Increases internal buffer size if needed.
     */
    protected void ensureBufferSpace(int expectedAdditions)
    {
        final int bufferLen = (buffer == null ? 0 : buffer.length);
        final int elementsCount = size();
        // +1 because there is always one empty slot in a deque.
        if (elementsCount >= bufferLen - expectedAdditions - 1)
        {
            final int newSize = resizer.grow(bufferLen, elementsCount, expectedAdditions + 1);
            assert newSize >= (elementsCount + expectedAdditions + 1) : "Resizer failed to" +
                    " return sensible new size: " + newSize + " <= " 
                    + (elementsCount + expectedAdditions);

            final int [] newBuffer = new int [newSize];
            if (bufferLen > 0)
            {
                toArray(newBuffer);
                tail = elementsCount;
                head = 0;
            }
            this.buffer = newBuffer;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
         public int [] toArray()
         
    {
        final int size = size();
        return toArray(new int [size]);
    }

    /**
     * Copies elements of this deque to an array. The content of the <code>target</code>
     * array is filled from index 0 (head of the queue) to index <code>size() - 1</code>
     * (tail of the queue).
     * 
     * @param target The target array must be large enough to hold all elements.
     * @return Returns the target argument for chaining.
     */
    public int [] toArray(int [] target)
    {
        assert target.length >= size() : "Target array must be >= " + size();

        if (head < tail)
        {
            // The contents is not wrapped around. Just copy.
            System.arraycopy(buffer, head, target, 0, size());
        }
        else if (head > tail)
        {
            // The contents is split. Merge elements from the following indexes:
            // [head...buffer.length - 1][0, tail - 1]
            final int rightCount = buffer.length - head;
            System.arraycopy(buffer, head, target, 0, rightCount);
            System.arraycopy(buffer,    0, target, rightCount, tail);
        }

        return target;
    }

    /**
     * Clone this object. The returned clone will reuse the same hash function
     * and array resizing strategy.
     */
    @Override
    public IntArrayDeque clone()
    {
        try
        {
            /*  */
            IntArrayDeque cloned = (IntArrayDeque) super.clone();
            cloned.buffer = buffer.clone();
            return cloned;
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Move one index to the left, wrapping around buffer. 
     */
    protected static int oneLeft(int index, int modulus)
    {
        if (index >= 1) return index - 1;
        return modulus - 1;
    }

    /**
     * Move one index to the right, wrapping around buffer. 
     */
    protected static int oneRight(int index, int modulus)
    {
        if (index + 1 == modulus) return 0;
        return index + 1;
    }

    /**
     * An iterator implementation for {@link ObjectArrayDeque#iterator}.
     */
    private final class ValueIterator extends AbstractIterator<IntCursor>
    {
        private final IntCursor cursor;
        private int remaining;

        public ValueIterator()
        {
            cursor = new IntCursor();
            cursor.index = oneLeft(head, buffer.length);
            this.remaining = size();
        }

        @Override
        protected IntCursor fetch()
        {
            if (remaining == 0)
                return done();

            remaining--;
            cursor.value = buffer[cursor.index = oneRight(cursor.index, buffer.length)];
            return cursor;
        }
    }

    /**
     * An iterator implementation for {@link ObjectArrayDeque#descendingIterator()}.
     */
    private final class DescendingValueIterator extends AbstractIterator<IntCursor>
    {
        private final IntCursor cursor;
        private int remaining;

        public DescendingValueIterator()
        {
            cursor = new IntCursor();
            cursor.index = tail;
            this.remaining = size();
        }

        @Override
        protected IntCursor fetch()
        {
            if (remaining == 0)
                return done();

            remaining--;
            cursor.value = buffer[cursor.index = oneLeft(cursor.index, buffer.length)];
            return cursor;
        }
    }

    /**
     * Returns a cursor over the values of this deque (in head to tail order). The
     * iterator is implemented as a cursor and it returns <b>the same cursor instance</b>
     * on every call to {@link Iterator#next()} (to avoid boxing of primitive types). To
     * read the current value (or index in the deque's buffer) use the cursor's public
     * fields. An example is shown below.
     * 
     * <pre>
     * for (IntValueCursor c : intDeque)
     * {
     *     System.out.println(&quot;buffer index=&quot; 
     *         + c.index + &quot; value=&quot; + c.value);
     * }
     * </pre>
     */
    public Iterator<IntCursor> iterator()
    {
        return new ValueIterator();
    }

    /**
     * Returns a cursor over the values of this deque (in tail to head order). The
     * iterator is implemented as a cursor and it returns <b>the same cursor instance</b>
     * on every call to {@link Iterator#next()} (to avoid boxing of primitive types). To
     * read the current value (or index in the deque's buffer) use the cursor's public
     * fields. An example is shown below.
     * 
     * <pre>
     * for (Iterator<IntCursor> i = intDeque.descendingIterator(); i.hasNext(); )
     * {
     *     final IntCursor c = i.next();
     *     System.out.println(&quot;buffer index=&quot; 
     *         + c.index + &quot; value=&quot; + c.value);
     * }
     * </pre>
     */
    public Iterator<IntCursor> descendingIterator()
    {
        return new DescendingValueIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IntProcedure> T forEach(T procedure)
    {
        forEach(procedure, head, tail);
        return procedure;
    }

    /**
     * Applies <code>procedure</code> to a slice of the deque,
     * <code>fromIndex</code>, inclusive, to <code>toIndex</code>, 
     * exclusive.
     */
    private void forEach(IntProcedure procedure, int fromIndex, final int toIndex)
    {
        final int [] buffer = this.buffer;
        for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length))
        {
            procedure.apply(buffer[i]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IntPredicate> T forEach(T predicate)
    {
        int fromIndex = head;
        int toIndex = tail;

        final int [] buffer = this.buffer;
        for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length))
        {
            if (!predicate.apply(buffer[i]))
                break;
        }
        
        return predicate;
    }

    /**
     * Applies <code>procedure</code> to all elements of this deque, tail to head. 
     */
    @Override
    public <T extends IntProcedure> T descendingForEach(T procedure)
    {
        descendingForEach(procedure, head, tail);
        return procedure;
    }

    /**
     * Applies <code>procedure</code> to a slice of the deque,
     * <code>toIndex</code>, exclusive, down to <code>fromIndex</code>, inclusive.
     */
    private void descendingForEach(IntProcedure procedure, 
        int fromIndex, final int toIndex)
    {
        if (fromIndex == toIndex)
            return;

        final int [] buffer = this.buffer;
        int i = toIndex;
        do
        {
            i = oneLeft(i, buffer.length);
            procedure.apply(buffer[i]);
        } while (i != fromIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends IntPredicate> T descendingForEach(T predicate)
    {
        descendingForEach(predicate, head, tail);
        return predicate;
    }
    
    /**
     * Applies <code>predicate</code> to a slice of the deque,
     * <code>toIndex</code>, exclusive, down to <code>fromIndex</code>, inclusive
     * or until the predicate returns <code>false</code>.
     */
    private void descendingForEach(IntPredicate predicate, 
        int fromIndex, final int toIndex)
    {
        if (fromIndex == toIndex)
            return;

        final int [] buffer = this.buffer;
        int i = toIndex;
        do
        {
            i = oneLeft(i, buffer.length);
            if (!predicate.apply(buffer[i]))
                break;
        } while (i != fromIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeAll(IntPredicate predicate)
    {
        int removed = 0;
        final int last = tail;
        final int bufLen = buffer.length;
        int from, to;
        from = to = head;
        try
        {
            for (from = to = head; from != last; from = oneRight(from, bufLen))
            {
                if (predicate.apply(buffer[from]))
                {
                    buffer[from] = ((int) 0);
                    removed++;
                    continue;
                }
    
                if (to != from)
                {
                    buffer[to] = buffer[from];
                    buffer[from] = ((int) 0);
                }
        
                to = oneRight(to, bufLen);
            }
        }
        finally
        {
            // Keep the deque in consistent state even if the predicate throws an exception.
            for (; from != last; from = oneRight(from, bufLen))
            {
                if (to != from)
                {
                    buffer[to] = buffer[from];
                    buffer[from] = ((int) 0);
                }
        
                to = oneRight(to, bufLen);
            }
            tail = to;
        }

        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(int e)
    {
        int fromIndex = head;
        int toIndex = tail;

        final int [] buffer = this.buffer;
        for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length))
        {
            if (((e) == (buffer[i])))
                return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int h = 1;
        int fromIndex = head;
        int toIndex = tail;

        final int [] buffer = this.buffer;
        for (int i = fromIndex; i != toIndex; i = oneRight(i, buffer.length))
        {
            h = 31 * h + rehash(this.buffer[i]);
        }
        return h;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    /*  */
    public boolean equals(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof IntDeque)
            {
                IntDeque other = (IntDeque) obj;
                if (other.size() == this.size())
                {
                    int fromIndex = head;
                    final int [] buffer = this.buffer;
                    int i = fromIndex;
                    for (IntCursor c : other)
                    {
                        if (!((c.value) == (buffer[i])))
                            return false;
                        i = oneRight(i, buffer.length);                        
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a new object of this class with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static /*  */
      IntArrayDeque newInstance()
    {
        return new IntArrayDeque();
    }

    /**
     * Returns a new object of this class with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static /*  */
        IntArrayDeque newInstanceWithCapacity(int initialCapacity)
    {
        return new IntArrayDeque(initialCapacity);
    }

    /**
     * Create a new deque by pushing a variable number of arguments to the end of it.
     */
    public static /*  */ 
        IntArrayDeque from(int... elements)
    {
        final IntArrayDeque coll = new IntArrayDeque(elements.length);
        coll.addLast(elements);
        return coll;
    }

    /**
     * Create a new deque by pushing a variable number of arguments to the end of it.
     */
    public static /*  */ 
        IntArrayDeque from(IntArrayDeque container)
    {
        return new IntArrayDeque(container);
    }
}
