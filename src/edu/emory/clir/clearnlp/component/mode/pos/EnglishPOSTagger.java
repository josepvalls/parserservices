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
package edu.emory.clir.clearnlp.component.mode.pos;

import java.io.ObjectInputStream;

import edu.emory.clir.clearnlp.classification.model.StringModel;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class EnglishPOSTagger extends AbstractPOSTagger
{
	/** Creates a pos tagger for collect. */
	public EnglishPOSTagger(POSConfiguration configuration)
	{
		super(configuration);
	}
	
	/** Creates a pos tagger for train. */
	public EnglishPOSTagger(POSFeatureExtractor[] extractors, Object lexicons)
	{
		super(extractors, lexicons);
	}
	
	/** Creates a pos tagger for bootstrap or evaluate. */
	public EnglishPOSTagger(POSFeatureExtractor[] extractors, Object lexicons, StringModel[] models, boolean bootstrap)
	{
		super(extractors, lexicons, models, bootstrap);
	}
	
	/** Creates a pos tagger for decode. */
	public EnglishPOSTagger(ObjectInputStream in)
	{
		super(in);
	}
	
	/** Creates a pos tagger for decode. */
	public EnglishPOSTagger(byte[] models)
	{
		super(models);
	}
	
	public void postProcess(POSState state) {}
}
