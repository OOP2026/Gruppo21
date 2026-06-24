package gui;

import controller.Controller;
import javax.swing.*;

public class GUI_Amministratore {
    public JPanel panelMain;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnSalva;
    private Controller controller;

    public GUI_Amministratore(Controller controller) {
        this.controller = controller;

        btnSalva.addActionListener(e -> {
            try {
                controller.aggiungiAmministratore(txtNome.getText(), txtCognome.getText(),
                        txtEmail.getText(), new String(txtPassword.getPassword()));
                JOptionPane.showMessageDialog(null, "Admin salvato!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        });
    }
}