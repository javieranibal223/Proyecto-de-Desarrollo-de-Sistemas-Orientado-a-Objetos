public class Odontologo {
    private String id;
    private String matricula;
    private String nombre;
    private String apellido;
    private String especialidad;

    public Odontologo(String id, String matricula, String nombre, String apellido, String especialidad) {
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
    }

    public String getId() { return id; }
    public String getMatricula() { return matricula; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEspecialidad() { return especialidad; }

    @Override
    public String toString() {
        return id + " | Dr(a). " + apellido + ", " + nombre + " (Mat: " + matricula + " - Esp: " + especialidad + ")";
    }
}
