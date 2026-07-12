package gui;

import controller.Controller;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI_TurnoLavorativo {
    public JPanel panelMain;
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JTextField txtInizio;
    private JTextField txtFine;
    private JButton btnSalva;
    private Controller controller;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GUI_TurnoLavorativo(Controller controller) {
        this.controller = controller;

        txtInizio.setText("2023-10-01 08:00");
        txtFine.setText("2023-10-01 16:00");

        btnSalva.addActionListener(e -> {
            try {
                LocalDateTime inizio = LocalDateTime.parse(txtInizio.getText(), formatter);
                LocalDateTime fine = LocalDateTime.parse(txtFine.getText(), formatter);
                //controller.aggiungiTurnoLavorativo(inizio, fine);
                JOptionPane.showMessageDialog(null, "Turno salvato!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}