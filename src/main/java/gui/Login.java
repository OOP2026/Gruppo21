package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private boolean selezione = false; //false per medico, true per ammnistratore


    public Login(JFrame main, Controller controller) {
        this.controller = controller;
        frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
                selezione = value.equals("Amministratore");
                System.out.println("Selezionato: " + value);
            }
        });
        ritornaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
