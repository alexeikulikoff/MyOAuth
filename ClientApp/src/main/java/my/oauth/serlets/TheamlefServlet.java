package my.oauth.serlets;

import javax.servlet.ServletContext;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public interface TheamlefServlet {

	default TemplateEngine initTemplateEngine(ServletContext context) {
		TemplateEngine templateEngine = new TemplateEngine();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode("xhtml");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(3600000L);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}
}
