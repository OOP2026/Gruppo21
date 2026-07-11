package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

public class Login {
    protected JFrame frame;
    private JPanel panel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JComboBox comboBox1;
    private JButton ritornaButton;
    private JButton confermaButton;
    private Controller controller;
    private boolean selezione = false;

    public Login(JFrame main, Controller controller) {
        this.controller = controller;
        frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        comboBox1.addActionListener(e -> {
            String value = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
            selezione = value.equals("Amministratore");
        });

        ritornaButton.addActionListener(e -> {
            main.setVisible(true);
            frame.dispose();
        });

        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textField1.getText();
                String password = new String(passwordField1.getPassword());

                try {
                    boolean accessoRiuscito = selezione ?
                            controller.loginAmministratore(email, password) :
                            controller.loginMedico(email, password);

                    if (accessoRiuscito) {
                        JOptionPane.showMessageDialog(frame, "Accesso eseguito con successo!");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Credenziali errate.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Errore Database: " + ex.getMessage());
                }
            }
        });
    }
}