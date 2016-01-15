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
package edu.emory.clir.clearnlp.component.trainer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import edu.emory.clir.clearnlp.bin.helper.AbstractNLPTrain;
import edu.emory.clir.clearnlp.classification.model.StringModel;
import edu.emory.clir.clearnlp.classification.trainer.AbstractTrainer;
import edu.emory.clir.clearnlp.collection.list.FloatArrayList;
import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;
import edu.emory.clir.clearnlp.component.AbstractStatisticalComponent;
import edu.emory.clir.clearnlp.component.configuration.AbstractConfiguration;
import edu.emory.clir.clearnlp.component.evaluation.AbstractEval;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.clearnlp.util.BinUtils;
import edu.emory.clir.clearnlp.util.IOUtils;

/**
 * @since 3.0.0
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractNLPTrainer
{
	protected AbstractConfiguration t_configuration;
	
//	====================================== CONSTRUCTORS ======================================
	
	public AbstractNLPTrainer(InputStream configuration)
	{	
		t_configuration = createConfiguration(configuration);
	}
	
	public ObjectDoublePair<AbstractStatisticalComponent<?,?,?,?,?>> train(List<String> trainFiles, List<String> developFiles)
	{
		Object lexicons = getLexicons(trainFiles);
		ObjectDoublePair<AbstractStatisticalComponent<?,?,?,?,?>> prev = train(trainFiles, developFiles, lexicons, null, 0);
		if (!t_configuration.isBootstrap() || AbstractNLPTrain.d_stop > 0) return prev;
		ObjectDoublePair<AbstractStatisticalComponent<?,?,?,?,?>> curr;
		byte[] backup;
		int boot = 1;
		
//		prev.o.getModels()
		
		try
		{
			while (true)
			{
				// save the previous model
//				backup = prev.o.toByteArray();
				backup = prev.o.modelsToByteArray();
				curr = train(trainFiles, developFiles, lexicons, prev.o.getModels(), boot++);
				
				if (prev.d >= curr.d)
				{
//					prev.o = createComponentForDecode(backup);
					prev.o.byteArrayToModels(backup);
					return prev;
				}
				
				prev = curr;
			}
		}
		catch (Exception e) {e.printStackTrace();}
		throw new IllegalStateException();
	}
	
	private Object getLexicons(List<String> trainFiles)
	{
		AbstractStatisticalComponent<?,?,?,?,?> component = createComponentForCollect();
		Object lexicons = null;
		
		if (component != null)
		{
			BinUtils.LOG.info("Collecting lexicons:\n");
			process(component, trainFiles, true);
			lexicons = component.getLexicons();
		}
		
		return lexicons;
	}
	
	private ObjectDoublePair<AbstractStatisticalComponent<?,?,?,?,?>> train(List<String> trainFiles, List<String> developFiles, Object lexicons, StringModel[] models, int boot)
	{
		// train
		AbstractStatisticalComponent<?,?,?,?,?> component = (models == null) ? createComponentForTrain(lexicons) : createComponentForBootstrap(lexicons, models);
		BinUtils.LOG.info("Generating training instances: "+boot+"\n");
		process(component, trainFiles, true);
		
		// evaluate
		AbstractTrainer[] trainers = t_configuration.getTrainers(component.getModels());
		component = createComponentForEvaluate(lexicons, component.getModels());
		double score = trainPipeline(component, trainers, developFiles);
		
		return new ObjectDoublePair<AbstractStatisticalComponent<?,?,?,?,?>>(component, score); 
	}
	
	/** Initializes the training configuration. */
	protected abstract AbstractConfiguration createConfiguration(InputStream in);
	
	/** Creates an NLP component for collecting lexicons. */
	protected abstract AbstractStatisticalComponent<?,?,?,?,?> createComponentForCollect();
	
	/** Creates an NLP component for training. */
	protected abstract AbstractStatisticalComponent<?,?,?,?,?> createComponentForTrain(Object lexicons);
	
	/** Creates an NLP component for bootstrap. */
	protected abstract AbstractStatisticalComponent<?,?,?,?,?> createComponentForBootstrap(Object lexicons, StringModel[] models);
	
	/** Creates an NLP component for evaluation. */
	protected abstract AbstractStatisticalComponent<?,?,?,?,?> createComponentForEvaluate(Object lexicons, StringModel[] models);
	
	/** Creates an NLP component for decode. */
	protected abstract AbstractStatisticalComponent<?,?,?,?,?> createComponentForDecode(byte[] models);
	
	private double trainPipeline(AbstractStatisticalComponent<?,?,?,?,?> component, AbstractTrainer[] trainers, List<String> developFiles)
	{
		AbstractTrainer trainer;
		double score = 0;
		
		for (int i=0; i<trainers.length; i++)
		{
			trainer = trainers[i];
			BinUtils.LOG.info(trainer.trainerInfoFull()+"\n");
		}
		
		switch (trainers[0].getTrainerType())
		{
		case ONLINE    : score = trainOnline  (component, trainers, developFiles); break;
		case ONE_VS_ALL: score = trainOneVsAll(component, trainers, developFiles); break;
		}
		
		BinUtils.LOG.info("\n");
		return score;
	}
	
	private double trainOnline(AbstractStatisticalComponent<?,?,?,?,?> component, AbstractTrainer[] trainers, List<String> developFiles)
	{
		int i, count, iter = -1, size = trainers.length;
		
		FloatArrayList[] weights = new FloatArrayList[size];
		StringModel[] models = component.getModels();
		AbstractEval<?> eval = component.getEval();
		boolean[] train = new boolean[size];
		double currScore, prevScore = 0;
		
		Arrays.fill(train, true);
		
		do
		{
			count = 0;
			iter++;
			
			for (i=0; i<size; i++)
			{
				if (train[i])
				{
					trainers[i].train();
					eval.clear();
					process(component, developFiles, false);
					currScore = eval.getScore();
					BinUtils.LOG.info(String.format("%3d:%3d: %s\n", iter, i, eval.toString()));
					
					if (prevScore < currScore)
					{
						count++;
						prevScore  = currScore;
						weights[i] = models[i].getWeightVector().cloneWeights();
					}
					else
					{
						train[i] = false;
						models[i].getWeightVector().setWeights(weights[i]);
					}
				}
			}
		}
		while (count > 0);
		
		return prevScore;
	}
	
	private double trainOneVsAll(AbstractStatisticalComponent<?,?,?,?,?> component, AbstractTrainer[] trainers, List<String> developFiles)
	{
//		AbstractEval<?> eval = component.getEval();
//		trainer.train();
//		process(component, developFiles, false);
//		double currScore = eval.getScore();
//		BinUtils.LOG.info(eval.toString());
//		return currScore;
		return 0;
	}
	
//	private double trainPipeline(AbstractStatisticalComponent<?,?,?,?,?> component, AbstractTrainer[] trainers, List<String> developFiles)
//	{
//		AbstractTrainer trainer;
//		double score = 0;
//		
//		try
//		{
//			for (int i=0; i<trainers.length; i++)
//			{
//				trainer = trainers[i];
//				BinUtils.LOG.info(trainer.trainerInfoFull()+"\n");
//				
//				switch (trainer.getTrainerType())
//				{
//				case ONLINE    : score = trainOnline  (component, (AbstractOnlineTrainer)  trainer, developFiles, i); break;
//				case ONE_VS_ALL: score = trainOneVsAll(component, (AbstractOneVsAllTrainer)trainer, developFiles);    break;
//				}
//			}
//		}
//		catch (Exception e) {e.printStackTrace();}
//		
//		BinUtils.LOG.info("\n");
//		return score;
//	}
//	
//	private double trainOnline(AbstractStatisticalComponent<?,?,?,?,?> component, AbstractOnlineTrainer trainer, List<String> developFiles, int modelID) throws Exception
//	{
//		StringModel model = component.getModel(modelID);
//		AbstractEval<?> eval = component.getEval();
//		double currScore, prevScore = 0;
//		byte[] prevWeights = null;
//		
//		for (int iter=1; ; iter++)
//		{
//			trainer.train();
//			eval.clear();
//			process(component, developFiles, false);
//			currScore = eval.getScore();
//			BinUtils.LOG.info(String.format("%3d: %s\n", iter, eval.toString()));
//			
//			if (0 < AbstractNLPTrain.d_stop && AbstractNLPTrain.d_stop <= currScore)
//				break;
//			
//			if (prevScore < currScore)
//			{
//				prevScore = currScore;
//				prevWeights = model.saveWeightVectorToByteArray();
//			}
//			else
//			{
//				model.loadWeightVectorFromByteArray(prevWeights);
//				break;
//			}			
//		}
//		
//		return prevScore;
//	}
	
	public void process(AbstractStatisticalComponent<?,?,?,?,?> component, List<String> filelist, boolean log)
	{
		for (String filename : filelist)
		{
			process(component, filename);
			if (log) BinUtils.LOG.info(".");
		}
		
		if (log) BinUtils.LOG.info("\n\n");
	}
	
	public void process(AbstractStatisticalComponent<?,?,?,?,?> component, String filename)
	{
		TSVReader reader = (TSVReader)t_configuration.getReader();
		reader.open(IOUtils.createFileInputStream(filename));
		DEPTree tree;
		
		while ((tree = reader.next()) != null)
			component.process(tree);

		reader.close();
	}
}
