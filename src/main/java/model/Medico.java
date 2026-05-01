package model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends UtenteRegistrato {

    private String matricola;
    private String specializzazione;
    private Reparto reparto;
    private List<Turno> turni;
    private List<Prestazione> prestazioni;

    public Medico(String login, String password, String matricola, String nome,
                  String cognome, String specializzazione, Reparto reparto) throws NullPointerException {
        super(login, password, nome, cognome);
        this.matricola = matricola;
        this.specializzazione = specializzazione;
        this.reparto = reparto;
        this.turni = new ArrayList<>();
        this.prestazioni = new ArrayList<>();

        reparto.aggiungiMedico(this);
    }

    public void aggiungiTurno(Turno turno) {
        turni.add(turno);
    }

    public void aggiungiPrestazione(Prestazione prestazione) {
        prestazioni.add(prestazione);
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Reparto getReparto() {
        return reparto;
    }

    @Override
    public String toString() {
        return "Dr. " + nome + " " + cognome + " - " + specializzazione;
    }
}