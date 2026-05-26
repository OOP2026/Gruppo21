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

		amministratori.add(new Amministratore(admin, admin));
		reparti.add(new Reparto());
	}

	//Logica

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
		boolean controllo = false;

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
		if(!medici.contains(medico)) throw new RuntimeException("Medico " + medico.getNome() + medico.getCognome() + " non è presente in memoria.");
		if(!ricoveri.contains(ricovero)) throw new RuntimeException("Ricovero non è presente in memoria.");

		for(Medico dottore : ricovero.getMedici()) {
			if(dottore.equals(medico)) throw new RuntimeException("Il medico è gia stato aggiunto in questo ricovero");
		}

		if(!turnoCompresoInRicovero(medico, ricovero)) throw new RuntimeException("Il medico " + medico.getNome() + medico.getCognome() +
				" sta venendo aggiunto in un ricovero fuori dai suoi turni.");

		ricovero.aggiungiMedico(medico);
	}

	public void aggiungiMedicoAlTurno(Medico medico, TurnoLavorativo turno) throws RuntimeException {
		if(!medici.contains(medico)) throw new RuntimeException("Medico " + medico.getNome() + medico.getCognome() + " non è presente in memoria.");
		if(!turni.contains(turno)) throw new RuntimeException("Turno non è presente in memoria.");

		for(Medico dottore : turno.getMedici()) {
			if(dottore.equals(medico)) throw new RuntimeException("Il medico è già stato aggiunto in quel turno");
		}

		if(turnoCompresoInTurno(medico, turno)) throw new RuntimeException("Un turno del medico si sovrappone col turno che si vuole aggiungere");

		turno.aggiungiMedico(medico);
	}

	//Costruttori Semplici

	public void aggiungiPaziente(String nome, String cognome, String COD_FISCALE) {
		pazienti.add(new Paziente(nome, cognome, COD_FISCALE));
	}

	public void aggiungiMedico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) throws RuntimeException {
		if(!reparti.contains(reparto)) throw new RuntimeException("Il reparto non esiste in memoria.");

		medici.add(new Medico(nome, cognome, email, password, tipoMedico, reparto));
	}

	//Costruttori Elaborati

	public void aggiungiRicovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws RuntimeException {
		if(!pazienti.contains(paziente)) throw new RuntimeException("Il paziente " + paziente.getCOD_FISCALE() + " non esiste in memoria.");

		if(pazienteGiaOccupato(paziente, dataOraInizio)) throw new RuntimeException("Il paziente " + paziente.getCOD_FISCALE() +
				" è già occupato da " + dataOraInizio.toString() + " a " + dataOraFine.toString() + "\nImpossibile aggiungere ricovero.");

		if(lettoGiaOccupato(letto, dataOraInizio)) throw new RuntimeException("Il letto " + letto.getCodiceLetto() +
				" è già occupato da " + dataOraInizio.toString() + " a " + dataOraFine.toString() + "\nImpossibile aggiungere ricovero.");

		ricoveri.add(new Ricovero(paziente, letto, dataOraInizio, dataOraFine));
	}
}
