package model;

import exception.ValidacionException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entidad combinada que relaciona un {@link Paciente} con un {@link Odontologo}
 * en una fecha y hora determinadas. Dos turnos son iguales si tienen el mismo identificador.
 */
public class Turno {

    /** Formato de texto de la fecha y hora del turno: DD/MM/AAAA HH:MM. */
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final int MAX_MOTIVO = 200;

    private Integer id;
    private final Paciente paciente;
    private final Odontologo odontologo;
    private final LocalDateTime fechaHora;
    private final String motivo;

    /**
     * Crea un turno nuevo, todavía sin identificador.
     *
     * @param paciente  paciente atendido
     * @param odontologo odontólogo que atiende
     * @param fechaHora fecha y hora del turno
     * @param motivo    motivo de la consulta (puede ser nulo)
     */
    public Turno(Paciente paciente, Odontologo odontologo, LocalDateTime fechaHora, String motivo) {
        this(null, paciente, odontologo, fechaHora, motivo);
    }

    /**
     * Crea un turno con identificador (datos leídos de la base o a actualizar).
     *
     * @param id         identificador asignado por la base de datos
     * @param paciente   paciente atendido
     * @param odontologo odontólogo que atiende
     * @param fechaHora  fecha y hora del turno
     * @param motivo     motivo de la consulta (puede ser nulo)
     */
    public Turno(Integer id, Paciente paciente, Odontologo odontologo, LocalDateTime fechaHora, String motivo) {
        this.id = id;
        this.paciente = paciente;
        this.odontologo = odontologo;
        this.fechaHora = fechaHora;
        this.motivo = motivo == null ? "" : motivo.trim();
    }

    /**
     * Verifica las reglas de negocio del turno (datos obligatorios y longitud).
     *
     * @throws ValidacionException si algún dato no cumple las reglas
     */
    public void validar() throws ValidacionException {
        if (paciente == null) {
            throw new ValidacionException("Seleccione un paciente.");
        }
        if (odontologo == null) {
            throw new ValidacionException("Seleccione un odontólogo.");
        }
        if (fechaHora == null) {
            throw new ValidacionException("La fecha y hora del turno son obligatorias.");
        }
        if (motivo.length() > MAX_MOTIVO) {
            throw new ValidacionException("El motivo no puede superar los 200 caracteres.");
        }
    }

    /**
     * Verifica que el turno nuevo no quede en el pasado.
     *
     * @throws ValidacionException si la fecha y hora ya pasaron
     */
    public void validarFuturo() throws ValidacionException {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new ValidacionException("El turno no puede registrarse en una fecha y hora pasadas.");
        }
    }

    /** @return identificador, o {@code null} si todavía no fue guardado */
    public Integer getId() { return id; }

    /** @param id identificador asignado por la base de datos */
    public void setId(Integer id) { this.id = id; }

    /** @return paciente atendido */
    public Paciente getPaciente() { return paciente; }

    /** @return odontólogo que atiende */
    public Odontologo getOdontologo() { return odontologo; }

    /** @return fecha y hora del turno */
    public LocalDateTime getFechaHora() { return fechaHora; }

    /** @return motivo de la consulta; cadena vacía si no se informó */
    public String getMotivo() { return motivo; }

    /**
     * @param o objeto a comparar
     * @return {@code true} si es un turno guardado con el mismo identificador
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turno)) {
            return false;
        }
        Turno otro = (Turno) o;
        return id != null && id.equals(otro.id);
    }

    /** @return código hash basado en el identificador */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /** @return descripción del turno */
    @Override
    public String toString() {
        return fechaHora.format(FORMATO) + " - " + paciente + " con " + odontologo;
    }
}
