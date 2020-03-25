package my.oauth.serlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.gson.Gson;

import my.oauth.app.Token;
import my.oauth.utils.Client;
import my.oauth.utils.TokenResponce;

public class CallBack extends HttpServlet implements TheamlefServlet {
	
	private Token token;
	
	public CallBack( Token token ) {
		super();
		this.token = token;
	}
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		
		String code = req.getParameter("code");
		
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://localhost:8002/token");
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri", Client.getRedirect_uris()[0]));

		httppost.setEntity(new UrlEncodedFormEntity(params));
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("Authorization", "Basic " + Client.getClient_id() + ":" + Base64.getEncoder().encodeToString(Client.getClient_secret().getBytes()));
		
		TokenResponce tokenResponce = null;
		
		try( CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httppost) ) {
			    HttpEntity entity = response.getEntity();
			    if (entity != null) {
			      try ( InputStream instream = entity.getContent(); 
			    		BufferedReader reader = new BufferedReader( new InputStreamReader(instream) );){
			        	String s;
			        	while((s = reader.readLine())!= null) {
			        		tokenResponce = new Gson().fromJson(s, TokenResponce.class);
			        		
			        		token.setAccess_token(tokenResponce.getAccess_token());
			        		token.setScope(tokenResponce.getScope());
			        		token.setToken_type(tokenResponce.getToken_type());
			        		
			        	}
			        }
			    }
		} 
		
		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());
		
		req.setAttribute("access_token",token.getAccess_token());
		req.setAttribute("scope",Arrays.toString(token.getScope()));
		req.setAttribute("refresh_token","");
		System.out.println(token);

		templateEngine.process("index", ctx, resp.getWriter());
	}
}
