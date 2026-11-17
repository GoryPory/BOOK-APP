/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookapp.bookapp.model;

/**
 *
 * @author radit
 */
import java.util.List;

public interface BaseModel<T, ID> {
    default void create(T item) {
        throw new UnsupportedOperationException("Create tidak diizinkan untuk model ini.");
    }

    default List<T> readAll() {
        throw new UnsupportedOperationException("ReadAll tidak diizinkan untuk model ini.");
    }

    default List<T> search(String keyword) {
        throw new UnsupportedOperationException("Search tidak diizinkan untuk model ini.");
    }

    default void update(T item) {
        throw new UnsupportedOperationException("Update tidak diizinkan untuk model ini.");
    }

    default void delete(ID id) {
        throw new UnsupportedOperationException("Delete tidak diizinkan untuk model ini.");
    }
}