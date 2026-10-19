package dao;

import java.util.List;

/**
 * Interfaz genérica para el patrón DAO.
 * @param <T> Tipo de entidad que va a manejar.
 */
public interface IDao<T> {
    T guardar(T t);
    T buscar(Integer id);
    void eliminar(Integer id);
    List<T> buscarTodos();
}