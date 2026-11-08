package exception;

/**
 * Se lanza cuando la entidad solicitada no existe en la base de datos.
 */
public class NoEncontradoException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción de lo buscado
     */
    public NoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
