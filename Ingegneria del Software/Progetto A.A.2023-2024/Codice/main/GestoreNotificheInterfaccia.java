package Elaborazione;

import java.rmi.Remote; 
import java.rmi.RemoteException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ArrayList;

public interface GestoreNotificheInterfaccia extends Remote 
{
    /**
     * RF04: Genera il testo della notifica per un nuovo prodotto.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @param prodotto il prodotto in questione
     * @return il testo della notifica
     * @throws RemoteException
     */
    String generaTestoNotificaProdotto(HashMap<String, Object> prodotto) throws RemoteException;

    /**
     * RF04: Genera il testo della notifica per un nuovo ordine.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @param ordine l'ordine in questione
     * @return il testo della notifica
     * @throws RemoteException
     */
    String generaTestoNotificaOrdine(HashMap<String, Object> ordine) throws RemoteException;

    /**
     * RF04: Genera il testo della notifica per un avviso.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @return il testo della notifica
     * @throws RemoteException
     */
    String generaTestoNotificaAvviso() throws RemoteException;

    /**
     * RF04: Genera il testo della notifica per un nuovo utente.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @param utente l'utente in questione
     * @return il testo della notifica
     * @throws RemoteException
     */
    String generaTestoNotificaUtente(HashMap<String, Object> utente) throws RemoteException;

    /**
     * RF04: Verifica la correttezza dei dati di una notifica.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @param data la data da verificare
     * @param ora l'ora da verificare
     * @param testoNotifica il testo da verificare
     * @return l'esito della verifica
     * @throws RemoteException
     */
    String verificaCorrettezzaDati(String data, String ora, String testoNotifica) throws RemoteException;

    /**
     * RF04: Inserisce una notifica nel database.
     *
     * @author Linda Monfermoso, Gabriele Magenta Biasina
     * @param dataPubblicazione la data in cui viene pubblicata la notifica
     * @param dataScadenza la data di scdenza della notifica
     * @param testoNotifica il testo della notifica
     * @param tipoUtente il tipo di utente interessato
     * @throws RemoteException
     */
    void inserimentoNotifica(HashMap<String, String> dataPubblicazione, HashMap<String, String> dataScadenza, String testoNotifica, String tipoUtente) throws RemoteException;
    
} 
