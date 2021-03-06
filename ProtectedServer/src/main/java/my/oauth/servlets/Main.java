package my.oauth.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

public class Main extends HttpServlet implements TheamlefServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());

		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());

		templateEngine.process("index", ctx, resp.getWriter());
	}
	

}
