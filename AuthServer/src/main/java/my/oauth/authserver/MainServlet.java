package my.oauth.authserver;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import my.oauth.servlets.TheamlefServlet;
import my.oauth.utils.Client;

public class MainServlet extends HttpServlet implements TheamlefServlet {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());

		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());

		req.setAttribute("client_id", Client.getClient_id());
		req.setAttribute("client_secret", Client.getClient_secret());
		req.setAttribute("scope", Arrays.toString( Client.getScope()));
		req.setAttribute("redirect_uri",   Arrays.toString(Client.getRedirect_uris()));
		
		req.setAttribute("authorizationEndpoint",  "http://localhost:8002/authorize");
		req.setAttribute("tokenEndpoint",  "http://localhost:8002/token");
		templateEngine.process("index", ctx, resp.getWriter());

	}
}
