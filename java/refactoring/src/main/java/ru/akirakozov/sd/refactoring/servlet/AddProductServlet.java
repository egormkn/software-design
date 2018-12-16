package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.Product;
import ru.akirakozov.sd.refactoring.db.ProductDAO;
import ru.akirakozov.sd.refactoring.web.Template;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String price = request.getParameter("price");

        Product product = new Product(name, Integer.parseInt(price));
        dao.add(product);

        Template.load("add.html")
                .render(response.getWriter());

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
