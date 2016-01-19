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

import javax.servlet.ServletContext;

import edu.emory.clir.clearnlp.coreference.sieve.AbstractSieve;
import edu.emory.clir.clearnlp.coreference.sieve.ExactStringMatch;
import edu.emory.clir.clearnlp.coreference.sieve.PreciseConstructMatch;
import edu.emory.clir.clearnlp.coreference.sieve.PronounMatch;
import edu.emory.clir.clearnlp.coreference.sieve.ProperHeadWordMatch;
import edu.emory.clir.clearnlp.coreference.sieve.RelaxedStringMatch;
import edu.emory.clir.clearnlp.coreference.sieve.SpeakerIdentification;
import edu.emory.clir.clearnlp.coreference.sieve.StrictHeadMatch;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

import javax.servlet.ServletContext;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	May 21, 2015
 */
public class SieveSystemCongiuration extends AbstractCorefConfiguration{
	private List<AbstractSieve> selectedSieves;
	
	public SieveSystemCongiuration(TLanguage lang, ServletContext _context){
		super(lang,_context);
		System.out.println("THIS IS THE CONTEXT"+String.valueOf(_context));
		selectedSieves = new ArrayList<>();
		
	}
	
	public List<AbstractSieve> getSieves(){
		return selectedSieves;
	}
	
	public void mountSieves(AbstractSieve... sieves){
		for(AbstractSieve sieve : sieves)	selectedSieves.add(sieve);
	}
	
	public void loadDefaultSieves(boolean decapitalize){	
		selectedSieves.add(new SpeakerIdentification(context));
		selectedSieves.add(new ExactStringMatch(context,decapitalize));	
		selectedSieves.add(new RelaxedStringMatch(context,decapitalize));
		selectedSieves.add(new PreciseConstructMatch(context));
		selectedSieves.add(new StrictHeadMatch(context));
		selectedSieves.add(new ProperHeadWordMatch(context));
		selectedSieves.add(new PronounMatch(context));
	}
	
	public void loadDefaultSieves(boolean decapitalize, boolean speakerIdentification, boolean exactStringMatch, boolean relaxedStringMatch, boolean preciseContuctMatch, boolean stringHeadMatch, boolean properHeadWordMatch, boolean pronunMatch){
		System.out.println("THIS IS THE CONTEXT"+String.valueOf(context));
		if(speakerIdentification) 	selectedSieves.add(new SpeakerIdentification(context));
		if(exactStringMatch) 	selectedSieves.add(new ExactStringMatch(context, decapitalize));	
		if(relaxedStringMatch)	selectedSieves.add(new RelaxedStringMatch(context,decapitalize));
		if(preciseContuctMatch)	selectedSieves.add(new PreciseConstructMatch(context));
		if(stringHeadMatch)		selectedSieves.add(new StrictHeadMatch(context));
		if(properHeadWordMatch)	selectedSieves.add(new ProperHeadWordMatch(context));
		if(pronunMatch)			selectedSieves.add(new PronounMatch(context));
	}
}
