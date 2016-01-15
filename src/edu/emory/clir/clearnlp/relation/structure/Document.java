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
package edu.emory.clir.clearnlp.relation.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.relation.chunk.AbstractChucker;

/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	Jul 2, 2015
 */
public class Document implements Serializable, Iterable<Entity>{
	private static final long serialVersionUID = 8332364748967299712L;
	
	private String s_title;
	private DEPTree t_title;
	private double w_confidence;
	private List<DEPTree> l_trees;
	private List<Entity> l_entities;
	private List<Entity> l_mainEntities;
	private List<Entity> l_nonMainEntities;
	
	public Document(String title){
		s_title = title;
		l_trees = new ArrayList<>();
	}
	
	public Document(String title, DEPTree titleTree){
		s_title = title;
		t_title = titleTree;
		l_trees = new ArrayList<>();
	}
	
	public Document(String title, List<DEPTree> trees){
		s_title = title;
		l_trees = trees;
	}
	
	public Document(String title, DEPTree titleTree, List<DEPTree> trees){
		s_title = title;
		t_title = titleTree;
		l_trees = trees;
	}
	
	public Document(Document document){
		s_title = document.getTitle();
		t_title = document.getTitleTree();
		w_confidence = document.getConfidence();
		l_trees =  new ArrayList<>(document.getTrees());
		l_entities =  new ArrayList<>(document.getEntities());
		l_mainEntities =  new ArrayList<>(document.getMainEntities());
		l_nonMainEntities =  new ArrayList<>(document.getNonMainEntities());
	}
	
	public String getTitle(){
		return s_title;
	}
	
	public DEPTree getTitleTree(){
		return t_title;
	}
	
	public double getConfidence(){
		return w_confidence;
	}
	
	public DEPTree getTree(int tree_id){
		return (tree_id < l_trees.size())? l_trees.get(tree_id) : null;
	}
	
	public List<DEPTree> getTrees(){
		return l_trees;
	}
	
	public int getTreeCount(){
		return l_trees.size();
	}
	
	public List<Entity> getEntities(){
		return l_entities;
	}
	
	public List<Entity> getEntities(AbstractChucker chunker){
		return extractEntities(chunker);
	}
	
	public Entity getMostFrequentEntity(){
		return Collections.max(l_entities);
	}

	public List<Entity> getMainEntities(){
		return l_mainEntities;
	}
	
	public List<Entity> getNonMainEntities(){
		return l_nonMainEntities;
	}
	
	public void setTitleTree(DEPTree tree){
		t_title = tree;
	}
	
	public void addTree(DEPTree tree){
		l_trees.add(tree);
	}
	
	public void addTrees(List<DEPTree> trees){
		l_trees.addAll(trees);
	}
	
	public void setConfidence(double confidence){
		w_confidence = confidence;
	}
	
	public void setMainEnities(List<Entity> entities){
		l_mainEntities = entities;
	}
	
	public void setNonMainEnities(List<Entity> entities){
		l_nonMainEntities = entities;
	}
	
	private List<Entity> extractEntities(AbstractChucker chunker){
		List<Chunk> chunks;
		int i, size = l_trees.size();
		List<Entity> list = new ArrayList<>();
		
		boolean hasAlias;
		for(i = 0; i < size; i++){
			chunks = chunker.getChunk(l_trees.get(i));
			
			for(Chunk chunk : chunks){
				hasAlias = false;
				for(Entity entity : list){
					if(entity.addAlias(i, chunk)){
						hasAlias = true; break;
					}
				}
				if(!hasAlias)	list.add(new Entity(i, chunk.getHeadNode(), chunk.getChunkNodes(), chunk.getTag()));
			}
		}
		Collections.sort(list, Collections.reverseOrder());
		
		l_entities = list;
		return l_entities;
	}

	@Override
	public Iterator<Entity> iterator() {
		return l_entities.iterator();
	}
}
