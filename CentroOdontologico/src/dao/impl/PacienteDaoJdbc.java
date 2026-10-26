package dao.impl;

import dao.IDao;
import model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDaoJdbc implements IDao<Paciente> {
    
    // Configura tu URL, usuario y contraseña según tu base de datos (ej. H2 o MySQL)
    private static final String DB_URL = "jdbc:h2:~/centro_odontologico;INIT=RUNSCRIPT FROM 'create.sql'";
    private static final String USER = "sa";
    private static final String PASS = "";

    @Override
    public Paciente guardar(Paciente paciente) {
        String query = "INSERT INTO pacientes (nombre, apellido, dni, fecha_ingreso) VALUES (?, ?, ?, ?)";
        
        // try-with-resources: Inicializa Connection y PreparedStatement aquí
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaIngreso())); // Suponiendo LocalDate
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paciente;
    }

    @Override
    public Paciente buscar(Integer id) {
        Paciente paciente = null;
        String query = "SELECT * FROM pacientes WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query)) {
             
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente();
                    paciente.setId(rs.getInt("id"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setApellido(rs.getString("apellido"));
                    paciente.setDni(rs.getString("dni"));
                    paciente.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paciente;
    }

    @Override
    public Paciente actualizar(Paciente paciente) {
        String query = "UPDATE pacientes SET nombre = ?, apellido = ?, dni = ?, fecha_ingreso = ? WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query)) {
             
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setString(3, paciente.getDni());
            ps.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            ps.setInt(5, paciente.getId());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paciente;
    }

    @Override
    public void eliminar(Integer id) {
        String query = "DELETE FROM pacientes WHERE id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query)) {
             
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paciente> buscarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        String query = "SELECT * FROM pacientes";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setDni(rs.getString("dni"));
                p.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
                pacientes.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
}