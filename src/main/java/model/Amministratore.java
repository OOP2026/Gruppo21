package model;

public class Amministratore extends UtenteRegistrato {

    public Amministratore(String login, String password) {
        super(login, password);
    }

    public void registraRicovero(Ricovero ricovero) {
        System.out.println("Ricovero registrato: " + ricovero);
    }
}
