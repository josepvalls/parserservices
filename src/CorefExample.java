import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;



import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.dcoref.Mention;
import edu.stanford.nlp.ie.machinereading.structure.EntityMention;
import edu.stanford.nlp.ie.machinereading.structure.ExtractionObject;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ie.machinereading.structure.RelationMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.time.TimeAnnotations.TimexAnnotation;
import edu.stanford.nlp.time.Timex;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

public class CorefExample {

  public static void main(String[] args) throws Exception {

  Annotation document = new Annotation("Barack Obama was born in Hawaii. He is the president. Obama was elected in 2008.");

  Properties props = new Properties();
  props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
  StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
  pipeline.annotate(document);
  System.out.println("---");
  System.out.println("coref chains");
  for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values())          {     
     System.out.println("\t"+cc);
  }
  for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class))
  {
    System.out.println("---");
    System.out.println("mentions");
    Map<Integer, CorefChain> corefChains  = sentence.get(CorefCoreAnnotations.CorefChainAnnotation.class);
    if (corefChains != null) {
    	for(CorefChain m : corefChains.values()){
    		System.out.println("\t"+m);
    	}
    }
    /*for (Mention m : sentence.get(CorefCoreAnnotations.CorefChainAnnotation.class)) {
      System.out.println("\t"+m);
     }*/
   }
  }
}