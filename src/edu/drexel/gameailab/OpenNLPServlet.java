package edu.drexel.gameailab;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.cmdline.TerminateToolException;
import opennlp.tools.cmdline.parser.ParserTool;


@SuppressWarnings("serial")
public class OpenNLPServlet extends HttpServlet {
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
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

	    ServletContext context = getServletContext();
	    InputStream modelIn = context.getResourceAsStream("/WEB-INF/en-parser-chunking.bin");
		
		ParserTool tool = new ParserTool();
		try {
			tool.run(modelIn,sent,resp.getWriter());
		} catch (TerminateToolException e) {

			if (e.getMessage() != null) {
				System.err.println(e.getMessage());
			}

			if (e.getCause() != null) {
				System.err.println(e.getCause().getMessage());
				e.getCause().printStackTrace(System.err);
			}

			System.exit(e.getCode());
		}
			
			
	}
}
