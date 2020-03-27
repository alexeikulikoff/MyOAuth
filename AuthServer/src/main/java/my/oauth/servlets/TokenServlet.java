package my.oauth.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SerializationUtils;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import my.oauth.servlets.TheamlefServlet;
import my.oauth.utils.AuthorizationEndpointRequest;
import my.oauth.utils.Client;
import my.oauth.utils.JWTTokenResponce;
import my.oauth.utils.MyJsonWebKey;
import my.oauth.utils.TokenResponce;
import redis.clients.jedis.Jedis;

import com.google.gson.Gson;

public class TokenServlet extends HttpServlet implements TheamlefServlet {

	private static final long serialVersionUID = 1L;

	private static final String kty = "RSA";
	private static final String kid = "k1";
	private static final String n = "nNa9MJolEVSXqozUSzEndH5I4Hv3nBl4nI7Gab-BU1hUveXcTqft8i-_DQox68XhZdFxztZ-JQFN8r4dUiZWnzVhS64hTLT9jJZeCrpQ0ltoapu55uKRJxyZMFr4Ls-HLtTX3iISaXLIpf48F3JmQ07eyPhwL3dwF9KRj9hBce408UH7_jh-iz-9w1ntRYTIniEmOzHr0499ZUUAOZJnSeBlOE3swq3nbmyxHcjA5Bne93HDxv2hpSQgLxoMqzvosGeDv3k1JZJ21glOcPJqtX9oJBuVp3uUct4rcoTW6dtQ6lf-mt2x_pC1B3UhKFDFVs8lNdPgOWZSeZb-C1r4Zw";
	private static final String e = "AQAB";
	
	private MyJsonWebKey myJsonWebKey;
	
	public TokenServlet() {
		super();
		myJsonWebKey = new MyJsonWebKey(kty, kid,n,e);
	}
	
	private String Grant_type(String body) {
		String res = null;
		String[] str = body.split("&");
		for (String s : str) {
			if (s.startsWith("grant_type")) {
				res = new String(s.split("=")[1]);
				break;
			}
		}
		return res;
	}

	private void noSQLSave(String access_token, TokenResponce token) {
		Jedis jedis = new Jedis("localhost");
		byte[] tokenSerialized = SerializationUtils.serialize(token);
		jedis.set(access_token.getBytes(), tokenSerialized);
		jedis.close();

	}
	private TokenResponce noSQLExtract(String access_token) {
		Jedis jedis = new Jedis("localhost");
		byte[] res = jedis.get(access_token.getBytes());
		TokenResponce token = (TokenResponce) SerializationUtils
				.deserialize(res);
		jedis.close();
		return token;

	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String auth = req.getHeader("Authorization");

		String str = auth.substring("Base ".length() + 1, auth.length());
		String client_id = str.split(":")[0];
		String client_secret = new String(
				Base64.getDecoder().decode(str.split(":")[1]));

		if (!client_id.equals(Client.getClient_id())) {

			System.out.println("Client is wrong!");
			return;
		}
		

		if (!client_secret.equals(Client.getClient_secret())) {
			System.out.println("Secret is wrong!");
			return;
		}

		// String body =
		// req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String body = req.getReader().lines().reduce("",
				(accumulator, actual) -> accumulator + actual);

		if (Grant_type(body).equals("authorization_code")) {

			/*
			 *  The simplest way to generate access_tocken
			 */

		//	String access_token = UUID.randomUUID().toString().replace("-", "");

			try {
			
				String jsonKey = new Gson().toJson(myJsonWebKey);
				PublicJsonWebKey publicJsonWebKey = PublicJsonWebKey.Factory.newPublicJwk( jsonKey );
			
				RsaJsonWebKey rsaJsonWebKey = new RsaJsonWebKey( (RSAPublicKey) publicJsonWebKey.getKey());
				
				System.out.println(rsaJsonWebKey);
				
				JwtClaims claims = new JwtClaims();
				claims.setIssuer("http://localhost:8002/"); 
				claims.setAudience("http://localhost:8003/"); 
				claims.setExpirationTimeMinutesInTheFuture(10); 
				claims.setGeneratedJwtId(); 
				claims.setIssuedAtToNow(); 
				claims.setNotBeforeMinutesInThePast(2);
				claims.setSubject(Client.getClient_id()); 
				claims.setClaim("email", "mail@example.com"); 

				JsonWebSignature jws = new JsonWebSignature();
				jws.setPayload(claims.toJson());
				
				
				jws.setKey(rsaJsonWebKey.getPrivateKey());
				jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
				jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

				String access_token = jws.getCompactSerialization();
			
				String token_type = "Bearer";

				String[] scope = AuthorizationEndpointRequest.getScope();
				
				TokenResponce token = new TokenResponce(client_id, access_token,token_type, scope);
								
				noSQLSave(access_token, token);

				String json = new Gson().toJson(token);
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.setContentType("application/json");
				PrintWriter out = resp.getWriter();
				out.print(json);
				
			} catch (JoseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		

		} else if (Grant_type(body).equals("refresh_token")) {

		} else {

		}

	}
}
