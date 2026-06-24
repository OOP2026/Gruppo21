package gui;

import controller.Controller;
import model.Reparto;
import javax.swing.*;

public class GUI_Stanza {
    public JPanel panelMain;
    private JComboBox<Reparto> comboReparto;
    private JButton btnSalva;
    private Controller controller;

    public GUI_Stanza(Controller controller) {
        this.controller = controller;

        if (controller.getReparti() != null) {
            for (Reparto r : controller.getReparti()) {
                comboReparto.addItem(r);
            }
        }

        btnSalva.addActionListener(e -> {
            try {
                Reparto selezionato = (Reparto) comboReparto.getSelectedItem();
                if (selezionato != null) {
                    controller.aggiungiStanza(selezionato);
                    JOptionPane.showMessageDialog(null, "Stanza aggiunta!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}