package com.carrotsearch.hppc.cursors;


/**
 * A cursor over entries of an associative container (short keys and short values).
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:28+0200", value = "HPPC generated from: ShortShortCursor.java") 
public final class ShortShortCursor
{
    /**
     * The current key and value's index in the container this cursor belongs to. The meaning of
     * this index is defined by the container (usually it will be an index in the underlying
     * storage buffer).
     */
    public int index;

    /**
     * The current key.
     */
    public short key;

    /**
     * The current value.
     */
    public short value;
    
    @Override
    public String toString()
    {
        return "[cursor, index: " + index + ", key: " + key + ", value: " + value + "]";
    }
}
