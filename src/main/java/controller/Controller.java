package controller;

import implementazione_dao.AmministratoreImplementazioneDAO;
import implementazione_dao.MedicoImplementazioneDAO;
import dao.DAO;
import model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private static final String RUOLO_AMMINISTRATORE = "Amministratore";
    private static final String RUOLO_MEDICO = "Medico";

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
        gestisce = new ArrayList<>();
        opera = new ArrayList<>();
    }

    public boolean loginAmministratore(String email, String password) throws SQLException {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        if (!adminDao.verificaCredenziali(email, password)) {
            return false;
        }

        this.utenteLoggatoRuolo = RUOLO_AMMINISTRATORE;
        svuotaMemoria();
        scaricaEOrchestraTabelle(adminDao, 0);
        return true;
    }

    public boolean loginMedico(String email, String password) throws SQLException {
        MedicoImplementazioneDAO medicoDao = new MedicoImplementazioneDAO();
        int idTrovato = medicoDao.recuperaIdMedico(email, password);

        if (idTrovato == -1) {
            return false;
        }

        this.utenteLoggatoRuolo = RUOLO_MEDICO;
        this.idUtenteLoggato = idTrovato;
        svuotaMemoria();
        scaricaEOrchestraTabelle(medicoDao, idTrovato);
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
        this.amministratori = dao.getAmministratori(idFiltro);

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

        for (Letto l : letti) {
            if (l.getStanza() != null) {
                Stanza sReale = trovaStanzaPerId(l.getStanza().getIdStanza());
                if (sReale != null) {
                    l.setStanza(sReale);
                    sReale.aggiungiLetto(l);
                }
            }
        }

        for (Ricovero r : ricoveri) {
            if (r.getPaziente() != null) {
                Paziente pReale = trovaPazientePerCodFiscale(r.getPaziente().getCodFiscale());
                if (pReale != null) {
                    r.setPaziente(pReale);
                    pReale.aggiungiRicovero(r);
                }
            }
            if (r.getLetto() != null) {
                Letto lReale = trovaLettoPerId(r.getLetto().getIdLetto());
                if (lReale != null) {
                    r.setLetto(lReale);
                    lReale.aggiungiRicovero(r);
                }
            }
        }

        for (Visita v : visite) {
            if (v.getMedico() != null && v.getRicovero() != null) {
                Medico mReale = trovaMedicoPerId(v.getMedico().getIdMedico());
                Ricovero rReale = trovaRicoveroPerId(v.getRicovero().getIdRicovero());

                if (mReale != null) v.setMedico(mReale);
                if (rReale != null) v.setRicovero(rReale);
            }
        }

        for (Gestisce link : gestisce) {
            Medico mReale = trovaMedicoPerId(link.getIdMedico());
            Ricovero rReale = trovaRicoveroPerId(link.getIdRicovero());
            if (mReale != null && rReale != null) {
                mReale.aggiungiRicovero(rReale);
                rReale.aggiungiMedico(mReale);
            }
        }

        for (Opera link : opera) {
            Medico mReale = trovaMedicoPerId(link.getIdMedico());
            InterventoChirurgico iReale = trovaInterventoPerId(link.getIdIntervento());
            if (mReale != null && iReale != null) {
                mReale.aggiungiIntervento(iReale);
                iReale.aggiungiMedico(mReale);
            }
        }
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
            if (p.getCodFiscale().equals(cf)) return p;
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
            if (l.getIdLetto() == id) return l;
        }
        return null;
    }

    private InterventoChirurgico trovaInterventoPerId(int id) {
        for (InterventoChirurgico i : interventi) {
            if (i.getIdIntervento() == id) return i;
        }
        return null;
    }

    public boolean isAmministratore() {
        return RUOLO_AMMINISTRATORE.equals(utenteLoggatoRuolo);
    }

    public String getUtenteLoggatoRuolo() { return utenteLoggatoRuolo; }
    public int getIdUtenteLoggato() { return idUtenteLoggato; }
    public List<Medico> getMedici() { return medici; }
    public List<Ricovero> getRicoveri() { return ricoveri; }
    public List<Amministratore> getAmministratori() { return amministratori; }
    public List<Paziente> getPazienti() { return pazienti; }
    public List<Reparto> getReparti() { return reparti; }
    public List<Stanza> getStanze() { return stanze; }
    public List<Letto> getLetti() { return letti; }
    public List<TurnoLavorativo> getTurni() { return turni; }
    public List<Gestisce> getGestisce() { return gestisce; }
    public List<Opera> getOpera() { return opera; }
    public List<Visita> getVisite() { return visite; }
    public List<InterventoChirurgico> getInterventi() { return interventi; }

    public void salvaPaziente(String codiceFiscale, String nome, String cognome, boolean isModifica) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        Paziente p = new Paziente(nome, cognome, codiceFiscale);

        if (isModifica) {
            dao.aggiornaPaziente(p);
        } else {
            dao.inserisciPaziente(p);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaPaziente(String codiceFiscale) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaPaziente(codiceFiscale);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaMedico(int id, String nome, String cognome, String email, String password, String tipo, Reparto reparto, boolean isModifica) throws Exception {
        Medico med = new Medico(nome, cognome, email, password, tipo, reparto);
        med.setIdMedico(id);

        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            dao.aggiornaMedico(med);
        } else {
            dao.inserisciMedico(med);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaMedico(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaMedico(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaReparto(int id, String nome, boolean isModifica) throws Exception {
        Reparto r = new Reparto(nome, id);
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();

        if (isModifica) {
            dao.aggiornaReparto(r);
        } else {
            dao.inserisciReparto(r);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaReparto(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaReparto(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaStanza(int id, Reparto r, boolean isModifica) throws Exception {
        Stanza s = new Stanza(r);
        s.setIdStanza(id);
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();

        if (isModifica) {
            dao.aggiornaStanza(s);
        } else {
            dao.inserisciStanza(s);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaStanza(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaStanza(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaLetto(int id, Stanza s, boolean isModifica) throws Exception {
        Letto l;
        if (isModifica) {
            l = new Letto(id, s);
        } else {
            l = new Letto(s);
        }

        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            dao.aggiornaLetto(l);
        } else {
            dao.inserisciLetto(l);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaLetto(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaLetto(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaTurno(int id, LocalDateTime inizio, LocalDateTime fine, Medico med, boolean isModifica) throws Exception {
        TurnoLavorativo t = new TurnoLavorativo(inizio, fine);
        t.setIdTurno(id);

        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            dao.aggiornaTurno(t, med.getIdMedico());
        } else {
            dao.inserisciTurno(t, med.getIdMedico());
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaTurno(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaTurno(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaRicovero(int id, LocalDateTime inizio, LocalDateTime fine, Paziente p, Letto l, boolean isModifica) throws Exception {
        Ricovero r = new Ricovero(id);
        r.setDataOraInizio(inizio);
        r.setDataOraFine(fine);

        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            dao.aggiornaRicovero(r, p.getCodFiscale(), l.getIdLetto());
        } else {
            dao.inserisciRicovero(r, p.getCodFiscale(), l.getIdLetto());
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaRicovero(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaRicovero(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaIntervento(int id, String nome, LocalDateTime inizio, LocalDateTime fine, Visita v, boolean isModifica) throws Exception {
        InterventoChirurgico i = new InterventoChirurgico(nome, inizio, fine, v);
        i.setIdIntervento(id);

        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            adminDao.aggiornaIntervento(i);
        } else {
            adminDao.inserisciIntervento(i);
        }
        scaricaEOrchestraTabelle(adminDao, 0);
    }

    public void eliminaIntervento(int id) throws Exception {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        adminDao.eliminaIntervento(id);
        scaricaEOrchestraTabelle(adminDao, 0);
    }

    public void salvaAmministratore(int id, String nome, String cognome, String email, String password, boolean isModifica) throws Exception {
        Amministratore a = new Amministratore(nome, cognome, email, password);
        a.setId(id);

        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        if (isModifica) {
            dao.aggiornaAmministratore(a);
        } else {
            dao.inserisciAmministratore(a);
        }
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void eliminaAmministratore(int id) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.eliminaAmministratore(id);
        scaricaEOrchestraTabelle(dao, 0);
    }

    // N:N - GESTISCE (Medico - Ricovero)
    public void collegaMedicoRicovero(int idMedico, int idRicovero) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.collegaMedicoRicovero(idMedico, idRicovero);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void scollegaMedicoRicovero(int idMedico, int idRicovero) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.scollegaMedicoRicovero(idMedico, idRicovero);
        scaricaEOrchestraTabelle(dao, 0);
    }

    // N:N - OPERA (Medico - Intervento)
    public void collegaMedicoIntervento(int idMedico, int idIntervento, String ruolo) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.collegaMedicoIntervento(idMedico, idIntervento, ruolo);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void scollegaMedicoIntervento(int idMedico, int idIntervento) throws Exception {
        AmministratoreImplementazioneDAO dao = new AmministratoreImplementazioneDAO();
        dao.scollegaMedicoIntervento(idMedico, idIntervento);
        scaricaEOrchestraTabelle(dao, 0);
    }

    public void salvaVisita(String nome, Ricovero r, Medico m) throws Exception {
        Visita v = new Visita(nome, r, m);
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        adminDao.inserisciVisita(v);
        scaricaEOrchestraTabelle(adminDao, 0);
    }

    public void eliminaVisita(int id) throws Exception {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        adminDao.eliminaVisita(id);
        scaricaEOrchestraTabelle(adminDao, 0);
    }
}