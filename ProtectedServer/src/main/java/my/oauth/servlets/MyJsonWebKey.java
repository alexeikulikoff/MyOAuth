package my.oauth.servlets;

public class MyJsonWebKey {
	private String kty;
	private String kid;
	private String n;
	private String e;
	public MyJsonWebKey(String kty, String kid, String n, String e) {
		super();
		this.kty = kty;
		this.kid = kid;
		this.n = n;
		this.e = e;
	}
	/**
	 * @return the kty
	 */
	public String getKty() {
		return kty;
	}
	/**
	 * @return the kid
	 */
	public String getKid() {
		return kid;
	}
	/**
	 * @return the n
	 */
	public String getN() {
		return n;
	}
	/**
	 * @return the e
	 */
	public String getE() {
		return e;
	}
	@Override
	public String toString() {
		return "{kty=" + kty + ", kid=" + kid + ", n=" + n + ", e="
				+ e + "}";
	}
	
	
}
