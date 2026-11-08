package view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Campo de fecha de solo lectura: la fecha se elige únicamente desde el calendario. Muestra el valor en formato DD/MM/AAAA
 * y permite elegirlo con un clic mediante {@link CalendarioDialogo}.
 */
public class SelectorFecha extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField txtFecha = new JTextField(10);
    private final JButton btnCalendario = new JButton("Calendario...");

    /** Construye el campo con su botón de calendario. */
    public SelectorFecha() {
        super(new BorderLayout(4, 0));
        txtFecha.setEditable(false);
        txtFecha.setToolTipText("Elija la fecha con el calendario");
        txtFecha.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                elegirFecha();
            }
        });
        add(txtFecha, BorderLayout.CENTER);
        add(btnCalendario, BorderLayout.EAST);
        btnCalendario.addActionListener(e -> elegirFecha());
    }

    /** @return el texto tal como está en el campo */
    public String getTexto() {
        return txtFecha.getText();
    }

    /**
     * @param fecha fecha a mostrar, o {@code null} para dejar el campo vacío
     */
    public void setFecha(LocalDate fecha) {
        txtFecha.setText(fecha == null ? "" : fecha.format(FORMATO));
    }

    /** Vacía el campo. */
    public void limpiar() {
        txtFecha.setText("");
    }

    private void elegirFecha() {
        LocalDate actual = parsear(txtFecha.getText());
        LocalDate inicial = actual != null ? actual : LocalDate.now();
        CalendarioDialogo.elegir(SwingUtilities.getWindowAncestor(this), inicial).ifPresent(this::setFecha);
    }

    private LocalDate parsear(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
