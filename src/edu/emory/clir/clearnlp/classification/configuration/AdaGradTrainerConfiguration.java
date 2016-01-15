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
package edu.emory.clir.clearnlp.classification.configuration;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class AdaGradTrainerConfiguration extends DefaultTrainerConfiguration
{
	private double  d_alpha;
	private double  d_rho;
	private double  d_bias;
	private boolean b_average;
	
	public AdaGradTrainerConfiguration(byte vectorType, boolean binary, int labelCutoff, int featureCutoff, int numberOfThreads, boolean average, double alpha, double rho, double bias)
	{
		super(vectorType, binary, labelCutoff, featureCutoff, numberOfThreads);
		setAverage(average);
		setLearningRate(alpha);
		setRidge(rho);
	}
	
	public boolean isAverage()
	{
		return b_average;
	}

	public double getLearningRate()
	{
		return d_alpha;
	}
	
	public double getRidge()
	{
		return d_rho;
	}
	
	public double getBias()
	{
		return d_bias;
	}
	
	public void setAverage(boolean average)
	{
		b_average = average;
	}
	
	public void setLearningRate(double alpha)
	{
		d_alpha = alpha;
	}

	public void setRidge(double rho)
	{
		d_rho = rho;
	}
	
	public void setBias(double bias)
	{
		d_bias = bias;
	}
}