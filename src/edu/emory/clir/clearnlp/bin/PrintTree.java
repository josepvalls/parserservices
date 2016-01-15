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
package edu.emory.clir.clearnlp.bin;

import edu.emory.clir.clearnlp.constituent.CTReader;
import edu.emory.clir.clearnlp.constituent.CTTree;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.constant.StringConst;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class PrintTree
{
	static public void main(String[] args)
	{
		String treeDir  = args[0];
		String treeFile = args[1];
		int    treeId   = Integer.parseInt(args[2]);
		
		CTReader reader = new CTReader(IOUtils.createFileInputStream(treeDir+StringConst.FW_SLASH+treeFile));
		CTTree tree = reader.nextTree(treeId);
		reader.close();
		
//		System.out.println(tree.toString());
		System.out.println(tree.toString(true,true,StringConst.NEW_LINE));
	}
}