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

import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	Jun 2, 2015
 */
public abstract class AbstractCorefConfiguration {
	protected TLanguage language;
	protected MentionConfiguration mention_config;
	
	public AbstractCorefConfiguration(TLanguage lang){
		language = lang;
	}
	
	public MentionConfiguration getMentionConfig(){
		return mention_config;
	}
	
	public void  loadDefaultMentionDetectors(){
		loadMentionDetectors(true, true, true);
	}
	
	public void  loadMentionDetectors(boolean pronoun, boolean common, boolean proper){
		mention_config = new MentionConfiguration(pronoun, common, proper);
	}
}
