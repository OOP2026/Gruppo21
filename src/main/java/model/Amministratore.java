package model;

public class Amministratore extends UtenteRegistrato {

    public Amministratore(String login, String password, String nome, String cognome) {
        super(login, password, nome, cognome);
    }

    public void registraRicovero(Ricovero ricovero) {
        System.out.println("Ricovero registrato: " + ricovero);
    }
}

