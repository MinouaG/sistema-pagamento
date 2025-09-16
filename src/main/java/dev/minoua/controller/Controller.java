package dev.minoua.controller;

import dev.minoua.model.Model;

import java.util.List;

public class Controller<M extends Model<M>> {
    public M model;

    // GET -> LISTAR TODOS OS REGISTROS
    public List<?> get() {
        try {
            return model.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // GET -> LISTAR UM REGISTRO PELO ID
    public Object get(Long id) {
        try {
            return model.find(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    // POST -> CRIAR UM NOVO REGISTRO
    public M post(M object) {
        try {
            model.save(object);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // PUT -> ATUALIZAR UM REGISTRO PELO ID
    public M put(Long id, M object) {
        try {
            M existingObject = model.find(id);
            if (existingObject != null) {
                model.update(id, object);
                return object;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // PATCH -> ATUALIZAR PARCIALMENTE UM REGISTRO PELO ID
    public M patch(Long id, M object) {
        try {
            M existingObject = model.find(id);
            if (existingObject != null) {
                model.partialUpdate(id, object);
                return object;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // DELETE -> DELETAR UM REGISTRO PELO ID
    public boolean delete(Long id) {
        try {
            return model.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}