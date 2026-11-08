package controller;

import dao.OdontologoDao;
import dao.PacienteDao;
import dao.TurnoDao;
import exception.DaoException;
import exception.NoEncontradoException;
import exception.ValidacionException;
import model.Odontologo;
import model.Paciente;
import model.Turno;
import view.TurnoVista;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador de la entidad combinada Turno.
 * <p>
 * Carga los pacientes y odontólogos disponibles en los combos, valida la fecha y
 * los datos del turno, y persiste mediante {@link TurnoDao}. La restricción de
 * un único turno por odontólogo y horario la controla la base de datos y se
 * informa como mensaje claro.
 */
public class TurnoControlador {

    private final TurnoVista vista;
    private final TurnoDao turnoDao;
    private final PacienteDao pacienteDao;
    private final OdontologoDao odontologoDao;

    /**
     * @param vista         pantalla de turnos
     * @param turnoDao      acceso a la tabla de turnos
     * @param pacienteDao   acceso a pacientes para el combo
     * @param odontologoDao acceso a odontólogos para el combo
     */
    public TurnoControlador(TurnoVista vista, TurnoDao turnoDao, PacienteDao pacienteDao, OdontologoDao odontologoDao) {
        this.vista = vista;
        this.turnoDao = turnoDao;
        this.pacienteDao = pacienteDao;
        this.odontologoDao = odontologoDao;
    }

    /** Recarga los combos de pacientes y odontólogos y la tabla de turnos. */
    public void cargarDatos() {
        ejecutar(() -> {
            vista.mostrarPacientes(pacienteDao.buscarTodos());
            vista.mostrarOdontologos(odontologoDao.buscarTodos());
            refrescarTabla();
        });
    }

    /**
     * Registra un turno nuevo.
     *
     * @param paciente   paciente seleccionado en el combo
     * @param odontologo odontólogo seleccionado en el combo
     * @param fechaTexto fecha y hora en formato DD/MM/AAAA HH:MM
     * @param motivo     motivo de la consulta (opcional)
     */
    public void guardar(Paciente paciente, Odontologo odontologo, String fechaTexto, String motivo) {
        ejecutar(() -> {
            Turno nuevo = construir(null, paciente, odontologo, fechaTexto, motivo);
            nuevo.validarFuturo();
            turnoDao.guardar(nuevo);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Turno registrado con ID " + nuevo.getId() + ".");
            refrescarTabla();
        });
    }

    /**
     * Modifica un turno existente.
     *
     * @param idTexto    identificador del turno seleccionado en la tabla
     * @param paciente   paciente seleccionado
     * @param odontologo odontólogo seleccionado
     * @param fechaTexto nueva fecha y hora en formato DD/MM/AAAA HH:MM
     * @param motivo     nuevo motivo (opcional)
     */
    public void actualizar(String idTexto, Paciente paciente, Odontologo odontologo, String fechaTexto, String motivo) {
        ejecutar(() -> {
            Turno turno = construir(parsearId(idTexto), paciente, odontologo, fechaTexto, motivo);
            if (!turnoDao.actualizar(turno)) {
                throw new NoEncontradoException("No existe un turno con ID " + turno.getId() + ".");
            }
            vista.mostrarMensaje("Turno actualizado correctamente.");
            refrescarTabla();
        });
    }

    /**
     * Elimina un turno. La confirmación la pide la vista antes de llamar aquí.
     *
     * @param idTexto identificador del turno a eliminar
     */
    public void eliminar(String idTexto) {
        ejecutar(() -> {
            Integer id = parsearId(idTexto);
            if (!turnoDao.eliminar(id)) {
                throw new NoEncontradoException("No existe un turno con ID " + id + ".");
            }
            vista.limpiarFormulario();
            vista.mostrarMensaje("Turno eliminado.");
            refrescarTabla();
        });
    }

    private void ejecutar(ManejadorExcepciones.Operacion operacion) {
        ManejadorExcepciones.ejecutar(operacion, vista::mostrarError);
    }

    private void refrescarTabla() throws DaoException {
        List<Turno> turnos = turnoDao.buscarTodos();
        vista.mostrarTurnos(turnos);
    }

    private Turno construir(Integer id, Paciente paciente, Odontologo odontologo, String fechaTexto, String motivo)
            throws ValidacionException {
        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaTexto.trim(), Turno.FORMATO);
        } catch (DateTimeParseException e) {
            throw new ValidacionException("La fecha y hora deben tener el formato DD/MM/AAAA HH:MM.");
        }
        Turno turno = new Turno(id, paciente, odontologo, fecha, motivo);
        turno.validar();
        return turno;
    }

    private Integer parsearId(String texto) throws ValidacionException {
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Seleccione un registro en la tabla para continuar.");
        }
    }
}
