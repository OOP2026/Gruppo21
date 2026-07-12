package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Medico extends UtenteRegistrato {

    private int idMedico;
    private String tipoMedico;
    private Reparto reparto;
    private List<Ricovero> ricoveri;
    private List<TurnoLavorativo> turniLavorativi;
    private List<InterventoChirurgico> interventi;
    private Amministratore amministratoreDiRiferimento;

    public Medico(int idMedico) throws BadArgsException {
        super("", "", "", "");
        this.idMedico = idMedico;
        this.ricoveri = new ArrayList<>();
        this.turniLavorativi = new ArrayList<>();
    }

    private void allocateAttribs(String tipoMedico, Reparto reparto) {
        this.tipoMedico = tipoMedico;
        this.reparto = reparto;
        this.ricoveri = new ArrayList<>();
        this.turniLavorativi = new ArrayList<>();
        this.interventi = new ArrayList<>();
    }

    public Medico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto) throws BadArgsException {
        super(nome, cognome, email, password);

        if(tipoMedico == null || reparto == null) throw new BadArgsException("La classe Medico ha degli attributi NULLI.");
        if(tipoMedico.isEmpty()) throw new BadArgsException("La classe Medico ha degli attributi VUOTI.");

        allocateAttribs(tipoMedico, reparto);
        reparto.aggiungiMedico(this);
    }

    public Medico(String nome, String cognome, String email, String password, String tipoMedico, Reparto reparto, Amministratore amministratoreDiRiferimento) throws BadArgsException {
        super(nome, cognome, email, password);

        if(tipoMedico == null || reparto == null) throw new BadArgsException("La classe Medico ha degli attributi NULLI.");
        if(tipoMedico.isEmpty()) throw new BadArgsException("La classe Medico ha degli attributi VUOTI.");

        allocateAttribs(tipoMedico, reparto);
        this.amministratoreDiRiferimento = amministratoreDiRiferimento;
        reparto.aggiungiMedico(this);
    }

    public void aggiungiRicovero(Ricovero ricovero) {
        ricoveri.add(ricovero);
    }

    public void aggiungiTurnoLavorativo(TurnoLavorativo turnoLavorativo) {
        turniLavorativi.add(turnoLavorativo);
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public Amministratore getAmministratoreDiRiferimento() {
        return amministratoreDiRiferimento;
    }

    public void setAmministratoreDiRiferimento(Amministratore amministratoreDiRiferimento) {
        this.amministratoreDiRiferimento = amministratoreDiRiferimento;
    }

    public List<TurnoLavorativo> getTurniLavorativi() {
        return turniLavorativi;
    }

    public void setTurniLavorativi(List<TurnoLavorativo> turniLavorativi) {
        this.turniLavorativi = turniLavorativi;
    }

    public List<Ricovero> getRicoveri() {
        return ricoveri;
    }

    public void setRicoveri(List<Ricovero> ricoveri) {
        this.ricoveri = ricoveri;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    public String getTipoMedico() {
        return tipoMedico;
    }

    public void setTipoMedico(String tipoMedico) {
        this.tipoMedico = tipoMedico;
    }

    public List<InterventoChirurgico> getInterventi() {
        return interventi;
    }

    public void setInterventi(List<InterventoChirurgico> interventi) {
        this.interventi = interventi;
    }

    public void aggiungiIntervento(InterventoChirurgico intervento) {
        interventi.add(intervento);
    }

    @Override
    public String toString() {
        return getNome() + " " + getCognome() + " - " + tipoMedico;
    }
}