package ru.akirakozov.sd.refactoring.db;

import java.sql.SQLException;
import java.util.List;

public class ProductDAO implements DAO<Product> {

    @Override
    public List<Product> getAll() {
        return Database.getAll("SELECT * FROM PRODUCT", rs -> {
            try {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                return new Product(name, price);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void add(Product product) {
        String[] params = {
                product.getName(),
                String.valueOf(product.getPrice())
        };
        Database.execute("INSERT INTO PRODUCT (NAME, PRICE) VALUES (?, ?)", params);
    }

    public Product getMax() {
        return Database.get("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", rs -> {
            try {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                return new Product(name, price);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Product getMin() {
        return Database.get("SELECT * FROM PRODUCT ORDER BY PRICE ASC LIMIT 1", rs -> {
            try {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                return new Product(name, price);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int sum() {
        return Database.get("SELECT SUM(price) FROM PRODUCT", rs -> {
            try {
                return rs.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int count() {
        return Database.get("SELECT COUNT(*) FROM PRODUCT", rs -> {
            try {
                return rs.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}