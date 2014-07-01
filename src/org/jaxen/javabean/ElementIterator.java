package org.jaxen.javabean;

import java.util.Iterator;

public class ElementIterator
    implements Iterator
{
    private Element parent;
    private String name;
    private Iterator iterator;

    public ElementIterator(Element parent,
                           String name,
                           Iterator iterator)
    {
        this.parent    = parent;
        this.name     = name;
        this.iterator = iterator;
    }

    @Override
	public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    @Override
	public Object next()
    {
        return new Element( parent,
                            this.name,
                            this.iterator.next() );
    }

    @Override
	public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
