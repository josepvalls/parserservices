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
package edu.emory.clir.clearnlp.dictionary;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface PathEnglishMPAnalyzer
{
	String ROOT = "edu/emory/clir/clearnlp/dictionary/morphology/english/";

	String INFLECTION_SUFFIX = ROOT + "inflection_suffix.xml";
	String ABBREVIATOIN_RULE = ROOT + "abbreviation.rule";
	String CARDINAL_BASE     = ROOT + "cardinal.base";
	String ORDINAL_BASE      = ROOT + "ordinal.base";
	
	String VERB          = "verb";
	String NOUN          = "noun";
	String ADJECTIVE     = "adjective";
	String ADVERB        = "adverb";
	String EXT_BASE      = ".base";
	String EXT_EXCEPTION = ".exc";
	
	String DERIVATION_SUFFIX_N2V = ROOT + "derivation_suffix_n2v.xml";
}
