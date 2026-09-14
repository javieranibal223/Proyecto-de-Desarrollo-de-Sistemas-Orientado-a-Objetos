import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacienteVista vistaPaciente = new PacienteVista();
            new PacienteControlador(vistaPaciente);
            vistaPaciente.setVisible(true);

            OdontologoVista vistaOdontologo = new OdontologoVista();
            new OdontologoControlador(vistaOdontologo);
            vistaOdontologo.setVisible(true);
        });
    }
}
