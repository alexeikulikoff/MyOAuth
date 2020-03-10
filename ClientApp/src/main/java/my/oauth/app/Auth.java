package my.oauth.app;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Auth extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException {
		
		String response_type = "code";
		String scope = "foo";
		String client_id = "oauth-client-1";
		String redirect_uri = "http://localhost:8001/callback";
		String state = UUID.randomUUID().toString().substring(0,6);
		String auth_url = "http://localhost:8002/authorize";
		
		String url_params = "response_type=" + response_type + " &scope=" + scope + "&client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&state=" + state;  
		resp.sendRedirect( auth_url +  "?" + url_params);
		
	}
}
