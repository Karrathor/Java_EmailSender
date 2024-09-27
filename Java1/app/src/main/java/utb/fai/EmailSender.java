package utb.fai;

import java.io.*;
import java.net.Socket;

public class EmailSender {

    private Socket socket; // Socket pro komunikaci s SMTP serverem
    private InputStream input; // Vstupní proud pro čtení odpovědí ze serveru
    private OutputStream output; // Výstupní proud pro odesílání příkazů serveru
    private byte[] responseBuffer = new byte[1024]; // Buffer pro ukládání odpovědí ze serveru

    // Konstruktor pro připojení k SMTP serveru pomocí IP adresy a portu
    public EmailSender(String host, int port) throws IOException {
        this.socket = new Socket(host, port); // Navázání socketového spojení se serverem
        this.input = socket.getInputStream(); // Inicializace vstupu
        this.output = socket.getOutputStream(); // Inicializace výstupu
        System.out.println("Connected to SMTP server at " + host + ":" + port); // Informace o úspěšném připojení
        readServerResponse(); // Čtení uvítací zprávy serveru
    }

    // Metoda pro odeslání e-mailu s kontrolou vstupních parametrů
    public void send(String from, String to, String subject, String text) throws IOException, InterruptedException {
        // Kontrola, zda jsou parametry neprázdné a platné
        if (from == null || from.isEmpty() || to == null || to.isEmpty() || subject == null || subject.isEmpty() || text == null || text.isEmpty()) {
            System.out.println("Invalid email parameters. Email will not be sent.");
            return; // Přerušíme odeslání e-mailu, pokud jsou parametry neplatné
        }

        // Odeslání příkazu EHLO
        sendCommand("EHLO localhost");

        // Příkaz MAIL FROM, kde se specifikuje odesílatel e-mailu
        sendCommand("MAIL FROM:<" + from + ">");

        // Příkaz RCPT TO, který udává příjemce e-mailu
        sendCommand("RCPT TO:<" + to + ">");

        // Příkaz DATA, který zahajuje odesílání obsahu e-mailu (hlavička a tělo)
        sendCommand("DATA");

        // Odeslání hlavičky e-mailu (předmět) a těla e-mailu (samotný obsah), tečka na samostatném řádku
        sendCommand("Subject: " + subject + "\r\n\r\n" + text + "\r\n.\r\n");

        // Čtení odpovědi po odeslání e-mailu
        readServerResponse();
    }

    // Metoda pro uzavření spojení se serverem
    public void close() throws IOException, InterruptedException {
        sendCommand("QUIT"); // Příkaz QUIT informuje server o ukončení komunikace
        socket.close(); // Zavření socketu
        System.out.println("Connection closed."); // Informace o úspěšném ukončení spojení
    }

    // Pomocná metoda pro odesílání příkazů serveru a čtení odpovědí
    private void sendCommand(String command) throws IOException, InterruptedException {
        output.write((command + "\r\n").getBytes()); // Odeslání příkazu serveru
        output.flush(); // Ujistíme se, že všechny bajty jsou odeslány

        // Čekáme na odpověď serveru
        Thread.sleep(500);
        readServerResponse();
    }

    // Pomocná metoda pro čtení odpovědí ze serveru
    private void readServerResponse() throws IOException {
        if (input.available() > 0) {
            int len = input.read(responseBuffer); // Přečteme odpověď do bufferu
            System.out.write(responseBuffer, 0, len); // Vypíšeme odpověď na konzoli
        }
    }
}
