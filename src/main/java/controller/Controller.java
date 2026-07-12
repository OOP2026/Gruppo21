package controller;

import exceptions.BadArgsException;
import implementazioneDao.AmministratoreImplementazioneDAO;
import implementazioneDao.MedicoImplementazioneDAO;
import dao.DAO;
import model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
        this.visite = dao.getVisite(idFiltro);
        this.interventi = dao.getInterventi(idFiltro);
        this.gestisce = dao.getCollegamentiGestisce();
        this.opera = dao.getCollegamentiOpera();

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

        for (Gestisce link : gestisce) {
            Medico mReale = trovaMedicoPerId(link.getId_medico());
            Ricovero rReale = trovaRicoveroPerId(link.getId_ricovero());
            if (mReale != null && rReale != null) {
                mReale.aggiungiRicovero(rReale);
                rReale.aggiungiMedico(mReale);
            }
        }

        for (Opera link : opera) {
            Medico mReale = trovaMedicoPerId(link.getId_medico());
            InterventoChirurgico iReale = trovaInterventoPerId(link.getId_intervento());
            String ruolo = link.getRuolo();
            if (mReale != null && iReale != null) {
                mReale.aggiungiIntervento(iReale);
                iReale.aggiungiMedico(mReale); // Opzionale: passare anche il parametro ruolo se supportato
            }
        }

        System.out.println("Orchestrazione completata con successo mediante strutture gerarchiche.");
    }

    //Cicli For filtrati per ID

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

    //Getter e setter

    public List<Medico> getMedici() {
        return medici;
    }

    public void setMedici(List<Medico> medici) {
        this.medici = medici;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    public void setRicoveri(List<Ricovero> ricoveri) {
        this.ricoveri = ricoveri;
    }

    public List<Amministratore> getAmministratori() {
        return amministratori;
    }

    public void setAmministratori(List<Amministratore> amministratori) {
        this.amministratori = amministratori;
    }

    public List<Paziente> getPazienti() {
        return pazienti;
    }

    public void setPazienti(List<Paziente> pazienti) {
        this.pazienti = pazienti;
    }

    public List<Reparto> getReparti() {
        return reparti;
    }

    public void setReparti(List<Reparto> reparti) {
        this.reparti = reparti;
    }

    public List<Stanza> getStanze() {
        return stanze;
    }

    public void setStanze(List<Stanza> stanze) {
        this.stanze = stanze;
    }

    public List<Letto> getLetti() {
        return letti;
    }

    public void setLetti(List<Letto> letti) {
        this.letti = letti;
    }

    public List<TurnoLavorativo> getTurni() {
        return turni;
    }

    public void setTurni(List<TurnoLavorativo> turni) {
        this.turni = turni;
    }

    public List<Visita> getVisite() {
        return visite;
    }

    public void setVisite(List<Visita> visite) {
        this.visite = visite;
    }

    public List<InterventoChirurgico> getInterventi() {
        return interventi;
    }

    public void setInterventi(List<InterventoChirurgico> interventi) {
        this.interventi = interventi;
    }

    public List<Gestisce> getGestisce() {
        return gestisce;
    }

    public void setGestisce(List<Gestisce> gestisce) {
        this.gestisce = gestisce;
    }

    public List<Opera> getOpera() {
        return opera;
    }

    public void setOpera(List<Opera> opera) {
        this.opera = opera;
    }

    public String getUtenteLoggatoRuolo() {
        return utenteLoggatoRuolo;
    }

    public void setUtenteLoggatoRuolo(String utenteLoggatoRuolo) {
        this.utenteLoggatoRuolo = utenteLoggatoRuolo;
    }

    public int getIdUtenteLoggato() {
        return idUtenteLoggato;
    }

    public void setIdUtenteLoggato(int idUtenteLoggato) {
        this.idUtenteLoggato = idUtenteLoggato;
    }

    //Adder
    public void aggiungiRicovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) {
        try {
            Ricovero r = new Ricovero(paziente, letto, dataOraInizio, dataOraFine);
        } catch (BadArgsException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggiungiPaziente(String nome, String cognome, String COD_Fiscale) {
        try {
            Paziente p = new Paziente(nome, cognome, COD_Fiscale);
        } catch (BadArgsException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggiungiAmministratoreAnonimo(String email, String password) {
        try {
            Amministratore a = new Amministratore(email, password);
        } catch (BadArgsException e) {
            throw new RuntimeException(e);
        }
    }

    public void aggiungiLetto(Stanza selezionata) {
        try {
            Letto l = new Letto(selezionata);
        } catch (BadArgsException e) {
            throw new RuntimeException(e);
        }
    }
}