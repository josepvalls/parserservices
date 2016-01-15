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
package edu.emory.clir.clearnlp.experiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.IOUtils;

public class HeaderReplacer
{
	public HeaderReplacer(String headerFile, String inputPath) throws IOException
	{
		String header = getHeader(headerFile);
		
		for (String inputFile : FileUtils.getFileList(inputPath, "java", true))
		{
			System.out.println(inputFile);
			replace(inputFile, getReplacement(inputFile, header));
		}
	}
	
	public void replace(String outputFile, String replacement) throws IOException
	{
		PrintStream stream = IOUtils.createBufferedPrintStream(outputFile);
		stream.print(replacement);
		stream.close();
	}
	
	public String getReplacement(String inputFile, String header) throws IOException
	{
		BufferedReader reader = IOUtils.createBufferedReader(inputFile);
		StringBuilder build = new StringBuilder();
		boolean add = false;
		String line;
		
		build.append(header);
		
		while ((line = reader.readLine()) != null)
		{
			if (!add && line.startsWith("package"))
				add = true;
			
			if (add)
			{
				build.append(line);
				build.append("\n");
			}
		}
		
		reader.close();
		return build.toString().trim();
	}
	
	public String getHeader(String headerFile) throws IOException
	{
		BufferedReader reader = IOUtils.createBufferedReader(headerFile);
		StringBuilder build = new StringBuilder();
		String line;
		
		build.append("/**\n");
		
		while ((line = reader.readLine()) != null)
		{
			build.append(" * ");
			build.append(line);
			build.append("\n");
		}
		
		build.append(" */\n");
		return build.toString();
	}
	
	static public void main(String[] args)
	{
		try
		{
			new HeaderReplacer(args[0], args[1]);
		}
		catch (IOException e) {e.printStackTrace();}
	}
}