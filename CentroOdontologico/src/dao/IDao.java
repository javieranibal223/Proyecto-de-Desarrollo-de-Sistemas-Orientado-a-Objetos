package dao;

import exception.DaoException;
import java.util.List;
import java.util.Optional;

/**
 * Contrato CRUD genérico del patrón DAO.
 * Todas las operaciones propagan {@link DaoException}; ninguna implementación
 * debe silenciar errores SQL.
 *
 * @param <T> tipo de entidad persistida
 */
public interface IDao<T> {

    /**
     * Inserta la entidad y le asigna el identificador generado.
     *
     * @param entidad entidad nueva, sin identificador
     * @return la misma entidad, ya con su ID
     * @throws DaoException si la inserción falla
     */
    T guardar(T entidad) throws DaoException;

    /**
     * Busca una entidad por su clave primaria.
     *
     * @param id clave primaria
     * @return la entidad, o vacío si no existe
     * @throws DaoException si la consulta falla
     */
    Optional<T> buscarPorId(Integer id) throws DaoException;

    /**
     * @return todas las entidades persistidas
     * @throws DaoException si la consulta falla
     */
    List<T> buscarTodos() throws DaoException;

    /**
     * @param entidad entidad con identificador
     * @return {@code true} si se actualizó una fila
     * @throws DaoException si la actualización falla
     */
    boolean actualizar(T entidad) throws DaoException;

    /**
     * @param id clave primaria de la entidad a borrar
     * @return {@code true} si se eliminó una fila
     * @throws DaoException si la eliminación falla
     */
    boolean eliminar(Integer id) throws DaoException;
}
