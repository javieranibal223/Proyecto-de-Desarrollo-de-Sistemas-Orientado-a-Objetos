package config;

import exception.DaoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica centralizada de conexiones JDBC a MySQL.
 * <p>
 * La URL y las credenciales se leen de las variables de entorno {@code DB_URL},
 * {@code DB_USER} y {@code DB_PASS}. Si no existen, se usan valores por defecto
 * para desarrollo local, así no quedan credenciales fijas en el código.
 */
public final class ConexionMySQL {

    private static final String URL = System.getenv().getOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/centro_odontologico?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires");
    private static final String USUARIO = System.getenv().getOrDefault("DB_USER", "root");
    private static final String CLAVE = System.getenv().getOrDefault("DB_PASS", "");

    private ConexionMySQL() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Abre una conexión nueva con la base de datos.
     *
     * @return conexión activa; debe cerrarse (por ejemplo, con try-with-resources)
     * @throws DaoException si no se puede conectar con el servidor MySQL
     */
    public static Connection obtener() throws DaoException {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            throw new DaoException("No se pudo conectar con MySQL. Verifique que el servidor esté activo "
                    + "y los datos de conexión. Detalle: " + e.getMessage(), e);
        }
    }
}
