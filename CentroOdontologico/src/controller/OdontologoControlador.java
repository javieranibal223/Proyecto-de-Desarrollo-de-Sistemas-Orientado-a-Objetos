package controller;

import dao.OdontologoDao;
import exception.DaoException;
import exception.NoEncontradoException;
import exception.ValidacionException;
import model.Odontologo;
import view.OdontologoVista;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del CRUD de odontólogos.
 * <p>
 * Valida los datos, verifica que la matrícula no esté repetida y delega la
 * persistencia en el {@link OdontologoDao}.
 */
public class OdontologoControlador {

    private final OdontologoVista vista;
    private final OdontologoDao dao;

    /**
     * @param vista pantalla que muestra los datos
     * @param dao   acceso a la tabla de odontólogos
     */
    public OdontologoControlador(OdontologoVista vista, OdontologoDao dao) {
        this.vista = vista;
        this.dao = dao;
    }

    /** Carga en la tabla todos los odontólogos registrados. */
    public void listar() {
        ejecutar(this::refrescarTabla);
    }

    /**
     * Busca un odontólogo por su matrícula y lo muestra en la tabla.
     *
     * @param matricula matrícula a buscar
     */
    public void buscarPorMatricula(String matricula) {
        ejecutar(() -> {
            if (matricula.isBlank()) {
                throw new ValidacionException("Ingrese una matrícula para buscar.");
            }
            Optional<Odontologo> encontrado = dao.buscarPorMatricula(matricula.trim());
            if (encontrado.isEmpty()) {
                throw new NoEncontradoException("No se encontró ningún odontólogo con matrícula " + matricula.trim() + ".");
            }
            vista.mostrarOdontologos(List.of(encontrado.get()));
        });
    }

    /**
     * Registra un odontólogo nuevo.
     *
     * @param matricula    matrícula profesional
     * @param nombre       nombre
     * @param apellido     apellido
     * @param especialidad especialidad seleccionada
     */
    public void guardar(String matricula, String nombre, String apellido, String especialidad) {
        ejecutar(() -> {
            Odontologo nuevo = construir(null, matricula, nombre, apellido, especialidad);
            validarMatriculaDisponible(nuevo);
            Odontologo guardado = dao.guardar(nuevo);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Odontólogo registrado con ID " + guardado.getId() + ".");
            refrescarTabla();
        });
    }

    /**
     * Actualiza los datos de un odontólogo existente.
     *
     * @param idTexto      identificador seleccionado en la tabla
     * @param matricula    nueva matrícula
     * @param nombre       nuevo nombre
     * @param apellido     nuevo apellido
     * @param especialidad nueva especialidad
     */
    public void actualizar(String idTexto, String matricula, String nombre, String apellido, String especialidad) {
        ejecutar(() -> {
            Integer id = parsearId(idTexto);
            Odontologo odontologo = construir(id, matricula, nombre, apellido, especialidad);
            validarMatriculaDisponible(odontologo);
            if (!dao.actualizar(odontologo)) {
                throw new NoEncontradoException("No existe un odontólogo con ID " + id + ".");
            }
            vista.mostrarMensaje("Odontólogo actualizado correctamente.");
            refrescarTabla();
        });
    }

    /**
     * Elimina un odontólogo. La confirmación la pide la vista antes de llamar aquí.
     *
     * @param idTexto identificador a eliminar
     */
    public void eliminar(String idTexto) {
        ejecutar(() -> {
            Integer id = parsearId(idTexto);
            if (!dao.eliminar(id)) {
                throw new NoEncontradoException("No existe un odontólogo con ID " + id + ".");
            }
            vista.limpiarFormulario();
            vista.mostrarMensaje("Odontólogo eliminado de los registros.");
            refrescarTabla();
        });
    }

    private void ejecutar(ManejadorExcepciones.Operacion operacion) {
        ManejadorExcepciones.ejecutar(operacion, vista::mostrarError);
    }

    private void refrescarTabla() throws DaoException {
        vista.mostrarOdontologos(dao.buscarTodos());
    }

    private Odontologo construir(Integer id, String matricula, String nombre, String apellido, String especialidad)
            throws ValidacionException {
        Odontologo odontologo = new Odontologo(id, matricula, nombre, apellido, especialidad);
        odontologo.validar();
        return odontologo;
    }

    private void validarMatriculaDisponible(Odontologo odontologo) throws DaoException, ValidacionException {
        Optional<Odontologo> existente = dao.buscarPorMatricula(odontologo.getMatricula());
        if (existente.isPresent() && !existente.get().getId().equals(odontologo.getId())) {
            throw new ValidacionException("Ya existe un odontólogo con la matrícula " + odontologo.getMatricula() + ".");
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
