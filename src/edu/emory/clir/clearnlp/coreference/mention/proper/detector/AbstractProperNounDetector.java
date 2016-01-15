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
package edu.emory.clir.clearnlp.coreference.mention.proper.detector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import edu.emory.clir.clearnlp.collection.ngram.Unigram;
import edu.emory.clir.clearnlp.coreference.mention.proper.ProperNoun;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.Splitter;
import edu.emory.clir.clearnlp.util.StringUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	May 13, 2015
 */
public abstract class AbstractProperNounDetector implements Serializable{
	private static final long serialVersionUID = -241011226924650852L;
	
	protected TLanguage language;
	
	public AbstractProperNounDetector(TLanguage l){ language = l; }
	
	abstract public boolean isProperNoun(DEPTree tree, DEPNode node);
	
	abstract public ProperNoun getProperNoun(DEPTree tree, DEPNode node);
	
	protected void addDictionary(InputStream in, Unigram<String> map){
		BufferedReader reader = IOUtils.createBufferedReader(in);
		String line, token;
		String[] t;
		int count;
		
		try{
			while ((line = reader.readLine()) != null){
				t = Splitter.splitTabs(line);
				token = StringUtils.toLowerCase(t[0]);
				count = Integer.parseInt(t[1]);
				map.add(token, count);
			}
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
