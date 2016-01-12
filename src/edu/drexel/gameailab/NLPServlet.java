package edu.drexel.gameailab;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;

@SuppressWarnings("serial")
public class NLPServlet extends HttpServlet {
	private StanfordCoreNLP pipeline;
	private String annotators;
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.pipeline = null;
		this.annotators = "";
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		this.doGet(req, resp);
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		if(req.getParameter("print").equals("xml"))
			resp.setContentType("application/xml");
		else
			resp.setContentType("text/plain");
		String annotators = req.getParameter("annotators");
		if(annotators==null)
			annotators="tokenize, ssplit, pos, lemma, ner, parse, dcoref";

		if(this.pipeline==null || !this.annotators.equals(annotators)){
			try {
				Properties props = new Properties();
				props.put("annotators",annotators);
				props.put("tokenize.options", "americanize=false");
				//props.put("dcoref.score", "true");
				//tokenize, ssplit, parse, lemma, ner, dcoref
				this.pipeline = new StanfordCoreNLP(props);
				this.annotators = annotators;
			} catch (Exception e) {
				System.err.println("Error " + e.getLocalizedMessage());
			}
		}

		// read some text in the text variable
		String text = req.getParameter("sent");
		if(text==null)
			text="Mary has several mice. She is very cute.";

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		if(pipeline==null){
			resp.getWriter().println("Error creating pipeline");
		}
		pipeline.annotate(document);
		
		//Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
		System.out.println(document.get(CorefChainAnnotation.class));
		
		if(req.getParameter("print").equals("prettyPrint"))
			pipeline.prettyPrint(document, resp.getWriter());
		else if(req.getParameter("print").equals("xml"))
			pipeline.xmlPrint(document, resp.getWriter());
		else if(req.getParameter("print").equals("toShorterString"))
			resp.getWriter().println(document.toShorterString());
		else {
	        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
	        for (CoreMap sentence : sentences) {
	            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
	            tree.pennPrint(resp.getWriter());
	            resp.getWriter().println();
	        }
        }
        /*
        if (sentences != null && sentences.size() > 0) {
          CoreMap sentence = sentences.get(0);
          Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
          resp.getWriter().println();
          resp.getWriter().println("The first sentence parsed is:");
          tree.pennPrint(resp.getWriter());
        }
        */
        
        /*
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence
					.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document
				.get(CorefChainAnnotation.class);
		*/

	}
}
