/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package my.oauth.authserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

public class AuthServer {
	public String getGreeting() {
		return "Hello world.";
	}
	private static final int port = 8002;

	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort( port );
		tomcat.getConnector();
		String contextPath = "";
		File base = new File(System.getProperty("java.io.tmpdir"));
		String docBase = base.getAbsolutePath();
		String templateBase = new File("src/main/resources/templates").getAbsolutePath();
		Context templateContext = tomcat.addWebapp(contextPath, templateBase);

		ErrorPage ep = new ErrorPage();
		ep.setErrorCode(404);
		ep.setLocation("/error.html");
		templateContext.addErrorPage(ep);
		templateContext.addMimeMapping("ext", "type");

		tomcat.addWebapp("/js/",
				new File("src/main/resources/static/js").getAbsolutePath());
		tomcat.addWebapp("/css/",
				new File("src/main/resources/static/css").getAbsolutePath());

		Tomcat.addServlet(templateContext, "MainServlet", new MainServlet());
		Tomcat.addServlet(templateContext, "Authorizer", new Authorizer());
		templateContext.addServletMappingDecoded("/app", "MainServlet");
		templateContext.addServletMappingDecoded("/authorize", "Authorizer");

		try {
			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException e) {

			e.printStackTrace();
		}

		/*
		 * ServerSocket serverSocket; try { serverSocket = new
		 * ServerSocket(port); System.out.println("Start listenning on port: " +
		 * port); try (Socket connection = serverSocket.accept()) {
		 * 
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * connection.getInputStream()) ); String line = null; while((line =
		 * reader.readLine())!=null) { System.out.println(line); } } catch
		 * (IOException e) {
		 * 
		 * System.out.println(e.getMessage());
		 * 
		 * } } catch (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
	}
}
