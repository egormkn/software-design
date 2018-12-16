package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

class QueryServletTest {

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
    void testMax() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("command")).thenReturn("max");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("command");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Product with max price"));
        assertTrue(result.contains("Product3"));
        assertTrue(result.contains("300"));
    }


    @Test
    void testMin() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("command")).thenReturn("min");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("command");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Product with min price"));
        assertTrue(result.contains("Product1"));
        assertTrue(result.contains("100"));
    }

    @Test
    void testSum() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("command")).thenReturn("sum");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("command");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Summary price"));
        assertTrue(result.contains("600"));
    }

    @Test
    void testCount() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("command")).thenReturn("count");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("command");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Number of products"));
        assertTrue(result.contains("3"));
    }

    @Test
    @DisplayName("Unknown command")
    void testUnknown() throws IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("command")).thenReturn("abcde");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);

        new QueryServlet().doGet(request, response);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter("command");
        writer.flush(); // it may not have been flushed yet...

        String result = stringWriter.toString();
        assertTrue(result.contains("Unknown command"));
        assertTrue(result.contains("abcde"));
    }
}