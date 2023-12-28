import static org.junit.Assert.*;

import DataBase.DbNotifiche;
import Elaborazione.GestoreNotifiche;
import UserInterface.UiNotifica;
import UserInterface.UiNotificaInterfaccia;
import org.junit.Before;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import DataBase.DbUtenti;
import Elaborazione.GestoreAccessi;

public class RF04GeneraNotifica {

    DbNotifiche dbNotifiche = new DbNotifiche();
    GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(dbNotifiche);

    HashMap<String, Object> ordine;
    HashMap<String, Object> prodotto;
    HashMap<String, Object> utente;

    @Before
    public void inizializzaHashMap() {
        ordine = new HashMap<>();
        prodotto = new HashMap<>();
        utente = new HashMap<>();


        ordine.put("username", "clea99");
        prodotto.put("tipo", "Libro");
        prodotto.put("autore", "Nikolaj S. Piskunov");
        prodotto.put("titolo", "Calcolo Integrale e Differenziale 2");
        utente.put("nome", "Aldo");
        utente.put("cognome", "Bruni");
    }

    /*
     * Test per verificare il corretto funzionamento del metodo "verificaCorrettezzaDati"
     */
    @Test
    public void testVerificaCorrettezzaDati1() throws RemoteException {
        // data in formato errato
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("12-12-12", "11:00:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("2024-20-01", "1111:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("2023-20-01", "11:00:00", ""));
        assertEquals("errore formato data", gestoreNotifiche.verificaCorrettezzaDati("", "11:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati2() throws RemoteException {
        // ora in formato errato
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "99:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "23:89", "Nuovo utente: Carla Rossi."));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "100:00", ""));
        assertEquals("errore formato ora", gestoreNotifiche.verificaCorrettezzaDati("2024-12-12", "", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati3() throws RemoteException {
        // data non coerente
        assertEquals("errore data", gestoreNotifiche.verificaCorrettezzaDati("2020-11-12", "11:00:00", "Nuovo utente: Carla Rossi."));
        assertEquals("errore data", gestoreNotifiche.verificaCorrettezzaDati("2020-11-12", "11:00:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati4() throws RemoteException {
        // testo notifica vuoto
        assertEquals("errore testo notifica", gestoreNotifiche.verificaCorrettezzaDati("2024-11-12", "11:00:00", ""));
    }

    @Test
    public void testVerificaCorrettezzaDati5() throws RemoteException {
        assertEquals("ok", gestoreNotifiche.verificaCorrettezzaDati("2024-11-12", "11:00:00", "Nuovo utente: Carla Rossi."));
    }

    /*
     * Test per verificare il corretto funzionamento dei metodi "generaTestoNotifica*"
     */
    @Test
    public void testGeneraTestoNotifica1() throws RemoteException {
        // genera testo prodotto
        assertEquals("Nuovo Libro: Nikolaj S. Piskunov, Calcolo Integrale e Differenziale 2.", gestoreNotifiche.generaTestoNotificaProdotto(prodotto));
    }

    @Test
    public void testGeneraTestoNotifica2() throws RemoteException {
        // genera testo ordine
        assertEquals("Nuovo Ordine: effettuato da clea99.", gestoreNotifiche.generaTestoNotificaOrdine(ordine));
    }

    @Test
    public void testGeneraTestoNotifica3() throws RemoteException {
        // genera testo avviso
        assertEquals("Avviso: ", gestoreNotifiche.generaTestoNotificaAvviso());
    }

    @Test
    public void testGeneraTestoNotifica4() throws RemoteException {
        // genera testo utente
        assertEquals("Nuovo Cliente: Aldo Bruni.", gestoreNotifiche.generaTestoNotificaUtente(utente));
    }

}