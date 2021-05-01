package programmieraufgaben;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden execute, disconnect
 * und checkPort befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Socket remoteClientSocket;
    private Scanner s;
    private PrintWriter pw;
    private String input;

    //Konstruktor
    public Server()
    {
        this.port = 0;
    }
    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {
        while(true)
        {
            try
            {
                System.out.println("[Server] Server starten...");
                serverSocket = new ServerSocket(port);
                System.out.println();
                System.out.println("[Server] Warten auf Verbindung...");
                //Warten auf Verbindung
                remoteClientSocket = serverSocket.accept();
                //Client verbunden
                System.out.println("[Server] Client verbunden: ");

                //Mit s können wir die Inputs vom Client annehmen.
                s = new Scanner(new BufferedReader((new InputStreamReader(remoteClientSocket.getInputStream()))));
                //Mit Pw können wir die Inputs vom Server zum Client senden.
                pw = new PrintWriter(new OutputStreamWriter(remoteClientSocket.getOutputStream()));

                //Die Inputs vom Client werden in history gespeichert.
                ArrayList<String> history = new ArrayList<>();

                while (s.hasNextLine())
                {
                    input = s.nextLine();
                    if(hasNums(input) )
                    {
                        String as = input;
                        String[] parts = as.split(" ");
                        if(parts.length > 2)
                        {
                            if(input.equals("DIV " + parts[1] +" "+ parts[2]) )
                            {
                                if(isInt(parts[1]) && isInt(parts[2]) )
                                {
                                    int d = Integer.parseInt(parts[1]);
                                    int r = Integer.parseInt(parts[2]);
                                    if(r == 0 )
                                    {

                                        pw.println("QUOTIENT undefined");
                                        pw.flush();
                                    }
                                    double result = (double)d/r;
                                    pw.println(result);
                                    pw.flush();

                                }
                                else
                                {
                                    pw.println("ERROR Falsches Format!");
                                    pw.flush();
                                }

                            }
                            else if(input.equals("ADD " + parts[1] +" "+ parts[2]))
                            {
                                if(isInt(parts[1]) && isInt(parts[2]) )
                                {
                                    int d = Integer.parseInt(parts[1]);
                                    int r = Integer.parseInt(parts[2]);
                                    int result = d+r;
                                    pw.println(result);
                                    pw.flush();
                                }
                                else
                                {
                                    pw.println("ERROR Falsches Format!");
                                    pw.flush();
                                }
                            }
                            else if(input.equals("SUB " + parts[1] +" "+ parts[2]))
                            {
                                if(isInt(parts[1]) && isInt(parts[2]) )
                                {
                                    int d = Integer.parseInt(parts[1]);
                                    int r = Integer.parseInt(parts[2]);
                                    int result = d-r;
                                    pw.println(result);
                                    pw.flush();
                                }
                                else
                                {
                                    pw.println("ERROR Falsches Format!");
                                    pw.flush();
                                }
                            }
                            else if(input.equals("MUL " + parts[1] +" "+ parts[2]))
                            {
                                if(isInt(parts[1]) && isInt(parts[2]) )
                                {
                                    int d = Integer.parseInt(parts[1]);
                                    int r = Integer.parseInt(parts[2]);
                                    int result = d*r;
                                    pw.println(result);
                                    pw.flush();
                                }
                                else
                                {
                                    pw.println("ERROR Falsches Format!");
                                    pw.flush();
                                }

                            }
                            else
                            {
                                pw.println("ERROR Unbekannte Anfrage!");
                                pw.flush();
                            }
                        }
                        else if(input.length()>6 && input.substring(0,7).equals("HISTORY") )
                        {
                            String s = "";
                            int y = Integer.parseInt(parts[1]);
                            for(int i = history.size() -1;  i >= history.size()-y  ; i--)
                            {
                                if(i < history.size())
                                {
                                    s = s + "_" + history.get(i);
                                }

                            }
                            pw.println(s);
                            pw.flush();

                        }
                        else
                        {
                            pw.println("ERROR Unbekannte Anfrage!");
                            pw.flush();
                        }

                    }
                    else if(input.equals("GET Time"))
                    {
                        //Annahme der Zeit
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        Date time = new Date();
                        pw.println(formatter.format(time));
                        pw.flush();

                    }
                    else if(input.equals("GET Date"))
                    {
                        //Annahme des Datums
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        pw.println(formatter.format(date));
                        pw.flush();

                    }
                    else if(input.equals("HISTORY") )
                    {
                        String s = "";
                        for(int i = 0;i < history.size(); i++)
                        {
                            if(i <= history.size()-1)
                            {
                                s = history.get(i)+"_"+s;
                            }
                            else {
                                s= history.get(i)+s;
                            }

                        }
                        pw.println(s);
                        pw.flush();
                    }
                    else if(input.equals("PING"))
                    {
                        pw.println("PONG");
                        pw.flush();
                    }
                    else if(input.equals("DISCARD"))
                    {
                        pw.println();
                        pw.flush();
                    }
                    else if (input.length() > 3 && input.substring(0, 4).equals("ECHO") )
                    {
                        String echo = input;
                        String[] asd = echo.split(" ");
                        String s = "";
                        for(int i = 1; i<asd.length;i++)
                        {

                            s += asd[i]+" ";

                        }
                        pw.println(s);
                        pw.flush();
                    }
                    else
                    {
                        pw.println("ERROR Unbekannte Anfrage!");
                        pw.flush();
                    }

                    if(!input.equals("DISCARD"))
                    {
                        history.add(input);
                    }

                }

            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect()  {
        while(true)
        {
            try {
                pw.close();
                s.close();
                remoteClientSocket.close();
                serverSocket.close();

            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    //Wir überprüfen hier, ob es eine Integer Variable in String gibt.
    public static boolean isInt(String s) {
        boolean isValidInteger = false;
        try
        {
            Integer.parseInt(s);

            isValidInteger = true;
        }
        catch (NumberFormatException e)
        {
            System.out.println(e);
        }

        return isValidInteger;
    }
    //Wir überprüfen hier, ob es eine Nummer in String gibt.
    public boolean hasNums(String text) {

        if (text.matches(".*\\d.*")) {
            return true;
        }
        return false;
    }

    //Wir überprüfen hier, ob String nur aus Integer besteht.
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        //
        if( isNumeric(port) )
        {
            this.port = Integer.parseInt(port);
            if( this.port == 2020)
            {
                return true;
            }
        }
        System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");

        return false;

    }

    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     * @return port
     */
    public int getPort() {
        return port;
    }
}
