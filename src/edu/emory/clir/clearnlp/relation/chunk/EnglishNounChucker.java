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
package edu.emory.clir.clearnlp.relation.chunk;

import java.util.function.Function;
import java.util.function.Predicate;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.pos.POSLibEn;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	Jul 7, 2015
 */
public class EnglishNounChucker extends AbstractChucker{
	
	public EnglishNounChucker(){
		super(TLanguage.ENGLISH);
	}

	@Override
	protected Function<DEPNode, String> getLabelFunction() {
		return new Function<DEPNode, String>(){

			@Override
			public String apply(DEPNode t) {
				return t.getPOSTag();
			}
		};
	}

	@Override
	protected Predicate<DEPNode> getChunkingNodePredicate() {
		return new Predicate<DEPNode>(){

			@Override
			public boolean test(DEPNode t) {
				return t.getPOSTag().startsWith(POSLibEn.POS_NN);
			}
		};
	}
}
