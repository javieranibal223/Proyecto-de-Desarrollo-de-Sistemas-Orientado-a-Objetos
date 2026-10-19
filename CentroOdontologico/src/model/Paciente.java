package model;

public class Paciente {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;    // NUEVO
    private String obraSocial;  // NUEVO

    public Paciente() {
    }

    // Constructor completo
    public Paciente(Integer id, String nombre, String apellido, String dni, String telefono, String obraSocial) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.obraSocial = obraSocial;
    }

    // Constructor sin ID (para inserciones)
    public Paciente(String nombre, String apellido, String dni, String telefono, String obraSocial) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.obraSocial = obraSocial;
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }

    // ¡CRÍTICO PARA LA VISTA! 
    // Esto es lo que mostrará visualmente el JList<Paciente>
    @Override
    public String toString() {
        return apellido + ", " + nombre + " (DNI: " + dni + " - " + obraSocial + ")";
    }
}