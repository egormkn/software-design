package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AddProductServletTest {

    @BeforeAll
    static void init() {
        execute("DROP TABLE PRODUCT");
        execute("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME  TEXT    NOT NULL, " +
                " PRICE INT     NOT NULL)");
        execute("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"Product1\", 100)");
        execute("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"Product2\", 200)");
        execute("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"Product3\", 300)");
    }

    private static void execute(String sql) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAdd() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("name")).thenReturn("Product4");
        Mockito.when(request.getParameter("price")).thenReturn("400");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new AddProductServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("name");
        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("price");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("OK"));

        check();
    }

    private void check() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new GetProductsServlet().doGet(request, response);

        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Product1"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("Product2"));
        assertTrue(result.contains("200"));
        assertTrue(result.contains("Product3"));
        assertTrue(result.contains("300"));
        assertTrue(result.contains("Product4"));
        assertTrue(result.contains("400"));
    }
}