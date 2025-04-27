package eu.cdevreeze.mybank;

import eu.cdevreeze.mybank.context.Application;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class ApplicationLauncher {

    public static void main(String[] args) throws LifecycleException {
        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        System.out.println("Port: " + port);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Wrapper welcomeServlet = Tomcat.addServlet(
                context,
                "welcomeServlet",
                Application.welcomeServlet
        );
        welcomeServlet.setLoadOnStartup(1);
        welcomeServlet.addMapping("/*");

        Wrapper transactionServlet = Tomcat.addServlet(
                context,
                "transactionServlet",
                Application.transactionServlet
        );
        transactionServlet.setLoadOnStartup(1);
        transactionServlet.addMapping("/transactions");

        tomcat.start();
    }
}
