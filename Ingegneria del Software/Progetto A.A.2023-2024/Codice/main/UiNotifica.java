package UserInterface;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.util.EventObject;
import java.util.HashMap;
import java.util.ArrayList;

import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.time.LocalTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import UserInterface.*;
import Elaborazione.*;

public class UiNotifica extends JOptionPane implements UiNotificaInterfaccia
{


	// registri
	private Registry registryUI;
	private Registry registryGestore;

	// componenti
	private GestoreNotificheInterfaccia gestoreNotifiche;

	// attributi
	// RF04: genera notifica (Monfermoso, Magenta Biasina)
	private HashMap<String, String> dataScadenza;
	private HashMap<String, String> dataPubblicazione;
	private String esitoVerifica;
	private String testoNotifica;


	// elementi grafici
	//RF04: genera notifica (Monfermoso, Magenta Biasina)
	private JLabel dataLabel;
	private JLabel oraLabel;
	private JLabel testoLabel;
	private JTextField dataField;
	private JTextField oraField;
	private JTextArea testoField;
	private JPanel modificaNotificaPanel;


	public UiNotifica(String hostGestore) throws RemoteException, NotBoundException
	{

		registryUI = LocateRegistry.getRegistry("127.0.0.1", 1100); // default: 1099
		registryGestore = LocateRegistry.getRegistry(hostGestore, 1099);

		gestoreNotifiche = (GestoreNotificheInterfaccia) registryGestore.lookup("gestoreNotifiche");

		inizializzaUIRF04();
	}

	/**
	 * RF04: Inizializza gli attributi per generare la finestra di dialogo di avvioGeneraNotifica()
	 *
	 * @author Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public void inizializzaUIRF04() {
		dataPubblicazione = new HashMap<>();
		dataScadenza = new HashMap<>();

		dataField = new JTextField("", 20);
		dataLabel = new JLabel("Inserisci data di scadenza: ");
		dataField.setToolTipText("Data");

		oraField = new JTextField("", 20);
		oraLabel = new JLabel("Inserisci l'ora di scadenza: ");
		oraField.setToolTipText("Ora");

		testoField = new JTextArea(10, 40);
		testoField.setLineWrap(true);
		testoLabel = new JLabel("Inserisci testo notifica:");
		testoField.setToolTipText("Testo");

		modificaNotificaPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.gridy=0;
		constraints.gridx=0;
		modificaNotificaPanel.add(dataLabel,constraints);

		constraints.gridy=0;
		constraints.gridx=2;
		constraints.gridwidth=1;
		modificaNotificaPanel.add(dataField,constraints);

		constraints.gridy=1;
		constraints.gridx=0;
		modificaNotificaPanel.add(oraLabel,constraints);

		constraints.gridy=1;
		constraints.gridx=2;
		constraints.gridwidth=1;
		modificaNotificaPanel.add(oraField,constraints);

		constraints.gridy=3;
		constraints.gridx=0;
		modificaNotificaPanel.add(testoLabel,constraints);

		constraints.gridy=3;
		constraints.gridx=2;
		constraints.gridwidth=5;
		modificaNotificaPanel.add(testoField,constraints);
	}

	/**
	 * RF04: Avvia la generazione di una notifica
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	public void avvioGeneraNotifica(String tipoNotifica, HashMap<String, Object> prodotto, HashMap<String, Object> ordine, HashMap<String, Object> utente, String tipoUtente) throws RemoteException {
		switch (tipoNotifica) {
			case "nuovo prodotto":
				testoNotifica = gestoreNotifiche.generaTestoNotificaProdotto(prodotto);
				do {
					this.mostraFormNotifica(testoNotifica);
					testoNotifica = testoField.getText();
					esitoVerifica = gestoreNotifiche.verificaCorrettezzaDati(dataScadenza.get("data"), dataScadenza.get("ora"), testoNotifica);
					if (esitoVerifica.contains("errore")) {
						mostraErrore(esitoVerifica);
					}
				} while(!Objects.equals(esitoVerifica, "ok"));
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoField.getText(), tipoUtente);
				break;
			case "nuovo ordine":
				testoNotifica = gestoreNotifiche.generaTestoNotificaOrdine(ordine);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoNotifica, tipoUtente);
				break;
			case "nuovo utente":
				testoNotifica = gestoreNotifiche.generaTestoNotificaUtente(utente);
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), setDataScadenzaDefault(), testoNotifica, tipoUtente);
				break;
			case "avviso":
				testoNotifica = gestoreNotifiche.generaTestoNotificaAvviso();
				do {
					this.mostraFormNotifica(testoNotifica);
					testoNotifica = testoField.getText();
					esitoVerifica = gestoreNotifiche.verificaCorrettezzaDati(dataScadenza.get("data"), dataScadenza.get("ora"), testoNotifica);
					if (esitoVerifica.contains("errore")) {
						mostraErrore(esitoVerifica);
					}
				} while(!Objects.equals(esitoVerifica, "ok"));
				gestoreNotifiche.inserimentoNotifica(setDataPubblicazione(), dataScadenza, testoNotifica, tipoUtente);
				break;
            default:
                throw new IllegalStateException("Valore inatteso: " + tipoNotifica);
        }
	}

	/**
	 * RF04: Mostra il form delle notifiche.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraFormNotifica(String testoNotifica) {
		testoField.setText(testoNotifica);

		this.showMessageDialog(null, modificaNotificaPanel, "Modifica notifica", this.QUESTION_MESSAGE);

		dataField.setBackground(Color.WHITE);
		oraField.setBackground(Color.WHITE);
		testoField.setBackground(Color.WHITE);

		dataScadenza.put("data", dataField.getText());
		dataScadenza.put("ora", oraField.getText());
	}

	public void avvioRicercaNotifiche() throws RemoteException
	{ 	// RF21	
	}

	/**
	 * RF04: Mostra un errore.
	 *
	 * @author  Linda Monfermoso, Gabriele Magenta Biasina
	 */
	private void mostraErrore(String tipoErrore) {
		String messaggio = "";
	// YELLOW per quanto riguarda errori di inserimento/sintassi
	// RED per quanto riguarda il mancato inserimento
		switch(tipoErrore) {
			case "errore formato data":
				messaggio = "La data fornita non e' in formato YYYY-MM-DD.";
				dataField.setBackground(Color.YELLOW);
				break;
			case "errore formato ora":
				messaggio = "L'ora fornita non e' in formato HH:mm:ss.";
				oraField.setBackground(Color.YELLOW);
				break;
			case "errore data":
				messaggio = "La data fornita non e' compatibile con la data di pubblicazione.";
				dataField.setBackground(Color.YELLOW);
				break;
			case "errore testo notifica":
				messaggio = "Il testo della notifica non può essere vuoto.";
				testoField.setBackground(Color.RED);
				break;
		}
		this.showMessageDialog(null, messaggio, "Errore", this.ERROR_MESSAGE, null);
	}

	private HashMap<String, String> setDataPubblicazione() {
		dataPubblicazione.put("data", LocalDate.now().toString());
		dataPubblicazione.put("ora", LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());

		return dataPubblicazione;
	}

	private HashMap<String, String> setDataScadenzaDefault() {
		dataScadenza.put("data", LocalDate.now().plusDays(1).toString());
		dataScadenza.put("ora", LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());

		return dataScadenza;
	}

}