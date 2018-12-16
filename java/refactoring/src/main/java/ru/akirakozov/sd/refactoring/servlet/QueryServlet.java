package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.ProductDAO;
import ru.akirakozov.sd.refactoring.web.Template;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {

    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command").toLowerCase();

        Template template = Template.load("query.html");

        switch (command) {
            case "max":
                template.set("message", "Product with max price")
                        .set("result", dao.getMax());
                break;
            case "min":
                template.set("message", "Product with min price")
                        .set("result", dao.getMin());
                break;
            case "sum":
                template.set("message", "Summary price")
                        .set("result", dao.sum());
                break;
            case "count":
                template.set("message", "Number of products")
                        .set("result", dao.count());
                break;
            default:
                template.set("message", "Unknown command")
                        .set("result", command);
                break;
        }

        template.render(response.getWriter());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
