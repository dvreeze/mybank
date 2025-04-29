package eu.cdevreeze.mybank.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class SpringBootstrappingServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringContainerHolder.getInstance().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        SpringContainerHolder.getInstance().destroy();
    }
}
