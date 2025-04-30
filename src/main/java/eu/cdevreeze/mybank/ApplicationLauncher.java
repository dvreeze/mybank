package eu.cdevreeze.mybank;

import eu.cdevreeze.mybank.context.ApplicationConfiguration;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationLauncher {

    public static void main(String[] args) throws LifecycleException {
        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        System.out.println("Port: " + port);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context tomcatContext = tomcat.addContext("", null);
        WebApplicationContext appContext = createApplicationContext(tomcatContext.getServletContext());

        Wrapper dispatcherServlet = Tomcat.addServlet(
                tomcatContext,
                "dispatcherServlet",
                new DispatcherServlet(appContext)
        );
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/*");

        tomcat.start();
    }

    private static WebApplicationContext createApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
        appCtx.register(ApplicationConfiguration.class);
        appCtx.setServletContext(servletContext);
        appCtx.refresh();
        appCtx.registerShutdownHook();
        return appCtx;
    }
}
