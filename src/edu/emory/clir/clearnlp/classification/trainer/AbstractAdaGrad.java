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
package edu.emory.clir.clearnlp.classification.trainer;

import edu.emory.clir.clearnlp.classification.model.SparseModel;
import edu.emory.clir.clearnlp.classification.model.StringModel;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
abstract public class AbstractAdaGrad extends AbstractOnlineTrainer
{
	protected double[] d_gradients;
	protected double   d_alpha;
	protected double   d_rho;
	protected double   d_bias;

	/**
	 * @param alpha the learning rate.
	 * @param rho the smoothing denominator. 
	 */
	public AbstractAdaGrad(SparseModel model, boolean average, double alpha, double rho, double bias)
	{
		super(model, average);
		init(alpha, rho, bias);
	}
	
	/**
	 * @param alpha the learning rate.
	 * @param rho the smoothing denominator. 
	 */
	public AbstractAdaGrad(StringModel model, int labelCutoff, int featureCutoff, boolean average, double alpha, double rho, double bias)
	{
		super(model, labelCutoff, featureCutoff, average);
		init(alpha, rho, bias);
	}
	
	private void init(double alpha, double rho, double bias)
	{
		d_gradients = new double[w_vector.size()];
		d_alpha     = alpha;
		d_rho       = rho;
		d_bias      = bias;
	}
	
	protected void updateWeight(int weightIndex, double v, int averageCount)
	{
		double cost = getCost(weightIndex) * v;
		w_vector.add(weightIndex, (float)cost);
		if (average()) d_average[weightIndex] += cost * averageCount;
	}
	
	private double getCost(int weightIndex)
	{
		return d_alpha / (d_rho + Math.sqrt(d_gradients[weightIndex]));
	}

	protected String getTrainerInfo(String type)
	{
		return String.format("AdaGrad-%s: alpha = %4.3f, rho = %4.3f, rho = %4.3f, average = %b", type, d_alpha, d_rho, d_bias, average());
	}
}