package model;

import exceptions.BadArgsException;

import java.util.ArrayList;
import java.util.List;

public class Medico extends UtenteRegistrato {

    private String tipoMedico;
    private Reparto reparto;
    private List<Ricovero> ricoveri;
    private List<TurnoLavorativo> turniLavorativi;
    private Amministratore amministratoreDiRiferimento;

    private void allocateAttribs(String tipoMedico, Reparto reparto) {
        this.tipoMedico = tipoMedico;
        this.reparto = reparto;
        this.ricoveri = new ArrayList<>();
        this.turniLavorativi = new ArrayList<>();
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

    public String getTipoMedico() {
        return tipoMedico;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public List<TurnoLavorativo> getTurniLavorativi() {return turniLavorativi;}

    @Override
    public String toString() {
        return getNome() + " " + getCognome() + " - " + tipoMedico;
    }
}