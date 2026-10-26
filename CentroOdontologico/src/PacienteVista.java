import model.Paciente;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import java.awt.*;

public class PacienteVista extends JFrame {

    // Componentes de la interfaz gráfica
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtFechaIngreso;
    
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Referencia al controlador
    private PacienteControlador controlador;

    public PacienteVista() {
        // Configuración básica de la ventana
        setTitle("Gestión de Pacientes - Centro Odontológico");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10)); // Cuadrícula de 7 filas y 2 columnas

        // Inicializar etiquetas y campos de texto
        add(new JLabel("  ID (Solo para buscar/actualizar/eliminar):"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("  Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel("  Apellido:"));
        txtApellido = new JTextField();
        add(txtApellido);

        add(new JLabel("  DNI:"));
        txtDni = new JTextField();
        add(txtDni);

        add(new JLabel("  Fecha Ingreso (YYYY-MM-DD):"));
        txtFechaIngreso = new JTextField();
        add(txtFechaIngreso);

        // Inicializar botones
        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar (por ID)");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar (por ID)");
        btnLimpiar = new JButton("Limpiar Campos");

        // Agregar botones a un panel especial
        JPanel panelBotones1 = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotones1.add(btnGuardar);
        panelBotones1.add(btnBuscar);
        add(panelBotones1);

        JPanel panelBotones2 = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotones2.add(btnActualizar);
        panelBotones2.add(btnEliminar);
        add(panelBotones2);
        
        // Fila extra para el botón de limpiar
        add(new JLabel("")); 
        add(btnLimpiar);

        // Configurar las acciones de los botones (Event Listeners)
        configurarEventos();
    }

    // Método para inyectar el controlador desde el Main
    public void setControlador(PacienteControlador controlador) {
        this.controlador = controlador;
    }

    // Método para arrancar la interfaz gráfica
    public void iniciar() {
        this.setLocationRelativeTo(null); // Centrar en la pantalla
        this.setVisible(true); // Hacer visible la ventana
    }

    // Método para mostrar pop-ups de éxito o error
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Método para cargar los datos en la pantalla cuando se busca un paciente
    public void mostrarDatosPaciente(Paciente paciente) {
        txtId.setText(String.valueOf(paciente.getId()));
        txtNombre.setText(paciente.getNombre());
        txtApellido.setText(paciente.getApellido());
        txtDni.setText(paciente.getDni());
        txtFechaIngreso.setText(paciente.getFechaIngreso().toString());
    }

    // --- Lógica de los botones ---
    private void configurarEventos() {
        
        // Evento: Botón Guardar
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                String dni = txtDni.getText();
                LocalDate fecha = LocalDate.parse(txtFechaIngreso.getText()); // Formato: YYYY-MM-DD
                
                controlador.registrarPaciente(nombre, apellido, dni, fecha);
                limpiarCampos();
            } catch (DateTimeParseException ex) {
                mostrarMensaje("Error: La fecha debe tener el formato YYYY-MM-DD");
            } catch (Exception ex) {
                mostrarMensaje("Error al guardar: Verifique los datos ingresados.");
            }
        });

        // Evento: Botón Buscar
        btnBuscar.addActionListener(e -> {
            try {
                Integer id = Integer.parseInt(txtId.getText());
                controlador.buscarPaciente(id);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese un ID numérico válido para buscar.");
            }
        });

        // Evento: Botón Actualizar
        btnActualizar.addActionListener(e -> {
            try {
                Integer id = Integer.parseInt(txtId.getText());
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                String dni = txtDni.getText();
                LocalDate fecha = LocalDate.parse(txtFechaIngreso.getText());
                
                controlador.actualizarPaciente(id, nombre, apellido, dni, fecha);
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese un ID numérico válido para actualizar.");
            } catch (DateTimeParseException ex) {
                mostrarMensaje("Error: La fecha debe tener el formato YYYY-MM-DD");
            }
        });

        // Evento: Botón Eliminar
        btnEliminar.addActionListener(e -> {
            try {
                Integer id = Integer.parseInt(txtId.getText());
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el paciente con ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.eliminarPaciente(id);
                    limpiarCampos();
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error: Ingrese un ID numérico válido para eliminar.");
            }
        });

        // Evento: Botón Limpiar
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    // Método auxiliar para vaciar las cajas de texto
    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtFechaIngreso.setText("");
    }
}