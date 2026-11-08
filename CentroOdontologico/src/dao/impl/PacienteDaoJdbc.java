package dao.impl;

import config.ConexionMySQL;
import dao.PacienteDao;
import exception.DaoException;
import model.Paciente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de {@link PacienteDao} sobre la tabla {@code pacientes} de MySQL.
 * <p>
 * Cada método abre su conexión con try-with-resources, así Connection,
 * PreparedStatement y ResultSet siempre se cierran. Los errores SQL no se
 * silencian: se envuelven en {@link DaoException} y se propagan al controlador.
 */
public class PacienteDaoJdbc implements PacienteDao {

    private static final String SELECT_BASE = "SELECT id, nombre, apellido, dni, fecha_ingreso FROM pacientes";
    private static final String INSERT = "INSERT INTO pacientes (nombre, apellido, dni, fecha_ingreso) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE pacientes SET nombre = ?, apellido = ?, dni = ?, fecha_ingreso = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM pacientes WHERE id = ?";
    private static final String BY_ID = SELECT_BASE + " WHERE id = ?";
    private static final String BY_DNI = SELECT_BASE + " WHERE dni = ?";
    private static final String ALL = SELECT_BASE + " ORDER BY apellido, nombre";

    /** {@inheritDoc} */
    @Override
    public Paciente guardar(Paciente paciente) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            cargarParametros(ps, paciente);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    paciente.setId(claves.getInt(1));
                }
            }
            return paciente;
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar el paciente en la base de datos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Paciente> buscarPorId(Integer id) throws DaoException {
        return buscarUno(BY_ID, id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Paciente> buscarPorDni(String dni) throws DaoException {
        return buscarUno(BY_DNI, dni);
    }

    /** {@inheritDoc} */
    @Override
    public List<Paciente> buscarTodos() throws DaoException {
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pacientes.add(mapear(rs));
            }
            return pacientes;
        } catch (SQLException e) {
            throw new DaoException("No se pudo obtener el listado de pacientes.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean actualizar(Paciente paciente) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            cargarParametros(ps, paciente);
            ps.setInt(5, paciente.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el paciente.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean eliminar(Integer id) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoException("No se puede eliminar el paciente porque tiene turnos asociados.", e);
        } catch (SQLException e) {
            throw new DaoException("No se pudo eliminar el paciente.", e);
        }
    }

    private Optional<Paciente> buscarUno(String sql, Object parametro) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo consultar el paciente.", e);
        }
    }

    private void cargarParametros(PreparedStatement ps, Paciente p) throws SQLException {
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getApellido());
        ps.setString(3, p.getDni());
        ps.setDate(4, Date.valueOf(p.getFechaIngreso()));
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        return new Paciente(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("dni"), rs.getDate("fecha_ingreso").toLocalDate());
    }
}
