package model;

import exception.ValidacionException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entidad que representa a un odontólogo del centro.
 * <p>
 * La matrícula profesional es única y se valida como numérica. Dos odontólogos
 * son iguales si tienen el mismo identificador.
 */
public class Odontologo {

    /** Especialidades admitidas por el centro. */
    public static final String[] ESPECIALIDADES = {"Ortodoncia", "Endodoncia", "Odontopediatría", "Cirugía Oral"};

    private static final Pattern PATRON_MATRICULA = Pattern.compile("\\d{3,10}");

    private Integer id;
    private final String matricula;
    private final String nombre;
    private final String apellido;
    private final String especialidad;

    /**
     * Crea un odontólogo nuevo, todavía sin identificador.
     *
     * @param matricula    matrícula profesional
     * @param nombre       nombre
     * @param apellido     apellido
     * @param especialidad especialidad odontológica
     */
    public Odontologo(String matricula, String nombre, String apellido, String especialidad) {
        this(null, matricula, nombre, apellido, especialidad);
    }

    /**
     * Crea un odontólogo con identificador (datos leídos de la base o a actualizar).
     *
     * @param id           identificador asignado por la base de datos
     * @param matricula    matrícula profesional
     * @param nombre       nombre
     * @param apellido     apellido
     * @param especialidad especialidad odontológica
     */
    public Odontologo(Integer id, String matricula, String nombre, String apellido, String especialidad) {
        this.id = id;
        this.matricula = matricula == null ? "" : matricula.trim();
        this.nombre = nombre == null ? "" : nombre.trim();
        this.apellido = apellido == null ? "" : apellido.trim();
        this.especialidad = especialidad == null ? "" : especialidad.trim();
    }

    /**
     * Verifica las reglas de negocio de la entidad.
     *
     * @throws ValidacionException si algún dato no cumple las reglas
     */
    public void validar() throws ValidacionException {
        if (!PATRON_MATRICULA.matcher(matricula).matches()) {
            throw new ValidacionException("La matrícula debe tener entre 3 y 10 dígitos numéricos.");
        }
        if (nombre.isEmpty() || apellido.isEmpty()) {
            throw new ValidacionException("Nombre y apellido son obligatorios.");
        }
        if (!Arrays.asList(ESPECIALIDADES).contains(especialidad)) {
            throw new ValidacionException("Seleccione una especialidad válida.");
        }
    }

    /** @return identificador, o {@code null} si todavía no fue guardado */
    public Integer getId() { return id; }

    /** @param id identificador asignado por la base de datos */
    public void setId(Integer id) { this.id = id; }

    /** @return matrícula profesional */
    public String getMatricula() { return matricula; }

    /** @return nombre */
    public String getNombre() { return nombre; }

    /** @return apellido */
    public String getApellido() { return apellido; }

    /** @return especialidad */
    public String getEspecialidad() { return especialidad; }

    /**
     * @param o objeto a comparar
     * @return {@code true} si es un odontólogo guardado con el mismo identificador
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Odontologo)) {
            return false;
        }
        Odontologo otro = (Odontologo) o;
        return id != null && id.equals(otro.id);
    }

    /** @return código hash basado en el identificador */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /** @return descripción legible para listas y combos */
    @Override
    public String toString() {
        return "Dr(a). " + apellido + ", " + nombre + " (Mat. " + matricula + " - " + especialidad + ")";
    }
}
