package controller;

import model.*;

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

	public Controller() {
		String admin = "admin";
		medici = new ArrayList<>();
		ricoveri = new ArrayList<>();
		amministratori = new ArrayList<>();
		pazienti = new ArrayList<>();
		turni = new ArrayList<>();
		reparti = new ArrayList<>();

		amministratori.add(new Amministratore(admin, admin));
		reparti.add(new Reparto());
	}

	// --- NUOVI GETTER PER LA GRAFICA ---
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

	// --- NUOVI METODI DI INSERIMENTO ---
	public void aggiungiAmministratore(String nome, String cognome, String email, String password) {
		amministratori.add(new Amministratore(nome, cognome, email, password));
	}

	public void aggiungiStanza(Reparto reparto) {
		new Stanza(reparto);
	}

	public void aggiungiLetto(String codice, Stanza stanza) {
		new Letto(codice, stanza);
	}

	public void aggiungiTurnoLavorativo(LocalDateTime inizio, LocalDateTime fine) {
		turni.add(new TurnoLavorativo(inizio, fine));
	}

	// --- METODI ORIGINALI ALGORITMICI ---
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

	public void aggiungiMedicoARicovero(Medico medico, Ricovero ricovero) throws RuntimeException {
		if(!medici.contains(medico)) throw new RuntimeException("Medico non è presente in memoria.");
		if(!ricoveri.contains(ricovero)) throw new RuntimeException("Ricovero non è presente in memoria.");
		for(Medico dottore : ricovero.getMedici()) {
			if(dottore.equals(medico)) throw new RuntimeException("Il medico è gia stato aggiunto in questo ricovero");
		}
		if(!turnoCompresoInRicovero(medico, ricovero)) throw new RuntimeException("Ricovero fuori dai turni.");
		ricovero.aggiungiMedico(medico);
	}

	public void aggiungiMedicoAlTurno(Medico medico, TurnoLavorativo turno) throws RuntimeException {
		if(!medici.contains(medico)) throw new RuntimeException("Medico non è presente in memoria.");
		if(!turni.contains(turno)) throw new RuntimeException("Turno non è presente in memoria.");
		for(Medico dottore : turno.getMedici()) {
			if(dottore.equals(medico)) throw new RuntimeException("Il medico è già stato aggiunto in quel turno");
		}
		if(turnoCompresoInTurno(medico, turno)) throw new RuntimeException("Sovrapposizione turno");
		turno.aggiungiMedico(medico);
	}

	// --- METODI ORIGINALI COSTRUTTORI ---
	public void aggiungiPaziente(String nome, String cognome, String COD_FISCALE) {
		pazienti.add(new Paziente(nome, cognome, COD_FISCALE));
	}

	public void aggiungiMedico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) throws RuntimeException {
		if(!reparti.contains(reparto)) throw new RuntimeException("Il reparto non esiste in memoria.");
		medici.add(new Medico(nome, cognome, email, password, tipoMedico, reparto));
	}

	public void aggiungiRicovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws RuntimeException {
		if(!pazienti.contains(paziente)) throw new RuntimeException("Il paziente non esiste in memoria.");
		if(pazienteGiaOccupato(paziente, dataOraInizio)) throw new RuntimeException("Il paziente è già occupato");
		if(lettoGiaOccupato(letto, dataOraInizio)) throw new RuntimeException("Il letto è già occupato");
		ricoveri.add(new Ricovero(paziente, letto, dataOraInizio, dataOraFine));
	}
}