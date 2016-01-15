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
package edu.emory.clir.clearnlp.feature.common;

import java.lang.reflect.Array;

import org.w3c.dom.Element;

import edu.emory.clir.clearnlp.feature.AbstractFeatureTemplate;
import edu.emory.clir.clearnlp.feature.type.FeatureType;
import edu.emory.clir.clearnlp.feature.type.FieldType;
import edu.emory.clir.clearnlp.feature.type.RelationType;
import edu.emory.clir.clearnlp.feature.type.SourceType;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CommonFeatureTemplate extends AbstractFeatureTemplate<CommonFeatureToken>
{
	private static final long serialVersionUID = 4568690939829851919L;

	public CommonFeatureTemplate(Element eFeature)
	{
		super(eFeature);
	}
	
	@Override
	protected CommonFeatureToken[] createFeatureTokens(int size)
	{
		return (CommonFeatureToken[])Array.newInstance(CommonFeatureToken.class, size);
	}

	@Override
	protected CommonFeatureToken createFeatureToken(SourceType source, RelationType relation, String field, int offset)
	{
		return new CommonFeatureToken(source, relation, field, offset);
	}

	@Override
	protected FeatureType initFeatureType()
	{
		FieldType field;
		
		for (CommonFeatureToken token : getFeatureTokens())
		{
			field = token.getField();
			
			if (FieldType.isSetField(field))
				return FeatureType.SET;
			else if (FieldType.isBooleanField(field))
				return FeatureType.BINARY;
		}
		
		return FeatureType.SIMPLE;
	}
}
