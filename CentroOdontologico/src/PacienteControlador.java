import java.util.ArrayList;
import java.util.List;

public class PacienteControlador {
    private PacienteVista vista;
    private List<Paciente> pacientesGuardados = new ArrayList<>();
    private int contadorId = 1;

    public PacienteControlador(PacienteVista vista) {
        this.vista = vista;
        this.vista.setId("PAC-" + String.format("%04d", contadorId));
        this.vista.getBtnGuardar().addActionListener(e -> agregarPaciente());
    }

    private void agregarPaciente() {
        String id = vista.getId();
        String dni = vista.getDni();
        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        String telefono = vista.getTelefono();
        String obraSocial = vista.getObraSocial();

        if (!dni.isBlank() && !nombre.isBlank() && !apellido.isBlank()) {
            Paciente nuevoPaciente = new Paciente(id, dni, nombre, apellido, telefono, obraSocial);
            pacientesGuardados.add(nuevoPaciente);
            vista.getModeloLista().addElement(nuevoPaciente);

            contadorId++;
            vista.limpiarCampos();
            vista.setId("PAC-" + String.format("%04d", contadorId));
        }
    }
}
