package com.carrotsearch.hppc;

import java.util.Set;


/**
 * A set of <code>char</code>s.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: CharSet.java") 
public interface CharSet extends CharCollection
{
    /**
     * Adds <code>k</code> to the set.
     * 
     * @return Returns <code>true</code> if this element was not part of the set before. Returns
     * <code>false</code> if an equal element is part of the set, <b>and replaces the existing
     * equal element</b> with the argument (if keys are object types).
     */
    public boolean add(char k);
    
    /**
     * Compares the specified object with this set for equality. Returns
     * <tt>true</tt> if and only if the specified object is also a
     * {@link ObjectSet} and both objects contains exactly the same objects.
     */
    public boolean equals(Object obj);

    /**
     * @return A hash code of elements stored in the set. The hash code
     * is defined identically to {@link Set#hashCode()} (sum of hash codes of elements
     * within the set). Because sum is commutative, this ensures that different order
     * of elements in a set does not affect the hash code.
     */
    public int hashCode();
}
