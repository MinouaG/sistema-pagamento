package dev.minoua.controller;

import dev.minoua.model.Model;
import java.util.List;

public abstract class Controller<T extends Model<T>> {

    public abstract List<T> getAll();

    public abstract T getById(Long id);

    public abstract T create(T object);

    public abstract T update(Long id, T object);

    public abstract T partialUpdate(Long id, T object);

    public abstract boolean delete(Long id);
}