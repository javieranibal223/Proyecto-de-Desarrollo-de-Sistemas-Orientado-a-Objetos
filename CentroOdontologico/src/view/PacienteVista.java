package view;

import controller.PacienteControlador;
import model.Paciente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla Swing del CRUD de pacientes: formulario, tabla y botones de acción.
 * <p>
 * No contiene lógica de negocio. Cada botón delega en el {@link PacienteControlador},
 * y los errores se muestran como cuadros de diálogo.
 */
public class PacienteVista extends JFrame {
    private static final long serialVersionUID = 1L;


    private static final String[] COLUMNAS = {"ID", "Apellido", "Nombre", "DNI", "Fecha de ingreso"};

    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final SelectorFecha selectorFecha = new SelectorFecha();
    private final JTextField txtBuscarDni = new JTextField(12);

    private final JButton btnNuevo = new JButton("Nuevo / Limpiar");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnBuscar = new JButton("Buscar por DNI");
    private final JButton btnVerTodos = new JButton("Ver todos");
    private final JButton btnExportar = new JButton("Exportar PDF");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private List<Paciente> filas = new ArrayList<>();

    private PacienteControlador controlador;

    /** Construye la ventana con el formulario, la tabla y los botones. */
    public PacienteVista() {
        super("Gestión de Pacientes - Centro Odontológico");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);
        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del paciente"));
        agregarCampo(formulario, "ID (seleccione en la tabla):", txtId);
        agregarCampo(formulario, "Nombre:", txtNombre);
        agregarCampo(formulario, "Apellido:", txtApellido);
        agregarCampo(formulario, "DNI (7 u 8 dígitos):", txtDni);
        agregarCampo(formulario, "Fecha de ingreso:", selectorFecha);
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
        busqueda.add(new JLabel("DNI:"));
        busqueda.add(txtBuscarDni);
        busqueda.add(btnBuscar);
        busqueda.add(btnVerTodos);
        busqueda.add(btnExportar);

        JPanel sur = new JPanel(new GridLayout(2, 1));
        sur.add(acciones);
        sur.add(busqueda);
        add(sur, BorderLayout.SOUTH);

        configurarEventos();
        setSize(820, 600);
    }

    /**
     * Asigna el controlador que atenderá las acciones de la pantalla.
     *
     * @param controlador controlador de pacientes
     */
    public void setControlador(PacienteControlador controlador) {
        this.controlador = controlador;
    }

    /** Centra la ventana en pantalla y la hace visible. */
    public void iniciar() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param mensaje texto a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje descripción del problema
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Reemplaza el contenido de la tabla por los pacientes indicados.
     *
     * @param pacientes pacientes a mostrar
     */
    public void mostrarPacientes(List<Paciente> pacientes) {
        tabla.clearSelection();
        filas = new ArrayList<>(pacientes);
        modeloTabla.setRowCount(0);
        for (Paciente p : filas) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getApellido(), p.getNombre(), p.getDni(), p.getFechaIngreso().format(Paciente.FORMATO_FECHA)
            });
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    public void limpiarFormulario() {
        tabla.clearSelection();
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        selectorFecha.limpiar();
    }

    private void cargarFormulario(Paciente p) {
        txtId.setText(String.valueOf(p.getId()));
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtDni.setText(p.getDni());
        selectorFecha.setFecha(p.getFechaIngreso());
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private void configurarEventos() {
        btnNuevo.addActionListener(e -> limpiarFormulario());

        btnGuardar.addActionListener(e -> controlador.guardar(
                txtNombre.getText(), txtApellido.getText(), txtDni.getText(), selectorFecha.getTexto()));

        btnActualizar.addActionListener(e -> controlador.actualizar(
                txtId.getText(), txtNombre.getText(), txtApellido.getText(), txtDni.getText(), selectorFecha.getTexto()));

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isBlank()) {
                mostrarError("Seleccione un paciente en la tabla para eliminarlo.");
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar el paciente con ID " + txtId.getText() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                controlador.eliminar(txtId.getText());
            }
        });

        btnBuscar.addActionListener(e -> controlador.buscarPorDni(txtBuscarDni.getText()));
        btnVerTodos.addActionListener(e -> controlador.listar());
        btnExportar.addActionListener(e -> exportar());
    }

    private void exportar() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar listado de pacientes");
        selector.setSelectedFile(new File("pacientes.pdf"));
        if (selector.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File destino = selector.getSelectedFile();
            if (!destino.getName().toLowerCase().endsWith(".pdf")) {
                destino = new File(destino.getAbsolutePath() + ".pdf");
            }
            controlador.exportarPdf(destino);
        }
    }
}
