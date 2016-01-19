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

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

@SuppressWarnings("serial")
public class TaggerServlet extends HttpServlet {
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
		//resp.getWriter().write('\n');
		
		
		ServletContext context = getServletContext();
		InputStream ist = context
				.getResourceAsStream("/WEB-INF/parser-models/wsj-0-18-bidirectional-nodistsim.tagger");
		InputStream istp = context
				.getResourceAsStream("/WEB-INF/parser-models/wsj-0-18-bidirectional-nodistsim.tagger.props");

							//wsj-0-18-bidirectional-nodistsim.tagger
							//wsj-0-18-bidirectional-nodistsim.tagger.props
		
		 MaxentTagger tagger = null;
//		try {
//			//tagger = new MaxentTagger(ist,istp);
//			tagger = new MaxentTagger();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 //tagger.tagString(toTag)
//		 
//		 
//		    PrintWriter pw = resp.getWriter();
//		    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(sent);
//		    documentPreprocessor.setTokenizerFactory(PTBTokenizerFactory.newWordTokenizerFactory(""));
//		    for (List<HasWord> sentence : documentPreprocessor) {
//		      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
//		      pw.println(Sentence.listToString(tSentence, false));
//		    }
//		    pw.close();
//		
		
		
		
	}
}
