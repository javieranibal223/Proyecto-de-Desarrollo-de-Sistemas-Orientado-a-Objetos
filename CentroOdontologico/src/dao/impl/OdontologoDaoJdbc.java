package dao.impl;

import config.ConexionMySQL;
import dao.OdontologoDao;
import exception.DaoException;
import model.Odontologo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de {@link OdontologoDao} sobre la tabla {@code odontologos}.
 * <p>
 * Usa try-with-resources en cada método y propaga los errores SQL como {@link DaoException}.
 */
public class OdontologoDaoJdbc implements OdontologoDao {

    private static final String SELECT_BASE = "SELECT id, matricula, nombre, apellido, especialidad FROM odontologos";
    private static final String INSERT = "INSERT INTO odontologos (matricula, nombre, apellido, especialidad) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE odontologos SET matricula = ?, nombre = ?, apellido = ?, especialidad = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM odontologos WHERE id = ?";
    private static final String BY_ID = SELECT_BASE + " WHERE id = ?";
    private static final String BY_MATRICULA = SELECT_BASE + " WHERE matricula = ?";
    private static final String ALL = SELECT_BASE + " ORDER BY apellido, nombre";

    /** {@inheritDoc} */
    @Override
    public Odontologo guardar(Odontologo odontologo) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            cargarParametros(ps, odontologo);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    odontologo.setId(claves.getInt(1));
                }
            }
            return odontologo;
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar el odontólogo en la base de datos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Odontologo> buscarPorId(Integer id) throws DaoException {
        return buscarUno(BY_ID, id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Odontologo> buscarPorMatricula(String matricula) throws DaoException {
        return buscarUno(BY_MATRICULA, matricula);
    }

    /** {@inheritDoc} */
    @Override
    public List<Odontologo> buscarTodos() throws DaoException {
        List<Odontologo> odontologos = new ArrayList<>();
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                odontologos.add(mapear(rs));
            }
            return odontologos;
        } catch (SQLException e) {
            throw new DaoException("No se pudo obtener el listado de odontólogos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean actualizar(Odontologo odontologo) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            cargarParametros(ps, odontologo);
            ps.setInt(5, odontologo.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el odontólogo.", e);
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
            throw new DaoException("No se puede eliminar el odontólogo porque tiene turnos asociados.", e);
        } catch (SQLException e) {
            throw new DaoException("No se pudo eliminar el odontólogo.", e);
        }
    }

    private Optional<Odontologo> buscarUno(String sql, Object parametro) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo consultar el odontólogo.", e);
        }
    }

    private void cargarParametros(PreparedStatement ps, Odontologo o) throws SQLException {
        ps.setString(1, o.getMatricula());
        ps.setString(2, o.getNombre());
        ps.setString(3, o.getApellido());
        ps.setString(4, o.getEspecialidad());
    }

    private Odontologo mapear(ResultSet rs) throws SQLException {
        return new Odontologo(rs.getInt("id"), rs.getString("matricula"), rs.getString("nombre"),
                rs.getString("apellido"), rs.getString("especialidad"));
    }
}
