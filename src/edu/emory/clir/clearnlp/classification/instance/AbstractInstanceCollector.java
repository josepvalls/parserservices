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
package edu.emory.clir.clearnlp.classification.instance;

import java.util.ArrayDeque;
import java.util.Deque;

import edu.emory.clir.clearnlp.classification.vector.AbstractFeatureVector;
import edu.emory.clir.clearnlp.collection.map.ObjectIntHashMap;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
abstract public class AbstractInstanceCollector<I extends AbstractInstance<F>, F extends AbstractFeatureVector>
{
	private ObjectIntHashMap<String> m_labels;
	private Deque<I> i_instances;
	protected int n_features;
	
	public AbstractInstanceCollector()
	{
		init();
	}
	
	abstract public void init();
	
	protected void initDefault()
	{
		i_instances = new ArrayDeque<>();
		m_labels    = new ObjectIntHashMap<String>();
		n_features  = 0;
	}
	
	public void addInstance(I instance)
	{
		i_instances.add(instance);
		addLabel(instance.getLabel());
		addFeatures(instance.getFeatureVector());
	}
	
	/** Called by {@link #addLexica(StringInstance)}. */
	protected void addLabel(String label)
	{
		m_labels.add(label);
	}
	
	/** Called by {@link #addLexica(StringInstance)}. */
	abstract protected void addFeatures(F vector);
	
	public int getLabelSize()
	{
		return m_labels.size();
	}
	
	public int getFeatureSize()
	{
		return n_features;
	}
	
	public ObjectIntHashMap<String> getLabelMap()
	{
		return m_labels;
	}
	
	public Deque<I> getInstances()
	{
		return i_instances;
	}
}