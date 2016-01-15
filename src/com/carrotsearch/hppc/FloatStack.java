package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.FloatCursor;

/**
 * An extension to {@link FloatArrayList} adding stack-related utility methods. The top of
 * the stack is at the <code>{@link #size()} - 1</code> element.
 * 
 * <p>See {@link ObjectArrayList} class for API similarities and differences against Java
 * Collections.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: FloatStack.java") 
public class FloatStack extends FloatArrayList
{
    /**
     * Create with default sizing strategy and initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public FloatStack()
    {
        super();
    }

    /**
     * Create with default sizing strategy and the given initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public FloatStack(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Create with a custom buffer resizing strategy.
     */
    public FloatStack(int initialCapacity, ArraySizingStrategy resizer)
    {
        super(initialCapacity, resizer);
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public FloatStack(FloatContainer container)
    {
        super(container);
    }

    /**
     * Adds one float to the stack.
     */
    public void push(float e1)
    {
        ensureBufferSpace(1);
        buffer[elementsCount++] = e1;
    }

    /**
     * Adds two floats to the stack.
     */
    public void push(float e1, float e2)
    {
        ensureBufferSpace(2);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
    }

    /**
     * Adds three floats to the stack.
     */
    public void push(float e1, float e2, float e3)
    {
        ensureBufferSpace(3);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
        buffer[elementsCount++] = e3;
    }

    /**
     * Adds four floats to the stack.
     */
    public void push(float e1, float e2, float e3, float e4)
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
    public void push(float [] elements, int start, int len)
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
    public void push(float... elements)
    {
        push(elements, 0, elements.length);
    }

    /**
     * Pushes all elements from another container to the top of the stack.
     */
    public int pushAll(FloatContainer container)
    {
        return addAll(container);
    }

    /**
     * Pushes all elements from another iterable to the top of the stack.
     */
    public int pushAll(Iterable<? extends FloatCursor> iterable)
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
    }

    /**
     * Discard the top element from the stack.
     */
    public void discard()
    {
        assert elementsCount > 0;

        elementsCount--;
        /*  */
    }

    /**
     * Remove the top element from the stack and return it.
     */
    public float pop()
    {
        assert elementsCount > 0;

        final float v = buffer[--elementsCount];
        /*  */
        return v;
    }

    /**
     * Peek at the top element on the stack.
     */
    public float peek()
    {
        assert elementsCount > 0;

        return buffer[elementsCount - 1];
    }

    /**
     * Returns a new object of this class with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static  FloatStack newInstance()
    {
        return new FloatStack();
    }

    /**
     * Returns a new object of this list with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static  FloatStack newInstanceWithCapacity(int initialCapacity)
    {
        return new FloatStack(initialCapacity);
    }

    /**
     * Create a stack by pushing a variable number of arguments to it.
     */
    public static  FloatStack from(float... elements)
    {
        final FloatStack stack = new FloatStack(elements.length);
        stack.push(elements);
        return stack;
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public static  FloatStack from(FloatContainer container)
    {
        return new FloatStack(container);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FloatStack clone()
    {
        return (FloatStack) super.clone();
    }
}
