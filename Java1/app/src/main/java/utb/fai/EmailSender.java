package utb.fai;

import java.io.*;
import java.net.Socket;

public class EmailSender {

    // Socket pro komunikaci se SMTP serverem
    private Socket socket;  
    // Vstupní proud pro čtení odpovědí od serveru
    private InputStream input;  
    // Výstupní proud pro odesílání příkazů serveru
    private OutputStream output;  

    // Konstruktor – naváže spojení se SMTP serverem
    public EmailSender(String host, int port) throws IOException {
        socket = new Socket(host, port);  // Vytvoření socketu pro spojení s hostem a portem
        input = socket.getInputStream();  // Získání vstupního proudu pro čtení odpovědí
        output = socket.getOutputStream();  // Získání výstupního proudu pro odesílání příkazů
        readResponse();  // Čtení úvodní odpovědi serveru po připojení
    }

    // Metoda pro odeslání e-mailu – odesílá SMTP příkazy pro zpracování emailu
    public void send(String from, String to, String subject, String text) throws IOException {
        sendCommand("EHLO localhost");  // Identifikace klienta SMTP serveru
        sendCommand("MAIL FROM:<" + from + ">");  // Nastavení odesílatele e-mailu
        sendCommand("RCPT TO:<" + to + ">");  // Nastavení příjemce e-mailu
        sendCommand("DATA");  // Označení začátku těla e-mailu
        sendCommand("Subject: " + subject + "\r\n\r\n" + text + "\r\n.");  // Odeslání předmětu a obsahu e-mailu
    }

    // Metoda pro uzavření spojení s SMTP serverem
    public void close() {
        try {
            sendCommand("QUIT");  // Odeslání příkazu k ukončení spojení
            socket.close();  // Zavření socketu
        } catch (IOException ignored) {
        }
    }

    // Pomocná metoda pro odesílání příkazů na SMTP server
    private void sendCommand(String command) throws IOException {
        output.write((command + "\r\n").getBytes());  // Zápis příkazu do výstupního proudu
        output.flush();  // Vyprázdnění bufferu a odeslání příkazu
        readResponse();  // Čtení odpovědi od serveru po odeslání příkazu
    }

    // Pomocná metoda pro čtení odpovědí od serveru
    private void readResponse() throws IOException {
        byte[] buffer = new byte[1024];  // Buffer pro uchování odpovědi od serveru
        if (input.available() > 0) {  // Kontrola, zda server poslal odpověď
            int len = input.read(buffer);  // Čtení odpovědi do bufferu
            System.out.write(buffer, 0, len);  // Výpis odpovědi do konzole
        }
    }
}
