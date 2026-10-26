import dao.IDao;
import dao.impl.PacienteDaoJdbc;
import model.Paciente;

public class Main {
    public static void main(String[] args) {
        
        // --- 1. LO QUE YA TENÍAS FUNCIONANDO PARA ODONTÓLOGOS ---
        // (Si tu código original era un poco distinto, podés reemplazar 
        // estas 3 líneas por las que tenías antes)
        OdontologoVista vistaOdontologo = new OdontologoVista();
        OdontologoControlador controladorOdontologo = new OdontologoControlador(vistaOdontologo); 
        vistaOdontologo.setVisible(true);

        // --- 2. LO NUEVO QUE AGREGAMOS PARA PACIENTES (El desafío MVC) ---
        PacienteVista vistaPaciente = new PacienteVista();
        IDao<Paciente> daoPaciente = new PacienteDaoJdbc();
        PacienteControlador controladorPaciente = new PacienteControlador(vistaPaciente, daoPaciente);
        
        vistaPaciente.setControlador(controladorPaciente);
        vistaPaciente.iniciar();
    }
}