package my.oauth.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SerializationUtils;

import com.google.gson.Gson;

import my.oauth.utils.FetchResponce;
import my.oauth.utils.TokenResponce;
import redis.clients.jedis.Jedis;

public class ResourceFetcher extends HttpServlet implements TheamlefServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Optional<TokenResponce> noSQLExtract(String access_token) {
		Jedis jedis = new Jedis("localhost");
		byte[] res = jedis.get(access_token.getBytes());
		jedis.close();
		return ( res != null ) ? Optional.of((TokenResponce) SerializationUtils.deserialize(res)) : Optional.empty();
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String auth = req.getHeader("Authorization");
		String inToken = auth.substring("Bearer ".length(), auth.length());
		
		Optional<TokenResponce> optToken = noSQLExtract(inToken);
		if (optToken.isPresent()) {
	/*		
			TokenResponce resppnce = optToken.get();
	*/		
			FetchResponce fetchResponce = new FetchResponce();
			fetchResponce.setName("Protected Resource");
			fetchResponce.setDescription("This data has been protected by OAuth 2.0");
			
			String json = new Gson().toJson( fetchResponce );
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.print( json );
			
		}else {
			System.out.println( "tocken not found" );
			
		}
		
		
	}	
}
