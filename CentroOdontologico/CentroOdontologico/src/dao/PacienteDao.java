package dao;

import model.Paciente;

/**
 * Interfaz específica para la entidad Paciente.
 */
public interface PacienteDao extends IDao<Paciente> {
    // Método de búsqueda específico por DNI
    Paciente buscarPorDni(String dni);
}