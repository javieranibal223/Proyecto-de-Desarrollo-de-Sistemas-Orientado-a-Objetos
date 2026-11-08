package view;

import controller.TurnoControlador;
import model.Odontologo;
import model.Paciente;
import model.Turno;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Pantalla Swing del CRUD de turnos. Relaciona un paciente con un odontólogo
 * mediante combos cargados desde la base de datos.
 * <p>
 * No contiene lógica de negocio: cada botón delega en el {@link TurnoControlador}.
 */
public class TurnoVista extends JFrame {
    private static final long serialVersionUID = 1L;


    private static final String[] COLUMNAS = {"ID", "Fecha y hora", "Paciente", "Odontólogo", "Motivo"};

    private final JTextField txtId = new JTextField();
    private final JComboBox<Paciente> cmbPaciente = new JComboBox<>();
    private final JComboBox<Odontologo> cmbOdontologo = new JComboBox<>();
    private static final String[] HORAS = generarHoras();

    private final SelectorFecha selectorFecha = new SelectorFecha();
    private final JComboBox<String> cmbHora = new JComboBox<>(HORAS);
    private final JTextField txtMotivo = new JTextField();

    private final JButton btnNuevo = new JButton("Nuevo / Limpiar");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private List<Turno> filas = new ArrayList<>();

    private TurnoControlador controlador;

    /** Construye la ventana con el formulario, la tabla y los botones. */
    public TurnoVista() {
        super("Gestión de Turnos - Centro Odontológico");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        txtId.setEditable(false);
        JPanel formulario = new JPanel(new GridLayout(6, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del turno"));
        agregarCampo(formulario, "ID (seleccione en la tabla):", txtId);
        agregarCampo(formulario, "Paciente:", cmbPaciente);
        agregarCampo(formulario, "Odontólogo:", cmbOdontologo);
        agregarCampo(formulario, "Fecha:", selectorFecha);
        agregarCampo(formulario, "Hora:", cmbHora);
        agregarCampo(formulario, "Motivo (opcional):", txtMotivo);
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
        add(acciones, BorderLayout.SOUTH);

        configurarEventos();
        setSize(900, 600);
    }

    /**
     * Asigna el controlador que atenderá las acciones de la pantalla.
     *
     * @param controlador controlador de turnos
     */
    public void setControlador(TurnoControlador controlador) {
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
     * Reemplaza el contenido del combo de pacientes.
     *
     * @param pacientes pacientes disponibles
     */
    public void mostrarPacientes(List<Paciente> pacientes) {
        cmbPaciente.setModel(new DefaultComboBoxModel<>(pacientes.toArray(new Paciente[0])));
    }

    /**
     * Reemplaza el contenido del combo de odontólogos.
     *
     * @param odontologos odontólogos disponibles
     */
    public void mostrarOdontologos(List<Odontologo> odontologos) {
        cmbOdontologo.setModel(new DefaultComboBoxModel<>(odontologos.toArray(new Odontologo[0])));
    }

    /**
     * Reemplaza el contenido de la tabla de turnos.
     *
     * @param turnos turnos a mostrar
     */
    public void mostrarTurnos(List<Turno> turnos) {
        tabla.clearSelection();
        filas = new ArrayList<>(turnos);
        modeloTabla.setRowCount(0);
        for (Turno t : filas) {
            modeloTabla.addRow(new Object[]{
                t.getId(), t.getFechaHora().format(Turno.FORMATO),
                t.getPaciente().toString(), t.getOdontologo().toString(), t.getMotivo()
            });
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    public void limpiarFormulario() {
        tabla.clearSelection();
        txtId.setText("");
        selectorFecha.limpiar();
        cmbHora.setSelectedIndex(0);
        txtMotivo.setText("");
        if (cmbPaciente.getItemCount() > 0) {
            cmbPaciente.setSelectedIndex(0);
        }
        if (cmbOdontologo.getItemCount() > 0) {
            cmbOdontologo.setSelectedIndex(0);
        }
    }

    private void cargarFormulario(Turno t) {
        txtId.setText(String.valueOf(t.getId()));
        cmbPaciente.setSelectedItem(t.getPaciente());
        cmbOdontologo.setSelectedItem(t.getOdontologo());
        selectorFecha.setFecha(t.getFechaHora().toLocalDate());
        seleccionarHora(t.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm")));
        txtMotivo.setText(t.getMotivo());
    }

    private static String[] generarHoras() {
        List<String> horas = new ArrayList<>();
        for (int minutos = 8 * 60; minutos <= 19 * 60 + 30; minutos += 30) {
            horas.add(String.format("%02d:%02d", minutos / 60, minutos % 60));
        }
        return horas.toArray(new String[0]);
    }

    private String fechaYHora() {
        String fecha = selectorFecha.getTexto().trim();
        return fecha.isEmpty() ? "" : fecha + " " + cmbHora.getSelectedItem();
    }

    private void seleccionarHora(String hora) {
        if (((DefaultComboBoxModel<String>) cmbHora.getModel()).getIndexOf(hora) < 0) {
            cmbHora.addItem(hora);
        }
        cmbHora.setSelectedItem(hora);
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private void configurarEventos() {
        btnNuevo.addActionListener(e -> limpiarFormulario());

        btnGuardar.addActionListener(e -> controlador.guardar(
                (Paciente) cmbPaciente.getSelectedItem(), (Odontologo) cmbOdontologo.getSelectedItem(),
                fechaYHora(), txtMotivo.getText()));

        btnActualizar.addActionListener(e -> controlador.actualizar(
                txtId.getText(), (Paciente) cmbPaciente.getSelectedItem(),
                (Odontologo) cmbOdontologo.getSelectedItem(), fechaYHora(), txtMotivo.getText()));

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isBlank()) {
                mostrarError("Seleccione un turno en la tabla para eliminarlo.");
                return;
            }
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar el turno con ID " + txtId.getText() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                controlador.eliminar(txtId.getText());
            }
        });
    }
}
