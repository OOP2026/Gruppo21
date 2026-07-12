package controller;

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

    public Controller() { svuotaMemoria(); }

    private void svuotaMemoria() {
        medici = new ArrayList<>(); ricoveri = new ArrayList<>();
        amministratori = new ArrayList<>(); pazienti = new ArrayList<>();
        reparti = new ArrayList<>(); stanze = new ArrayList<>();
        letti = new ArrayList<>(); turni = new ArrayList<>();
        visite = new ArrayList<>(); interventi = new ArrayList<>();
        gestisce = new ArrayList<>(); opera = new ArrayList<>();
    }

    public boolean loginAmministratore(String email, String password) throws SQLException {
        AmministratoreImplementazioneDAO adminDao = new AmministratoreImplementazioneDAO();
        if (!adminDao.verificaCredenziali(email, password)) return false;
        this.utenteLoggatoRuolo = "Amministratore"; svuotaMemoria(); scaricaEOrchestraTabelle(adminDao, 0); return true;
    }

    public boolean loginMedico(String email, String password) throws SQLException {
        MedicoImplementazioneDAO medicoDao = new MedicoImplementazioneDAO();
        int idTrovato = medicoDao.recuperaIdMedico(email, password);
        if (idTrovato == -1) return false;
        this.utenteLoggatoRuolo = "Medico"; this.idUtenteLoggato = idTrovato; svuotaMemoria(); scaricaEOrchestraTabelle(medicoDao, idTrovato); return true;
    }

    private void scaricaEOrchestraTabelle(DAO dao, int idFiltro) throws SQLException {
        this.reparti = dao.getReparti(idFiltro); this.pazienti = dao.getPazienti(idFiltro); this.medici = dao.getMedici(idFiltro);
        this.stanze = dao.getStanze(idFiltro); this.turni = dao.getTurniLavorativi(idFiltro); this.ricoveri = dao.getRicoveri(idFiltro);
        this.letti = dao.getLetti(idFiltro); this.visite = dao.getVisite(idFiltro); this.interventi = dao.getInterventi(idFiltro);
        this.gestisce = dao.getCollegamentiGestisce(); this.opera = dao.getCollegamentiOpera(); this.amministratori = dao.getAmministratori(idFiltro);

        for (Stanza s : stanze) { if (s.getReparto() != null) { Reparto rReale = trovaRepartoPerId(s.getReparto().getId()); if (rReale != null) { s.setReparto(rReale); rReale.aggiungiStanza(s); } } }
        for (Medico m : medici) { if (m.getReparto() != null) { Reparto rReale = trovaRepartoPerId(m.getReparto().getId()); if (rReale != null) { m.setReparto(rReale); rReale.aggiungiMedico(m); } } }
        for (TurnoLavorativo t : turni) { if (t.getMedico() != null) { Medico mReale = trovaMedicoPerId(t.getMedico().getIdMedico()); if (mReale != null) { t.setMedico(mReale); mReale.aggiungiTurnoLavorativo(t); } } }
        for (Letto l : letti) { if (l.getStanza() != null) { Stanza sReale = trovaStanzaPerId(l.getStanza().getIdStanza()); if (sReale != null) { l.setStanza(sReale); sReale.aggiungiLetto(l); } } }
        for (Ricovero r : ricoveri) {
            if (r.getPaziente() != null) { Paziente pReale = trovaPazientePerCodFiscale(r.getPaziente().getCOD_FISCALE()); if (pReale != null) { r.setPaziente(pReale); pReale.aggiungiRicovero(r); } }
            if (r.getLetto() != null) { Letto lReale = trovaLettoPerId(r.getLetto().getId_letto()); if (lReale != null) { r.setLetto(lReale); lReale.aggiungiRicovero(r); } }
        }
        for (Visita v : visite) {

            if (v.getMedico() != null && v.getRicovero() != null) {
                Medico mReale = trovaMedicoPerId(v.getMedico().getIdMedico());
                Ricovero rReale = trovaRicoveroPerId(v.getRicovero().getIdRicovero());

            }
        }
        for (Gestisce link : gestisce) {
            Medico mReale = trovaMedicoPerId(link.getId_medico()); Ricovero rReale = trovaRicoveroPerId(link.getId_ricovero());
            if (mReale != null && rReale != null) { mReale.aggiungiRicovero(rReale); rReale.aggiungiMedico(mReale); }
        }
        for (Opera link : opera) {
            Medico mReale = trovaMedicoPerId(link.getId_medico()); InterventoChirurgico iReale = trovaInterventoPerId(link.getId_intervento());
            if (mReale != null && iReale != null) { mReale.aggiungiIntervento(iReale); iReale.aggiungiMedico(mReale); }
        }
    }

    private Reparto trovaRepartoPerId(int id) { for (Reparto r : reparti) if (r.getId() == id) return r; return null; }
    private Medico trovaMedicoPerId(int id) { for (Medico m : medici) if (m.getIdMedico() == id) return m; return null; }
    private Paziente trovaPazientePerCodFiscale(String cf) { for (Paziente p : pazienti) if (p.getCOD_FISCALE().equals(cf)) return p; return null; }
    private Ricovero trovaRicoveroPerId(int id) { for (Ricovero r : ricoveri) if (r.getIdRicovero() == id) return r; return null; }
    private Stanza trovaStanzaPerId(int id) { for (Stanza s : stanze) if (s.getIdStanza() == id) return s; return null; }
    private Letto trovaLettoPerId(int id) { for (Letto l : letti) if (l.getId_letto() == id) return l; return null; }
    private InterventoChirurgico trovaInterventoPerId(int id) { for (InterventoChirurgico i : interventi) if (i.getIdIntervento() == id) return i; return null; }

    public boolean isAmministratore() { return "Amministratore".equals(utenteLoggatoRuolo); }
    public String getUtenteLoggatoRuolo() { return utenteLoggatoRuolo; }
    public int getIdUtenteLoggato() { return idUtenteLoggato; }

    public List<Medico> getMedici() { return medici; } public List<Ricovero> getRicoveri() { return ricoveri; }
    public List<Amministratore> getAmministratori() { return amministratori; } public List<Paziente> getPazienti() { return pazienti; }
    public List<Reparto> getReparti() { return reparti; } public List<Stanza> getStanze() { return stanze; }
    public List<Letto> getLetti() { return letti; } public List<TurnoLavorativo> getTurni() { return turni; }
    public List<Gestisce> getGestisce() { return gestisce; } public List<Opera> getOpera() { return opera; }
    public List<Visita> getVisite() { return visite; } public List<InterventoChirurgico> getInterventi() { return interventi; }


    // Pazienti
    public void salvaPaziente(String cf, String n, String c, boolean m) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaPaziente(new Paziente(n,c,cf)); else d.inserisciPaziente(new Paziente(n,c,cf)); scaricaEOrchestraTabelle(d,0); }
    public void eliminaPaziente(String cf) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaPaziente(cf); scaricaEOrchestraTabelle(d,0); }

    // Medici
    public void salvaMedico(int id, String n, String c, String e, String p, String t, Reparto r, boolean m) throws Exception { Medico med = new Medico(n,c,e,p,t,r); med.setIdMedico(id); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaMedico(med); else d.inserisciMedico(med); scaricaEOrchestraTabelle(d,0); }
    public void eliminaMedico(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaMedico(id); scaricaEOrchestraTabelle(d,0); }

    // Reparti
    public void salvaReparto(int id, String n, boolean m) throws Exception { Reparto r = new Reparto(n,id); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaReparto(r); else d.inserisciReparto(r); scaricaEOrchestraTabelle(d,0); }
    public void eliminaReparto(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaReparto(id); scaricaEOrchestraTabelle(d,0); }

    // Stanze
    public void salvaStanza(int id, Reparto r, boolean m) throws Exception { Stanza s = new Stanza(r); s.setIdStanza(id); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaStanza(s); else d.inserisciStanza(s); scaricaEOrchestraTabelle(d,0); }
    public void eliminaStanza(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaStanza(id); scaricaEOrchestraTabelle(d,0); }

    // Letti
    public void salvaLetto(int id, Stanza s, boolean m) throws Exception { Letto l = new Letto(s); if(m) l = new Letto(id, s); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaLetto(l); else d.inserisciLetto(l); scaricaEOrchestraTabelle(d,0); }
    public void eliminaLetto(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaLetto(id); scaricaEOrchestraTabelle(d,0); }

    // Turni
    public void salvaTurno(int id, LocalDateTime i, LocalDateTime f, Medico med, boolean m) throws Exception { TurnoLavorativo t = new TurnoLavorativo(i,f); t.setIdTurno(id); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaTurno(t, med.getIdMedico()); else d.inserisciTurno(t, med.getIdMedico()); scaricaEOrchestraTabelle(d,0); }
    public void eliminaTurno(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaTurno(id); scaricaEOrchestraTabelle(d,0); }

    // Ricoveri
    public void salvaRicovero(int id, LocalDateTime i, LocalDateTime f, Paziente p, Letto l, boolean m) throws Exception { Ricovero r = new Ricovero(id); r.setDataOraInizio(i); r.setDataOraFine(f); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaRicovero(r, p.getCOD_FISCALE(), l.getId_letto()); else d.inserisciRicovero(r, p.getCOD_FISCALE(), l.getId_letto()); scaricaEOrchestraTabelle(d,0); }
    public void eliminaRicovero(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaRicovero(id); scaricaEOrchestraTabelle(d,0); }

    // Interventi
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
    // Amministratori
    public void salvaAmministratore(int id, String n, String c, String e, String p, boolean m) throws Exception { Amministratore a = new Amministratore(n,c,e,p); a.setId(id); AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); if(m) d.aggiornaAmministratore(a); else d.inserisciAmministratore(a); scaricaEOrchestraTabelle(d,0); }
    public void eliminaAmministratore(int id) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.eliminaAmministratore(id); scaricaEOrchestraTabelle(d,0); }

    // N:N - GESTISCE (Medico - Ricovero)
    public void collegaMedicoRicovero(int idM, int idR) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.collegaMedicoRicovero(idM, idR); scaricaEOrchestraTabelle(d,0); }
    public void scollegaMedicoRicovero(int idM, int idR) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.scollegaMedicoRicovero(idM, idR); scaricaEOrchestraTabelle(d,0); }

    // N:N - OPERA (Medico - Intervento)
    public void collegaMedicoIntervento(int idM, int idI, String ruolo) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.collegaMedicoIntervento(idM, idI, ruolo); scaricaEOrchestraTabelle(d,0); }
    public void scollegaMedicoIntervento(int idM, int idI) throws Exception { AmministratoreImplementazioneDAO d = new AmministratoreImplementazioneDAO(); d.scollegaMedicoIntervento(idM, idI); scaricaEOrchestraTabelle(d,0); }

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