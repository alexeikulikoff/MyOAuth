package my.oauth.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SerializationUtils;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import com.google.gson.Gson;

import my.oauth.utils.FetchResponce;

import my.oauth.utils.TokenResponce;
import redis.clients.jedis.Jedis;

public class ResourceFetcher extends HttpServlet implements TheamlefServlet {

	private static final long serialVersionUID = 1L;

	private static final String kty = "RSA";
	private static final String kid = "k1";
	private static final String n = "nNa9MJolEVSXqozUSzEndH5I4Hv3nBl4nI7Gab-BU1hUveXcTqft8i-_DQox68XhZdFxztZ-JQFN8r4dUiZWnzVhS64hTLT9jJZeCrpQ0ltoapu55uKRJxyZMFr4Ls-HLtTX3iISaXLIpf48F3JmQ07eyPhwL3dwF9KRj9hBce408UH7_jh-iz-9w1ntRYTIniEmOzHr0499ZUUAOZJnSeBlOE3swq3nbmyxHcjA5Bne93HDxv2hpSQgLxoMqzvosGeDv3k1JZJ21glOcPJqtX9oJBuVp3uUct4rcoTW6dtQ6lf-mt2x_pC1B3UhKFDFVs8lNdPgOWZSeZb-C1r4Zw";
	private static final String e = "AQAB";

	private MyJsonWebKey myJsonWebKey = new MyJsonWebKey(kty, kid, n, e);

	private Optional<TokenResponce> noSQLExtract(String access_token) {
		Jedis jedis = new Jedis("localhost");
		byte[] res = jedis.get(access_token.getBytes());
		jedis.close();
		return (res != null)
				? Optional
						.of((TokenResponce) SerializationUtils.deserialize(res))
				: Optional.empty();
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String auth = req.getHeader("Authorization");
		String inToken = auth.substring("Bearer ".length(), auth.length());
		
		String jsonKey = new Gson().toJson(myJsonWebKey);

		PublicJsonWebKey rsaJsonWebKey;
		try {
			rsaJsonWebKey = PublicJsonWebKey.Factory.newPublicJwk(jsonKey);
			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime().setAllowedClockSkewInSeconds(30)
					.setRequireSubject().setExpectedIssuer("Issuer")
					.setExpectedAudience("Audience")
					.setVerificationKey(rsaJsonWebKey.getKey())
					.setJwsAlgorithmConstraints(ConstraintType.WHITELIST,AlgorithmIdentifiers.RSA_USING_SHA256)
					.build();
			try {
			
				JwtClaims jwtClaims = jwtConsumer.processToClaims(inToken);
				
				System.out.println( jwtClaims );
			
			} catch (InvalidJwtException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JoseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Optional<TokenResponce> optToken = noSQLExtract(inToken);

		if (optToken.isPresent()) {

			// TokenResponce resppnce = optToken.get();

			FetchResponce fetchResponce = new FetchResponce();
			fetchResponce.setName("Protected Resource");

			fetchResponce.setDescription(
					"This data has been protected by OAuth 2.0");

			String json = new Gson().toJson(fetchResponce);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.print(json);

		} else {
			System.out.println("tocken not found");

		}

	}
}
