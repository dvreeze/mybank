package eu.cdevreeze.mybank.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getRequestURI().equalsIgnoreCase("/")) {
            response.setContentType("text/html; charset=UTF-8");
            String welcomeHtml = """
                    <html>
                      <body>
                        <h1>Transactions web application</h1>
                        <p>Very low tech implementation, directly using the Servlet API</p>
                      </body>
                    </html>""";
            response.getWriter().print(welcomeHtml);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
