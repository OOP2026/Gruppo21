package controller;

import implementazioneDao.AmministratoreImplementazioneDAO;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    // Il Controller detiene lo STATO DELL'APP
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

        // Ordina al DAO di prepararsi e orchestra il download
        adminDao.istanziaMemoriaLocale(0);
        scaricaTabelleAmministratore(adminDao);

        return true;
    }

    private void scaricaTabelleAmministratore(AmministratoreImplementazioneDAO dao) throws SQLException {
        System.out.println("Controller: Inizio orchestrazione e popolamento memoria locale...");

        // Il Controller riceve i dati già incastrati dal DAO e se ne appropria
        this.reparti = dao.getReparti(0);
        this.pazienti = dao.getPazienti(0);
        this.medici = dao.getMedici(0);
        this.amministratori = dao.getAmministratori(0);
        this.turni = dao.getTurniLavorativi(0);
        this.stanze = dao.getStanze(0);
        this.letti = dao.getLetti(0);
        this.ricoveri = dao.getRicoveri(0);
        this.visite = dao.getVisite(0);

        System.out.println("Controller: Memoria allocata con successo. Sono il direttore d'orchestra!");
    }

    public boolean isAmministratore() {
        return "Amministratore".equals(utenteLoggatoRuolo);
    }

    // --- GETTER PER L'INTERFACCIA GRAFICA ---
    // La GUI chiederà sempre e solo al Controller
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