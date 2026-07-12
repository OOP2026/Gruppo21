import exceptions.BadArgsException;
import model.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MainTestOspedale {

    public static void main(String[] args) throws BadArgsException {


        Reparto reparto = new Reparto("Cardiologia", 1);

        Stanza stanza = new Stanza(reparto);

        Letto letto = new Letto(1, stanza);

        Paziente paziente = new Paziente(
                "Mario",
                "Rossi",
                "RSSMRA90A01H501X"
        );

        Medico medico = new Medico(
                "Marco",
                "Verdi",
                "mverdi@email.com",
                "password",
                "Cardiologo",
                reparto
        );

        Ricovero ricovero = new Ricovero(
                paziente,
                letto,
                LocalDateTime.of(2026, 4, 27, 8, 0),
                LocalDateTime.of(2026, 5, 3, 10, 0)
        );

        Visita visita = new Visita("Visita cardiologica");


        InterventoChirurgico intervento = new InterventoChirurgico("Angioplastica");


        TurnoLavorativo turno1 = new TurnoLavorativo(
                LocalDateTime.of(2026, 4, 27, 7, 0),
                LocalDateTime.of(2026, 4, 27, 7, 59)
        );

        TurnoLavorativo turno2 = new TurnoLavorativo(
                LocalDateTime.of(2026, 4, 27, 8, 0),
                LocalDateTime.of(2026, 4, 27, 8, 59)
        );

        turno1.aggiungiMedico(medico);
        turno2.aggiungiMedico(medico);

        try {
            ricovero.aggiungiMedico(medico);
        } catch (Exception e) {
            System.out.println("Errore nell'aggiunta del medico: " + e.getMessage());
        }

        System.out.println(paziente);
        System.out.println(medico);
        System.out.println(ricovero);
        System.out.println(visita);
        System.out.println(intervento);
        System.out.println(turno1);
        System.out.println(turno2);
    }
}