package dao;

import exception.DaoException;
import model.Odontologo;
import java.util.Optional;

/**
 * Operaciones específicas de odontólogos, además del CRUD genérico.
 */
public interface OdontologoDao extends IDao<Odontologo> {

    /**
     * Busca un odontólogo por su matrícula profesional.
     *
     * @param matricula matrícula a buscar
     * @return el odontólogo encontrado, o vacío
     * @throws DaoException si la consulta falla
     */
    Optional<Odontologo> buscarPorMatricula(String matricula) throws DaoException;
}
