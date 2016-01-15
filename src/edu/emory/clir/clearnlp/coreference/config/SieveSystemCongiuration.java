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
package edu.emory.clir.clearnlp.coreference.config;

import java.util.ArrayList;
import java.util.List;

import edu.emory.clir.clearnlp.coreference.sieve.AbstractSieve;
import edu.emory.clir.clearnlp.coreference.sieve.ExactStringMatch;
import edu.emory.clir.clearnlp.coreference.sieve.PreciseConstructMatch;
import edu.emory.clir.clearnlp.coreference.sieve.PronounMatch;
import edu.emory.clir.clearnlp.coreference.sieve.ProperHeadWordMatch;
import edu.emory.clir.clearnlp.coreference.sieve.RelaxedStringMatch;
import edu.emory.clir.clearnlp.coreference.sieve.SpeakerIdentification;
import edu.emory.clir.clearnlp.coreference.sieve.StrictHeadMatch;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	May 21, 2015
 */
public class SieveSystemCongiuration extends AbstractCorefConfiguration{
	private List<AbstractSieve> selectedSieves;
	
	public SieveSystemCongiuration(TLanguage lang){
		super(lang);
		selectedSieves = new ArrayList<>();
	}
	
	public List<AbstractSieve> getSieves(){
		return selectedSieves;
	}
	
	public void mountSieves(AbstractSieve... sieves){
		for(AbstractSieve sieve : sieves)	selectedSieves.add(sieve);
	}
	
	public void loadDefaultSieves(boolean decapitalize){	
		selectedSieves.add(new SpeakerIdentification());
		selectedSieves.add(new ExactStringMatch(decapitalize));	
		selectedSieves.add(new RelaxedStringMatch(decapitalize));
		selectedSieves.add(new PreciseConstructMatch());
		selectedSieves.add(new StrictHeadMatch());
		selectedSieves.add(new ProperHeadWordMatch());
		selectedSieves.add(new PronounMatch());
	}
	
	public void loadDefaultSieves(boolean decapitalize, boolean speakerIdentification, boolean exactStringMatch, boolean relaxedStringMatch, boolean preciseContuctMatch, boolean stringHeadMatch, boolean properHeadWordMatch, boolean pronunMatch){
		if(speakerIdentification) 	selectedSieves.add(new SpeakerIdentification());
		if(exactStringMatch) 	selectedSieves.add(new ExactStringMatch(decapitalize));	
		if(relaxedStringMatch)	selectedSieves.add(new RelaxedStringMatch(decapitalize));
		if(preciseContuctMatch)	selectedSieves.add(new PreciseConstructMatch());
		if(stringHeadMatch)		selectedSieves.add(new StrictHeadMatch());
		if(properHeadWordMatch)	selectedSieves.add(new ProperHeadWordMatch());
		if(pronunMatch)			selectedSieves.add(new PronounMatch());
	}
}
