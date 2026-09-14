import javax.swing.*;
import java.awt.*;

public class OdontologoVista extends JFrame {
    private JTextField txtId = new JTextField(10);
    private JTextField txtMatricula = new JTextField(10);
    private JTextField txtNombre = new JTextField(10);
    private JTextField txtApellido = new JTextField(10);
    private JComboBox<String> comboEspecialidad = new JComboBox<>();
    private JButton btnGuardar = new JButton("Guardar Odontólogo");

    private DefaultListModel<Odontologo> modeloLista = new DefaultListModel<>();
    private JList<Odontologo> listaOdontologos = new JList<>(modeloLista);

    public OdontologoVista() {
        super("Gestión de Odontólogos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId.setEditable(false);
        panelForm.add(new JLabel("ID Odontólogo:"));
        panelForm.add(txtId);

        panelForm.add(new JLabel("Matrícula N°:"));
        panelForm.add(txtMatricula);

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellido);

        panelForm.add(new JLabel("Especialidad:"));
        comboEspecialidad.addItem("Ortodoncia");
        comboEspecialidad.addItem("Endodoncia");
        comboEspecialidad.addItem("Odontopediatría");
        comboEspecialidad.addItem("Cirugía Oral");
        panelForm.add(comboEspecialidad);

        panelForm.add(new JLabel(""));
        panelForm.add(btnGuardar);

        add(panelForm, BorderLayout.NORTH);

        listaOdontologos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOdontologos.setVisibleRowCount(5);
        add(new JScrollPane(listaOdontologos), BorderLayout.CENTER);

        setSize(480, 400);
        setLocationRelativeTo(null);
    }

    public String getId() { return txtId.getText(); }
    public void setId(String id) { txtId.setText(id); }
    public String getMatricula() { return txtMatricula.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getApellido() { return txtApellido.getText(); }
    public String getEspecialidad() { return (String) comboEspecialidad.getSelectedItem(); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public DefaultListModel<Odontologo> getModeloLista() { return modeloLista; }

    public void limpiarCampos() {
        txtMatricula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        comboEspecialidad.setSelectedIndex(0);
    }
}
