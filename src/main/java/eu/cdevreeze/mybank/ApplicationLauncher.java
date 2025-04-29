package eu.cdevreeze.mybank;

import eu.cdevreeze.mybank.web.TransactionServlet;
import eu.cdevreeze.mybank.web.WelcomeServlet;
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
                new WelcomeServlet()
        );
        welcomeServlet.setLoadOnStartup(1);
        welcomeServlet.addMapping("/*");

        Wrapper transactionServlet = Tomcat.addServlet(
                context,
                "transactionServlet",
                new TransactionServlet()
        );
        transactionServlet.setLoadOnStartup(1);
        transactionServlet.addMapping("/transactions");

        tomcat.start();
    }
}
