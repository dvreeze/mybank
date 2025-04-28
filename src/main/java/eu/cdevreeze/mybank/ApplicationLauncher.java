package eu.cdevreeze.mybank;

import eu.cdevreeze.mybank.context.MyBankApplicationConfiguration;
import eu.cdevreeze.mybank.web.TransactionServlet;
import eu.cdevreeze.mybank.web.WelcomeServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class ApplicationLauncher {

    public static void main(String[] args) throws LifecycleException {
        GenericApplicationContext appContext =
                new AnnotationConfigApplicationContext(MyBankApplicationConfiguration.class);
        appContext.registerShutdownHook();

        int port = Integer.parseInt(System.getProperty("server.port", "8080"));
        System.out.println("Port: " + port);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Wrapper welcomeServlet = Tomcat.addServlet(
                context,
                "welcomeServlet",
                appContext.getBean(WelcomeServlet.class)
        );
        welcomeServlet.setLoadOnStartup(1);
        welcomeServlet.addMapping("/*");

        Wrapper transactionServlet = Tomcat.addServlet(
                context,
                "transactionServlet",
                appContext.getBean(TransactionServlet.class)
        );
        transactionServlet.setLoadOnStartup(1);
        transactionServlet.addMapping("/transactions");

        tomcat.start();
    }
}
