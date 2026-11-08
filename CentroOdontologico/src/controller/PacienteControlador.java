package controller;

import dao.PacienteDao;
import exception.DaoException;
import exception.NoEncontradoException;
import exception.ValidacionException;
import export.ExportadorPacientesPdf;
import model.Paciente;
import view.PacienteVista;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del CRUD de pacientes.
 * <p>
 * Recibe las acciones de la {@link PacienteVista}, parsea y valida los datos,
 * invoca al {@link PacienteDao} y devuelve el resultado a la vista. Las excepciones
 * se traducen a mensajes mediante {@link ManejadorExcepciones}.
 */
public class PacienteControlador {

    private final PacienteVista vista;
    private final PacienteDao dao;
    private final ExportadorPacientesPdf exportador;

    /**
     * @param vista      pantalla que muestra los datos
     * @param dao        acceso a la tabla de pacientes
     * @param exportador generador del listado en PDF
     */
    public PacienteControlador(PacienteVista vista, PacienteDao dao, ExportadorPacientesPdf exportador) {
        this.vista = vista;
        this.dao = dao;
        this.exportador = exportador;
    }

    /** Carga en la tabla todos los pacientes registrados. */
    public void listar() {
        ejecutar(this::refrescarTabla);
    }

    /**
     * Registra un paciente nuevo tras validar sus datos y que el DNI no esté repetido.
     *
     * @param nombre     nombre ingresado
     * @param apellido   apellido ingresado
     * @param dni        DNI ingresado
     * @param fechaTexto fecha en formato DD/MM/AAAA
     */
    public void guardar(String nombre, String apellido, String dni, String fechaTexto) {
        ejecutar(() -> {
            Paciente nuevo = construir(null, nombre, apellido, dni, fechaTexto);
            validarDniDisponible(nuevo);
            Paciente guardado = dao.guardar(nuevo);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Paciente registrado con ID " + guardado.getId() + ".");
            refrescarTabla();
        });
    }

    /**
     * Actualiza los datos de un paciente existente.
     *
     * @param idTexto    identificador del paciente (seleccionado en la tabla)
     * @param nombre     nuevo nombre
     * @param apellido   nuevo apellido
     * @param dni        nuevo DNI
     * @param fechaTexto nueva fecha en formato DD/MM/AAAA
     */
    public void actualizar(String idTexto, String nombre, String apellido, String dni, String fechaTexto) {
        ejecutar(() -> {
            Integer id = parsearId(idTexto);
            Paciente paciente = construir(id, nombre, apellido, dni, fechaTexto);
            validarDniDisponible(paciente);
            if (!dao.actualizar(paciente)) {
                throw new NoEncontradoException("No existe un paciente con ID " + id + ".");
            }
            vista.mostrarMensaje("Paciente actualizado correctamente.");
            refrescarTabla();
        });
    }

    /**
     * Elimina un paciente. La confirmación del usuario la pide la vista antes de llamar aquí.
     *
     * @param idTexto identificador del paciente a eliminar
     */
    public void eliminar(String idTexto) {
        ejecutar(() -> {
            Integer id = parsearId(idTexto);
            if (!dao.eliminar(id)) {
                throw new NoEncontradoException("No existe un paciente con ID " + id + ".");
            }
            vista.limpiarFormulario();
            vista.mostrarMensaje("Paciente eliminado de los registros.");
            refrescarTabla();
        });
    }

    /**
     * Busca un paciente por DNI y lo muestra en la tabla.
     *
     * @param dni documento a buscar
     */
    public void buscarPorDni(String dni) {
        ejecutar(() -> {
            Optional<Paciente> encontrado = dao.buscarPorDni(dni.trim());
            if (encontrado.isEmpty()) {
                throw new NoEncontradoException("No se encontró ningún paciente con DNI " + dni + ".");
            }
            vista.mostrarPacientes(List.of(encontrado.get()));
        });
    }

    /**
     * Genera un PDF con el listado completo de pacientes.
     *
     * @param destino archivo PDF a crear
     */
    public void exportarPdf(File destino) {
        ejecutar(() -> {
            exportador.exportar(dao.buscarTodos(), destino);
            vista.mostrarMensaje("PDF generado en: " + destino.getAbsolutePath());
        });
    }

    private void ejecutar(ManejadorExcepciones.Operacion operacion) {
        ManejadorExcepciones.ejecutar(operacion, vista::mostrarError);
    }

    private void refrescarTabla() throws DaoException {
        vista.mostrarPacientes(dao.buscarTodos());
    }

    private Paciente construir(Integer id, String nombre, String apellido, String dni, String fechaTexto)
            throws ValidacionException {
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto.trim(), Paciente.FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new ValidacionException("La fecha de ingreso debe tener el formato DD/MM/AAAA.");
        }
        Paciente paciente = new Paciente(id, nombre, apellido, dni, fecha);
        paciente.validar();
        return paciente;
    }

    private void validarDniDisponible(Paciente paciente) throws DaoException, ValidacionException {
        Optional<Paciente> existente = dao.buscarPorDni(paciente.getDni());
        if (existente.isPresent() && !existente.get().getId().equals(paciente.getId())) {
            throw new ValidacionException("Ya existe un paciente registrado con el DNI " + paciente.getDni() + ".");
        }
    }

    private Integer parsearId(String texto) throws ValidacionException {
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Seleccione un registro en la tabla para continuar.");
        }
    }
}
