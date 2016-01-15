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
package edu.emory.clir.clearnlp.component.mode.dep;

import java.io.InputStream;

import org.w3c.dom.Element;

import edu.emory.clir.clearnlp.component.configuration.AbstractConfiguration;
import edu.emory.clir.clearnlp.component.utils.NLPMode;
import edu.emory.clir.clearnlp.util.XmlUtils;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DEPConfiguration extends AbstractConfiguration
{
	private boolean eval_punct;
	private String root_label;
	private int beam_size;
	
//	============================== Initialization ==============================
	
	public DEPConfiguration(String rootLabel)
	{
		super(NLPMode.dep);
		setRootLabel(rootLabel);
	}
	
	public DEPConfiguration(InputStream in)
	{
		super(NLPMode.dep, in);
		init();
	}
	
	private void init()
	{
		Element eMode = getModeElement();

		boolean evalPunct = XmlUtils.getBooleanTextContent(XmlUtils.getFirstElementByTagName(eMode, "evaluate_punctuation"));
		String rootLabel  = XmlUtils.getTrimmedTextContent(XmlUtils.getFirstElementByTagName(eMode, "root_label"));
		int beamSize = XmlUtils.getIntegerTextContent(XmlUtils.getFirstElementByTagName(eMode, "beam_size"));
		
		setEvaluatePunctuation(evalPunct);
		setRootLabel(rootLabel);
		setBeamSize(beamSize);
	}
	
	public int getBeamSize()
	{
		return beam_size;
	}
	
	public void setBeamSize(int size)
	{
		beam_size = size;
	}
	
	public String getRootLabel()
	{
		return root_label;
	}
	
	public void setRootLabel(String label)
	{
		root_label = label;
	}
	
	public void setEvaluatePunctuation(boolean eval)
	{
		eval_punct = eval;
	}
	
	public boolean evaluatePunctuation()
	{
		return eval_punct;
	}
}
