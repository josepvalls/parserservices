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
import edu.emory.clir.clearnlp.coreference.AbstractCoreferenceResolution;
import edu.emory.clir.clearnlp.coreference.SieveSystemCoreferenceResolution;
import edu.emory.clir.clearnlp.coreference.config.SieveSystemCongiuration;
import edu.emory.clir.clearnlp.coreference.mention.AbstractMention;
import edu.emory.clir.clearnlp.coreference.utils.structures.CoreferantSet;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
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
		System.out.println("THIS IS THE CONTEXT"+String.valueOf(context));
        SieveSystemCongiuration config = new SieveSystemCongiuration(TLanguage.ENGLISH,context);
        //config.loadDefaultMentionDetectors();
        //config.loadDefaultSieves(true, true, true, true, true, false, false, false);

        //SieveSystemCongiuration config = new SieveSystemCongiuration(TLanguage.ENGLISH);
        config.loadMentionDetectors(true, false, true);
        config.loadDefaultSieves(false, true, true, false, true, true, true, true);

        AbstractCoreferenceResolution coref = new SieveSystemCoreferenceResolution((SieveSystemCongiuration) config);
        //List<String> l_filePaths = FileUtils.getFileList("src/test/resources/edu/emory/clir/clearnlp/coreference/coref/microsoft/parsed", ".cnlp", false);

        List<DEPTree> trees;
        Pair<List<AbstractMention>, CoreferantSet> resolution;
        
        //ServletContext context = getServletContext();
        InputStream testFile = context.getResourceAsStream("/WEB-INF/clearnlp/mc500.dev.comprehension1.cnlp");
        
        
            trees = getTestDocuments(testFile, 9);
            resolution = coref.getEntities(trees);

            printCorefCluster(resolution.o1,resolution.o2,resp);
		
		
		resp.getWriter().println(text);

	}
	  public  List<DEPTree> getTestDocuments(InputStream in, int NERpos) {
	        DEPTree tree;
	        List<DEPTree> trees = new ArrayList<>();

	        try {
	            TSVReader reader = new TSVReader(0, 1, 2, 3, NERpos, 4, 5, 6, -1, -1);
	            reader.open(in);

	            while ((tree = reader.next()) != null) {
	                trees.add(tree);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return trees;
	    }
	
    public void printCorefCluster(List<AbstractMention> mentions, CoreferantSet set,HttpServletResponse resp) throws IOException {

        AbstractMention mention;
        int i, linkedId, size = mentions.size();

        for (i = 0; i < size; i++) {
            mention = mentions.get(i);
            linkedId = set.findClusterHead(i);

            if (linkedId == i) {
            	resp.getWriter().println(i + ": " + mention.getWordFrom() + "\t(Singleton)");
            } else {
            	resp.getWriter().println(i + ": " + mention.getWordFrom() + "\t->\t" + linkedId + ": " + mentions.get(linkedId).getWordFrom());
            }
        }
    }
}
