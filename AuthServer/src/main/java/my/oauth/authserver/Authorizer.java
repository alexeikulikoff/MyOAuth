package my.oauth.authserver;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class Authorizer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine initTemplateEngine() {
		TemplateEngine templateEngine = new TemplateEngine();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver( this.getServletContext() );
		templateResolver.setTemplateMode("xhtml");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs( 3600000L );
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver( templateResolver );
		return templateEngine;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		WebContext ctx = new WebContext( req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine();
		
		Map<String, String[]> mp = req.getParameterMap();
		
		mp.keySet().forEach(s-> {
			System.out.println(s + " : " + mp.get(s)[0]);
		});
		
		String client_id = mp.containsKey("client_id") ? mp.get("client_id")[0] : null;
		
		req.setAttribute("user_name", client_id);
		templateEngine.process("authorize", ctx, resp.getWriter());

	}
}
