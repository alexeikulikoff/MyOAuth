package my.oauth.utils;

public class JWTTokenResponce {

	private String access_token;
	private String token_type;
	private String scope;
	public JWTTokenResponce(String access_token, String token_type,
			String scope) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.scope = scope;
	}
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}
	@Override
	public String toString() {
		return "JWTTokenResponce [access_token=" + access_token
				+ ", token_type=" + token_type + ", scope=" + scope + "]";
	}
	
	
}
