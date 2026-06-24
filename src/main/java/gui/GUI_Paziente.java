package gui;

import controller.Controller;
import javax.swing.*;

public class GUI_Paziente {
    public JPanel panelMain;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtCodiceFiscale;
    private JButton btnSalva;
    private Controller controller;

    public GUI_Paziente(Controller controller) {
        this.controller = controller;

        btnSalva.addActionListener(e -> {
            try {
                controller.aggiungiPaziente(txtNome.getText(), txtCognome.getText(), txtCodiceFiscale.getText());
                JOptionPane.showMessageDialog(null, "Paziente salvato!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}