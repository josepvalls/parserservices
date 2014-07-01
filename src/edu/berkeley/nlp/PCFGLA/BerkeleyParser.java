package edu.berkeley.nlp.PCFGLA;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.berkeley.nlp.io.PTBLineLexer;
import edu.berkeley.nlp.syntax.Tree;
import edu.berkeley.nlp.ui.TreeJPanel;
import edu.berkeley.nlp.util.Numberer;

/**
 * Reads in the Penn Treebank and generates N_GRAMMARS different grammars.
 * 
 * @author Slav Petrov
 */
public class BerkeleyParser {
	static TreeJPanel tjp;

	public static void main(String[] args) {

		double threshold = 1.0;

		CoarseToFineMaxRuleParser parser = null;
		ParserData pData = ParserData
				.Load("/Users/josepvalls/Dropbox/projects/AppEngine/parserservices/war/WEB-INF/eng_sm6.gr");
		if (pData == null) {
			System.out.println("Failed to load grammar from file");
			System.exit(1);
		}
		Grammar grammar = pData.getGrammar();
		Lexicon lexicon = pData.getLexicon();
		Numberer.setNumberers(pData.getNumbs());
		parser = new CoarseToFineMaxRuleParser(grammar, lexicon, threshold, -1,
				false, false, false, false, false, true, true);
		parser.binarization = pData.getBinarization();

		try {
			PrintWriter outputData = new PrintWriter(new OutputStreamWriter(
					System.out));
			PTBLineLexer tokenizer = new PTBLineLexer();

			String line = "A strong storm is approaching.";
			String sentenceID = "";
			List<String> sentence = tokenizer.tokenizeLine(line);
			ArrayList<String> posTags = null;// new ArrayList<String>();

			List<Tree<String>> parsedTrees = null;

			parsedTrees = new ArrayList<Tree<String>>();
			Tree<String> parsedTree = parser.getBestConstrainedParse(sentence,
					posTags, null);
			parsedTrees.add(parsedTree);

			outputTrees(parsedTrees, outputData, parser, line, sentenceID);

			outputData.flush();
			outputData.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}

	private static void outputTrees(List<Tree<String>> parseTrees,
			PrintWriter outputData, CoarseToFineMaxRuleParser parser,
			String line, String sentenceID) {

		for (Tree<String> parsedTree : parseTrees) {
			if (!parsedTree.getChildren().isEmpty()) {
				String treeString = parsedTree.getChildren().get(0).toString();
				if (parsedTree.getChildren().size() != 1) {
					System.err.println("ROOT has more than one child!");
					parsedTree.setLabel("");
					treeString = parsedTree.toString();
				}
				outputData.write("( " + treeString + " )\n");
			} else {
				outputData.write("(())\n");
			}

		}

		outputData.flush();

	}

}
