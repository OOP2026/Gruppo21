package controller;

import dao.DAO;
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

        scaricaEOrchestraTabelle(adminDao);
        return true;
    }

    private void scaricaEOrchestraTabelle(DAO dao) throws SQLException {
        this.reparti = dao.getReparti(0);
        this.pazienti = dao.getPazienti(0);
        this.medici = dao.getMedici(0);
        this.stanze = dao.getStanze(0);
        this.turni = dao.getTurniLavorativi(0);
        this.ricoveri = dao.getRicoveri(0);
        this.letti = dao.getLetti(0);
        this.visite = dao.getVisite(0);

        for (Stanza s : stanze) {
            if (s.getReparto() != null) {
                int idRepartoFK = s.getReparto().getId(); // Leggiamo l'ID dal guscio
                Reparto rReale = trovaRepartoPerId(idRepartoFK);
                if (rReale != null) {
                    s.setReparto(rReale);
                    rReale.aggiungiStanza(s);
                }
            }
        }

        for (Medico m : medici) {
            int idRepartoFK = m.getReparto().getId();
            Reparto rReale = trovaRepartoPerId(idRepartoFK);
            if (rReale != null) { rReale.aggiungiMedico(m); }
        }

        for (Ricovero r : ricoveri) {
            if (r.getPaziente() != null) {
                String cfPazienteFK = r.getPaziente().getCOD_FISCALE();
                Paziente pReale = trovaPazientePerCodFiscale(cfPazienteFK);
                if (pReale != null) {
                    r.setPaziente(pReale);
                    pReale.aggiungiRicovero(r);
                }
            }
            if (r.getLetto() != null) {
                int idLettoFK = r.getLetto().getId_letto();
                Letto lReale = trovaLettoPerId(idLettoFK);
                if (lReale != null) {
                    r.setLetto(lReale);
                    lReale.aggiungiRicovero(r);
                }
            }
        }

        for (TurnoLavorativo t : turni) {
            if (t.getMedico() != null) {
                int idMedicoFK = t.getMedico().getIdMedico();
                Medico mReale = trovaMedicoPerId(idMedicoFK);
                if (mReale != null) {
                    t.setMedico(mReale);
                    mReale.aggiungiTurnoLavorativo(t);
                }
            }
        }

        // Fai lo stesso con le Visite (Ricovero e Medico)
    }

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

    private Stanza trovaStanzaPerId(int id) {
        for (Stanza s : stanze) {
            if (s.getIdStanza() == id) return s;
        }
        return null;
    }

    private Letto trovaLettoPerId(int id) {
        for (Letto l : letti) {
            if (l.getId_letto() == id) return l;
        }
        return null;
    }

    public List<Reparto> getReparti() { return reparti; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Medico> getMedici() { return medici; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
}