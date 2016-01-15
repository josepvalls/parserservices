package com.carrotsearch.hppc.cursors;

/**
 * A cursor over a collection of <code>bytes</code>.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:28+0200", value = "HPPC generated from: ByteCursor.java") 
public final class ByteCursor
{
    /**
     * The current value's index in the container this cursor belongs to. The meaning of
     * this index is defined by the container (usually it will be an index in the underlying
     * storage buffer).
     */
    public int index;

    /**
     * The current value.
     */
    public byte value;

    @Override
    public String toString()
    {
        return "[cursor, index: " + index + ", value: " + value + "]";
    }
}