package my.oauth.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.CorsFilter;

import com.google.gson.Gson;

public class MainServlet extends HttpServlet {
	/**
	* 
	*/
	
	
	private static final long serialVersionUID = 1L;
	
	public static final String ANY_ORIGIN = "*";
	
	private static FilterConfig generateFilterConfig(
	            final String allowedHttpHeaders, final String allowedHttpMethods,
	            final String allowedOrigins, final String exposedHeaders,
	            final String supportCredentials, final String preflightMaxAge,
	            final String decorateRequest) {
	        FilterConfig filterConfig = new FilterConfig() {

	            @Override
	            public String getFilterName() {
	                return "cors-filter";
	            }

	          
	            @Override
	            public String getInitParameter(String name) {
	                if (CorsFilter.PARAM_CORS_ALLOWED_HEADERS
	                        .equalsIgnoreCase(name)) {
	                    return allowedHttpHeaders;
	                } else if (CorsFilter.PARAM_CORS_ALLOWED_METHODS
	                        .equalsIgnoreCase(name)) {
	                    return allowedHttpMethods;
	                } else if (CorsFilter.PARAM_CORS_ALLOWED_ORIGINS
	                        .equalsIgnoreCase(name)) {
	                    return allowedOrigins;
	                } else if (CorsFilter.PARAM_CORS_EXPOSED_HEADERS
	                        .equalsIgnoreCase(name)) {
	                    return exposedHeaders;
	                } else if (CorsFilter.PARAM_CORS_SUPPORT_CREDENTIALS
	                        .equalsIgnoreCase(name)) {
	                    return supportCredentials;
	                } else if (CorsFilter.PARAM_CORS_PREFLIGHT_MAXAGE
	                        .equalsIgnoreCase(name)) {
	                    return preflightMaxAge;
	                } else if (CorsFilter.PARAM_CORS_REQUEST_DECORATE
	                        .equalsIgnoreCase(name)) {
	                    return decorateRequest;
	                }
	                return null;
	            }

	            @Override
	            public Enumeration<String> getInitParameterNames() {
	                return null;
	            }


				@Override
				public ServletContext getServletContext() {
					// TODO Auto-generated method stub
					return this.getServletContext();
				}
	        };

	        return filterConfig;
	} 
	public static FilterConfig getDefaultFilterConfig() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = CorsFilter.DEFAULT_DECORATE_REQUEST;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */ }
	
		Gson g = new Gson();
		Param param = g.fromJson(jb.toString(), Param.class);
	
		System.out.println(param);
		
		PrintWriter writer = resp.getWriter();
		Param responce = new Param();
//		responce.setValue("hello again!");
//		resp.setContentType("application/json; charset=utf-8");
	
		resp.setContentType("text/plain");
		//resp.setHeader("Location:", "http://localhost:8002/authorize");
		resp.sendRedirect("http://localhost:8002/authorize");
		
		//out.write("http://localhost:8002/authorize" );
	//	writer.write("hello");
	//	writer.close();
		
	}
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException {

		RequestDispatcher view = req.getRequestDispatcher("index.html");
		try {
			view.forward(req, resp);

		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
