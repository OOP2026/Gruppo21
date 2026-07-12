package controller;

import implementazioneDao.AmministratoreImplementazioneDAO;
import implementazioneDao.MedicoImplementazioneDAO;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    // Liste principali in memoria
    private List<Medico> medici;
    private List<Ricovero> ricoveri;
    private List<Amministratore> amministratori;
    private List<Paziente> pazienti;
    private List<Reparto> reparti;
    private List<Stanza> stanze;
    private List<Letto> letti;
    private List<TurnoLavorativo> turni;
    private List<Visita> visite;
    private List<InterventoChirurgico> interventi;

    private String utenteLoggatoRuolo = null;
    private String emailUtenteLoggato = null;

    public Controller() {
        svuotaMemoria();
    }

    private void svuotaMemoria() {
        medici = new ArrayList<>();
        ricoveri = new ArrayList<>();
        amministratori = new ArrayList<>();
        pazienti = new ArrayList<>();
        reparti = new ArrayList<>();
        stanze = new ArrayList<>();
        letti = new ArrayList<>();
        turni = new ArrayList<>();
        visite = new ArrayList<>();
        interventi = new ArrayList<>();
    }

    public boolean loginAmministratore(String email, String password) throws SQLException {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        if (!adminDao.verificaCredenziali(email, password)) return false;

        this.utenteLoggatoRuolo = "Amministratore";
        this.emailUtenteLoggato = email;

        svuotaMemoria();
        scaricaTabelleAmministratore(adminDao);
        return true;
    }

    private void scaricaTabelleAmministratore(AmministratoreImplementazioneDAO dao) throws SQLException {
        // 1. IL DAO FA TUTTO IL LAVORO PESANTE (Scarica e incastra gli oggetti tramite FK)
        dao.istanziaMemoriaLocale(0);

        // 2. IL CONTROLLER SI LIMITA A RITIRARE LE LISTE GIÀ PRONTE E ASSEMBLATE
        this.reparti = dao.getReparti(0);
        this.stanze = dao.getStanze(0);
        this.letti = dao.getLetti(0);
        this.pazienti = dao.getPazienti(0);
        this.medici = dao.getMedici(0);
        this.ricoveri = dao.getRicoveri(0);
        this.turni = dao.getTurniLavorativi(0);
        this.visite = dao.getVisite(0);

        // this.interventi = dao.getInterventi(0); // Decommenta quando implementerai gli Interventi nel DAO

        System.out.println("Memoria Controller allocata con le liste già interconnesse dal DAO!");
    }

    // =========================================================================
    // I vecchi metodi "trovaPerId" e i cicli "for" sono stati rimossi perché
    // adesso l'assemblaggio degli array interni avviene in automatico nel DAO!
    // =========================================================================

    public boolean isAmministratore() {
        return "Amministratore".equals(utenteLoggatoRuolo);
    }

    // --- GETTER PER L'INTERFACCIA GRAFICA ---
    public List<Reparto> getReparti() { return reparti; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Medico> getMedici() { return medici; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Letto> getLetti() { return letti; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
    public List<TurnoLavorativo> getTurni() { return turni; }
    public List<Visita> getVisite() { return visite; }
    public List<InterventoChirurgico> getInterventi() { return interventi; }
}