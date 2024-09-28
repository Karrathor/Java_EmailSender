package utb.fai;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        // Ověření, zda bylo předáno alespoň 6 argumentů (host, port, from, to, subject, message)
        if (args.length < 6) {
            System.out.println("Invalid number of arguments. Email will not be sent.");
            return;
        }

        // Přiřazení hodnot z argumentů
        String host = args[0];           // Hostitel (např. localhost)
        int port = Integer.parseInt(args[1]); // Port (např. 9999)
        String from = args[2];           // E-mailová adresa odesílatele
        String to = args[3];             // E-mailová adresa příjemce
        String subject = args[4];        // Předmět e-mailu
        String message = args[5];        // Obsah e-mailu

        // Vypsání informací pro kontrolu
        System.out.println("Using host: " + host);
        System.out.println("Using port: " + port);
        System.out.println("From: " + from);
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        // Vytvoření instance EmailSender a odeslání e-mailu
        try {
            EmailSender emailSender = new EmailSender(host, port);

            // Odeslání e-mailu s parametry předanými z argumentů
            emailSender.send(from, to, subject, message);

            // Uzavření spojení
            emailSender.close();

        } catch (Exception e) {
            e.printStackTrace(); // Zpracování výjimek při připojení nebo odesílání
        }
    }
}
