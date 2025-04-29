package eu.cdevreeze.mybank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.cdevreeze.mybank.model.Transaction;
import eu.cdevreeze.mybank.service.TransactionService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class TransactionServlet extends HttpServlet {

    private static final String ID = "id";
    private static final String REFERENCE = "reference";
    private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    private TransactionService transactionService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // I'd rather do this from a ServletContextListener
        SpringContainerHolder.getInstance().initOnce();
        ApplicationContext appContext = SpringContainerHolder.getInstance().getContainer();
        this.transactionService = appContext.getBean(TransactionService.class);
        this.objectMapper = appContext.getBean(ObjectMapper.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        // I'd rather do this from a ServletContextListener
        SpringContainerHolder.getInstance().destroyOnce();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> parameterNames = new HashSet<>(Collections.list(request.getParameterNames()));

        if (request.getRequestURI().equalsIgnoreCase("/transactions")) {
            if (parameterNames.equals(Set.of(ID))) {
                findTransactionById(request, response);
            } else if (parameterNames.equals(Set.of(REFERENCE))) {
                findTransactionsByReference(request, response);
            } else if (parameterNames.isEmpty()) {
                findAllTransactions(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().equalsIgnoreCase("/transactions")) {
            createTransaction(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void findAllTransactions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Transaction> transactions = transactionService.findAll();

        String json = objectMapper.writeValueAsString(transactions);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().print(json);
    }

    private void findTransactionById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = Objects.requireNonNull(request.getParameter(ID));
        Optional<Transaction> transactionOption = transactionService.findById(id);

        String json = objectMapper.writeValueAsString(transactionOption);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().print(json);
    }

    private void findTransactionsByReference(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reference = Objects.requireNonNull(request.getParameter(REFERENCE));
        List<Transaction> transactions = transactionService.findByReference(reference);

        String json = objectMapper.writeValueAsString(transactions);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().print(json);
    }

    private void createTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int amount = Integer.parseInt(Objects.requireNonNull(request.getParameter("amount")));
        String reference = Objects.requireNonNull(request.getParameter(REFERENCE));
        Transaction transaction = transactionService.create(amount, Instant.now(), reference);

        String json = objectMapper.writeValueAsString(transaction);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().print(json);
    }
}
