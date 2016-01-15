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
package edu.emory.clir.clearnlp.component.configuration;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface ConfigurationXML
{
	String E_LANGUAGE	= "language";
	String E_READER		= "reader";
	String E_MODEL		= "model";
	String E_COLUMN		= "column";
	String E_BOOTSTRAPS	= "bootstraps";
	
	String A_TYPE		= "type";
	String A_FIELD		= "field";
	String A_INDEX		= "index";
	String A_NAME		= "name";
	
	String FIELD_ID		= "id";
	String FIELD_FORM	= "form";
	String FIELD_LEMMA	= "lemma";
	String FIELD_POS 	= "pos";
	String FIELD_FEATS 	= "feats";
	String FIELD_HEADID	= "headId";
	String FIELD_DEPREL	= "deprel";
	String FIELD_SHEADS	= "sheads";
	String FIELD_XHEADS	= "xheads";
	String FIELD_NAMENT	= "nament";
	String FIELD_COREF	= "coref";
	String FIELD_SEQTAG	= "seqtag";
	
	String V_SUPPORT_VECTOR_MACHINE	= "svm";
	String V_LOGISTIC_REGRESSION	= "lr";
	
//	========================== TRAINER ==========================
	
	String E_TRAINER			= "trainer";
	String A_ALGORITHM			= "algorithm";
	String A_LABEL_CUTOFF		= "labelCutoff";
	String A_FEATURE_CUTOFF		= "featureCutoff";
	String A_NUMBER_OF_THREADS	= "threads";
	String ALG_ADAGRAD			= "adagrad";
	String ALG_LIBLINEAR		= "liblinear";
	String E_THREAD_SIZE  		= "thread_size";

	String E_BEAM_SIZE			= "beam_size";
	String E_KNOWLEDGE_PATH		= "knowledge_path";
}
