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
package edu.emory.clir.clearnlp.tokenization;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.emory.clir.clearnlp.dictionary.english.DTAbbreviation;
import edu.emory.clir.clearnlp.dictionary.english.DTHyphen;
import edu.emory.clir.clearnlp.dictionary.universal.DTCompound;
import edu.emory.clir.clearnlp.tokenization.english.ApostropheEnglishTokenizer;
import edu.emory.clir.clearnlp.util.StringUtils;
import edu.emory.clir.clearnlp.util.constant.CharConst;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class EnglishTokenizer extends AbstractTokenizer
{
	private final String[] L_BRACKETS = {"\"","(","{","["};
	private final String[] R_BRACKETS = {"\"",")","}","]"};
	
	private ApostropheEnglishTokenizer d_apostrophe;
	private DTAbbreviation             d_abbreviation;
	private DTCompound                 d_compound;
	private DTHyphen                   d_hyphen;
	
	public EnglishTokenizer()
	{
		d_apostrophe   = new ApostropheEnglishTokenizer();
		d_abbreviation = new DTAbbreviation();
		d_compound     = new DTCompound(TLanguage.ENGLISH);
		d_hyphen       = new DTHyphen();
	}
	
//	----------------------------------- Tokenize -----------------------------------
	
	@Override
	protected int adjustFirstNonSymbolGap(char[] cs, int beginIndex, String t)
	{
		return 0;
	}
	
	@Override
	protected int adjustLastSymbolSequenceGap(char[] cs,  int endIndex, String t)
	{
		char sym = cs[endIndex];
		
		if (sym == CharConst.PERIOD)
		{
			if (d_abbreviation.isAbbreviationEndingWithPeriod(StringUtils.toLowerCase(t)))
				return 1;
		}
		
		return 0;
	}

	@Override
	protected boolean preserveSymbolInBetween(char[] cs, int index)
	{
		return d_hyphen.preserveHyphen(cs, index);
	}
	
	@Override
	protected boolean tokenizeWordsMore(List<String> tokens, String original, String lower, char[] lcs)
	{
		return tokenize(tokens, original, lower, lcs, d_apostrophe) || tokenize(tokens, original, lower, lcs, d_compound); 
	}
	
//	----------------------------------- Segmentize -----------------------------------
	
	@Override
	public List<List<String>> segmentize(InputStream in)
	{
		List<List<String>> sentences = new ArrayList<>();
		int[] brackets = new int[R_BRACKETS.length];
		List<String> tokens = tokenize(in);
		int bIndex, i, size = tokens.size();
		boolean isTerminal = false;
		String token;
		
		for (i=0, bIndex=0; i<size; i++)
		{
			token = tokens.get(i);
			countBrackets(token, brackets);
			
			if (isTerminal || isFinalMarksOnly(token))
			{
				if (i+1 < size && isFollowedByBracket(tokens.get(i+1), brackets))
				{
					isTerminal = true;
					continue;
				}
				
				sentences.add(tokens.subList(bIndex, bIndex = i+1));
				isTerminal = false;
			}
		}
		
		if (bIndex < size)
			sentences.add(tokens.subList(bIndex, size));
		
		return sentences;
	}
		
	/** Called by {@link EnglishSegmenter#getSentencesRaw(BufferedReader)}. */
	private void countBrackets(String str, int[] brackets)
	{
		if (str.equals("\""))
			brackets[0] += (brackets[0] == 0) ? 1 : -1;
		else
		{
			int i, size = brackets.length;
			
			for (i=1; i<size; i++)
			{
				if      (str.equals(L_BRACKETS[i]))
					brackets[i]++;
				else if (str.equals(R_BRACKETS[i]))
					brackets[i]--; 
			}
		}
	}
	
	/** Called by {@link EnglishSegmenter#getSentencesRaw(BufferedReader)}. */
	private boolean isFollowedByBracket(String str, int[] brackets)
	{
		int i, size = R_BRACKETS.length;
		
		for (i=0; i<size; i++)
		{
			if (brackets[i] > 0 && str.equals(R_BRACKETS[i]))
				return true;
		}
		
		return false;
	}
}
