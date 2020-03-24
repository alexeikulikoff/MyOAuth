package my.oauth.authserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.oauth.servlets.TheamlefServlet;
import my.oauth.utils.TokenResponce;

import com.google.gson.Gson;

public class Token  extends HttpServlet implements TheamlefServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String contentType = req.getHeader("Content-Type");
		String auth = req.getHeader("Authorization");
		
		System.out.println( auth );
	//	String  body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String body = req.getReader().lines()
			    .reduce("", (accumulator, actual) -> accumulator + actual);
		
		
		System.out.println(contentType);
		System.out.println(body);
		
		
		TokenResponce token = new TokenResponce("123","456","foo bar");
		
		String json = new Gson().toJson(token);
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.print( json );

	}	
}
