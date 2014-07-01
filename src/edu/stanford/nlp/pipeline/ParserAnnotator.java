package edu.stanford.nlp.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CyclicCoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.parser.lexparser.ParserConstraint;
import edu.stanford.nlp.parser.lexparser.ParserAnnotations.ConstraintAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.ReflectionLoading;
import edu.stanford.nlp.util.StringUtils;

/**
 * This class will add parse information to an Annotation.
 * It assumes that the Annotation already contains the tokenized words
 * as a List&lt;CoreLabel&gt; in the TokensAnnotation under each
 * particular CoreMap in the SentencesAnnotation.
 * If the words have POS tags, they will be used.
 *
 * If the input does not already have sentences, it adds parse information
 * to the Annotation under the key
 * {@code DeprecatedAnnotations.ParsePLAnnotation.class} as a {@code List<Tree>}.
 * Otherwise, they are added to each sentence's CoreMap (get with
 * {@code CoreAnnotations.SentencesAnnotation}) under
 * {@code CoreAnnotations.TreeAnnotation}).
 *
 * @author Jenny Finkel
 */
public class ParserAnnotator implements Annotator {

  private final boolean VERBOSE;
  private final boolean BUILD_GRAPHS;
  private final LexicalizedParser parser;

  private final Function<Tree, Tree> treeMap;

  /** Do not parse sentences larger than this sentence length */
  private final int maxSentenceLength;

  /** 
   * Stop parsing if we exceed this time limit, in milliseconds. 
   * Use 0 for no limit.
   */
  private final long maxParseTime;

  public static final String[] DEFAULT_FLAGS = { "-retainTmpSubcategories" };

  public ParserAnnotator(boolean verbose, int maxSent) {
    this(System.getProperty("parse.model", LexicalizedParser.DEFAULT_PARSER_LOC), verbose, maxSent, DEFAULT_FLAGS);
  }

  public ParserAnnotator(String parserLoc,
                         boolean verbose,
                         int maxSent,
                         String[] flags) {
    this(loadModel(parserLoc, verbose, flags), verbose, maxSent);
  }

  public ParserAnnotator(LexicalizedParser parser, boolean verbose, int maxSent) {
    this(parser, verbose, maxSent, null);
  }

  public ParserAnnotator(LexicalizedParser parser, boolean verbose, int maxSent, Function<Tree, Tree> treeMap) {
    VERBOSE = verbose;
    this.BUILD_GRAPHS = true;
    this.parser = parser;
    this.maxSentenceLength = maxSent;
    this.treeMap = treeMap;
    this.maxParseTime = 0;
  }


  public ParserAnnotator(String annotatorName, Properties props) {
    String model = props.getProperty(annotatorName + ".model", LexicalizedParser.DEFAULT_PARSER_LOC);
    if (model == null) {
      throw new IllegalArgumentException("No model specified for " +
                                         "Parser annotator " +
                                         annotatorName);
    }
    this.VERBOSE =
      PropertiesUtils.getBool(props, annotatorName + ".debug", false);

    String[] flags =
      convertFlagsToArray(props.getProperty(annotatorName + ".flags"));
    this.parser = loadModel(model, VERBOSE, flags);
    this.maxSentenceLength =
      PropertiesUtils.getInt(props, annotatorName + ".maxlen", -1);

    String treeMapClass = props.getProperty(annotatorName + ".treemap");
    if (treeMapClass == null) {
      this.treeMap = null;
    } else {
      this.treeMap = ReflectionLoading.loadByReflection(treeMapClass, props);
    }

    this.maxParseTime = 
      PropertiesUtils.getLong(props, annotatorName + ".maxtime", 0);

    this.BUILD_GRAPHS = PropertiesUtils.getBool(props, annotatorName + ".buildgraphs", true);
  }

  public static String signature(String annotatorName, Properties props) {
    StringBuilder os = new StringBuilder();
    os.append(annotatorName + ".model:" +
            props.getProperty(annotatorName + ".model",
                    LexicalizedParser.DEFAULT_PARSER_LOC));
    os.append(annotatorName + ".debug:" +
            props.getProperty(annotatorName + ".debug", "false"));
    os.append(annotatorName + ".flags:" +
            props.getProperty(annotatorName + ".flags", ""));
    os.append(annotatorName + ".maxlen:" +
            props.getProperty(annotatorName + ".maxlen", "-1"));
    os.append(annotatorName + ".treemap:" +
            props.getProperty(annotatorName + ".treemap", ""));
    os.append(annotatorName + ".maxtime:" +
            props.getProperty(annotatorName + ".maxtime", "0"));
    os.append(annotatorName + ".buildgraphs:" +
            props.getProperty(annotatorName + ".buildgraphs", "true"));
    return os.toString();
  }

  public static String[] convertFlagsToArray(String parserFlags) {
    if (parserFlags == null) {
      return DEFAULT_FLAGS;
    } else if (parserFlags.trim().equals("")) {
      return StringUtils.EMPTY_STRING_ARRAY;
    } else {
      return parserFlags.trim().split("\\s+");
    }
  }

  private static LexicalizedParser loadModel(String parserLoc,
                                                    boolean verbose,
                                                    String[] flags) {
    if (verbose) {
      System.err.println("Loading Parser Model [" + parserLoc + "] ...");
    }
    LexicalizedParser result = LexicalizedParser.loadModel(parserLoc, flags);
    // lp.setOptionFlags(new String[]{"-outputFormat", "penn,typedDependenciesCollapsed", "-retainTmpSubcategories"});
    // treePrint = lp.getTreePrint();

    return result;
  }

  @Override
  public void annotate(Annotation annotation) {
    if (annotation.containsKey(CoreAnnotations.SentencesAnnotation.class)) {
      // parse a tree for each sentence
      for (CoreMap sentence: annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
        final List<CoreLabel> words = sentence.get(CoreAnnotations.TokensAnnotation.class);
        if (VERBOSE) {
          System.err.println("Parsing: " + words);
        }
        Tree tree = null;
        // generate the constituent tree
        if (maxSentenceLength <= 0 || words.size() < maxSentenceLength) {
          final List<ParserConstraint> constraints = sentence.get(ConstraintAnnotation.class);
          // If there is a max time specified, we parse in a separate
          // thread and interrupt that thread if it takes too long.
          if (maxParseTime > 0) {
            final Tree[] treeMem = { null };
            Thread thread = new Thread(new Runnable() {
                public void run() {
                  treeMem[0] = process(constraints, words);
                }
              });
            try {
              thread.start();
              thread.join(maxParseTime);
              tree = treeMem[0];
              if (thread.isAlive()) {
                // We've already waited as long as we're willing to wait
                thread.stop();
                if (VERBOSE) {
                  System.err.println("WARNING: " + 
                                     "The parser took too long to parse: " +
                                     Sentence.listToString(words));
                }
              }
            } catch (InterruptedException e) {
              if (VERBOSE) {
                System.err.println("WARNING: Parsing of sentence failed: " +
                                   Sentence.listToString(words));
                e.printStackTrace();
              }
            }
          } else {
            tree = process(constraints, words);
          }
        }
        // tree == null may happen if the parser takes too long or if
        // the sentence is longer than the max length
        if (tree == null) {
          tree = ParserAnnotatorUtils.xTree(words);
        }

        if (treeMap != null) {
          tree = treeMap.apply(tree);
        }

        ParserAnnotatorUtils.fillInParseAnnotations(VERBOSE, BUILD_GRAPHS, sentence, tree);
      }
    } else {
      throw new RuntimeException("unable to find sentences in: " + annotation);
    }
  }

  private Tree process(List<ParserConstraint> constraints, 
                       List<CoreLabel> words) {
    LexicalizedParserQuery pq = parser.parserQuery();
    pq.setConstraints(constraints);
    pq.parse(words);
    Tree tree = null;
    try {
      tree = pq.getBestParse();
      // -10000 denotes unknown words
      tree.setScore(pq.getPCFGScore() % -10000.0);
    } catch (OutOfMemoryError e) {
      System.err.println("WARNING: Parsing of sentence ran out of memory.  " +
                         "Will ignore and continue: " +
                         Sentence.listToString(words));
    }
    return tree;
  }

  @SuppressWarnings("unused")
  private Tree doOneSentence(List<? extends CoreLabel> words) {
    // convert to CyclicCoreLabels because the parser hates CoreLabels
    List<CyclicCoreLabel> newWords = new ArrayList<CyclicCoreLabel>();
    for (CoreLabel fl : words) {
      CyclicCoreLabel ml = new CyclicCoreLabel();
      ml.setWord(fl.word());
      ml.setValue(fl.word());
      newWords.add(ml);
    }

    if(maxSentenceLength <= 0 || newWords.size() < maxSentenceLength) {
      return parser.apply(newWords);
    } else {
      return ParserAnnotatorUtils.xTree(newWords);
    }
  }

}
