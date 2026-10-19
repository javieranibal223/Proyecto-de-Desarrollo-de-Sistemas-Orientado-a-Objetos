import javax.swing.*;
import java.awt.*;
import model.Paciente; // Importación obligatoria para que reconozca la Entidad

public class PacienteVista extends JFrame {
    private JTextField txtId = new JTextField(10);
    private JTextField txtDni = new JTextField(10);
    private JTextField txtNombre = new JTextField(10);
    private JTextField txtApellido = new JTextField(10);
    private JTextField txtTelefono = new JTextField(10);
    private JComboBox<String> comboObraSocial = new JComboBox<>();
    private JButton btnGuardar = new JButton("Guardar Paciente");

    private DefaultListModel<Paciente> modeloLista = new DefaultListModel<>();
    private JList<Paciente> listaPacientes = new JList<>(modeloLista);

    public PacienteVista() {
        super("Gestión de Pacientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        
        txtId.setEditable(false);
        panelForm.add(new JLabel("ID Paciente:"));
        panelForm.add(txtId);
        
        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellido);
        
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(txtTelefono);
        
        panelForm.add(new JLabel("Obra Social:"));
        comboObraSocial.addItem("Particular");
        comboObraSocial.addItem("OSDE");
        comboObraSocial.addItem("Swiss Medical");
        comboObraSocial.addItem("IOMA");
        panelForm.add(comboObraSocial);
        
        panelForm.add(new JLabel(""));
        panelForm.add(btnGuardar);

        add(panelForm, BorderLayout.NORTH);

        listaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPacientes.setVisibleRowCount(5);
        add(new JScrollPane(listaPacientes), BorderLayout.CENTER);

        setSize(460, 420);
        setLocationRelativeTo(null);
    }

    public String getId() { return txtId.getText(); }
    public void setId(String id) { txtId.setText(id); }
    public String getDni() { return txtDni.getText(); }
    public String getNombre() { return txtNombre.getText(); }
    public String getApellido() { return txtApellido.getText(); }
    public String getTelefono() { return txtTelefono.getText(); }
    public String getObraSocial() { return (String) comboObraSocial.getSelectedItem(); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public DefaultListModel<Paciente> getModeloLista() { return modeloLista; }

    public void limpiarCampos() {
        txtId.setText(""); // Opcional, dependiendo de si limpias el ID al guardar
        txtDni.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        comboObraSocial.setSelectedIndex(0);
    }
}