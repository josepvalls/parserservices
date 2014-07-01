package edu.drexel.gameailab;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.KBestViterbiParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.SemanticHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.util.Filters;
import edu.stanford.nlp.util.ScoredObject;
import edu.stanford.nlp.util.Timing;

@SuppressWarnings("serial")
public class ParserServlet extends HttpServlet {
	LexicalizedParser lp = null;
	PennTreebankLanguagePack tlp = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Timing tim = new Timing();
			System.err.print("Loading parser from serialized file...");
			ServletContext context = getServletContext();
			InputStream is = context
					.getResourceAsStream("/WEB-INF/englishPCFG.ser");
			ObjectInputStream in = new ObjectInputStream(is);
			this.lp = LexicalizedParser.loadModel(in);
			in.close();
			this.tlp = new PennTreebankLanguagePack();
			System.err.println(" done [" + tim.toSecondsString() + " sec].");
		} catch (IOException e) {
			System.err.println("Error " + e.getLocalizedMessage());
		}

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
		// String print =
		// "oneline,penn,latexTree,words,wordsAndTags,rootSymbolOnly,typedDependencies,typedDependenciesCollapsed,semanticGraph,makeCopulaHead";
		// makeCopulaHead,dependencies,collocations,conllStyleDependencies,conll2007
		String sent = req.getParameter("sent");

		

		if (sent == null)
			sent = "Bell, a company which is based in LA, makes and distributes computer products.";
		String print = req.getParameter("print");
		if (print == null)
			print = "oneline";
		int kbest;
		try {
			kbest = Integer.parseInt(req.getParameter("k"));
		} catch (NumberFormatException e1) {
			kbest = 1;
		}

		//try {
			TokenizerFactory<CoreLabel> tokenizerFactory = (TokenizerFactory<CoreLabel>) PTBTokenizer
					.factory(new CoreLabelTokenFactory(), "");
			List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(
					new StringReader(sent)).tokenize();


			if (kbest > 1) {
				List<ScoredObject<Tree>> bestparses = null ;
			      //LexicalizedParserQuery pq = new LexicalizedParserQuery(lp);
					LexicalizedParserQuery pq = this.lp.parserQuery();
			      if (pq.parse(rawWords)) {
			    	   bestparses = pq.getKGoodFactoredParses(kbest);// pq.getKBestPCFGParses(kbest);
			        //bestparse.setScore(pq.getPCFGScore() % -10000.0);
			      }
				/*
				
				LexicalizedParserQuery lpq = this.lp.parserQuery();
				KBestViterbiParser x;
				lpq.parse(rawWords);
				List<ScoredObject<Tree>> trees = lpq.getKBestPCFGParses(kbest);
				for(ScoredObject<Tree> parse : trees){
					resp.getWriter().println(parse.score());
					this.output(print, parse.object(), resp.getWriter());
				}*/
					for(ScoredObject<Tree> parse : bestparses){
					resp.getWriter().println(parse.score());
					this.output(print, parse.object(), resp.getWriter());
					}
			} else {
				Tree parse = this.lp.apply(rawWords);
				this.output(print, parse, resp.getWriter());
				resp.getWriter().write('\n');
			}
		/*} catch (Exception e) {
			resp.getWriter().println(e.getMessage());
		}*/
	}
	private void output(String print,Tree parse, PrintWriter pw ){
		// Output
		for (String print_type : print.split(",")) {
			if (print_type.replace(" ", "").equals("makeCopulaHead")) {
				GrammaticalStructureFactory gsf = tlp
						.grammaticalStructureFactory(
								Filters.<String> acceptFilter(),
								new SemanticHeadFinder(false));
				GrammaticalStructure gs = gsf
						.newGrammaticalStructure(parse);
				pw.write(
						GrammaticalStructure.dependenciesToString(gs,
								gs.typedDependencies(false), parse,
								false, false));
			} else if (print_type.replace(" ", "").equals(
					"typedDependencies")) {
				GrammaticalStructureFactory gsf = tlp
						.grammaticalStructureFactory(
								Filters.<String> acceptFilter(),
								new SemanticHeadFinder(true));
				GrammaticalStructure gs = gsf
						.newGrammaticalStructure(parse);
				pw.write(
						GrammaticalStructure.dependenciesToString(gs,
								gs.typedDependencies(false), parse,
								false, false));
			} else {
				TreePrint tp = new TreePrint(print_type.replaceAll(" ",
						""));
				tp.printTree(parse, pw);
			}
			pw.write('\n');
		}
	}
}
