package view;

import controller.OdontologoControlador;
import model.Odontologo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla Swing del CRUD de odontólogos: formulario, tabla y botones de acción.
 * <p>
 * No contiene lógica de negocio: cada botón delega en el {@link OdontologoControlador}.
 */
public class OdontologoVista extends JFrame {
    private static final long serialVersionUID = 1L;


    private static final String[] COLUMNAS = {"ID", "Matrícula", "Apellido", "Nombre", "Especialidad"};

    private final JTextField txtId = new JTextField();
    private final JTextField txtMatricula = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JComboBox<String> cmbEspecialidad = new JComboBox<>(Odontologo.ESPECIALIDADES);

    private final JButton btnNuevo = new JButton("Nuevo / Limpiar");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");

    private final JTextField txtBuscarMatricula = new JTextField(12);
    private final JButton btnBuscar = new JButton("Buscar por matrícula");
    private final JButton btnVerTodos = new JButton("Ver todos");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private List<Odontologo> filas = new ArrayList<>();

    private OdontologoControlador controlador;

    /** Construye la ventana con el formulario, la tabla y los botones. */
    public OdontologoVista() {
        super("Gestión de Odontólogos - Centro Odontológico");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);
        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del odontólogo"));
        agregarCampo(formulario, "ID (seleccione en la tabla):", txtId);
        agregarCampo(formulario, "Matrícula N° (solo dígitos):", txtMatricula);
        agregarCampo(formulario, "Nombre:", txtNombre);
        agregarCampo(formulario, "Apellido:", txtApellido);
        agregarCampo(formulario, "Especialidad:", cmbEspecialidad);
        add(formulario, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0) {
                cargarFormulario(filas.get(fila));
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        acciones.add(btnNuevo);
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.CENTER));
        busqueda.add(new JLabel("Matrícula:"));
        busqueda.add(txtBuscarMatricula);
        busqueda.add(btnBuscar);
        busqueda.add(btnVerTodos);

        JPanel sur = new JPanel(new GridLayout(2, 1));
        sur.add(acciones);
        sur.add(busqueda);
        add(sur, BorderLayout.SOUTH);

        configurarEventos();
        setSize(820, 560);
    }

    /**
     * Asigna el controlador que atenderá las acciones de la pantalla.
     *
     * @param controlador controlador de odontólogos
     */
    public void setControlador(OdontologoControlador controlador) {
        this.controlador = controlador;
    }

    /** Centra la ventana en pantalla y la hace visible. */
    public void iniciar() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * @param mensaje texto informativo a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @param mensaje descripción del error a mostrar
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Reemplaza el contenido de la tabla.
     *
     * @param odontologos odontólogos a mostrar
     */
    public void mostrarOdontologos(List<Odontologo> odontologos) {
        tabla.clearSelection();
        filas = new ArrayList<>(odontologos);
        modeloTabla.setRowCount(0);
        for (Odontologo o : filas) {
            modeloTabla.addRow(new Object[]{
                o.getId(), o.getMatricula(), o.getApellido(), o.getNombre(), o.getEspecialidad()
            });
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    public void limpiarFormulario() {
        tabla.clearSelection();
        txtId.setText("");
        txtMatricula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        cmbEspecialidad.setSelectedIndex(0);
    }

    private void cargarFormulario(Odontologo o) {
        txtId.setText(String.valueOf(o.getId()));
        txtMatricula.setText(o.getMatricula());
        txtNombre.setText(o.getNombre());
        txtApellido.setText(o.getApellido());
        cmbEspecialidad.setSelectedItem(o.getEspecialidad());
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private void configurarEventos() {
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> controlador.buscarPorMatricula(txtBuscarMatricula.getText()));
        btnVerTodos.addActionListener(e -> controlador.listar());

        btnGuardar.addActionListener(e -> controlador.guardar(
                txtMatricula.getText(), txtNombre.getText(), txtApellido.getText(),
                (String) cmbEspecialidad.getSelectedItem()));

        btnActualizar.addActionListener(e -> controlador.actualizar(
                txtId.getText(), txtMatricula.getText(), txtNombre.getText(), txtApellido.getText(),
                (String) cmbEspecialidad.getSelectedItem()));

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isBlank()) {
                mostrarError("Seleccione un odontólogo en la tabla para eliminarlo.");
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar el odontólogo con ID " + txtId.getText() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                controlador.eliminar(txtId.getText());
            }
        });
    }
}
