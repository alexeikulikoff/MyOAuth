package my.oauth.utils;

public class Client {
	private static String client_id;
	private static String client_secret;
	private static String[] redirect_uris;
	private static String[] scope;

	@Override
	public boolean equals(Object obj) {

		return client_id.equals(((Client) obj).client_id);
	}

	public static String getClient_id() {
		return Client.client_id;
	}

	public static void setClient_id(String client_id) {
		Client.client_id = client_id;
	}

	public static String getClient_secret() {
		return client_secret;
	}

	public static void setClient_secret(String client_secret) {
		Client.client_secret = client_secret;
	}

	public static String[] getRedirect_uris() {
		return redirect_uris;
	}

	public static void setRedirect_uris(String[] redirect_uris) {
		Client.redirect_uris = redirect_uris;
	}

	public static String[] getScope() {
		return scope;
	}

	public static void setScope(String[] scope) {
		Client.scope = scope;
	}

}
