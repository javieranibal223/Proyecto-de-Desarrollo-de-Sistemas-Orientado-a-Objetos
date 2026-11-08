import controller.OdontologoControlador;
import controller.PacienteControlador;
import controller.TurnoControlador;
import dao.impl.OdontologoDaoJdbc;
import dao.impl.PacienteDaoJdbc;
import dao.impl.TurnoDaoJdbc;
import export.ExportadorPacientesPdf;
import view.MenuPrincipalVista;
import view.OdontologoVista;
import view.PacienteVista;
import view.TurnoVista;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada: construye las vistas, los controladores y los DAO, y abre
 * el menú principal en el hilo de Swing.
 */
public class Main {

    /**
     * Arranca la aplicación.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacienteVista vistaPaciente = new PacienteVista();
            PacienteControlador controladorPaciente = new PacienteControlador(
                    vistaPaciente, new PacienteDaoJdbc(), new ExportadorPacientesPdf());
            vistaPaciente.setControlador(controladorPaciente);

            OdontologoVista vistaOdontologo = new OdontologoVista();
            OdontologoControlador controladorOdontologo = new OdontologoControlador(
                    vistaOdontologo, new OdontologoDaoJdbc());
            vistaOdontologo.setControlador(controladorOdontologo);

            TurnoVista vistaTurno = new TurnoVista();
            TurnoControlador controladorTurno = new TurnoControlador(
                    vistaTurno, new TurnoDaoJdbc(), new PacienteDaoJdbc(), new OdontologoDaoJdbc());
            vistaTurno.setControlador(controladorTurno);

            MenuPrincipalVista menu = new MenuPrincipalVista();
            menu.getBtnPacientes().addActionListener(e -> {
                controladorPaciente.listar();
                vistaPaciente.iniciar();
            });
            menu.getBtnOdontologos().addActionListener(e -> {
                controladorOdontologo.listar();
                vistaOdontologo.iniciar();
            });
            menu.getBtnTurnos().addActionListener(e -> {
                controladorTurno.cargarDatos();
                vistaTurno.iniciar();
            });
            menu.iniciar();
        });
    }
}
