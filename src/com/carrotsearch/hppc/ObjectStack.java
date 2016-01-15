package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.ObjectCursor;

/**
 * An extension to {@link ObjectArrayList} adding stack-related utility methods. The top of
 * the stack is at the <code>{@link #size()} - 1</code> element.
 * 
 * A brief comparison of the API against the Java Collections framework:
 * <table class="nice" summary="Java Collections Stack and HPPC ObjectStack, related methods.">
 * <caption>Java Collections Stack and HPPC {@link ObjectStack}, related methods.</caption>
 * <thead>
 *     <tr class="odd">
 *         <th scope="col">{@linkplain java.util.Stack java.util.Stack}</th>
 *         <th scope="col">{@link ObjectStack}</th>  
 *     </tr>
 * </thead>
 * <tbody>
 * <tr            ><td>push           </td><td>push           </td></tr>
 * <tr class="odd"><td>pop            </td><td>pop, discard   </td></tr>
 * <tr            ><td>peek           </td><td>peek           </td></tr>
 * <tr class="odd"><td>removeRange, 
 *                     removeElementAt</td><td>removeRange, remove, discard</td></tr>
 * <tr            ><td>size           </td><td>size           </td></tr>
 * <tr class="odd"><td>clear          </td><td>clear, release </td></tr>
 * <tr            ><td>               </td><td>+ other methods from {@link ObjectArrayList}</td></tr>
 * </tbody>
 * </table>
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: ObjectStack.java") 
public class ObjectStack<KType> extends ObjectArrayList<KType>
{
    /**
     * Create with default sizing strategy and initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public ObjectStack()
    {
        super();
    }

    /**
     * Create with default sizing strategy and the given initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public ObjectStack(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Create with a custom buffer resizing strategy.
     */
    public ObjectStack(int initialCapacity, ArraySizingStrategy resizer)
    {
        super(initialCapacity, resizer);
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public ObjectStack(ObjectContainer<KType> container)
    {
        super(container);
    }

    /**
     * Adds one KType to the stack.
     */
    public void push(KType e1)
    {
        ensureBufferSpace(1);
        buffer[elementsCount++] = e1;
    }

    /**
     * Adds two KTypes to the stack.
     */
    public void push(KType e1, KType e2)
    {
        ensureBufferSpace(2);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
    }

    /**
     * Adds three KTypes to the stack.
     */
    public void push(KType e1, KType e2, KType e3)
    {
        ensureBufferSpace(3);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
        buffer[elementsCount++] = e3;
    }

    /**
     * Adds four KTypes to the stack.
     */
    public void push(KType e1, KType e2, KType e3, KType e4)
    {
        ensureBufferSpace(4);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
        buffer[elementsCount++] = e3;
        buffer[elementsCount++] = e4;
    }

    /**
     * Add a range of array elements to the stack.
     */
    public void push(KType [] elements, int start, int len)
    {
        assert start >= 0 && len >= 0;

        ensureBufferSpace(len);
        System.arraycopy(elements, start, buffer, elementsCount, len);
        elementsCount += len;
    }

    /**
     * Vararg-signature method for pushing elements at the top of the stack.
     * <p><b>This method is handy, but costly if used in tight loops (anonymous 
     * array passing)</b></p>
     */
    public void push(KType... elements)
    {
        push(elements, 0, elements.length);
    }

    /**
     * Pushes all elements from another container to the top of the stack.
     */
    public int pushAll(ObjectContainer<? extends KType> container)
    {
        return addAll(container);
    }

    /**
     * Pushes all elements from another iterable to the top of the stack.
     */
    public int pushAll(Iterable<? extends ObjectCursor<? extends KType>> iterable)
    {
        return addAll(iterable);
    }

    /**
     * Discard an arbitrary number of elements from the top of the stack.
     */
    public void discard(int count)
    {
        assert elementsCount >= count;

        elementsCount -= count;
        /*  */
        java.util.Arrays.fill(buffer, elementsCount, elementsCount + count, null);
        /*  */
    }

    /**
     * Discard the top element from the stack.
     */
    public void discard()
    {
        assert elementsCount > 0;

        elementsCount--;
        /*  */
        buffer[elementsCount] = null; 
        /*  */
    }

    /**
     * Remove the top element from the stack and return it.
     */
    public KType pop()
    {
        assert elementsCount > 0;

        final KType v = buffer[--elementsCount];
        /*  */
        buffer[elementsCount] = null; 
        /*  */
        return v;
    }

    /**
     * Peek at the top element on the stack.
     */
    public KType peek()
    {
        assert elementsCount > 0;

        return buffer[elementsCount - 1];
    }

    /**
     * Returns a new object of this class with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static <KType> ObjectStack<KType> newInstance()
    {
        return new ObjectStack<KType>();
    }

    /**
     * Returns a new object of this list with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static <KType> ObjectStack<KType> newInstanceWithCapacity(int initialCapacity)
    {
        return new ObjectStack<KType>(initialCapacity);
    }

    /**
     * Create a stack by pushing a variable number of arguments to it.
     */
    public static <KType> ObjectStack<KType> from(KType... elements)
    {
        final ObjectStack<KType> stack = new ObjectStack<KType>(elements.length);
        stack.push(elements);
        return stack;
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public static <KType> ObjectStack<KType> from(ObjectContainer<KType> container)
    {
        return new ObjectStack<KType>(container);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectStack<KType> clone()
    {
        return (ObjectStack<KType>) super.clone();
    }
}
