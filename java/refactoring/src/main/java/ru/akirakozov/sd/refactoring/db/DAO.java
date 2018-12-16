package ru.akirakozov.sd.refactoring.db;

import java.util.List;

public interface DAO<T> {

    List<T> getAll();

    void add(T t);
}
