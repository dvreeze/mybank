package eu.cdevreeze.mybank;

import eu.cdevreeze.mybank.context.MyBankApplicationConfiguration;
import eu.cdevreeze.mybank.web.TransactionServlet;
import eu.cdevreeze.mybank.web.WelcomeServlet;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ApplicationLauncher {

    public static void main(String[] args) throws LifecycleException {
        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        System.out.println("Port: " + port);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context tomcatContext = tomcat.addContext("", null);
        WebApplicationContext appContext = createApplicationContext(tomcatContext.getServletContext());

        Wrapper welcomeServlet = Tomcat.addServlet(
                tomcatContext,
                "welcomeServlet",
                new WelcomeServlet()
        );
        welcomeServlet.setLoadOnStartup(1);
        welcomeServlet.addMapping("/*");

        Wrapper transactionServlet = Tomcat.addServlet(
                tomcatContext,
                "transactionServlet",
                new TransactionServlet(appContext)
        );
        transactionServlet.setLoadOnStartup(1);
        transactionServlet.addMapping("/transactions");

        tomcat.start();
    }

    private static WebApplicationContext createApplicationContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
        appCtx.register(MyBankApplicationConfiguration.class);
        appCtx.setServletContext(servletContext);
        appCtx.refresh();
        appCtx.registerShutdownHook();
        return appCtx;
    }
}
