import dao.IDao;
import model.Paciente;
import usecase.*;
import java.time.LocalDate;

public class PacienteControlador {
    
    private PacienteVista vista;
    private IDao<Paciente> pacienteDao;

    // Inyección de dependencias a través del constructor
    public PacienteControlador(PacienteVista vista, IDao<Paciente> pacienteDao) {
        this.vista = vista;
        this.pacienteDao = pacienteDao;
    }

    public void registrarPaciente(String nombre, String apellido, String dni, LocalDate fecha) {
        Paciente nuevoPaciente = new Paciente(nombre, apellido, dni, fecha);
        
        // Llamada al Caso de Uso
        GuardarPacienteCU guardarCU = new GuardarPacienteCU();
        Paciente pacienteGuardado = guardarCU.ejecutar(nuevoPaciente, pacienteDao);
        
        // Actualización de la vista
        vista.mostrarMensaje("Paciente registrado exitosamente con ID: " + pacienteGuardado.getId());
    }

    public void buscarPaciente(Integer id) {
        BuscarPacienteCU buscarCU = new BuscarPacienteCU();
        Paciente paciente = buscarCU.ejecutar(id, pacienteDao);
        
        if (paciente != null) {
            vista.mostrarDatosPaciente(paciente);
        } else {
            vista.mostrarMensaje("No se encontró el paciente con ID: " + id);
        }
    }

    public void actualizarPaciente(Integer id, String nombre, String apellido, String dni, LocalDate fecha) {
        Paciente paciente = new Paciente(id, nombre, apellido, dni, fecha);
        
        ActualizarPacienteCU actualizarCU = new ActualizarPacienteCU();
        actualizarCU.ejecutar(paciente, pacienteDao);
        
        vista.mostrarMensaje("Paciente actualizado con éxito.");
    }

    public void eliminarPaciente(Integer id) {
        EliminarPacienteCU eliminarCU = new EliminarPacienteCU();
        eliminarCU.ejecutar(id, pacienteDao);
        
        vista.mostrarMensaje("Paciente eliminado de los registros.");
    }
}