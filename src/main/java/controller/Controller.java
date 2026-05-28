package controller;

import jdk.vm.ci.meta.Local;
import model.*;

import java.sql.Array;
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

	public Controller() {
		String admin = "admin";
		medici = new ArrayList<>();
		ricoveri = new ArrayList<>();
		amministratori = new ArrayList<>();
		pazienti = new ArrayList<>();
		turni = new ArrayList<>();
		reparti = new ArrayList<>();
		stanze = new ArrayList<>();
		letti = new ArrayList<>();

		amministratori.add(new Amministratore(admin, admin));
		reparti.add(new Reparto());
	}

	//Logica algoritmica privata

	private boolean lettoGiaOccupato(Letto letto, LocalDateTime dataOraInizio) {
		for (Ricovero ricoveroInLetto : letto.getRicoveri())
			if (ricoveroInLetto.getDataOraFine().isAfter(dataOraInizio)) return true;
		return false;
	}

	private boolean pazienteGiaOccupato(Paziente paziente, LocalDateTime dataOraInizio) {
		for (Ricovero ricoveroInPaziente : paziente.getRicoveri())
			if (ricoveroInPaziente.getDataOraFine().isAfter(dataOraInizio)) return true;
		return false;
	}

	private boolean turnoCompresoInRicovero(Medico medico, Ricovero ricovero) {
		for (TurnoLavorativo turno : medico.getTurniLavorativi())
			if (!turno.getDataOraFine().isBefore(ricovero.getDataOraInizio())) return true;
		return false;
	}

	private boolean turnoCompresoInTurno(Medico medico, TurnoLavorativo turno) {
		for (TurnoLavorativo turniMedico : medico.getTurniLavorativi())
			if (turniMedico.getDataOraFine().isAfter(turno.getDataOraInizio())) return true;
		return false;
	}

	private boolean emailAmministratoreEsistente(String email) {
		for (Amministratore amministratore : amministratori)
			if (amministratore.getEmail().equals(email)) return true;
		return false;
	}

	private boolean codiceFiscalePazienteEsistente(String COD_FISCALE) {
		for (Paziente paziente : pazienti)
			if (paziente.getCOD_FISCALE().equals(COD_FISCALE)) return true;
		return false;
	}

	//Logica algoritmica pubblica


	public void collegaMedicoARicovero(Medico medico, Ricovero ricovero) throws IllegalArgumentException, RuntimeException {
		if (!medici.contains(medico))
			throw new IllegalArgumentException("Medico " + medico.getNome() + medico.getCognome() + " non è presente in memoria.");
		if (!ricoveri.contains(ricovero)) throw new IllegalArgumentException("Ricovero non è presente in memoria.");

		for (Medico dottore : ricovero.getMedici()) {
			if (dottore.equals(medico)) throw new RuntimeException("Il medico è gia stato aggiunto in questo ricovero");
		}

		if (!turnoCompresoInRicovero(medico, ricovero))
			throw new RuntimeException("Il medico " + medico.getNome() + medico.getCognome() +
					" sta venendo aggiunto in un ricovero fuori dai suoi turni.");

		ricovero.aggiungiMedico(medico);
	}

	public void collegaMedicoAlTurno(Medico medico, TurnoLavorativo turno) throws IllegalArgumentException, RuntimeException {
		if (!medici.contains(medico))
			throw new IllegalArgumentException("Medico " + medico.getNome() + medico.getCognome() + " non è presente in memoria.");
		if (!turni.contains(turno)) throw new IllegalArgumentException("Turno non è presente in memoria.");

		for (Medico dottore : turno.getMedici()) {
			if (dottore.equals(medico)) throw new RuntimeException("Il medico è già stato aggiunto in quel turno");
		}

		if (turnoCompresoInTurno(medico, turno))
			throw new RuntimeException("Un turno del medico si sovrappone col turno che si vuole aggiungere");

		turno.aggiungiMedico(medico);
	}

	//Addizionatori Semplici

	public void aggiungiPaziente(String nome, String cognome, String COD_FISCALE) throws RuntimeException {
		if (codiceFiscalePazienteEsistente(COD_FISCALE))
			throw new RuntimeException("Il codice fiscale " + COD_FISCALE + " è già stato usato per un altro paziente.");
		pazienti.add(new Paziente(nome, cognome, COD_FISCALE));
	}

	public void aggiungiMedico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) throws IllegalArgumentException {
		if (!reparti.contains(reparto))
			throw new IllegalArgumentException("Il reparto non esiste in memoria.");
		medici.add(new Medico(nome, cognome, email, password, tipoMedico, reparto));
	}

	public void aggiungiTurno(LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws RuntimeException {
		if (dataOraInizio.isAfter(dataOraFine))
			throw new RuntimeException("Il turno non può finire prima di iniziare.\nData fine: " + dataOraFine.toString() + ". Data Inizio: " + dataOraInizio.toString());
		turni.add(new TurnoLavorativo(dataOraInizio, dataOraFine));
	}

	public void aggiungiAmministratoreAnonimo(String email, String password) throws RuntimeException {
		if (emailAmministratoreEsistente(email))
			throw new RuntimeException("Email " + email + " già usata per un amministratore.");
		amministratori.add(new Amministratore(email, password));
	}

	public void aggiungiAmministratore(String nome, String cognome, String email, String password) throws RuntimeException {
		if (emailAmministratoreEsistente(email))
			throw new RuntimeException("Email " + email + " già usata per un amministratore.");
		amministratori.add(new Amministratore(nome, cognome, email, password));
	}

	public void aggiungiReparto() {reparti.add(new Reparto());}

	public void aggiungiStanza(Reparto reparto) throws IllegalArgumentException {
		if(!reparti.contains(reparto)) throw new IllegalArgumentException("Il reparto non esiste in memoria.");
		stanze.add(new Stanza(reparto));
	}

	//Addizionatori Elaborati

	public void aggiungiLetto(String codiceLetto, Stanza stanza) throws IllegalArgumentException, RuntimeException {
		if(!stanze.contains(stanza)) throw new IllegalArgumentException("La stanza non è in memoria.");

		for (Letto letto : letti) {
			if(letto.getCodiceLetto().equals(codiceLetto))
				throw new RuntimeException("Il letto " + letto.getCodiceLetto() + " ha lo stesso codice letto del letto che si sta creando.");
		}

		letti.add(new Letto(codiceLetto, stanza));
	}

	public void aggiungiRicovero(Paziente paziente, Letto letto, LocalDateTime dataOraInizio, LocalDateTime dataOraFine) throws IllegalArgumentException, RuntimeException {
		if (!pazienti.contains(paziente))
			throw new IllegalArgumentException("Il paziente " + paziente.getCOD_FISCALE() + " non esiste in memoria.");

		if (pazienteGiaOccupato(paziente, dataOraInizio))
			throw new RuntimeException("Il paziente " + paziente.getCOD_FISCALE() +
					" è già occupato da " + dataOraInizio.toString() + " a " + dataOraFine.toString() + "\nImpossibile aggiungere ricovero.");

		if (lettoGiaOccupato(letto, dataOraInizio)) throw new RuntimeException("Il letto " + letto.getCodiceLetto() +
				" è già occupato da " + dataOraInizio.toString() + " a " + dataOraFine.toString() + "\nImpossibile aggiungere ricovero.");

		ricoveri.add(new Ricovero(paziente, letto, dataOraInizio, dataOraFine));
	}

	/*
	DELETER

	 La variabile forced all'interno di questi metodi serve a definirne il modo in cui eliminare qualcosa
		false (Safe): Se c'è una singola cosa che fa riferimento alla cosa che si sta eliminando, crea un eccezione. Se passa tutti i controlli, elimina la cosa.
		true (Forced): Elimina sia la cosa che qualsiasi cosa che ne faccia riferimento.

		Se forced non viene specificata, si presuppone una safe delete.

	Deleter forzosi
	 */

	public void rimuoviPaziente(Paziente paziente, boolean forced) throws RuntimeException {
		for (Ricovero ricovero : ricoveri) {
			if (ricovero.getPaziente().equals(paziente)) {
				if (!forced) throw new RuntimeException("Safe delete impossibile.\nIl paziente " + paziente.getCOD_FISCALE() + "è coinvolto in un ricovero");
				ricoveri.remove(ricovero);
			}
		}

		pazienti.remove(paziente);
	}

	public void rimuoviMedico(Medico medico, boolean forced) throws RuntimeException {
		for (TurnoLavorativo turno : turni) {
			List<Medico> mediciInTurno = turno.getMedici();

			if(mediciInTurno.contains(medico)) {
				if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl medico è coinvolto in un turno.");
				mediciInTurno.remove(medico);
			}
		}

		//Mettere un controllo sulla classe prestazione - Per ora non ha attributi

		for (Reparto reparto : reparti) {
			List<Medico> mediciInReparto = reparto.getMedici();

			if(mediciInReparto.contains(medico)) {
				if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl medico è coinvolto in un reparto.");
				mediciInReparto.remove(medico);
			}
		}
	}

	public void rimuoviTurno(TurnoLavorativo turno, boolean forced) throws RuntimeException {
		for (Medico medico : medici ) {
			List<TurnoLavorativo> turniLavorativiMedico = medico.getTurniLavorativi();

			if(turniLavorativiMedico.contains(turno)) {
				if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl turno è contenuto da un medico.");
				turniLavorativiMedico.remove(turno);
			}
		}
	}

	public void rimuoviAmministratore(Amministratore amministratore, boolean forced) throws RuntimeException {
		amministratori.remove(amministratore);
	}

	public void rimuoviRicovero(Ricovero ricovero, boolean forced) throws RuntimeException {
		for (Medico medico : medici) {
			List<Medico> mediciInRicovero = ricovero.getMedici();

			if(mediciInRicovero.contains(medico)) {
				if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl ricovero è collegato ad almeno un medico.");
				mediciInRicovero.remove(medico);
			}
		}

		if(pazienti.contains(ricovero.getPaziente())) {
			if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl ricovero è collegato ad un paziente.");
			pazienti.remove(ricovero.getPaziente());
		}

		for (Letto letto : letti) {
			List<Ricovero> ricoveriInLetto = letto.getRicoveri();

			if(ricoveriInLetto.contains(ricovero)) {
				if(!forced) throw new RuntimeException("Safe delete impossibile.\nIl ricovero è contenuto in un letto.\"");
				ricoveriInLetto.remove(ricovero);
			}
		}

		ricoveri.remove(ricovero);
	}
 /*
 	TODO: Rendere le forced delete più consistenti tra le entità.
	TODO: Aggiungere override metodi rimozione.

	public void rimuoviReparto(Reparto reparto, boolean forced) throws RuntimeException {
		for(Stanza stanza : stanze) {
			Reparto repartoInStanza = stanza.getReparto();

			if(repartoInStanza.equals(reparto)) {
				if(!forced)
					throw new RuntimeException("Safe delete impossibile.\nUna stanza è contenuta in un reparto.");
				reparti.remove(reparto);
			}
		}
	}

	public void rimuoviStanza(Stanza stanza, boolean forced) throws RuntimeException {
		for(Letto letto : letti) {
			Stanza lettoInStanza = letto.getStanza();

			if(lettoInStanza.equals(stanza)) {
				if(!forced)
					throw new RuntimeException("Safe delete impossibile.\nUna stanza è contenuta in un reparto.");
				stanze.remove(stanza);
			}
		}
	}

	public void rimuoviLetto(Letto letto, boolean forced) throws RuntimeException {
		for(Ricovero ricovero : ricoveri) {
			Letto lettoDiRicovero = ricovero.getLetto();

			if(lettoDiRicovero.equals(letto)) {
				if(!forced)
					throw new RuntimeException("Safe delete impossibile.\nUna letto è contenuto in un ricovero.");
			}
		}
	}

  */
}