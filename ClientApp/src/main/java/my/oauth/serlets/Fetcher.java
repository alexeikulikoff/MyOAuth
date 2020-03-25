package my.oauth.serlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.google.gson.Gson;

import my.oauth.app.Token;
import my.oauth.utils.Client;
import my.oauth.utils.FetchResponce;
import my.oauth.utils.TokenResponce;

public class Fetcher extends HttpServlet implements TheamlefServlet{

	private Token token;
	
	public Fetcher( Token token ) {
		super();
		this.token = token;
	}
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String auth = "Bearer " + token.getAccess_token();
		
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://localhost:8003/resource");
		
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("Authorization", auth);
		
		FetchResponce fetchResponce = null;
		try( CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httppost) ) {
		    HttpEntity entity = response.getEntity();
		    if (entity != null) {
		       try ( InputStream instream = entity.getContent(); 
		    		BufferedReader reader = new BufferedReader( new InputStreamReader(instream) );){
		        	String s;
		        	while((s = reader.readLine())!= null) {
		        		fetchResponce = new Gson().fromJson(s, FetchResponce.class);
		        	}
		        }
		     }
		  }
		WebContext ctx = new WebContext(req, resp, getServletConfig().getServletContext(), req.getLocale());
		TemplateEngine templateEngine = initTemplateEngine(this.getServletContext());
		
		req.setAttribute("name", fetchResponce.getName());
		req.setAttribute("description", fetchResponce.getDescription());
		templateEngine.process("data", ctx, resp.getWriter());
		
	}
}
