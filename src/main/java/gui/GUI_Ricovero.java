package gui;

import controller.Controller;
import model.Letto;
import model.Paziente;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUI_Ricovero {
    public JPanel panelMain;
    private JComboBox<Paziente> comboPaziente;
    private JComboBox<Letto> comboLetto;
    private JTextField txtInizio;
    private JTextField txtFine;
    private JButton btnSalva;
    private Controller controller;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GUI_Ricovero(Controller controller) {
        this.controller = controller;

        txtInizio.setText("2023-10-01 08:00");
        txtFine.setText("2023-10-10 18:00");

        if (controller.getPazienti() != null) {
            for (Paziente p : controller.getPazienti()) comboPaziente.addItem(p);
        }
        if (controller.getLetti() != null) {
            for (Letto l : controller.getLetti()) comboLetto.addItem(l);
        }

        btnSalva.addActionListener(e -> {
            try {
                LocalDateTime inizio = LocalDateTime.parse(txtInizio.getText(), formatter);
                LocalDateTime fine = LocalDateTime.parse(txtFine.getText(), formatter);
                controller.aggiungiRicovero((Paziente) comboPaziente.getSelectedItem(),
                        (Letto) comboLetto.getSelectedItem(), inizio, fine);
                JOptionPane.showMessageDialog(null, "Ricovero salvato!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}