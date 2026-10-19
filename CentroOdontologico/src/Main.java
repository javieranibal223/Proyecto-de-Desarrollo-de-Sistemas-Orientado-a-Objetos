import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Se inicializa la parte de Pacientes
            PacienteVista vistaPaciente = new PacienteVista();
            new PacienteControlador(vistaPaciente);
            vistaPaciente.setVisible(true);

            // Se inicializa la parte de Odontólogos (Se mantiene intacto)
            OdontologoVista vistaOdontologo = new OdontologoVista();
            new OdontologoControlador(vistaOdontologo);
            vistaOdontologo.setVisible(true);
        });
    }
}