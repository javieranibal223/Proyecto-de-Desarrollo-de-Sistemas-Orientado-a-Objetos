package dao;

import exception.DaoException;
import model.Paciente;
import java.util.Optional;

/**
 * Operaciones específicas de pacientes, además del CRUD genérico.
 */
public interface PacienteDao extends IDao<Paciente> {

    /**
     * Busca un paciente por su DNI.
     *
     * @param dni documento a buscar
     * @return el paciente encontrado, o vacío
     * @throws DaoException si la consulta falla
     */
    Optional<Paciente> buscarPorDni(String dni) throws DaoException;
}
