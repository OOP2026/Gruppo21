package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InserimentoEntitaFrame extends JFrame {

    private Controller controller;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public InserimentoEntitaFrame(Controller controller) {
        this.controller = controller;

        setTitle("Gestione Inserimenti Sistema");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. SCHEDA MEDICO
        JPanel panelMedico = new JPanel(new GridLayout(7, 2, 10, 10));
        panelMedico.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField txtNomeMedico = new JTextField();
        JTextField txtCognomeMedico = new JTextField();
        JTextField txtEmailMedico = new JTextField();
        JPasswordField txtPassMedico = new JPasswordField();
        JTextField txtTipoMedico = new JTextField();
        JComboBox<Reparto> comboReparto = new JComboBox<>();
        if (controller.getReparti() != null) {
            for (Reparto r : controller.getReparti()) comboReparto.addItem(r);
        }
        JButton btnSalvaMedico = new JButton("Registra Medico");
        panelMedico.add(new JLabel("Nome:")); panelMedico.add(txtNomeMedico);
        panelMedico.add(new JLabel("Cognome:")); panelMedico.add(txtCognomeMedico);
        panelMedico.add(new JLabel("Email:")); panelMedico.add(txtEmailMedico);
        panelMedico.add(new JLabel("Password:")); panelMedico.add(txtPassMedico);
        panelMedico.add(new JLabel("Specializzazione:")); panelMedico.add(txtTipoMedico);
        panelMedico.add(new JLabel("Reparto:")); panelMedico.add(comboReparto);
        panelMedico.add(new JLabel("")); panelMedico.add(btnSalvaMedico);

        btnSalvaMedico.addActionListener(e -> {
            try {
                controller.aggiungiMedico(txtNomeMedico.getText(), txtCognomeMedico.getText(),
                        txtEmailMedico.getText(), new String(txtPassMedico.getPassword()),
                        txtTipoMedico.getText(), (Reparto) comboReparto.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Medico salvato!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage()); }
        });

        // 2. SCHEDA PAZIENTE
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
                controller.aggiungiPaziente(txtNomePaziente.getText(), txtCognomePaziente.getText(), txtCodiceFiscale.getText());
                JOptionPane.showMessageDialog(this, "Paziente salvato!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage()); }
        });

        // 3. SCHEDA AMMINISTRATORE
        JPanel panelAdmin = new JPanel(new GridLayout(5, 2, 10, 10));
        panelAdmin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField txtNomeAdmin = new JTextField();
        JTextField txtCognomeAdmin = new JTextField();
        JTextField txtEmailAdmin = new JTextField();
        JPasswordField txtPassAdmin = new JPasswordField();
        JButton btnSalvaAdmin = new JButton("Registra Admin");
        panelAdmin.add(new JLabel("Nome:")); panelAdmin.add(txtNomeAdmin);
        panelAdmin.add(new JLabel("Cognome:")); panelAdmin.add(txtCognomeAdmin);
        panelAdmin.add(new JLabel("Email:")); panelAdmin.add(txtEmailAdmin);
        panelAdmin.add(new JLabel("Password:")); panelAdmin.add(txtPassAdmin);
        panelAdmin.add(new JLabel("")); panelAdmin.add(btnSalvaAdmin);

        btnSalvaAdmin.addActionListener(e -> {
            try {
                controller.aggiungiAmministratore(txtNomeAdmin.getText(), txtCognomeAdmin.getText(),
                        txtEmailAdmin.getText(), new String(txtPassAdmin.getPassword()));
                JOptionPane.showMessageDialog(this, "Amministratore salvato!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage()); }
        });

        // 4. SCHEDA STANZA E LETTO (Raggruppate per comodità visiva)
        JPanel panelStruttura = new JPanel(new GridLayout(5, 2, 10, 10));
        panelStruttura.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JComboBox<Reparto> comboRepartoStanza = new JComboBox<>();
        if (controller.getReparti() != null) {
            for (Reparto r : controller.getReparti()) comboRepartoStanza.addItem(r);
        }
        JButton btnSalvaStanza = new JButton("Aggiungi Stanza a Reparto");

        JComboBox<Stanza> comboStanzaLetto = new JComboBox<>();
        for (Stanza s : controller.getTutteStanze()) comboStanzaLetto.addItem(s);
        JTextField txtCodiceLetto = new JTextField();
        JButton btnSalvaLetto = new JButton("Aggiungi Letto a Stanza");

        panelStruttura.add(new JLabel("Seleziona Reparto per Stanza:")); panelStruttura.add(comboRepartoStanza);
        panelStruttura.add(new JLabel("")); panelStruttura.add(btnSalvaStanza);
        panelStruttura.add(new JLabel("---")); panelStruttura.add(new JLabel("---"));
        panelStruttura.add(new JLabel("Stanza per Letto:")); panelStruttura.add(comboStanzaLetto);
        panelStruttura.add(new JLabel("Codice Letto:")); panelStruttura.add(txtCodiceLetto);
        panelStruttura.add(new JLabel("")); panelStruttura.add(btnSalvaLetto);

        btnSalvaStanza.addActionListener(e -> {
            try {
                controller.aggiungiStanza((Reparto) comboRepartoStanza.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Stanza aggiunta! Riapri la finestra per aggiornare i dati.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage()); }
        });
        btnSalvaLetto.addActionListener(e -> {
            try {
                controller.aggiungiLetto(txtCodiceLetto.getText(), (Stanza) comboStanzaLetto.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Letto salvato! Riapri la finestra per aggiornare i dati.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage()); }
        });

        // 5. SCHEDA RICOVERO E TURNO
        JPanel panelOperazioni = new JPanel(new GridLayout(7, 2, 10, 10));
        panelOperazioni.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JComboBox<Paziente> comboPazienti = new JComboBox<>();
        for (Paziente p : controller.getPazienti()) comboPazienti.addItem(p);
        JComboBox<Letto> comboLetti = new JComboBox<>();
        for (Letto l : controller.getTuttiLetti()) comboLetti.addItem(l);
        JTextField txtInizio = new JTextField("2023-10-01 08:00");
        JTextField txtFine = new JTextField("2023-10-10 18:00");
        JButton btnSalvaRicovero = new JButton("Registra Ricovero");
        JButton btnSalvaTurno = new JButton("Registra Turno (usa date sopra)");

        panelOperazioni.add(new JLabel("Paziente:")); panelOperazioni.add(comboPazienti);
        panelOperazioni.add(new JLabel("Letto:")); panelOperazioni.add(comboLetti);
        panelOperazioni.add(new JLabel("Data/Ora Inizio (yyyy-MM-dd HH:mm):")); panelOperazioni.add(txtInizio);
        panelOperazioni.add(new JLabel("Data/Ora Fine (yyyy-MM-dd HH:mm):")); panelOperazioni.add(txtFine);
        panelOperazioni.add(new JLabel("")); panelOperazioni.add(btnSalvaRicovero);
        panelOperazioni.add(new JLabel("")); panelOperazioni.add(btnSalvaTurno);

        btnSalvaRicovero.addActionListener(e -> {
            try {
                LocalDateTime inizio = LocalDateTime.parse(txtInizio.getText(), formatter);
                LocalDateTime fine = LocalDateTime.parse(txtFine.getText(), formatter);
                controller.aggiungiRicovero((Paziente) comboPazienti.getSelectedItem(), (Letto) comboLetti.getSelectedItem(), inizio, fine);
                JOptionPane.showMessageDialog(this, "Ricovero salvato!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore formato data o inserimento: " + ex.getMessage()); }
        });

        btnSalvaTurno.addActionListener(e -> {
            try {
                LocalDateTime inizio = LocalDateTime.parse(txtInizio.getText(), formatter);
                LocalDateTime fine = LocalDateTime.parse(txtFine.getText(), formatter);
                controller.aggiungiTurnoLavorativo(inizio, fine);
                JOptionPane.showMessageDialog(this, "Turno salvato!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Errore formato data: " + ex.getMessage()); }
        });

        // Aggiunta finale schede
        tabbedPane.addTab("Medico", panelMedico);
        tabbedPane.addTab("Paziente", panelPaziente);
        tabbedPane.addTab("Amministratore", panelAdmin);
        tabbedPane.addTab("Stanze e Letti", panelStruttura);
        tabbedPane.addTab("Ricoveri e Turni", panelOperazioni);
        add(tabbedPane);
    }
}