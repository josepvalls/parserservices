package com.carrotsearch.hppc;

import java.util.Arrays;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.predicates.ObjectPredicate;

/**
 * Common superclass for collections. 
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: AbstractObjectCollection.java") 
abstract class AbstractObjectCollection<KType> implements ObjectCollection<KType>
{
    /**
     * Default implementation uses a predicate for removal.
     */
    /*  */
    @SuppressWarnings("unchecked")
    /*  */
    @Override
    public int removeAll(final ObjectLookupContainer<? extends KType> c)
    {
        // We know c holds sub-types of KType and we're not modifying c, so go unchecked.
        final ObjectContainer<KType> c2 = (ObjectContainer<KType>) c;
        return this.removeAll(new ObjectPredicate<KType>()
        {
            public boolean apply(KType k)
            {
                return c2.contains(k);
            }
        });
    }

    /**
     * Default implementation uses a predicate for retaining.
     */
    /*  */
    @SuppressWarnings("unchecked")
    /*  */
    @Override
    public int retainAll(final ObjectLookupContainer<? extends KType> c)
    {
        // We know c holds sub-types of KType and we're not modifying c, so go unchecked.
        final ObjectContainer<KType> c2 = (ObjectContainer<KType>) c;
        return this.removeAll(new ObjectPredicate<KType>()
        {
            public boolean apply(KType k)
            {
                return !c2.contains(k);
            }
        });
    }

    /**
     * Default implementation redirects to {@link #removeAll(ObjectPredicate)}
     * and negates the predicate.
     */
    @Override
    public int retainAll(final ObjectPredicate<? super KType> predicate)
    {
        return removeAll(new ObjectPredicate<KType>()
        {
            public boolean apply(KType value)
            {
                return !predicate.apply(value);
            };
        });
    }

    /**
     * Default implementation of copying to an array.
     */
    @Override
     
    @SuppressWarnings("unchecked")
    public KType [] toArray(Class<? super KType> clazz)
      
    {
        final int size = size();
        final KType [] array = 
         
           (KType []) java.lang.reflect.Array.newInstance(clazz, size);
         

        int i = 0;
        for (ObjectCursor<KType> c : this)
        {
            array[i++] = c.value;
        }
        return array;
    }

    /*  */
    @Override
    public Object[] toArray()
    {
        final Object [] array = new Object [size()];
        int i = 0;
        for (ObjectCursor<KType> c : this)
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
