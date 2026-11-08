package model;

import exception.ValidacionException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Entidad que representa a un paciente del centro odontológico.
 * <p>
 * Los datos se fijan en el constructor. Solo el identificador puede modificarse,
 * y lo asigna la base de datos al guardar la entidad. Dos pacientes son iguales
 * si tienen el mismo identificador.
 */
public class Paciente {

    private static final Pattern PATRON_DNI = Pattern.compile("\\d{7,8}");

    /** Formato de fecha usado en la interfaz, la tabla y el PDF: DD/MM/AAAA. */
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer id;
    private final String nombre;
    private final String apellido;
    private final String dni;
    private final LocalDate fechaIngreso;

    /**
     * Crea un paciente nuevo, todavía sin identificador.
     *
     * @param nombre       nombre del paciente
     * @param apellido     apellido del paciente
     * @param dni          documento (solo dígitos)
     * @param fechaIngreso fecha de alta en el centro
     */
    public Paciente(String nombre, String apellido, String dni, LocalDate fechaIngreso) {
        this(null, nombre, apellido, dni, fechaIngreso);
    }

    /**
     * Crea un paciente con identificador (datos leídos de la base o a actualizar).
     *
     * @param id           identificador asignado por la base de datos
     * @param nombre       nombre del paciente
     * @param apellido     apellido del paciente
     * @param dni          documento (solo dígitos)
     * @param fechaIngreso fecha de alta en el centro
     */
    public Paciente(Integer id, String nombre, String apellido, String dni, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre == null ? "" : nombre.trim();
        this.apellido = apellido == null ? "" : apellido.trim();
        this.dni = dni == null ? "" : dni.trim();
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * Verifica las reglas de negocio de la entidad.
     *
     * @throws ValidacionException si algún dato no cumple las reglas
     */
    public void validar() throws ValidacionException {
        if (nombre.isEmpty() || apellido.isEmpty()) {
            throw new ValidacionException("Nombre y apellido son obligatorios.");
        }
        if (!PATRON_DNI.matcher(dni).matches()) {
            throw new ValidacionException("El DNI debe tener entre 7 y 8 dígitos numéricos.");
        }
        if (fechaIngreso == null || fechaIngreso.isAfter(LocalDate.now())) {
            throw new ValidacionException("La fecha de ingreso es obligatoria y no puede ser futura.");
        }
    }

    /** @return identificador, o {@code null} si todavía no fue guardado */
    public Integer getId() { return id; }

    /** @param id identificador asignado por la base de datos */
    public void setId(Integer id) { this.id = id; }

    /** @return nombre del paciente */
    public String getNombre() { return nombre; }

    /** @return apellido del paciente */
    public String getApellido() { return apellido; }

    /** @return DNI del paciente */
    public String getDni() { return dni; }

    /** @return fecha de ingreso al centro */
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    /**
     * @param o objeto a comparar
     * @return {@code true} si es un paciente guardado con el mismo identificador
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        Paciente otro = (Paciente) o;
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
        return apellido + ", " + nombre + " (DNI " + dni + ")";
    }
}
