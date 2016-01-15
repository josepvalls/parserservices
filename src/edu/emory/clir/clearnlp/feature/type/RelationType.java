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
package edu.emory.clir.clearnlp.feature.type;

import java.io.Serializable;

/**
 * The Enum DEPRelationType.
 *
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 * @since 3.0.0
 */
public enum RelationType implements Serializable
{
	/** The head. */
	h,
	/** The left-most dependent. */
	lmd,
	/** The right-most dependent. */
	rmd,
	/** The left-nearest dependent. */
	lnd,
	/** The right-nearest dependent. */
	rnd,
	/** The left-nearest sibling. */
	lns,
	/** The right-nearest sibling. */
	rns,
	
	/** The grand head. */
	h2,
	/** The 2nd left-most dependent. */
	lmd2,
	/** The 2nd right-most dependent. */
	rmd2,
	/** The 2nd left-nearest dependent. */
	lnd2,
	/** The 2nd right-nearest dependent. */
	rnd2,
	/** The 2nd left-nearest sibling. */
	lns2,
	/** The 2nd right-nearest sibling. */
	rns2;
}