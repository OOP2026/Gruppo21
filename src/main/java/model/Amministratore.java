package model;

public class Amministratore extends UtenteRegistrato {

    public Amministratore(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);
    }

    public Amministratore(String email, String password) {
        super(email, password);
    }

    private void gestireAnagraficaPazienti() {
        System.out.println("Gestione anagrafica pazienti");
    }

    private void rimuoviUtente() {
        System.out.println("Utente rimosso");
    }

    private boolean aggiungereRicovero() {
        System.out.println("Ricovero aggiunto");
        return true;
    }

    public boolean aggiungereTurnoLavorativo() {
        System.out.println("Turno lavorativo aggiunto");
        return true;
    }
}