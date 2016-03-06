package edu.drexel.gameailab;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.clir.clearnlp.collection.pair.Pair;
import edu.emory.clir.clearnlp.component.AbstractComponent;
import edu.emory.clir.clearnlp.component.mode.dep.DEPConfiguration;
import edu.emory.clir.clearnlp.component.mode.srl.SRLConfiguration;
import edu.emory.clir.clearnlp.component.utils.GlobalLexica;
import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.coreference.AbstractCoreferenceResolution;
import edu.emory.clir.clearnlp.coreference.SieveSystemCoreferenceResolution;
import edu.emory.clir.clearnlp.coreference.config.SieveSystemCongiuration;
import edu.emory.clir.clearnlp.coreference.mention.AbstractMention;
import edu.emory.clir.clearnlp.coreference.utils.structures.CoreferantSet;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.Joiner;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;

@SuppressWarnings("serial")
public class ClearNLPServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//this.pipeline = null;
	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException{
		this.doGet(req, resp);
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//if(req.getParameter("print").equals("xml"))
			//resp.setContentType("application/xml");
		//else
			resp.setContentType("text/plain");

		// read some text in the text variable
		String text = req.getParameter("sent");
		if(text==null)
			text="Mary has several mice. She is very cute.";

		ServletContext context = getServletContext();
		
		
		TLanguage language = TLanguage.ENGLISH;
		AbstractTokenizer tokenizer  = NLPUtils.getTokenizer(language);
		
		NLPUtils.context = context;
		
	
		GlobalLexica.initDistributionalSemanticsWords("/WEB-INF/clearnlp/brown-rcv1.clean.tokenized-CoNLL03.txt-c1000-freq1.txt.xz");
		GlobalLexica.initNamedEntityDictionary("/WEB-INF/clearnlp/general-en-ner-gazetteer.xz");
		
		// initialize statistical models
		AbstractComponent morph = NLPUtils.getMPAnalyzer(language);
		AbstractComponent pos = NLPUtils.getPOSTagger   (language, "/WEB-INF/clearnlp/general-en-pos.xz");
		AbstractComponent dep = NLPUtils.getDEPParser   (language, "/WEB-INF/clearnlp/general-en-dep.xz", new DEPConfiguration("root"));
		AbstractComponent srl = NLPUtils.getSRLabeler   (language, "/WEB-INF/clearnlp/general-en-srl.xz", new SRLConfiguration(4, 3));
		AbstractComponent ner = NLPUtils.getNERecognizer(language, "/WEB-INF/clearnlp/general-en-ner.xz");

		AbstractComponent[] components = new AbstractComponent[]{pos, morph, dep, srl, ner};
		List<String> tokens = tokenizer.tokenize("I have a new employee.");
		DEPTree tree = new DEPTree(tokens);
		for (AbstractComponent component : components)
			component.process(tree);
		System.out.println(tree.toString()+"\n");
		resp.getWriter().println(tree.toString());

		

	}
}
