package programmieraufgaben;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden connect, disconnect,
 * request und extract befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Client {
    //Diese Variable gibt den Socket an an dem die Verbindung aufgabaut werden soll
    private Socket clientSocket;
    //Für In/Output
    private PrintWriter pw;
    private Scanner h;
    private Scanner p;

    /**
     * Hier werden die Verbindungsinformationen abgefragt und eine Verbindung eingerichtet.
     */
    public void connect() {

        System.out.println("Mit welchem Server wollen Sie sich verbinden?");
        System.out.println();
        System.out.print("IP-Adresse: ");
        h = new Scanner(System.in);
        String host = h.nextLine();
        //Überprüfung der IP-Adresse
        if(host.equals("localhost") || host.equals("127.0.0.1"))
        {
            System.out.print("Port: ");
            p = new Scanner(System.in);
            String port = p.nextLine();
            //Überprüfung des Ports
            if(port.equals("2020"))
            {
                try
                {
                    clientSocket = new Socket();
                    //Verbindung mit Server am End Point
                    clientSocket.connect(new InetSocketAddress(host, Integer.parseInt(port)));

                    System.out.println("\nEine TCP-Verbindung zum Server mit IP-Adresse localhost (Port: 2020) wurde\n" +
                            "hergestellt. Sie können nun Ihre Anfragen an den Server stellen.\n");

                }
                catch (IOException e) {
                    System.out.println("Fehler beim Verbindungsaufbau! Es konnte keine TCP-Verbindung zum Server mit\n" +
                            "IP-Adresse localhost (Port: 2020) hergestellt werden.");
                    //e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
            }

        }
        else
        {
            System.out.print("Falsche IP-Adresse! Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost möglich.");
        }

    }


    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect()  {
        try{
            System.out.println("Die Verbindung zum Server wurde beendet.\n");
            pw.close();
            h.close();
            p.close();
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * In dieser Methode sollen die Eingaben des Benutzers an den Server gesendet und die Antwort empfangen werden
     * @param userInput Eingabe des Benutzers
     * @return Die vom Server empfangene Nachricht
     */
    public String request(String userInput) {
        try
        {
            //Mit Pw können wir die Inputs vom Client zum Server senden
            pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            pw.println(userInput);
            pw.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return  "";
    }

    /**
     * Die vom Server empfangene Nachricht soll hier für die Konsolenausgabe aufbereitet werden.
     * @param reply Die vom Server empfangene Nachricht
     * @return Ausgabe für die Konsole
     */
    public String extract(String reply) {

        try
        {
            //Mit x können wir die Antwort vom Server annehmen.
            Scanner x = new Scanner(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));

            if (x.hasNextLine())
            {
                reply = x.nextLine();
                String s = "";
                if(reply.contains("_"))
                {
                    //Hier wird die Antwort vom Server in String Array geteilt, falls die "-" enthält.
                    String[] parts = reply.split("_");
                    for(int i=0; i< parts.length;i++)
                    {
                        s += parts[i]+"\n";
                    }
                    return s+ "";
                }

                return reply+ "\n";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gibt den Status der Verbindung an
     * @return Wenn die Verbindung aufgebaut ist: TRUE sonst FALSE
     */
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected());
    }
}
