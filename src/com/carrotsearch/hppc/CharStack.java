package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.CharCursor;

/**
 * An extension to {@link CharArrayList} adding stack-related utility methods. The top of
 * the stack is at the <code>{@link #size()} - 1</code> element.
 * 
 * <p>See {@link ObjectArrayList} class for API similarities and differences against Java
 * Collections.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: CharStack.java") 
public class CharStack extends CharArrayList
{
    /**
     * Create with default sizing strategy and initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public CharStack()
    {
        super();
    }

    /**
     * Create with default sizing strategy and the given initial capacity.
     * 
     * @see BoundedProportionalArraySizingStrategy
     */
    public CharStack(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * Create with a custom buffer resizing strategy.
     */
    public CharStack(int initialCapacity, ArraySizingStrategy resizer)
    {
        super(initialCapacity, resizer);
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public CharStack(CharContainer container)
    {
        super(container);
    }

    /**
     * Adds one char to the stack.
     */
    public void push(char e1)
    {
        ensureBufferSpace(1);
        buffer[elementsCount++] = e1;
    }

    /**
     * Adds two chars to the stack.
     */
    public void push(char e1, char e2)
    {
        ensureBufferSpace(2);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
    }

    /**
     * Adds three chars to the stack.
     */
    public void push(char e1, char e2, char e3)
    {
        ensureBufferSpace(3);
        buffer[elementsCount++] = e1;
        buffer[elementsCount++] = e2;
        buffer[elementsCount++] = e3;
    }

    /**
     * Adds four chars to the stack.
     */
    public void push(char e1, char e2, char e3, char e4)
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
    public void push(char [] elements, int start, int len)
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
    public void push(char... elements)
    {
        push(elements, 0, elements.length);
    }

    /**
     * Pushes all elements from another container to the top of the stack.
     */
    public int pushAll(CharContainer container)
    {
        return addAll(container);
    }

    /**
     * Pushes all elements from another iterable to the top of the stack.
     */
    public int pushAll(Iterable<? extends CharCursor> iterable)
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
    public char pop()
    {
        assert elementsCount > 0;

        final char v = buffer[--elementsCount];
        /*  */
        return v;
    }

    /**
     * Peek at the top element on the stack.
     */
    public char peek()
    {
        assert elementsCount > 0;

        return buffer[elementsCount - 1];
    }

    /**
     * Returns a new object of this class with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static  CharStack newInstance()
    {
        return new CharStack();
    }

    /**
     * Returns a new object of this list with no need to declare generic type (shortcut
     * instead of using a constructor).
     */
    public static  CharStack newInstanceWithCapacity(int initialCapacity)
    {
        return new CharStack(initialCapacity);
    }

    /**
     * Create a stack by pushing a variable number of arguments to it.
     */
    public static  CharStack from(char... elements)
    {
        final CharStack stack = new CharStack(elements.length);
        stack.push(elements);
        return stack;
    }

    /**
     * Create a stack by pushing all elements of another container to it.
     */
    public static  CharStack from(CharContainer container)
    {
        return new CharStack(container);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CharStack clone()
    {
        return (CharStack) super.clone();
    }
}
