package my.oauth.serlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.oauth.utils.Client;

public class Authorizer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String response_type = "code";
		//String[] scopes = Client.getScope();
		String scopes = "read:user";
		//String client_id = Client.getClient_id();
		String client_id = "611f43c030371f5d7297";
	//	String redirect_uri = Client.getRedirect_uris()[0];
		String redirect_uri = Client.getRedirect_uris()[0];
		String state = UUID.randomUUID().toString().replace("-", "");
		
		
		//String auth_url = "http://localhost:8002/authorize";
		String auth_url = "https://github.com/login/oauth/authorize/";
		
		String scope = "read:user";
		/////for (String s : scopes) {
		//	scope += " " +  s;
		//}
		
		//scope = scope.trim();
		
		
		String url_params = "response_type=" + response_type + "&scope=" + scope + "&client_id=" + client_id
				+ "&redirect_uri=" + redirect_uri + "&state=" + state;
		resp.sendRedirect(auth_url + "?" + url_params);

	}
}
