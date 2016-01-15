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
package edu.emory.clir.clearnlp.dictionary.universal;

import java.io.InputStream;
import java.util.Set;

import edu.emory.clir.clearnlp.dictionary.AbstractDTTokenizer;
import edu.emory.clir.clearnlp.dictionary.PathTokenizer;
import edu.emory.clir.clearnlp.util.CharUtils;
import edu.emory.clir.clearnlp.util.DSUtils;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.Splitter;
import edu.emory.clir.clearnlp.util.constant.StringConst;


/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DTCurrency extends AbstractDTTokenizer
{
	private Set<String> s_currency;
	private Set<String> s_dollar;
	
	public DTCurrency()
	{
		InputStream currency = IOUtils.getInputStreamsFromClasspath(PathTokenizer.CURRENCY);
		InputStream dollar   = IOUtils.getInputStreamsFromClasspath(PathTokenizer.CURRENCY_DOLLAR);

		init(currency, dollar);
	}
	
	public DTCurrency(InputStream currency, InputStream dollar)
	{
		init(currency, dollar);
	}
	
	public void init(InputStream currency, InputStream dollar)
	{
		s_currency = DSUtils.createStringHashSet(currency, true, true);
		s_dollar   = DSUtils.createStringHashSet(dollar  , true, true);
		
		for (String s : s_dollar)
			s_currency.add(s+StringConst.DOLLAR);
	}
	
	public boolean isCurrencyDollar(String lower)
	{
		return s_dollar.contains(lower);
	}
	
	public boolean isCurrency(String lower)
	{
		return s_currency.contains(lower);
	}
	
	/** @return "US$1" -> ["US$", "1"]. */
	public String[] tokenize(String original, String lower, char[] lcs)
	{
		int i, len = original.length();
		
		for (String currency : s_currency)
		{
			if (lower.startsWith(currency))
			{
				i = currency.length();
				
				if (i < len && CharUtils.isDigit(lcs[i]))
					return Splitter.split(original, i);
			}
			else if (lower.endsWith(currency))
			{
				i = len - currency.length();
				
				if (0 <= i-1 && CharUtils.isDigit(lcs[i-1]))
					return Splitter.split(original, i);
			}
		}
		
		return null;
	}
}