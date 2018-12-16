package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.db.ProductDAO;
import ru.akirakozov.sd.refactoring.web.Template;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = dao.getAll();
        Template.load("get.html")
                .set("list", products)
                .render(response.getWriter());
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
