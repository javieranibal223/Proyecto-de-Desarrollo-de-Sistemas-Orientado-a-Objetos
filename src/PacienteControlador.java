import java.util.List;
import model.Paciente;          // <-- Soluciona el error de compilación
import dao.PacienteDao;         // <-- Importamos la interfaz DAO
import dao.impl.PacienteDaoJdbc; // <-- Importamos la implementación JDBC

public class PacienteControlador {
    private PacienteVista vista;
    private PacienteDao dao; // Reemplazamos el ArrayList en memoria por el DAO

    public PacienteControlador(PacienteVista vista) {
        this.vista = vista;
        this.dao = new PacienteDaoJdbc(); // Inicializamos la conexión a la base
        
        // Como la Base de Datos genera el ID automáticamente, ponemos un texto fijo
        this.vista.setId("Autogenerado"); 
        
        this.vista.getBtnGuardar().addActionListener(e -> agregarPaciente());
        
        // Cargamos los pacientes que ya existan guardados en la BD al abrir la ventana
        cargarPacientesGuardados();
    }

    private void agregarPaciente() {
        String dni = vista.getDni();
        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        String telefono = vista.getTelefono();
        String obraSocial = vista.getObraSocial();

        if (!dni.isBlank() && !nombre.isBlank() && !apellido.isBlank()) {
            
            // Creamos el paciente (Usamos el constructor SIN id, porque lo genera la DB)
            Paciente nuevoPaciente = new Paciente(nombre, apellido, dni, telefono, obraSocial);
            
            // Guardamos en la BASE DE DATOS usando el DAO
            Paciente pacienteGuardado = dao.guardar(nuevoPaciente);
            
            // Agregamos el paciente (ya con su ID real de la base de datos) a la lista visual
            vista.getModeloLista().addElement(pacienteGuardado);

            // Limpiamos los campos para un nuevo ingreso
            vista.limpiarCampos();
            vista.setId("Autogenerado");
        }
    }

    // Método extra para que la lista no empiece vacía si ya había datos en la BD
    private void cargarPacientesGuardados() {
        vista.getModeloLista().clear();
        List<Paciente> pacientes = dao.buscarTodos();
        for (Paciente p : pacientes) {
            vista.getModeloLista().addElement(p);
        }
    }
}