package exception;

/**
 * Error de la capa de acceso a datos (conexión, SQL o restricciones de la base).
 */
public class DaoException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción comprensible para el usuario
     * @param causa   excepción original de JDBC
     */
    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
