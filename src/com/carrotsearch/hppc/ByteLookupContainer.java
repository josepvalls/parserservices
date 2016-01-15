package com.carrotsearch.hppc;

/**
 * Marker interface for containers that can check if they contain a given object in
 * at least time <code>O(log n)</code> and ideally in amortized 
 * constant time <code>O(1)</code>.
 */
 @javax.annotation.Generated(date = "2014-09-08T10:42:29+0200", value = "HPPC generated from: ByteLookupContainer.java") 
public interface ByteLookupContainer extends ByteContainer
{
    public boolean contains(byte e);
}
