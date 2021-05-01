package programmieraufgaben;


import java.util.*;
import java.util.regex.Pattern;


public class PackageCreator {

    /**
     * Hier sollen die Kommandozeilen-Abfragen abgefragt und die Antworten
     * gespeichert werden
     * Es sollte auf Fehlerbehandlung geachtet werden (falsche Eingaben, ...)
     *
     * @param dataPackage Hier wird das Objekt übergeben in das die abgefragten Werte gespeichert werden sollen
     * @return Gibt das als Parameter übergebene Objekt, dass mit den abgefragten Werten befüllt wurde zurück
     */
    public DataPackage fillParameters(DataPackage dataPackage)  {


        dataPackage = new DataPackage(dataPackage.getDataPackageLength());

        Scanner input1 = new Scanner(System.in);
        System.out.print("Version: ");
        String ver = input1.nextLine();
        dataPackage.setVersion(ver);


        Scanner input2 = new Scanner(System.in);
        System.out.print("Absender: ");
        String absn = input2.nextLine();
        dataPackage.setAbsender(absn);

        Scanner input3 = new Scanner(System.in);
        System.out.print("Empfänger: ");
        String empf = input3.nextLine();
        dataPackage.setEmpfanger(empf);


        Scanner input4 = new Scanner(System.in);
        System.out.println("Nachricht: ");
        String nachr;
        input4.useDelimiter("\n.\n+");
        while(true)
        {
            nachr = input4.next();
            break;
        }
        dataPackage.setNachricht(nachr) ;



        return dataPackage;
    }

    /**
     * Aus dem als Parameter übergebenen Paket sollen die Informationen
     * ausgelesen und in einzelne Datenpakete aufgeteilt werden
     *
     * @param dataPackage Hier wird das Objekt übergeben in das das Resultat gespeichert werden soll
     * @return Gibt das als Parameter übergebene Objekt mit den aufgeteiltet Datenpaketen zurück
     */
    public List<DataPackage> splitPackage(DataPackage dataPackage)  {
        List<DataPackage> dataPackages = new LinkedList<>();

        //

        String nachr = dataPackage.getNachricht();
       // String parts[] = nachr.split("<CR><LF>");

        String delimiters = "\\s";
        //String delimiters = "\n+|\\w+";
        Pattern p = Pattern.compile( delimiters );
        String parts[] = p.split(nachr);




        for(int i = 0; i< parts.length; i++)
        {

            String ver = dataPackage.getVersion();
            String absn = dataPackage.getAbsender();
            String empf = dataPackage.getEmpfanger();
            dataPackage = new DataPackage(parts[i].length(), ver, absn, empf, parts[i]);


            dataPackages.add(dataPackage);
        }

        //
        System.out.println("Es sind " + dataPackages.size() + " Datenpakete notwendig.");
        System.out.println();


        return dataPackages;
    }

    /**
     * Diese Methode gibt den Inhalt der empfangenen Pakete in der Komandozeile aus
     *
     * @param dataPackages Hier wird die Liste übergeben, deren Elemente in die Kommandozeile ausgegeben werden sollen
     */
    public void printOutPackage(List<DataPackage> dataPackages) {
        //System.out.println(dataPackages.toString());
        int i = 1;
        for(DataPackage x : dataPackages)
        {
            System.out.println(x.toString());
            System.out.println("Paketlaufnummer: " + i);
            if(x.getNachricht() == "")
            {
                System.out.println("Datenteil-Länge: 2" );
                System.out.println("Datenteil: "+ "\\n" );
            }
            else{
                System.out.println("Datenteil-Länge: " + x.getNachricht().length());
                System.out.println("Datenteil: " + x.getNachricht());
            }

            System.out.println();
            i++;


        }


    }
}
