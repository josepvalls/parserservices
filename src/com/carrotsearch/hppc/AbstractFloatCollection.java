package com.carrotsearch.hppc;

import java.util.Arrays;

import com.carrotsearch.hppc.cursors.FloatCursor;
import com.carrotsearch.hppc.predicates.FloatPredicate;

/**
 * Common superclass for collections. 
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: AbstractFloatCollection.java") 
abstract class AbstractFloatCollection implements FloatCollection
{
    /**
     * Default implementation uses a predicate for removal.
     */
    /*  */
    @Override
    public int removeAll(final FloatLookupContainer c)
    {
        // We know c holds sub-types of float and we're not modifying c, so go unchecked.
        final FloatContainer c2 = (FloatContainer) c;
        return this.removeAll(new FloatPredicate()
        {
            public boolean apply(float k)
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
    public int retainAll(final FloatLookupContainer c)
    {
        // We know c holds sub-types of float and we're not modifying c, so go unchecked.
        final FloatContainer c2 = (FloatContainer) c;
        return this.removeAll(new FloatPredicate()
        {
            public boolean apply(float k)
            {
                return !c2.contains(k);
            }
        });
    }

    /**
     * Default implementation redirects to {@link #removeAll(FloatPredicate)}
     * and negates the predicate.
     */
    @Override
    public int retainAll(final FloatPredicate predicate)
    {
        return removeAll(new FloatPredicate()
        {
            public boolean apply(float value)
            {
                return !predicate.apply(value);
            };
        });
    }

    /**
     * Default implementation of copying to an array.
     */
    @Override
        public float [] toArray()
        
    {
        final int size = size();
        final float [] array = 
                   new float [size];   
            

        int i = 0;
        for (FloatCursor c : this)
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
