package my.oauth.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import my.oauth.utils.AuthorizationEndpointRequest;
import my.oauth.utils.Client;

public class Authorizer extends HttpServlet implements TheamlefServlet {
	private static final long serialVersionUID = 1L;

	private String state = null;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String redirect_uri = Client.getRedirect_uris()[0];
		String code = UUID.randomUUID().toString().substring(0, 8);
		String url = redirect_uri + "?" + "code=" + code + "&" + "state=" + state;
	
		resp.sendRedirect(url);

	}

	private void showError(HttpServletRequest req, HttpServletResponse resp, TemplateEngine templateEngine, String error) {
		req.setAttribute("show_error", "");
		req.setAttribute("show_aprove", "d-none");
		req.setAttribute("error_message", error);
		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		try {
			templateEngine.process("aprove", ctx, resp.getWriter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());
		
		String client_id = req.getParameter("client_id");
		
		if (client_id.equals(Client.getClient_id())) {

			String reqid = UUID.randomUUID().toString().substring(0, 8);
			
			String[] scope = req.getParameter("scope").split(" ");
			String state = req.getParameter("state") ;
			String redirect_uri = req.getParameter("redirect_uri");
			String response_type = req.getParameter("response_type");
			
			AuthorizationEndpointRequest.setClient_id(client_id);
			AuthorizationEndpointRequest.setState( state );
			AuthorizationEndpointRequest.setRedirect_uri( redirect_uri );
			AuthorizationEndpointRequest.setResponse_type( response_type );
			AuthorizationEndpointRequest.setScope(scope);
			AuthorizationEndpointRequest.setUser(client_id);
			AuthorizationEndpointRequest.setReqid(reqid);
			
			req.setAttribute("show_error", "d-none");
			req.setAttribute("show_aprove", "");
			req.setAttribute("client_name", client_id);
			req.setAttribute("client_id", client_id);
			req.setAttribute("client_uri",redirect_uri);
			req.setAttribute("scopes", scope);
			req.setAttribute("reqid", reqid);
			
			templateEngine.process("aprove", ctx, resp.getWriter());

		} else {
			req.setAttribute("show_error", "");
			req.setAttribute("show_aprove", "d-none");
			templateEngine.process("aprove", ctx, resp.getWriter());
		}

	}
}
