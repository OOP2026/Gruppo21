package gui;

import controller.Controller;
import model.Stanza;
import javax.swing.*;

public class GUI_Letto {
    public JPanel panelMain;
    private JTextPane textPane1;
    private JTextField txtCodice;
    private JComboBox<Stanza> comboStanza;
    private JButton btnSalva;
    private Controller controller;

    public GUI_Letto(Controller controller) {
        this.controller = controller;

        if (controller.getTutteStanze() != null) {
            for (Stanza s : controller.getTutteStanze()) {
                comboStanza.addItem(s);
            }
        }

        btnSalva.addActionListener(e -> {
            try {
                Stanza selezionata = (Stanza) comboStanza.getSelectedItem();
                if (selezionata != null) {
                    controller.aggiungiLetto(txtCodice.getText(), selezionata);
                    JOptionPane.showMessageDialog(null, "Letto salvato!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}