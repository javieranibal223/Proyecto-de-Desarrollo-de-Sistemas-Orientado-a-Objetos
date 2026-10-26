package model;

import java.time.LocalDate;

public class Paciente {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor para registrar (sin ID, ya que se genera automáticamente en la BD)
    public Paciente(String nombre, String apellido, String dni, LocalDate fechaIngreso) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // Constructor completo (ideal para cuando traemos datos de la BD o actualizamos)
    public Paciente(Integer id, String nombre, String apellido, String dni, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}