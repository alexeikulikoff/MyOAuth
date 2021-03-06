package my.oauth.utils;

import java.io.Serializable;
import java.util.Arrays;

public class TokenResponce implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String client_id;
	private String access_token;
	private String token_type;
	private String[] scope;
	
	public TokenResponce(String client_id, String access_token, String token_type, String[] scope) {
		super();
		this.client_id = client_id;
		this.access_token = access_token;
		this.token_type = token_type;
		this.scope = scope;
	}
	public String getAccess_token() {
		return access_token;
	}
	
	public String getToken_type() {
		return token_type;
	}
	
	public String[] getScope() {
		return scope;
	}
	public String getClient_id() {
		return client_id;
	}
	@Override
	public String toString() {
		return "TokenResponce [client_id=" + client_id + ", access_token=" + access_token + ", token_type=" + token_type
				+ ", scope=" + Arrays.toString(scope) + "]";
	}
	
	
	
	
}
