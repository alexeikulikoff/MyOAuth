package my.oauth.utils;



public class AuthorizationEndpointRequest {
	private static String response_type;
	private static String client_id;
	private static String redirect_uri;
	private static String state;
	private static String[] scope;
	private static String user;
	private static String reqid;
	public static String getResponse_type() {
		return response_type;
	}
	public static void setResponse_type(String response_type) {
		AuthorizationEndpointRequest.response_type = response_type;
	}
	public static String getClient_id() {
		return client_id;
	}
	public static void setClient_id(String client_id) {
		AuthorizationEndpointRequest.client_id = client_id;
	}
	public static String getRedirect_uri() {
		return redirect_uri;
	}
	public static void setRedirect_uri(String redirect_uri) {
		AuthorizationEndpointRequest.redirect_uri = redirect_uri;
	}
	public static String getState() {
		return state;
	}
	public static void setState(String state) {
		AuthorizationEndpointRequest.state = state;
	}
	public static String[] getScope() {
		return scope;
	}
	public static void setScope(String[] scope) {
		AuthorizationEndpointRequest.scope = scope;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		AuthorizationEndpointRequest.user = user;
	}
	public static String getReqid() {
		return reqid;
	}
	public static void setReqid(String reqid) {
		AuthorizationEndpointRequest.reqid = reqid;
	}
	
	
	
}
