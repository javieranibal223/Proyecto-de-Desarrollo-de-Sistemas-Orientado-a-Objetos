package exception;

/**
 * Error al generar un archivo de exportación (PDF).
 */
public class ExportacionException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción del problema
     * @param causa   excepción original de E/S
     */
    public ExportacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
