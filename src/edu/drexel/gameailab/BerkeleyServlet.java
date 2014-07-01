package edu.drexel.gameailab;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.berkeley.nlp.PCFGLA.CoarseToFineMaxRuleParser;
import edu.berkeley.nlp.PCFGLA.Grammar;
import edu.berkeley.nlp.PCFGLA.Lexicon;
import edu.berkeley.nlp.PCFGLA.ParserData;
import edu.berkeley.nlp.io.PTBLineLexer;
import edu.berkeley.nlp.syntax.Tree;
import edu.berkeley.nlp.util.Numberer;
import edu.stanford.nlp.util.Timing;

@SuppressWarnings("serial")
public class BerkeleyServlet extends HttpServlet {
	ParserData pData;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Timing tim = new Timing();
		System.err.print("Loading parser from serialized file...");

		ServletContext context = getServletContext();
		InputStream is = context.getResourceAsStream("/WEB-INF/eng_sm6.gr");

		pData = ParserData.Load(is);
		if (pData == null) {
			System.err.print("Error loading parser from serialized file...");
		}

		System.err.println(" done [" + tim.toSecondsString() + " sec].");

	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.doGet(req, resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		// read some text in the text variable
		String line = req.getParameter("sent");
		if (line == null)
			line = "Mary has several mice. She is very cute.";

		double threshold = 1.0;

		CoarseToFineMaxRuleParser parser = null;
		
		Grammar grammar = pData.getGrammar();
		Lexicon lexicon = pData.getLexicon();
		Numberer.setNumberers(pData.getNumbs());
		parser = new CoarseToFineMaxRuleParser(grammar, lexicon, threshold, -1,
				false, false, false, false, false, true, true);
		parser.binarization = pData.getBinarization();

		try {
			PrintWriter outputData = resp.getWriter();
			PTBLineLexer tokenizer = new PTBLineLexer();

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
	}

	private void outputTrees(List<Tree<String>> parseTrees,
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
