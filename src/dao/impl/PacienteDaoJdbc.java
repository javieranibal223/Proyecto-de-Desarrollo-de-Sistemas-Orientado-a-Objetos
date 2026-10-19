package dao.impl;

import dao.PacienteDao;
import model.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de acceso a datos para Paciente utilizando JDBC.
 */
public class PacienteDaoJdbc implements PacienteDao {

    // Configuración de base de datos (Ajusta la URL, usuario y contraseña según tu motor de DB)
    private final static String DB_JDBC_DRIVER = "org.h2.Driver";
    private final static String DB_URL = "jdbc:h2:~/centro_odontologico;INIT=RUNSCRIPT FROM 'create.sql'";
    private final static String DB_USER ="sa";
    private final static String DB_PASSWORD = "";

    // Consultas SQL con columnas explícitas y campos nuevos
    private final static String SQL_INSERT = "INSERT INTO pacientes (nombre, apellido, dni, telefono, obra_social) VALUES (?, ?, ?, ?, ?)";
    private final static String SQL_SELECT = "SELECT id, nombre, apellido, dni, telefono, obra_social FROM pacientes WHERE id = ?";
    private final static String SQL_DELETE = "DELETE FROM pacientes WHERE id = ?";
    private final static String SQL_SELECT_ALL = "SELECT id, nombre, apellido, dni, telefono, obra_social FROM pacientes";
    private final static String SQL_SELECT_BY_DNI = "SELECT id, nombre, apellido, dni, telefono, obra_social FROM pacientes WHERE dni = ?";

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DB_JDBC_DRIVER);
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inicio de transacción manual

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paciente.getNombre());
            preparedStatement.setString(2, paciente.getApellido());
            preparedStatement.setString(3, paciente.getDni());
            preparedStatement.setString(4, paciente.getTelefono());
            preparedStatement.setString(5, paciente.getObraSocial());
            
            preparedStatement.executeUpdate();
            
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                paciente.setId(keys.getInt(1));
            }

            connection.commit(); // Confirmar transacción

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return paciente;
    }

    @Override
    public Paciente buscar(Integer id) {
        Paciente paciente = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT)) {
            
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                paciente = new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("obra_social")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paciente;
    }

    @Override
    public void eliminar(Integer id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE)) {
            
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paciente> buscarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL)) {
            
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                pacientes.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("obra_social")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        Paciente paciente = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_DNI)) {
            
            preparedStatement.setString(1, dni);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                paciente = new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("telefono"),
                        rs.getString("obra_social")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paciente;
    }
}