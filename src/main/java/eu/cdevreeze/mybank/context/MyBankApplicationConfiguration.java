package eu.cdevreeze.mybank.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.cdevreeze.mybank.service.TransactionService;
import eu.cdevreeze.mybank.web.TransactionServlet;
import eu.cdevreeze.mybank.web.WelcomeServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBankApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return getObjectMapper();
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionService();
    }

    @Bean
    public WelcomeServlet welcomeServlet() {
        return new WelcomeServlet();
    }

    @Bean
    public TransactionServlet transactionServlet(/* TransactionService transactionService, ObjectMapper objectMapper */) {
        return new TransactionServlet(/* transactionService, objectMapper */);
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        // See https://www.baeldung.com/jackson-serialize-dates
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
