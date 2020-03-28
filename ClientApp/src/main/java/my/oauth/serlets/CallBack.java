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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.gson.Gson;

import my.oauth.app.Token;
import my.oauth.utils.Client;
import my.oauth.utils.GitHubToken;
import my.oauth.utils.TokenResponce;

public class CallBack extends HttpServlet implements TheamlefServlet {

	private Token token;

	public CallBack(Token token) {
		super();
		this.token = token;
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String code = req.getParameter("code");
		String state = req.getParameter("state");

		System.out.println(code);
		System.out.println(state);
		HttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost("https://github.com/login/oauth/access_token");
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("client_id", "611f43c030371f5d7297"));
		params.add(new BasicNameValuePair("client_secret", "0d013731236e856bf283698a4cfd1d7ed2ed9439"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri", Client.getRedirect_uris()[0]));
		params.add(new BasicNameValuePair("state", state));

		httppost.setEntity(new UrlEncodedFormEntity(params));
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("Accept", "application/json");
		
	//	httppost.setHeader("Authorization", "Basic " + Client.getClient_id() + ":"
	//			+ Base64.getEncoder().encodeToString(Client.getClient_secret().getBytes()));

		TokenResponce tokenResponce = null;
		StringBuilder sb = new StringBuilder();
		try (CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httppost)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try (InputStream instream = entity.getContent();
						BufferedReader reader = new BufferedReader(new InputStreamReader(instream));) {
					String s;
					while ((s = reader.readLine()) != null) {
						System.out.println(s);
						sb.append(s);
					}
				}
			}
		}
		
		GitHubToken gitToken = new Gson().fromJson( sb.toString() , GitHubToken.class);
		
		System.out.println( gitToken);
		
		HttpClient httpclient2 = HttpClients.createDefault();

		HttpGet httppost2 = new HttpGet("https://api.github.com/user");
		List<NameValuePair> params2 = new ArrayList<>();
		//params2.add(new BasicNameValuePair("client_id", "611f43c030371f5d7297"));
	//	params2.add(new BasicNameValuePair("client_secret", "4df850852ddd9cffde94b8dc9556f89ca95c5652"));
	//	params2.add(new BasicNameValuePair("code", code));
	//	params2.add(new BasicNameValuePair("redirect_uri", Client.getRedirect_uris()[0]));
		//params2.add(new BasicNameValuePair("Authorization", gitToken.getAccess_token() + " OAUTH-TOKEN"));

	
		httppost2.setHeader("Authorization", "token " +  gitToken.getAccess_token());
		
		
	//	httppost.setHeader("Authorization", "Basic " + Client.getClient_id() + ":"
	//			+ Base64.getEncoder().encodeToString(Client.getClient_secret().getBytes()));

	
		StringBuilder sb2 = new StringBuilder();
		try (CloseableHttpResponse response2 = (CloseableHttpResponse) httpclient2.execute(httppost2)) {
			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				try (InputStream instream = entity2.getContent();
						BufferedReader reader = new BufferedReader(new InputStreamReader(instream));) {
					String s;
					while ((s = reader.readLine()) != null) {
						System.out.println(s);
						sb2.append(s);
					}
				}
			}
		}

		
		
		
		
		
		/*
		 * HttpPost httppost = new
		 * HttpPost("https://github.com/login/oauth/access_token"); List<NameValuePair>
		 * params = new ArrayList<>(); params.add(new BasicNameValuePair("grant_type",
		 * "authorization_code")); params.add(new BasicNameValuePair("code", code));
		 * params.add(new BasicNameValuePair("redirect_uri",
		 * Client.getRedirect_uris()[0]));
		 * 
		 * httppost.setEntity(new UrlEncodedFormEntity(params));
		 * httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		 * httppost.setHeader("Authorization", "Basic " + Client.getClient_id() + ":" +
		 * Base64.getEncoder().encodeToString(Client.getClient_secret().getBytes()));
		 * 
		 * TokenResponce tokenResponce = null; StringBuilder sb = new StringBuilder();
		 * try( CloseableHttpResponse response = (CloseableHttpResponse)
		 * httpclient.execute(httppost) ) { HttpEntity entity = response.getEntity(); if
		 * (entity != null) { try ( InputStream instream = entity.getContent();
		 * BufferedReader reader = new BufferedReader( new InputStreamReader(instream)
		 * );){ String s; while((s = reader.readLine())!= null) { System.out.println(s);
		 * sb.append(s); } } } }
		 * 
		 * tokenResponce = new Gson().fromJson( sb.toString() , TokenResponce.class);
		 * 
		 * token.setAccess_token(tokenResponce.getAccess_token());
		 * token.setScope(tokenResponce.getScope());
		 * token.setToken_type(tokenResponce.getToken_type());
		 */

		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());

		// req.setAttribute("access_token",token.getAccess_token());
		// req.setAttribute("scope",Arrays.toString(token.getScope()));
		// req.setAttribute("refresh_token","");
		// System.out.println(token);

		templateEngine.process("index", ctx, resp.getWriter());
	}
}
