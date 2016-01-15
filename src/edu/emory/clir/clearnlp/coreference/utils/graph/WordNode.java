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
package edu.emory.clir.clearnlp.coreference.utils.graph;


/**
 * @author 	Yu-Hsin(Henry) Chen ({@code yu-hsin.chen@emory.edu})
 * @version	1.0
 * @since 	May 18, 2015
 */
public class WordNode {
	private int id;
	private String word;

	public WordNode(String s){
		id = -1;
		word = s;
	}
	
	public WordNode(int i, String s){
		id = i;
		word = s;
	}
	
	public WordNode(WordNode node){
		id = -1;
		word = node.getWord();
	}
	
	public WordNode(int i, WordNode node){
		id = i;
		word = node.getWord();
	}
	
	public int getID(){
		return id;
	}
	
	public String getWord(){
		return word;
	}
	
	public void setWord(String s){
		word = s;
	}
	
	@Override
	public String toString(){
		return id + ": " + word;
	}
}
