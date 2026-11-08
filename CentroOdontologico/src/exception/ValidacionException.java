package exception;

/**
 * Se lanza cuando los datos ingresados no cumplen las reglas de negocio.
 */
public class ValidacionException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje motivo de la validación fallida
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
