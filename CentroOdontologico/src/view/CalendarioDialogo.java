package view;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

/**
 * Calendario mensual modal: el usuario navega entre meses y elige un día con un clic.
 */
public final class CalendarioDialogo {

    private static final Locale ES = Locale.forLanguageTag("es-AR");

    private CalendarioDialogo() {
        // Clase utilitaria: no se instancia.
    }

    /**
     * Muestra el calendario y espera la elección del usuario.
     *
     * @param owner   ventana propietaria del diálogo (puede ser nula)
     * @param inicial fecha que se muestra al abrir el calendario
     * @return la fecha elegida, o vacío si el usuario cancela
     */
    public static Optional<LocalDate> elegir(Window owner, LocalDate inicial) {
        JDialog dialogo = new JDialog(owner, "Elegir fecha", Dialog.ModalityType.APPLICATION_MODAL);
        LocalDate[] seleccion = new LocalDate[1];
        YearMonth[] mes = {YearMonth.from(inicial)};

        JLabel lblMes = new JLabel("", SwingConstants.CENTER);
        lblMes.setFont(lblMes.getFont().deriveFont(Font.BOLD, 15f));
        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        JPanel grilla = new JPanel(new GridLayout(0, 7, 4, 4));

        Runnable dibujar = () -> {
            grilla.removeAll();
            for (DayOfWeek d : DayOfWeek.values()) {
                grilla.add(encabezado(d.getDisplayName(TextStyle.SHORT, ES)));
            }
            String nombreMes = mes[0].getMonth().getDisplayName(TextStyle.FULL, ES);
            lblMes.setText(Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1)
                    + " " + mes[0].getYear());
            int espacios = mes[0].atDay(1).getDayOfWeek().getValue() - 1;
            for (int i = 0; i < espacios; i++) {
                grilla.add(new JLabel(""));
            }
            for (int dia = 1; dia <= mes[0].lengthOfMonth(); dia++) {
                LocalDate fecha = mes[0].atDay(dia);
                JButton boton = new JButton(String.valueOf(dia));
                boton.addActionListener(e -> {
                    seleccion[0] = fecha;
                    dialogo.dispose();
                });
                grilla.add(boton);
            }
            grilla.revalidate();
            grilla.repaint();
        };

        btnAnterior.addActionListener(e -> {
            mes[0] = mes[0].minusMonths(1);
            dibujar.run();
        });
        btnSiguiente.addActionListener(e -> {
            mes[0] = mes[0].plusMonths(1);
            dibujar.run();
        });

        JButton btnHoy = new JButton("Hoy");
        btnHoy.addActionListener(e -> {
            seleccion[0] = LocalDate.now();
            dialogo.dispose();
        });
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialogo.dispose());

        JPanel cabecera = new JPanel(new BorderLayout(6, 0));
        cabecera.add(btnAnterior, BorderLayout.WEST);
        cabecera.add(lblMes, BorderLayout.CENTER);
        cabecera.add(btnSiguiente, BorderLayout.EAST);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pie.add(btnHoy);
        pie.add(btnCancelar);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(cabecera, BorderLayout.NORTH);
        contenido.add(grilla, BorderLayout.CENTER);
        contenido.add(pie, BorderLayout.SOUTH);

        dialogo.setContentPane(contenido);
        dibujar.run();
        dialogo.pack();
        dialogo.setResizable(false);
        dialogo.setLocationRelativeTo(owner);
        dialogo.setVisible(true);
        return Optional.ofNullable(seleccion[0]);
    }

    private static JLabel encabezado(String texto) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
        return etiqueta;
    }
}
