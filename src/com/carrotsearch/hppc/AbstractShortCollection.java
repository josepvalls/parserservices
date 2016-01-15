package com.carrotsearch.hppc;

import java.util.Arrays;

import com.carrotsearch.hppc.cursors.ShortCursor;
import com.carrotsearch.hppc.predicates.ShortPredicate;

/**
 * Common superclass for collections. 
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: AbstractShortCollection.java") 
abstract class AbstractShortCollection implements ShortCollection
{
    /**
     * Default implementation uses a predicate for removal.
     */
    /*  */
    @Override
    public int removeAll(final ShortLookupContainer c)
    {
        // We know c holds sub-types of short and we're not modifying c, so go unchecked.
        final ShortContainer c2 = (ShortContainer) c;
        return this.removeAll(new ShortPredicate()
        {
            public boolean apply(short k)
            {
                return c2.contains(k);
            }
        });
    }

    /**
     * Default implementation uses a predicate for retaining.
     */
    /*  */
    @Override
    public int retainAll(final ShortLookupContainer c)
    {
        // We know c holds sub-types of short and we're not modifying c, so go unchecked.
        final ShortContainer c2 = (ShortContainer) c;
        return this.removeAll(new ShortPredicate()
        {
            public boolean apply(short k)
            {
                return !c2.contains(k);
            }
        });
    }

    /**
     * Default implementation redirects to {@link #removeAll(ShortPredicate)}
     * and negates the predicate.
     */
    @Override
    public int retainAll(final ShortPredicate predicate)
    {
        return removeAll(new ShortPredicate()
        {
            public boolean apply(short value)
            {
                return !predicate.apply(value);
            };
        });
    }

    /**
     * Default implementation of copying to an array.
     */
    @Override
        public short [] toArray()
        
    {
        final int size = size();
        final short [] array = 
                   new short [size];   
            

        int i = 0;
        for (ShortCursor c : this)
        {
            array[i++] = c.value;
        }
        return array;
    }

    /*  */

    /**
     * Convert the contents of this container to a human-friendly string.
     */
    @Override
    public String toString()
    {
        return Arrays.toString(this.toArray());
    }
}
