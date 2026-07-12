package controller;

import implementazioneDao.AmministratoreImplementazioneDAO;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    // Memoria Globale dell'App
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
        medici = new ArrayList<>(); ricoveri = new ArrayList<>();
        amministratori = new ArrayList<>(); pazienti = new ArrayList<>();
        reparti = new ArrayList<>(); stanze = new ArrayList<>();
        letti = new ArrayList<>(); turni = new ArrayList<>();
        visite = new ArrayList<>(); interventi = new ArrayList<>();
    }

    public boolean loginAmministratore(String email, String password) throws SQLException {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        if (!adminDao.verificaCredenziali(email, password)) return false;

        this.utenteLoggatoRuolo = "Amministratore";
        this.emailUtenteLoggato = email;

        svuotaMemoria();
        scaricaEOrchestraTabelle(adminDao);
        return true;
    }

    private void scaricaEOrchestraTabelle(AmministratoreImplementazioneDAO dao) throws SQLException {
        // 1. IL DAO SCARICA I DATI PIATTI (E CREA I GUSCI TEMPORANEI PER LE FK)
        this.reparti = dao.getReparti(0);
        this.pazienti = dao.getPazienti(0);
        this.medici = dao.getMedici(0);
        this.stanze = dao.getStanze(0);
        this.turni = dao.getTurniLavorativi(0);
        this.ricoveri = dao.getRicoveri(0);
        // this.letti = dao.getLetti(0);
        // this.visite = dao.getVisite(0);

        System.out.println("Dati piatti scaricati. Inizio l'orchestrazione dei collegamenti...");

        // 2. IL CONTROLLER ELIMINA I GUSCI E LI SOSTITUISCE CON LE REFERENZE REALI E UNICHE

        // Collega Stanze -> Reparti
        for (Stanza s : stanze) {
            if (s.getReparto() != null) {
                int idRepartoFK = s.getReparto().getId(); // Leggiamo l'ID dal guscio
                Reparto rReale = trovaRepartoPerId(idRepartoFK);
                if (rReale != null) {
                    s.setReparto(rReale); // Sostituisce il guscio con l'oggetto reale
                    rReale.aggiungiStanza(s); // Vincolo bidirezionale
                }
            }
        }

        // Collega Medici -> Reparti
        for (Medico m : medici) {
            // Nota: Se hai getReparto() in Medico, usalo per estrarre l'ID
            // int idRepartoFK = m.getReparto().getId();
            // Reparto rReale = trovaRepartoPerId(idRepartoFK);
            // if (rReale != null) { rReale.aggiungiMedico(m); }
        }

        // Collega Ricoveri -> Pazienti e Letti
        for (Ricovero r : ricoveri) {
            if (r.getPaziente() != null) {
                String cfPazienteFK = r.getPaziente().getCOD_FISCALE();
                Paziente pReale = trovaPazientePerCodFiscale(cfPazienteFK);
                if (pReale != null) {
                    r.setPaziente(pReale);
                    pReale.aggiungiRicovero(r); // Vincolo bidirezionale senza duplicati!
                }
            }
            // Fai lo stesso per il letto: trovaLettoPerId(...)
        }

        // Fai lo stesso con i Turni e i Medici
        // Fai lo stesso con le Visite (Ricovero e Medico)

        System.out.println("Orchestrazione completata con successo tramite ArrayList!");
    }

    // =====================================================================
    // METODI DI RICERCA (Garantiscono che lavoriamo sempre su UNICA istanza)
    // =====================================================================

    private Reparto trovaRepartoPerId(int id) {
        for (Reparto r : reparti) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    private Medico trovaMedicoPerId(int id) {
        for (Medico m : medici) {
            if (m.getIdMedico() == id) return m;
        }
        return null;
    }

    private Paziente trovaPazientePerCodFiscale(String cf) {
        for (Paziente p : pazienti) {
            if (p.getCOD_FISCALE().equals(cf)) return p;
        }
        return null;
    }

    private Ricovero trovaRicoveroPerId(int id) {
        for (Ricovero r : ricoveri) {
            if (r.getIdRicovero() == id) return r;
        }
        return null;
    }

    // Aggiungi qui trovaStanzaPerId e trovaLettoPerId...

    // =====================================================================
    // GETTER PER L'INTERFACCIA GRAFICA
    // =====================================================================

    public List<Reparto> getReparti() { return reparti; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Medico> getMedici() { return medici; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
}