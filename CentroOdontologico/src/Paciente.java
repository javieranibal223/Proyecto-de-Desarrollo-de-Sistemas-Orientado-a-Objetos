public class Paciente {
    private String id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String obraSocial;

    public Paciente(String id, String dni, String nombre, String apellido, String telefono, String obraSocial) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.obraSocial = obraSocial;
    }

    public String getId() { return id; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getObraSocial() { return obraSocial; }

    @Override
    public String toString() {
        return id + " | " + apellido + ", " + nombre + " (DNI: " + dni + " - OS: " + obraSocial + ")";
    }
}