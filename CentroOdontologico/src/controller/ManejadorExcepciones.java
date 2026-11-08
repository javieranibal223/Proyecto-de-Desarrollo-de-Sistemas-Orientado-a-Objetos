package controller;

import exception.DaoException;
import exception.ExportacionException;
import exception.NoEncontradoException;
import exception.ValidacionException;
import java.util.function.Consumer;

/**
 * Utilidad común de los controladores: ejecuta una operación y traduce cualquier
 * excepción de la aplicación en un mensaje claro para el usuario, sin dejar que
 * la aplicación se caiga.
 */
public final class ManejadorExcepciones {

    /** Operación de controlador que puede lanzar excepciones de la aplicación. */
    @FunctionalInterface
    public interface Operacion {

        /**
         * Ejecuta la operación.
         *
         * @throws Exception errores de validación, de datos o de exportación
         */
        void realizar() throws Exception;
    }

    private ManejadorExcepciones() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Ejecuta la operación y muestra cualquier error mediante el callback indicado.
     *
     * @param operacion    operación a ejecutar
     * @param mostrarError callback que muestra el mensaje de error en la vista
     */
    public static void ejecutar(Operacion operacion, Consumer<String> mostrarError) {
        try {
            operacion.realizar();
        } catch (ValidacionException | NoEncontradoException | DaoException | ExportacionException e) {
            mostrarError.accept(e.getMessage());
        } catch (Exception e) {
            mostrarError.accept("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}
