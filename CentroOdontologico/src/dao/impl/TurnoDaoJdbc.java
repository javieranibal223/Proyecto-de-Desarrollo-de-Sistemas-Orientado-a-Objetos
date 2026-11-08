package dao.impl;

import config.ConexionMySQL;
import dao.TurnoDao;
import exception.DaoException;
import model.Odontologo;
import model.Paciente;
import model.Turno;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de {@link TurnoDao}.
 * <p>
 * Las consultas hacen JOIN con {@code pacientes} y {@code odontologos} para
 * reconstruir la entidad combinada completa en una sola lectura.
 */
public class TurnoDaoJdbc implements TurnoDao {

    private static final String SELECT_BASE =
            "SELECT t.id, t.fecha_hora, t.motivo, "
            + "p.id AS paciente_id, p.nombre AS paciente_nombre, p.apellido AS paciente_apellido, "
            + "p.dni AS paciente_dni, p.fecha_ingreso AS paciente_fecha, "
            + "o.id AS odontologo_id, o.matricula AS odontologo_matricula, o.nombre AS odontologo_nombre, "
            + "o.apellido AS odontologo_apellido, o.especialidad AS odontologo_especialidad "
            + "FROM turnos t "
            + "JOIN pacientes p ON p.id = t.paciente_id "
            + "JOIN odontologos o ON o.id = t.odontologo_id";
    private static final String BY_ID = SELECT_BASE + " WHERE t.id = ?";
    private static final String ALL = SELECT_BASE + " ORDER BY t.fecha_hora";
    private static final String INSERT = "INSERT INTO turnos (paciente_id, odontologo_id, fecha_hora, motivo) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE turnos SET paciente_id = ?, odontologo_id = ?, fecha_hora = ?, motivo = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM turnos WHERE id = ?";

    /** {@inheritDoc} */
    @Override
    public Turno guardar(Turno turno) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            cargarParametros(ps, turno);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    turno.setId(claves.getInt(1));
                }
            }
            return turno;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoException("No se pudo guardar el turno: el odontólogo ya tiene un turno en esa fecha y hora.", e);
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar el turno en la base de datos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Turno> buscarPorId(Integer id) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo consultar el turno.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<Turno> buscarTodos() throws DaoException {
        List<Turno> turnos = new ArrayList<>();
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                turnos.add(mapear(rs));
            }
            return turnos;
        } catch (SQLException e) {
            throw new DaoException("No se pudo obtener el listado de turnos.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean actualizar(Turno turno) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            cargarParametros(ps, turno);
            ps.setInt(5, turno.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DaoException("No se pudo modificar el turno: el odontólogo ya tiene un turno en esa fecha y hora.", e);
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el turno.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean eliminar(Integer id) throws DaoException {
        try (Connection con = ConexionMySQL.obtener();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo eliminar el turno.", e);
        }
    }

    private void cargarParametros(PreparedStatement ps, Turno t) throws SQLException {
        ps.setInt(1, t.getPaciente().getId());
        ps.setInt(2, t.getOdontologo().getId());
        ps.setTimestamp(3, Timestamp.valueOf(t.getFechaHora()));
        ps.setString(4, t.getMotivo().isEmpty() ? null : t.getMotivo());
    }

    private Turno mapear(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente(rs.getInt("paciente_id"), rs.getString("paciente_nombre"),
                rs.getString("paciente_apellido"), rs.getString("paciente_dni"),
                rs.getDate("paciente_fecha").toLocalDate());
        Odontologo odontologo = new Odontologo(rs.getInt("odontologo_id"), rs.getString("odontologo_matricula"),
                rs.getString("odontologo_nombre"), rs.getString("odontologo_apellido"),
                rs.getString("odontologo_especialidad"));
        return new Turno(rs.getInt("id"), paciente, odontologo,
                rs.getTimestamp("fecha_hora").toLocalDateTime(), rs.getString("motivo"));
    }
}
