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
package edu.emory.clir.clearnlp.conversion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.clir.clearnlp.constituent.CTNode;
import edu.emory.clir.clearnlp.constituent.CTTagEn;
import edu.emory.clir.clearnlp.constituent.CTTree;
import edu.emory.clir.clearnlp.conversion.headrule.HeadRule;
import edu.emory.clir.clearnlp.conversion.headrule.HeadRuleMap;
import edu.emory.clir.clearnlp.conversion.headrule.HeadTagSet;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.util.PatternUtils;

/**
 * Abstract constituent to dependency converter.
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
abstract public class AbstractC2DConverter
{
	protected HeadRuleMap m_headrules;
	protected HeadRule    r_default;
	
	public AbstractC2DConverter(HeadRuleMap headrules, HeadRule defaultRule)
	{
		m_headrules = headrules;
		r_default   = defaultRule;
	}
	
	/**
	 * Sets the head of the specific node and all its sub-nodes.
	 * Calls {@link AbstractC2DConverter#findHeads(CTNode)}.
	 */
	protected void setHeads(CTNode curr)
	{
		// terminal nodes become the heads of themselves
		if (curr.isTerminal())
		{
			curr.setC2DInfo(new C2DInfo(curr));
			return;
		}
		
		// set the heads of all children
		for (CTNode child : curr.getChildrenList())
			setHeads(child);
		
		// stop traversing if it is the top node
		if (curr.isConstituentTag(CTTagEn.TOP))
			return;
		
		// only one child
		if (curr.getChildrenSize() == 1)
		{
			curr.setC2DInfo(new C2DInfo(curr.getChild(0)));
			return;
		}
		
		// find the headrule of the current node
		HeadRule rule = m_headrules.get(curr.getConstituentTag());
				
		if (rule == null)
		{
			System.err.println("Error: headrules not found for \""+curr.getConstituentTag()+"\"");
			rule = r_default;
		}
		
		// abstract method
		setHeadsAux(rule, curr);
	}
	
	/**
	 * @return the head of the specific node list according to the specific headrule.
	 * Every other node in the list becomes the dependent of the head node.
	 * @param rule the headrule to be consulted.
	 * @param nodes the list of nodes.
	 * @param flagSize the number of head flags.
	 */
	protected CTNode getHead(HeadRule rule, List<CTNode> nodes, int flagSize)
	{
		CTNode head = getDefaultHead(nodes);
		
		if (head == null)
		{
			nodes = new ArrayList<>(nodes);
			if (rule.isRightToLeft()) Collections.reverse(nodes);
			
			int i, size = nodes.size(), flag;
			int[] flags = new int[size];
			CTNode child;
			
			for (i=0; i<size; i++)
				flags[i] = getHeadFlag(nodes.get(i));
			
			outer: for (flag=0; flag<flagSize; flag++)
			{
				for (HeadTagSet tagset : rule.getHeadTags())
				{
					for (i=0; i<size; i++)
					{
						child = nodes.get(i);
						
						if (flags[i] == flag && tagset.matches(child))
						{
							head = child;
							break outer;
						}
					}
				}
			}
			
			outer: for (flag=0; flag<flagSize; flag++)
			{
				for (HeadTagSet tagset : rule.getHeadTags())
				{
					for (i=0; i<size; i++)
					{
						child = nodes.get(i);
						
						if (flags[i] == flag && tagset.matches(child))
						{
							head = child;
							break outer;
						}
					}
				}
			}
		}
		
		if (head == null)
			throw new IllegalStateException("Head not found");
		
		CTNode parent = head.getParent();
		
		for (CTNode node : nodes)
		{
			if (node != head && !node.getC2DInfo().hasHead())
				node.getC2DInfo().setHead(head, getDEPLabel(node, parent, head));
		}
		
		return head;
	}
	
	private CTNode getDefaultHead(List<CTNode> nodes)
	{
		CTNode head = null;
		
		for (CTNode node : nodes)
		{
			if (!node.isEmptyCategoryTerminal())
			{
				if (head != null) return null;
				head = node;
			}
		}

		return head;
	}
	
	/** @return the dependency tree converted from the specific constituent tree without head information. */
	protected DEPTree initDEPTree(CTTree cTree)
	{
		List<CTNode>  cNodes = cTree.getTokenList();
		List<DEPNode> dNodes = new ArrayList<>();
		String form, pos;
		DEPNode dNode;
		int id;
		
		for (CTNode cNode : cNodes)
		{
			id   = cNode.getTokenID() + 1;
			form = PatternUtils.revertBrackets(cNode.getWordForm());
			pos  = cNode.getConstituentTag();
			
			dNode = new DEPNode(id, form, pos, cNode.getC2DInfo().getDEPFeat());
			dNode.initSecondaryHeads();
			dNodes.add(dNode);
		}
		
		return new DEPTree(dNodes);
	}
	
	/**
	 * Sets the head of the specific constituent node using the specific headrule.
	 * Called by {@link #setHeads(CTNode)}.
	 */
	abstract protected void setHeadsAux(HeadRule rule, CTNode curr);
	
	/**
	 * @return the head flag of the specific constituent node.
	 * @see EnglishC2DConverter#getHeadFlag(CTNode).
	 */
	abstract protected int getHeadFlag(CTNode child);
	
	/**
	 * Returns a dependency label given the specific phrase structure.
	 * @param C the current node.
	 * @param P the parent of {@code C}.
	 * @param p the head of {@code P}.
	 * @return a dependency label given the specific phrase structure.
	 */
	abstract protected String getDEPLabel(CTNode C, CTNode P, CTNode p);
	
	/**
	 * Returns the dependency tree converted from the specific constituent tree.
	 * If the constituent tree contains only empty categories, returns {@code null}.
	 * @param cTree the constituent tree to convert.
	 * @return the dependency tree converted from the specific constituent tree.
	 */
	abstract public DEPTree toDEPTree(CTTree cTree);
}