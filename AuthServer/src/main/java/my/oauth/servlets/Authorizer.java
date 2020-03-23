package my.oauth.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import my.auth.utils.Client;

public class Authorizer extends HttpServlet implements TheamlefServlet {
	private static final long serialVersionUID = 1L;

	private String state = null;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String redirect_uri = Client.getRedirect_uris()[0];
		String code = UUID.randomUUID().toString().substring(0, 8);
		String url = redirect_uri + "?" + "code=" + code + "&" + "state=" + state;
		System.out.println(url);
		resp.sendRedirect(url);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());
		Map<String, String[]> mp = req.getParameterMap();
		String client_id = mp.get("client_id")[0];
		if (client_id.equals(Client.getClient_id())) {

			String reqid = UUID.randomUUID().toString().substring(0, 8);
			state = mp.get("state")[0];

			req.setAttribute("show_error", "d-none");
			req.setAttribute("show_aprove", "");
			req.setAttribute("client_name", client_id);
			req.setAttribute("client_id", client_id);
			req.setAttribute("client_uri", Client.getRedirect_uris()[0]);
			req.setAttribute("scopes", Client.getScope());
			req.setAttribute("reqid", reqid);
			templateEngine.process("aprove", ctx, resp.getWriter());

		} else {
			req.setAttribute("show_error", "");
			req.setAttribute("show_aprove", "d-none");
			templateEngine.process("aprove", ctx, resp.getWriter());
		}

	}
}
