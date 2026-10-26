import java.util.ArrayList;
import java.util.List;

public class OdontologoControlador {
    private OdontologoVista vista;
    private List<Odontologo> odontologosGuardados = new ArrayList<>();
    private int contadorId = 1;

    public OdontologoControlador(OdontologoVista vista) {
        this.vista = vista;
        this.vista.setId("ODO-" + String.format("%04d", contadorId));
        this.vista.getBtnGuardar().addActionListener(e -> agregarOdontologo());
    }

    private void agregarOdontologo() {
        String id = vista.getId();
        String matricula = vista.getMatricula();
        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        String especialidad = vista.getEspecialidad();

        if (!matricula.isBlank() && !nombre.isBlank() && !apellido.isBlank()) {
            Odontologo nuevoOdontologo = new Odontologo(id, matricula, nombre, apellido, especialidad);
            odontologosGuardados.add(nuevoOdontologo);
            vista.getModeloLista().addElement(nuevoOdontologo);

            contadorId++;
            vista.limpiarCampos();
            vista.setId("ODO-" + String.format("%04d", contadorId));
        }
    }
}