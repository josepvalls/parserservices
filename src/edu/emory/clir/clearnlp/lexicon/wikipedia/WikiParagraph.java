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
package edu.emory.clir.clearnlp.lexicon.wikipedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.emory.clir.clearnlp.util.Joiner;
import edu.emory.clir.clearnlp.util.constant.StringConst;

/**
 * @since 3.0.0.
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class WikiParagraph implements Serializable
{
	private static final long serialVersionUID = 7011678413565546215L;
	private List<String> l_sentences;
	
	public WikiParagraph()
	{
		l_sentences = new ArrayList<>();		
	}
	
	public List<String> getSentences()
	{
		return l_sentences;
	}
	
	public void addSentence(String sentence)
	{
		l_sentences.add(sentence);
	}
	
	@Override
	public String toString() 
	{
		return Joiner.join(l_sentences, StringConst.NEW_LINE);
	}
}
