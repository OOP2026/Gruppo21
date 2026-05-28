package gui;

import controller.Controller;
import model.Reparto;

import javax.swing.*;
import java.awt.*;

public class InserimentoEntitaFrame extends JFrame {

    private Controller controller;

    public InserimentoEntitaFrame(Controller controller) {
        this.controller = controller;

        setTitle("Gestione Inserimenti");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- SCHEDA MEDICO ---
        JPanel panelMedico = new JPanel(new GridLayout(7, 2, 10, 10));
        panelMedico.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNomeMedico = new JTextField();
        JTextField txtCognomeMedico = new JTextField();
        JTextField txtEmailMedico = new JTextField();
        JPasswordField txtPassMedico = new JPasswordField();
        JTextField txtTipoMedico = new JTextField();

        JComboBox<Reparto> comboReparto = new JComboBox<>();
        if (controller.getReparti() != null) {
            for (Reparto r : controller.getReparti()) {
                comboReparto.addItem(r);
            }
        }

        JButton btnSalvaMedico = new JButton("Registra Medico");

        panelMedico.add(new JLabel("Nome:")); panelMedico.add(txtNomeMedico);
        panelMedico.add(new JLabel("Cognome:")); panelMedico.add(txtCognomeMedico);
        panelMedico.add(new JLabel("Email:")); panelMedico.add(txtEmailMedico);
        panelMedico.add(new JLabel("Password:")); panelMedico.add(txtPassMedico);
        panelMedico.add(new JLabel("Specializzazione (Tipo):")); panelMedico.add(txtTipoMedico);
        panelMedico.add(new JLabel("Seleziona Reparto:")); panelMedico.add(comboReparto);
        panelMedico.add(new JLabel("")); panelMedico.add(btnSalvaMedico);

        btnSalvaMedico.addActionListener(e -> {
            try {
                Reparto repartoSelezionato = (Reparto) comboReparto.getSelectedItem();
                if (repartoSelezionato == null) {
                    throw new RuntimeException("Nessun reparto disponibile o selezionato!");
                }

                controller.aggiungiMedico(
                        txtNomeMedico.getText(),
                        txtCognomeMedico.getText(),
                        txtEmailMedico.getText(),
                        new String(txtPassMedico.getPassword()),
                        txtTipoMedico.getText(),
                        repartoSelezionato
                );
                JOptionPane.showMessageDialog(this, "Medico salvato con successo!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- SCHEDA PAZIENTE ---
        JPanel panelPaziente = new JPanel(new GridLayout(4, 2, 10, 10));
        panelPaziente.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtNomePaziente = new JTextField();
        JTextField txtCognomePaziente = new JTextField();
        JTextField txtCodiceFiscale = new JTextField();
        JButton btnSalvaPaziente = new JButton("Registra Paziente");

        panelPaziente.add(new JLabel("Nome:")); panelPaziente.add(txtNomePaziente);
        panelPaziente.add(new JLabel("Cognome:")); panelPaziente.add(txtCognomePaziente);
        panelPaziente.add(new JLabel("Codice Fiscale:")); panelPaziente.add(txtCodiceFiscale);
        panelPaziente.add(new JLabel("")); panelPaziente.add(btnSalvaPaziente);

        btnSalvaPaziente.addActionListener(e -> {
            try {
                controller.aggiungiPaziente(
                        txtNomePaziente.getText(),
                        txtCognomePaziente.getText(),
                        txtCodiceFiscale.getText()
                );
                JOptionPane.showMessageDialog(this, "Paziente salvato con successo!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        tabbedPane.addTab("Nuovo Medico", panelMedico);
        tabbedPane.addTab("Nuovo Paziente", panelPaziente);
        add(tabbedPane);
    }
}