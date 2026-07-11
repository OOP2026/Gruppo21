package controller;

import dao.DAO;
import exceptions.BadArgsException;
import implementazioneDao.AmministratoreImplementazioneDAO;
import implementazioneDao.MedicoImplementazioneDAO;
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
	private List<TurnoLavorativo> turni;
	private DAO Dao;

	// Gestione sessione e permessi
	private String utenteLoggatoRuolo = null;
	private String emailUtenteLoggato = null;

	public Controller() throws BadArgsException {
		medici = new ArrayList<>();
		ricoveri = new ArrayList<>();
		amministratori = new ArrayList<>();
		pazienti = new ArrayList<>();
		turni = new ArrayList<>();
		reparti = new ArrayList<>();
	}

	// --- LOGICA LOGIN E PERMESSI ---

	public boolean loginAmministratore(String email, String password) throws SQLException {
		Dao = new AmministratoreImplementazioneDAO();
		if(!Dao.verificaCredenziali(email, password)) return false;

		this.utenteLoggatoRuolo = "Amministratore";
		this.emailUtenteLoggato = email;
		return true;
	}

	public boolean loginMedico(String email, String password) throws SQLException {
		Dao = new MedicoImplementazioneDAO();
		if(!Dao.verificaCredenziali(email, password)) return false;

		this.utenteLoggatoRuolo = "Medico";
		this.emailUtenteLoggato = email;
		return true;
	}

	public boolean isAmministratore() {
		return "Amministratore".equals(utenteLoggatoRuolo);
	}

	// --- METODI DI LOGICA ESISTENTI ---

	public List<Reparto> getReparti() { return reparti; }
	public List<Paziente> getPazienti() { return pazienti; }

	public List<Stanza> getTutteStanze() {
		List<Stanza> tutte = new ArrayList<>();
		for(Reparto r : reparti) tutte.addAll(r.getStanze());
		return tutte;
	}

	public List<Letto> getTuttiLetti() {
		List<Letto> tutti = new ArrayList<>();
		for(Stanza s : getTutteStanze()) tutti.addAll(s.getLetti());
		return tutti;
	}

	public void aggiungiAmministratore(String nome, String cognome, String email, String password) throws BadArgsException {
		amministratori.add(new Amministratore(nome, cognome, email, password));
	}

	public void aggiungiAmministratoreAnonimo(String email, String password) throws BadArgsException {
		amministratori.add(new Amministratore(email, password));
	}

	public void aggiungiStanza(Reparto reparto) throws BadArgsException {
		new Stanza(reparto);
	}

	public void aggiungiLetto(String codice, Stanza stanza) throws BadArgsException {
		new Letto(codice, stanza);
	}

	public void aggiungiTurnoLavorativo(LocalDateTime inizio, LocalDateTime fine) throws BadArgsException {
		turni.add(new TurnoLavorativo(inizio, fine));
	}

	private boolean lettoGiaOccupato(Letto letto, LocalDateTime dataOraInizio) {
		for(Ricovero ricoveroInLetto: letto.getRicoveri())
			if(ricoveroInLetto.getDataOraFine().isAfter(dataOraInizio)) return true;
		return false;
	}

	private boolean pazienteGiaOccupato(Paziente paziente, LocalDateTime dataOraInizio) {
		for(Ricovero ricoveroInPaziente : paziente.getRicoveri())
			if(ricoveroInPaziente.getDataOraFine().isAfter(dataOraInizio)) return true;
		return false;
	}

	private boolean turnoCompresoInRicovero(Medico medico, Ricovero ricovero) {
		for(TurnoLavorativo turno : medico.getTurniLavorativi())
			if(!turno.getDataOraFine().isBefore(ricovero.getDataOraInizio())) return true;
		return false;
	}

	private boolean turnoCompresoInTurno(Medico medico, TurnoLavorativo turno) {
		for(TurnoLavorativo turniMedico : medico.getTurniLavorativi())
			if(turniMedico.getDataOraFine().isAfter(turno.getDataOraInizio())) return true;
		return false;
	}

	public void aggiungiMedicoARicovero(Medico medico, Ricovero ricovero) throws Exception {
		if(!medici.contains(medico)) throw new Exception("Medico non presente.");
		if(!ricoveri.contains(ricovero)) throw new Exception("Ricovero non presente.");
		for(Medico dottore : ricovero.getMedici()) {
			if(dottore.equals(medico)) throw new Exception("Medico già aggiunto.");
		}
		if(!turnoCompresoInRicovero(medico, ricovero)) throw new Exception("Fuori turno.");
		ricovero.aggiungiMedico(medico);
	}

	public void aggiungiMedicoAlTurno(Medico medico, TurnoLavorativo turno) throws Exception {
		if(!medici.contains(medico)) throw new Exception("Medico non presente.");
		if(!turni.contains(turno)) throw new Exception("Turno non presente.");
		for(Medico dottore : turno.getMedici()) {
			if(dottore.equals(medico)) throw new Exception("Medico già aggiunto.");
		}
		if(turnoCompresoInTurno(medico, turno)) throw new Exception("Sovrapposizione turno.");
		turno.aggiungiMedico(medico);
	}

	public void aggiungiPaziente(String nome, String cognome, String COD_FISCALE) throws BadArgsException {
		pazienti.add(new Paziente(nome, cognome, COD_FISCALE));
	}

	public void aggiungiMedico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) throws Exception {
		if(!reparti.contains(reparto)) throw new Exception("Reparto non esistente.");
		medici.add(new Medico(nome, cognome, email, password, tipoMedico, reparto));
	}

	public void aggiungiRicovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws Exception {
		if(!pazienti.contains(paziente)) throw new Exception("Paziente inesistente.");
		if(pazienteGiaOccupato(paziente, dataOraInizio)) throw new Exception("Paziente occupato.");
		if(lettoGiaOccupato(letto, dataOraInizio)) throw new Exception("Letto occupato.");
		ricoveri.add(new Ricovero(paziente, letto, dataOraInizio, dataOraFine));
	}
}