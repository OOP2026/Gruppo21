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

        this.reparti = dao.getTuttiIReparti();
        this.stanze = dao.getTutteLeStanze();
        this.letti = dao.getTuttiILetti();
        this.pazienti = dao.getTuttiIPazienti();
        this.medici = dao.getTuttiIMedici();
        this.ricoveri = dao.getTuttiIRicoveri();
        this.turni = dao.getTuttiITurniLavorativi();
        this.visite = dao.getTutteLeVisite();
        this.interventi = dao.getTuttiGliInterventiChirurgici();

        for (Stanza s : stanze) {
            Reparto r = trovaRepartoPerId(s.getIdReparto());
            if (r != null) {
                s.setReparto(r);
                r.aggiungiStanza(s);
            }
        }

        for (Letto l : letti) {
            Stanza s = trovaStanzaPerId(l.getIdStanza());
            if (s != null) {
                l.setStanza(s);
                s.aggiungiLetto(l);
            }
        }

        for (Medico m : medici) {
            Reparto r = trovaRepartoPerId(m.getIdReparto());
            if (r != null) {
                r.aggiungiMedico(m);
            }
        }

        for (Ricovero ric : ricoveri) {
            Paziente p = trovaPazientePerCodFiscale(ric.getCodFiscalePaziente());
            Letto l = trovaLettoPerId(ric.getIdLetto());

            if (p != null) {
                p.aggiungiRicovero(ric);
            }
            if (l != null) {
                l.aggiungiRicovero(ric);
            }
        }

        for (Visita v : visite) {
            Ricovero ric = trovaRicoveroPerId(v.getIdRicovero());
            Medico m = trovaMedicoPerId(v.getIdMedico());

            if (ric != null) {
                // v.setRicovero(ric); // Decommenta se hai aggiunto setRicovero() nel Model
            }
            if (m != null) {
                // v.setMedico(m); // Decommenta se hai aggiunto setMedico() nel Model
            }
        }

        for (TurnoLavorativo t : turni) {
            Medico m = trovaMedicoPerId(t.getIdMedico());
            if (m != null) {
                t.aggiungiMedico(m);
            }
        }

        for (InterventoChirurgico i : interventi) {
            Ricovero ric = trovaRicoveroPerId(i.getIdRicovero());
            Medico m = trovaMedicoPerId(i.getIdMedico());
        }

        System.out.println("Memoria allocata e interconnessa con successo (senza HashMap).");
    }

    private Reparto trovaRepartoPerId(int id) {
        for (Reparto r : reparti) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    private Stanza trovaStanzaPerId(int id) {
        for (Stanza s : stanze) {
            // Assicurati di avere getIdStanza() in Stanza
            // Sostituisci il metodo in base a come lo hai chiamato
            // if (s.getIdStanza() == id) return s;
        }
        return null; // Aggiusta la logica dopo aver inserito l'ID Stanza
    }

    private Letto trovaLettoPerId(int id) {
        for (Letto l : letti) {
            // Sostituisci con il getter corretto dell'ID numerico
            // if (l.getIdLetto() == id) return l;
        }
        return null; // Aggiusta la logica dopo aver inserito l'ID Letto
    }

    private Medico trovaMedicoPerId(int id) {
        for (Medico m : medici) {
            if (m.getIdMedico() == id) {
                return m;
            }
        }
        return null;
    }

    private Paziente trovaPazientePerCodFiscale(String cf) {
        for (Paziente p : pazienti) {
            if (p.getCOD_FISCALE().equals(cf)) {
                return p;
            }
        }
        return null;
    }

    private Ricovero trovaRicoveroPerId(int id) {
        for (Ricovero r : ricoveri) {
            if (r.getIdRicovero() == id) {
                return r;
            }
        }
        return null;
    }

    public boolean isAmministratore() {
        return "Amministratore".equals(utenteLoggatoRuolo);
    }

    public List<Reparto> getReparti() { return reparti; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Medico> getMedici() { return medici; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Letto> getLetti() { return letti; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
    public List<TurnoLavorativo> getTurni() { return turni; }
}