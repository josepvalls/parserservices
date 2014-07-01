package edu.drexel.gameailab;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@SuppressWarnings("serial")
public class MaltServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		/*
		try {
			Timing tim = new Timing();
			System.err.print("Loading parser from serialized file...");
			ServletContext context = getServletContext();
			InputStream is = context
					.getResourceAsStream("/WEB-INF/englishPCFG.ser");
			ObjectInputStream in = new ObjectInputStream(is);
			this.lp = LexicalizedParser.loadModel(in);
			in.close();
			System.err.println(" done [" + tim.toSecondsString() + " sec].");
		} catch (IOException e) {
			System.err.println("Error " + e.getLocalizedMessage());
		}
		*/

	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		this.doGet(req, resp);
	}

	
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		String sent = req.getParameter("sent");

		if(sent==null)
			sent = "Bell, a company which is based in LA, makes and distributes computer products.";
		String print = req.getParameter("print");
		if(print==null)
			print = "oneline";
		resp.getWriter().write('\n');
		
		
		ServletContext context = getServletContext();
		InputStream is = context
				.getResourceAsStream("/WEB-INF/parser-models/wsj-0-18-bidirectional-nodistsim.tagger");

		
		 MaxentTagger tagger = new MaxentTagger(is);
		    TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
											   "untokenizable=noneKeep");
		    PrintWriter pw = resp.getWriter();
		    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(sent);
		    documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
		    for (List<HasWord> sentence : documentPreprocessor) {
		      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
		      pw.println(Sentence.listToString(tSentence, false));
		    }
		    pw.close();
		
		
		
		
	}
}
