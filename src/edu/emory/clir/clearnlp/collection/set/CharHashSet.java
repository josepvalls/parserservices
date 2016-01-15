/**
 * Copyright 2014, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.clir.clearnlp.collection.set;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.carrotsearch.hppc.CharOpenHashSet;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CharHashSet extends CharOpenHashSet implements Serializable
{
	private static final long serialVersionUID = -3796053685010557911L;
	
	public CharHashSet()
	{
		super();
	}
	
	public CharHashSet(int initialCapacity)
	{
		super(initialCapacity);
	}
	
	public CharHashSet(char... characters)
	{
		for (char c : characters)
			add(c);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		addAll((char[])in.readObject());
	}

	private void writeObject(ObjectOutputStream o) throws IOException
	{
		o.writeObject(toArray());
	}
	
	public void addAll(char[] array)
	{
		for (char item : array)
			add(item);
	}
}