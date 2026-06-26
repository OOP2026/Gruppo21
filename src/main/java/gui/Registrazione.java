package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.Objects;

public class Registrazione {
    protected JFrame frame;
    private JPanel panel;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JPasswordField passwordField1;
    private JButton ritornaButton;
    private JButton confermaButton;
    private boolean selezione = false; //false per medico, true per ammnistratore
    private Controller controller;

    private String email;
    private String password;

    private void registrazione(Controller controller) {
        String email = textField1.getText();
        String password = Arrays.toString(passwordField1.getPassword());
        //Viene salvata come "[PASSWORD]", ove PASSWORD è cio' che inserisce l'utente.

        if (selezione) {
            controller.aggiungiAmministratoreAnonimo(email, password);
            chiamaGUIAmministratore(controller);
        }
        else {}//aggiungiMedico con altri reparti vari

        System.out.println("Aggiunto amministratore. Email: " + email + ". Password: " + password + "\n");
    }

    private boolean checkEmail() {
        return email.contains("@") && email.contains(".");
    }

    private void ritornaAlMain(JFrame main) {
        main.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    private void chiamaGUIAmministratore(Controller controller) {
        GUI_Home amministratore = new GUI_Home(controller);
        amministratore.frame.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    private void chiamaGUIMedico(Controller controller) {
        GUI_Medico medico = new GUI_Medico(controller);
        medico.frame.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    public Registrazione(JFrame main, Controller controller) {
        this.controller = controller;
        frame = new JFrame("Registrazione");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        passwordField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrazione(controller);
            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                email = textField1.getText();
                if(!checkEmail()) {
                    textField1.setText("");
                    JOptionPane.showMessageDialog(frame, "Attenzione: l'email inserita non è valida.\nDeve contenere la chiocciola (@) e un punto (dominio).\nIl campo è stato svuotato.", "Errore Formato Email", JOptionPane.WARNING_MESSAGE);
                }
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
                ritornaAlMain(main);
            }
        });
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrazione(controller);
            }
        });
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                email = textField1.getText();
                if(!email.isEmpty() && !checkEmail()) {
                    textField1.setText("");
                    JOptionPane.showMessageDialog(frame, "Attenzione: l'email inserita non è valida.\nDeve contenere la chiocciola (@) e un punto (dominio).\nIl campo è stato svuotato.", "Errore Formato Email", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}