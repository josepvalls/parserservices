package com.carrotsearch.hppc;

import java.util.Arrays;

import com.carrotsearch.hppc.cursors.CharCursor;
import com.carrotsearch.hppc.predicates.CharPredicate;

/**
 * Common superclass for collections. 
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: AbstractCharCollection.java") 
abstract class AbstractCharCollection implements CharCollection
{
    /**
     * Default implementation uses a predicate for removal.
     */
    /*  */
    @Override
    public int removeAll(final CharLookupContainer c)
    {
        // We know c holds sub-types of char and we're not modifying c, so go unchecked.
        final CharContainer c2 = (CharContainer) c;
        return this.removeAll(new CharPredicate()
        {
            public boolean apply(char k)
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
    public int retainAll(final CharLookupContainer c)
    {
        // We know c holds sub-types of char and we're not modifying c, so go unchecked.
        final CharContainer c2 = (CharContainer) c;
        return this.removeAll(new CharPredicate()
        {
            public boolean apply(char k)
            {
                return !c2.contains(k);
            }
        });
    }

    /**
     * Default implementation redirects to {@link #removeAll(CharPredicate)}
     * and negates the predicate.
     */
    @Override
    public int retainAll(final CharPredicate predicate)
    {
        return removeAll(new CharPredicate()
        {
            public boolean apply(char value)
            {
                return !predicate.apply(value);
            };
        });
    }

    /**
     * Default implementation of copying to an array.
     */
    @Override
        public char [] toArray()
        
    {
        final int size = size();
        final char [] array = 
                   new char [size];   
            

        int i = 0;
        for (CharCursor c : this)
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
