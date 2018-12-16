package ru.akirakozov.sd.refactoring.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Database {

    private static Dotenv env = Dotenv
            .configure()
            .ignoreIfMissing()
            .ignoreIfMalformed()
            .load();

    public static Connection getConnection() {
        String url = Optional.ofNullable(env.get("DATABASE_URL")).orElse("sqlite:test.db");
        return getConnection(url);
    }

    public static Connection getConnection(String url) {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:" + url);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
        return connection;
    }

    public static <T> T get(String sql, Function<ResultSet, T> builder) {
        T result = null;

        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                result = builder.apply(resultSet);
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static <T> List<T> getAll(String sql, Function<ResultSet, T> builder) {
        List<T> result = new ArrayList<>();

        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                result.add(builder.apply(resultSet));
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static int execute(String sql) {
        int result;
        try (Connection connection = Database.getConnection()) {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int execute(String sql, String[] params) {
        int result;
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            result = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
