package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import token.*;

public class Scanner {
	final char EOF = (char) -1;
	private int riga;
	private PushbackReader buffer;
	private String log;

	final public Set<Character> skipChars = Set.of(' ', '\n', '\t', '\r', EOF);

	final public Map<Character, TokenType> charTypeMap = Map.of('+', TokenType.PLUS, '-', TokenType.MINUS, '*',
			TokenType.TIMES, '/', TokenType.DIVIDE, ';', TokenType.SEMI, '=', TokenType.OP_ASSIGN);
	final public Map<String, TokenType> keywordsMap = Map.of("print", TokenType.PRINT, "float", TokenType.FLOAT, "int",
			TokenType.INT);

	// skpChars: insieme caratteri di skip (include EOF) e inizializzazione
	// letters: insieme lettere e inizializzazione
	// digits: cifre e inizializzazione

	// char_type_Map: mapping fra caratteri '+', '-', '*', '/', ';', '=', ';' e il
	// TokenType corrispondente

	// keyWordsMap: mapping fra le stringhe "print", "float", "int" e il
	// TokenType corrispondente

	public Scanner(String fileName) throws FileNotFoundException {
		this.buffer = new PushbackReader(new FileReader(fileName));
		riga = 1;
		// inizializzare campi che non hanno inizializzazione
		log = "";
	}

	public Token nextToken() throws LexicalException {
		char nextChar;
		// nextChar contiene il prossimo carattere dell'input (non consumato).
		try {
			nextChar = peekChar();
		} catch (IOException e) {
			throw new LexicalException("Impossibile leggere carattere.");
		} // Catturate l'eccezione IOException e
			// ritornate una LexicalException che la contiene

		// Avanza nel buffer leggendo i carattere in skipChars
		// incrementando riga se leggi '\n'.
		// Se raggiungi la fine del file ritorna il Token EOF
		if (skipChars.contains(nextChar)) {
			while(skipChars.contains(nextChar)) {
				if (nextChar == EOF) {
					return new Token(TokenType.EOF, riga);
				}
				if (nextChar == '\n') {
					riga++;
				}
				try {
					readChar();
					nextChar = peekChar();
				} catch (IOException e) {
					throw new LexicalException("Impossibile leggere carattere.");
				}
			}
		}

		// Se nextChar e' in letters
		// return scanId()
		// che legge tutte le lettere minuscole e ritorna un Token ID o
		// il Token associato Parola Chiave (per generare i Token per le
		// parole chiave usate l'HaskMap di corrispondenza
		if (Character.isLetter(nextChar)) {
			try {
				return scanId();
			} catch (IOException e) {
				throw new LexicalException("Impossibile leggere carattere.");
			}
		}

		// Se nextChar e' o in operators oppure
		// ritorna il Token associato con l'operatore o il delimitatore
		if (charTypeMap.containsValue(nextChar)) {
			return new Token(charTypeMap.get(nextChar), riga);
		}

		// Se nextChar e' in numbers
		// return scanNumber()
		// che legge sia un intero che un float e ritorna il Token INUM o FNUM
		// i caratteri che leggete devono essere accumulati in una stringa
		// che verra' assegnata al campo valore del Token
		if (Character.isDigit(nextChar)) {
			try {
				return scanNumber();
			} catch (IOException e) {
				throw new LexicalException("Impossibile leggere carattere.");
			}
		}

		// Altrimenti il carattere NON E' UN CARATTERE LEGALE sollevate una
		// eccezione lessicale dicendo la riga e il carattere che la hanno
		// provocata.
		else {
			throw new LexicalException("Carattere illegale " + nextChar + " alla riga " + riga);
		}
	}

	private Token scanNumber() throws IOException {
		ArrayList<Character> number = new ArrayList<Character>();

		while (Character.isDigit(peekChar()) || peekChar() == '.') {
			char c = readChar();
			number.add(c);
		}
		if (String.join("", number.toString()).matches("[0-9]+.([0-9]{1,5})")) {
			return new Token(TokenType.FLOAT, riga, String.join("", number.toString()));
		} else
			return new Token(TokenType.INT, riga, String.join("", number.toString()));
	}

	private Token scanId() throws IOException {
		ArrayList<Character> id = new ArrayList<Character>();

		while (Character.isDigit(peekChar()) || peekChar() == '.') {
			char c = readChar();
			id.add(c);
		}
		if (keywordsMap.containsValue(id.toString())) {
			return new Token(TokenType.FLOAT, riga, id.toString());
		} else
			return new Token(TokenType.INT, riga, id.toString());
	}

	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}
