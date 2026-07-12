package controller;

import implementazioneDao.AmministratoreImplementazioneDAO;
import implementazioneDao.MedicoImplementazioneDAO;
import dao.DAO;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
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
    private List<Gestisce> gestisce;
    private List<Opera> opera;

    private String utenteLoggatoRuolo = null;
    private int idUtenteLoggato = -1;

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
        svuotaMemoria();
        scaricaEOrchestraTabelle(adminDao, 0);
        return true;
    }

    public boolean loginMedico(String email, String password, int idMedico) throws SQLException {
        MedicoImplementazioneDAO medicoDao = new MedicoImplementazioneDAO();
        if (!medicoDao.verificaCredenziali(email, password)) return false;

        this.utenteLoggatoRuolo = "Medico";
        this.idUtenteLoggato = idMedico;
        svuotaMemoria();
        scaricaEOrchestraTabelle(medicoDao, idMedico);
        return true;
    }

    private void scaricaEOrchestraTabelle(DAO dao, int idFiltro) throws SQLException {
        this.reparti = dao.getReparti(idFiltro);
        this.pazienti = dao.getPazienti(idFiltro);
        this.medici = dao.getMedici(idFiltro);
        this.stanze = dao.getStanze(idFiltro);
        this.turni = dao.getTurniLavorativi(idFiltro);
        this.ricoveri = dao.getRicoveri(idFiltro);
        this.letti = dao.getLetti(idFiltro);
        this.visite = dao.getVisites(idFiltro);
        this.interventi = dao.getInterventi(idFiltro);
        this.gestisce = dao.getCollegamentiGestisce();

        for (Stanza s : stanze) {
            if (s.getReparto() != null) {
                Reparto rReale = trovaRepartoPerId(s.getReparto().getId());
                if (rReale != null) {
                    s.setReparto(rReale);
                    rReale.aggiungiStanza(s);
                }
            }
        }

        for (Medico m : medici) {
            if (m.getReparto() != null) {
                Reparto rReale = trovaRepartoPerId(m.getReparto().getId());
                if (rReale != null) {
                    m.setReparto(rReale);
                    rReale.aggiungiMedico(m);
                }
            }
        }

        for (TurnoLavorativo t : turni) {
            if (t.getMedico() != null) {
                Medico mReale = trovaMedicoPerId(t.getMedico().getIdMedico());
                if (mReale != null) {
                    t.setMedico(mReale);
                    mReale.aggiungiTurnoLavorativo(t);
                }
            }
        }

        for (Ricovero r : ricoveri) {
            if (r.getPaziente() != null) {
                Paziente pReale = trovaPazientePerCodFiscale(r.getPaziente().getCOD_FISCALE());
                if (pReale != null) {
                    r.setPaziente(pReale);
                    pReale.aggiungiRicovero(r);
                }
            }
        }

        for (int[] link : linksGestisce) {
            Medico mReale = trovaMedicoPerId(link[0]);
            Ricovero rReale = trovaRicoveroPerId(link[1]);
            if (mReale != null && rReale != null) {
                mReale.aggiungiRicovero(rReale);
                rReale.aggiungiMedico(mReale);
            }
        }

        List<Object[]> linksOpera = dao.getCollegamentiOpera();
        for (Object[] link : linksOpera) {
            Medico mReale = trovaMedicoPerId((Integer) link[0]);
            InterventoChirurgico iReale = trovaInterventoPerId((Integer) link[1]);
            String ruolo = (String) link[2];
            if (mReale != null && iReale != null) {
                mReale.aggiungiIntervento(iReale);
                iReale.aggiungiMedico(mReale); // Opzionale: passare anche il parametro ruolo se supportato
            }
        }

        System.out.println("Orchestrazione completata con successo mediante strutture gerarchiche.");
    }

    private Reparto trovaRepartoPerId(int id) {
        for (Reparto r : reparti) { if (r.getId() == id) return r; }
        return null;
    }

    private Medico trovaMedicoPerId(int id) {
        for (Medico m : medici) { if (m.getIdMedico() == id) return m; }
        return null;
    }

    private Paziente trovaPazientePerCodFiscale(String cf) {
        for (Paziente p : pazienti) { if (p.getCOD_FISCALE().equals(cf)) return p; }
        return null;
    }

    private Ricovero trovaRicoveroPerId(int id) {
        for (Ricovero r : ricoveri) { if (r.getIdRicovero() == id) return r; }
        return null;
    }

    private InterventoChirurgico trovaInterventoPerId(int id) {
        for (InterventoChirurgico i : interventi) { if (i.getIdIntervento() == id) return i; }
        return null;
    }

    public boolean isAmministratore() { return "Amministratore".equals(utenteLoggatoRuolo); }

    // --- INTERFACCIA PER LO STRATO DELLA VISTA (GETTER) ---
    public List<Reparto> getReparti() { return reparti; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Medico> getMedici() { return medici; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
    public List<TurnoLavorativo> getTurni() { return turni; }
    public List<Visita> getVisite() { return visite; }
    public List<InterventoChirurgico> getInterventi() { return interventi; }
}