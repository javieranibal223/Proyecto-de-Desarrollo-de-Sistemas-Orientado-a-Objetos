package view;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de menú principal desde la que se abren las pantallas de cada entidad.
 */
public class MenuPrincipalVista extends JFrame {
    private static final long serialVersionUID = 1L;


    private final JButton btnPacientes = new JButton("Gestión de pacientes");
    private final JButton btnOdontologos = new JButton("Gestión de odontólogos");
    private final JButton btnTurnos = new JButton("Gestión de turnos");

    /** Construye el menú con un botón por entidad. */
    public MenuPrincipalVista() {
        super("Centro Odontológico - Menú principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));
        add(btnPacientes);
        add(btnOdontologos);
        add(btnTurnos);
        setSize(380, 260);
    }

    /** @return botón que abre la gestión de pacientes */
    public JButton getBtnPacientes() { return btnPacientes; }

    /** @return botón que abre la gestión de odontólogos */
    public JButton getBtnOdontologos() { return btnOdontologos; }

    /** @return botón que abre la gestión de turnos */
    public JButton getBtnTurnos() { return btnTurnos; }

    /** Centra el menú en pantalla y lo muestra. */
    public void iniciar() {
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
