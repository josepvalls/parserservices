/**
 * Copyright 2015, Emory University
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
package edu.emory.clir.clearnlp.component.mode.srl;

import java.io.ObjectInputStream;
import java.util.List;

import edu.emory.clir.clearnlp.classification.instance.StringInstance;
import edu.emory.clir.clearnlp.classification.model.StringModel;
import edu.emory.clir.clearnlp.classification.prediction.StringPrediction;
import edu.emory.clir.clearnlp.classification.vector.StringFeatureVector;
import edu.emory.clir.clearnlp.component.AbstractStatisticalComponent;
import edu.emory.clir.clearnlp.component.mode.srl.state.AbstractSRLState;
import edu.emory.clir.clearnlp.dependency.DEPTree;

/**
 * @since 3.1.3
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractSRLabeler extends AbstractStatisticalComponent<String, AbstractSRLState, SRLEval, SRLFeatureExtractor, SRLConfiguration>
{
	/** Creates a semantic role labeler for train. */
	public AbstractSRLabeler(SRLConfiguration configuration, SRLFeatureExtractor[] extractors, Object lexicons)
	{
		super(configuration, extractors, lexicons, false, 2);
	}
	
	/** Creates a semantic role labeler for bootstrap or evaluate. */
	public AbstractSRLabeler(SRLConfiguration configuration, SRLFeatureExtractor[] extractors, Object lexicons, StringModel[] models, boolean bootstrap)
	{
		super(configuration, extractors, lexicons, models, bootstrap);
	}
	
	/** Creates a semantic role labeler for decode. */
	public AbstractSRLabeler(SRLConfiguration configuration, ObjectInputStream in)
	{
		super(configuration, in);
	}
	
	/** Creates a semantic role labeler for decode. */
	public AbstractSRLabeler(SRLConfiguration configuration, byte[] models)
	{
		super(configuration, models);
	}
	
//	====================================== LEXICONS ======================================

	@Override
	public Object getLexicons() {return null;}

	@Override
	public void setLexicons(Object lexicons) {}
	
//	====================================== EVAL ======================================

	@Override
	protected void initEval()
	{
		c_eval = new SRLEval();
	}

//	====================================== PROCESS ======================================

	@Override
	public void process(DEPTree tree)
	{
		AbstractSRLState state = getState(tree);
		List<StringInstance> instances = process(state);
		
		if (isTrainOrBootstrap())
			addInstances(state, instances);
		else if (isEvaluate())
			c_eval.countCorrect(tree, state.getOracle());
	}
	
	protected abstract AbstractSRLState getState(DEPTree tree);
	
	private void addInstances(AbstractSRLState state, List<StringInstance> instances)
	{
		for (StringInstance instance : instances)
			s_models[instance.getFeatureVector().getModelID()].addInstance(instance);
	}
	
	@Override
	protected StringFeatureVector createStringFeatureVector(AbstractSRLState state)
	{
		StringFeatureVector vector = f_extractors[0].createStringFeatureVector(state);
		if (isTrainOrBootstrap()) vector.setModelID(state.getModelIndex());
		return vector;
	}

	@Override
	protected String getAutoLabel(AbstractSRLState state, StringFeatureVector vector)
	{
		StringPrediction p = s_models[state.getModelIndex()].predictBest(vector);
		return p.getLabel();
	}

//	====================================== ONLINE TRAIN ======================================

	@Override
	public void onlineTrain(List<DEPTree> trees)
	{
	}

	@Override
	protected void onlineLexicons(DEPTree tree)
	{
	}
}
