package my.oauth.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SerializationUtils;

import my.oauth.servlets.TheamlefServlet;
import my.oauth.utils.AuthorizationEndpointRequest;
import my.oauth.utils.Client;
import my.oauth.utils.TokenResponce;
import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

public class TokenServlet  extends HttpServlet implements TheamlefServlet {

	private static final long serialVersionUID = 1L;

	private String Grant_type(String body) {
		String res = null;
		String[] str = body.split("&");
		for(String s : str) {
			if (s.startsWith("grant_type")) {
				res = new String( s.split("=")[1] );
				break;
			}
		}
		return res;
	}
	
	private void noSQLSave(String access_token, TokenResponce token) {
		Jedis jedis = new Jedis("localhost");
		byte[] tokenSerialized = SerializationUtils.serialize( token );
		jedis.set(access_token.getBytes(), tokenSerialized);
		jedis.close();
		
	}
	private TokenResponce noSQLExtract(String access_token) {
		Jedis jedis = new Jedis("localhost");
		byte[] res = jedis.get(access_token.getBytes());
		TokenResponce token = (TokenResponce) SerializationUtils.deserialize(res);
		jedis.close();
		return token;
		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		
		String auth = req.getHeader("Authorization");
		
		String str = auth.substring("Base ".length() + 1, auth.length());
		String client_id = str.split(":")[0];
		String client_secret = new String( Base64.getDecoder().decode( str.split(":")[1]));
		
		if (!client_id.equals(Client.getClient_id())) {
		
			System.out.println("Client is wrong!");
			return ;
		}
		System.out.println(client_secret);
		System.out.println(Client.getClient_secret());
		
		if (!client_secret.equals(Client.getClient_secret())) {
			System.out.println("Secret is wrong!");
			return ;
		}
		
	//	String  body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String body = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		
		if (Grant_type( body ).equals("authorization_code")) {
			
			String access_token = UUID.randomUUID().toString().replace("-", "") ;
			String token_type = "Bearer";
			String[] scope = AuthorizationEndpointRequest.getScope();
			TokenResponce token = new TokenResponce(client_id,access_token,token_type,scope);
			
			noSQLSave(access_token, token);
			
			String json = new Gson().toJson(token);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.print( json );
			
		}else if (Grant_type( body ).equals("refresh_token")) {
			
		}else {
			
		}

	}	
}
