package com.carrotsearch.hppc;

import com.carrotsearch.hppc.cursors.CharObjectCursor;

/**
 * An associative container with unique binding from keys to a single value.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: CharObjectMap.java") 
public interface CharObjectMap<VType> 
    extends CharObjectAssociativeContainer<VType>
{
    /**
     * Place a given key and value in the container.
     * 
     * @return The value previously stored under the given key in the map is returned.
     */
    public VType put(char key, VType value);

      
      

      
      

    /**
     * @return Returns the value associated with the given key or the default value
     * for the key type, if the key is not associated with any value. 
     *
     * <b>Important note:</b> For primitive type values, the value returned for a non-existing
     * key may not be the default value of the primitive type (it may be any value previously
     * assigned to that slot).
     */
    public VType get(char key);

    /**
     * @return Returns the value associated with the given key or the provided default value if the
     * key is not associated with any value. 
     */
    public VType getOrDefault(char key, VType defaultValue);

    /**
     * Puts all keys from another container to this map, replacing the values
     * of existing keys, if such keys are present.   
     * 
     * @return Returns the number of keys added to the map as a result of this
     * call (not previously present in the map). Values of existing keys are overwritten.
     */
    public int putAll(
        CharObjectAssociativeContainer<? extends VType> container);

    /**
     * Puts all keys from an iterable cursor to this map, replacing the values
     * of existing keys, if such keys are present.   
     * 
     * @return Returns the number of keys added to the map as a result of this
     * call (not previously present in the map). Values of existing keys are overwritten.
     */
    public int putAll(
        Iterable<? extends CharObjectCursor<? extends VType>> iterable);

    /**
     * Remove all values at the given key. The default value for the key type is returned
     * if the value does not exist in the map. 
     */
    public VType remove(char key);
    
    /**
     * Compares the specified object with this set for equality. Returns
     * <tt>true</tt> if and only if the specified object is also a
     * {@link CharObjectMap} and both objects contains exactly the same key-value pairs.
     */
    public boolean equals(Object obj);

    /**
     * @return A hash code of elements stored in the map. The hash code
     * is defined as a sum of hash codes of keys and values stored
     * within the set). Because sum is commutative, this ensures that different order
     * of elements in a set does not affect the hash code.
     */
    public int hashCode();
}
