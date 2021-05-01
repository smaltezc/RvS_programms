package programmieraufgaben;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


/**
 * Eine vereinfachte Switch Engine mit folgenden möglichen Kommandos:
 * frame <Eingangsportnummer> <Absenderadresse> <Zieladresse>,
 * table,
 * statistics,
 * del Xs bzw. del Xmin,
 * exit
 */
public class SwitchingEngine {

    private int[] ziel;
    private int[] absender;
    private int[] port;
    private  int countFrames; //für frames
    private ArrayList<String> sTable; //für table
    private ArrayList<String> sTatic; //für statistics
    /**
     * Diese Methode überprüft die Eingabe und erstellt die für den
     * weiteren Funktionsablauf nötige Datenstruktur
     * @param portNumber Anzahl der Ports, die der Switch verwalten soll
     * @return Gibt bei erfolgreicher erstellung TRUE sonst FALSE zurück
     */
    public boolean createSwitch(int portNumber)
    {
        //Wir überprüfen hier, ob die Port Nummer eine natürliche Zahl ist.
        //Wenn die eine natürliche Zahl ist, dann erstellen wir die Datenstruktur
        if(portNumber >= 1 )
        {
            countFrames = 0;
            sTable = new ArrayList<>();
            sTatic = new ArrayList<>();
            port = new int[portNumber];
            for (int p = 0; p<port.length;p++)
            {
                port[p] = p+1;
            }
            absender = new int[254];
            for (int a = 0; a<absender.length;a++)
            {
                absender[a] = a+1;
            }
            ziel = new int[255];
            for (int z = 0; z<ziel.length;z++)
            {
                ziel[z] = z+1;
            }
            System.out.println();
            System.out.println("Ein " + portNumber + "-Port-Switch wurde erzeugt.\n");
            return true;
        }
        else
        {
            System.out.println();
            System.out.println("Falsche Eingabe: "+portNumber+" ist keine Natürliche Zahl!\n");
            return false;
        }

    }

    /**
     * Diese Methode überprüft und interpretiert die Eingaben und führt
     * die geforderten Funktionen aus.
     * @param command Anweisung die der Switch verarbeiten soll
     * @return Gibt an ob der Switch beendet werden soll: TRUE beenden, FALSE weitermachen
     */
    public boolean handleCommand(String command)
    {
        boolean result = false;
        String[] input = command.split(" ");
        switch (input[0])
        {
            //abhaengig von der Eingabe des ersten Wort
            case "frame" :
                setFrame(command, input);
                break;
            case "table" :
                printTable(input);
                break;
            case "statistics" :
                printStatistics(port.length,input);
                break;
            case "del" :
                delTable(input);
                break;
            case "exit" :
                result = true;
                break;
            default :
                System.out.println("Ungültige Eingabe! \n");
        }
        return result;
    }
    //löscht eine Adresse aus der Tabelle, wenn nach einem bestimmten
    //Zeitraum keine Frames empfangen wurden, die diese Adresse als Quelladresse
    //enthielten
    public void delTable(String[] input)
    {
        String intValue = input[1].replaceAll("[^0-9]", "");
        int x= Integer.parseInt(intValue);
        if(x>0 && input.length == 2)
        {

            long ms =0;
            String min = "";
            String s ="";
            if(input[1].length() == intValue.length()+3)//3 30 300 min
            {
                min = input[1].substring(intValue.length(),intValue.length()+3);
                ms = (long) x *60*1000;
            }
            else if(input[1].length() == intValue.length()+1)// 3 30 300s
            {
                s = input[1].substring(intValue.length(),intValue.length()+1);
                ms = x* 1000L;
            }

            if(min.equals("min") || s.equals("s"))
            {
                String returnMsg = "";
                for(int i=0;i< sTable.size();i++)
                {
                    String[] table = sTable.get(i).split("\t\t");
                    String time=table[2]; //table[] = [adresse, port, zeit]


                    try {
                        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
                        Date now = new Date();
                        java.text.DateFormat df = new java.text.SimpleDateFormat("HH:mm:ss");
                        Date jetzt = df.parse(sdfDate.format(now));
                        Date date = df.parse(time);
                        long diff = jetzt.getTime() - date.getTime();

                        if(diff > ms)
                        {
                            sTable.remove(i);
                            returnMsg += table[0]+" ";
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (hasNums(returnMsg))
                {
                    System.out.println("Folgende Adressen wurden aus der Switch-Tabelle gelöscht: "+returnMsg.replace(" ",", "));
                    System.out.println();
                }
                else {
                        System.out.println("Es wurden keine Adressen aus der Switch-Tabelle gelöscht.\n");
                }

            }
            else {
                System.out.println("Ungültige Eingabe! \n");
            }
        }
        else
        {
            System.out.println("Ungültige Eingabe! \n");
        }

    }
    //Statistics
    public void printStatistics(int por,String[] input)
    {
        if(input.length == 1)
        {
            System.out.println("Port"+ "\t\t" +"Frames");
            for (int i=0;i<por;i++)
            {
                System.out.println(sTatic.get(i));
            }
            System.out.println();
        }
        else
        {
            System.out.println("Ungültige Eingabe! \n");
        }

    }


    //printing switch-table
    public void printTable(String[] input)
    {
        if(input.length == 1)
        {
            if(sTable.isEmpty())
            {
                System.out.println("Die Tabelle ist leer!\n");
            }
            else
            {
                int[] sortAdress = new int[sTable.size()];
                int[] ports = new int[sTable.size()];
                int[] table;
                System.out.println("Adresse"+"\t\t"+ "Port" +"\t\t"+ "Zeit");
                for(int i=0;i<sTable.size();i++)
                {
                    table = toInt(sTable.get(i));
                    sortAdress[i] = table[0];
                    ports[i] = table[1];
                }
                Arrays.sort(sortAdress);
                for(int j=0;j< sTable.size();j++)
                {

                    for(int m=0;m<sTable.size();m++)
                    {
                        table = toInt(sTable.get(m));
                        if(sortAdress[j] == table[0] )
                        {
                            System.out.println(sTable.get(m));
                        }
                    }

                }
                System.out.println();
            }
        }
        else
        {
            System.out.println("Ungültige Eingabe! \n");
        }

    }
    //Wir füllen hier die Tabelle und Statistiken in Abhängigkeit von den Eingabe frames aus.
    public void setFrame(String command,String[] input)
    {
        int portZahl = port.length;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        String strTime = formatter.format(time);//für die aktuelle Zeit

        //Wir überprüfen hier, ob die Inputs von frames eine Nummer besitzen,
        //und splitete Inputs des frames insgesamt aus 4 Woerter bestehen .
        if (hasNums(command) && input.length==4)
        {
            int[] frameZahl = new int[3];//für jede Zahl in der Inputs des Frames.
            for(int i=1;i<4;i++)
            {
                frameZahl[i-1] = Integer.parseInt(input[i]);
            }
            // frame 1 22 33
            // frameZahl[] = [1, 22, 33]
            //Wir prüfen hier, ob die Tabelle leer ist und die Zahlen der Inputs des Frames in der angeforderten Form vorliegt.
            if( frameZahl[0] <= portZahl && frameZahl[1] < 255 && frameZahl[2] < 256
                && frameZahl[0]>0 && frameZahl[1]>0 && frameZahl[2]>0)
            {
                //Wenn die Tabelle leer ist, adden wir die Daten von Frames in die Tabelle und statistik.
                if(sTable.isEmpty())
                {
                    port[frameZahl[0]-1] = frameZahl[0];        //frame 2 22 33 -> port[1]=2
                    absender[frameZahl[1]-1] = frameZahl[1];    //frame 2 22 33 -> absender[21]=22
                    ziel[frameZahl[2]-1] =frameZahl[2];
                    sTable.add(absender[frameZahl[1]-1] +"\t\t"+ port[frameZahl[0]-1]+"\t\t"+strTime);
                    countFrames++;

                    //Es gibt keinen Eintrag fuer Ziel in der Switch-Tabelle. In diesem Fall erfolgt eine Ausgabe des Frames
                    //auf allen Ports außer Port p. alle Frames der Ports werden am Anfang um 1 gestiegen.
                    //eingegebende frame der ports ist auch inklusiv, denn die Tabelle leer ist.
                    for(int w=0;w<portZahl;w++)
                    {
                        sTatic.add(port[w]+"\t\t"+countFrames);
                    }

                    //für Broadcast Adresse
                    if(255 == frameZahl[2] )
                    {
                        System.out.println("Broadcast: Ausgabe auf allen Ports außer Port "+ frameZahl[0] + ".\n");
                    }
                    else
                    {
                        System.out.println("Ausgabe auf allen Ports außer Port "+ frameZahl[0] + ".\n");
                    }
                }
                else
                {
                    //fürs Abbrechen des for-loops
                    boolean pause = false;
                    boolean pause2 =false;
                    //Zugriff auf die Daten in der Tabelle
                    int tablength = sTable.size();
                    for(int t=0;t<tablength;t++)
                    {
                        //die Adresse, ports und zeit in der Tabelle speichern.
                        int[] tab = toInt(sTable.get(t)); //tab[]= [adresse, port, zeit]
                        //frame 1 22 33
                        port[frameZahl[0]-1] = frameZahl[0];    //port[0]=1
                        absender[frameZahl[1]-1] = frameZahl[1];//absender[21]=22
                        ziel[frameZahl[2]-1] = frameZahl[2];    //ziel[32]=33

                        int prt =port[frameZahl[0]-1];
                        int absnd =absender[frameZahl[1]-1];
                        int z = ziel[frameZahl[2]-1];

                        //In der Switch-Tabelle existiert ein Eintrag fuer Ziel mit Port p. Der Eingangsport
                        //und der Ausgangsport sind also identisch. Demzufolge findet eine Filterung des
                        //Frames statt und der Frame wird vom Switch verworfen.    // 1 22 33
                        if(prt == tab[1] && tab[0] == z) // frame 1 60 22  -> 1=tab[1] & tab[0]=22
                        {
                            pause =true;
                            sTable.add(absnd +"\t\t" +prt+"\t\t"+strTime);
                            //Zugriff auf die Daten in der Statistik
                            for(int p=0;p<sTatic.size();p++)
                            {
                                String s[] = sTatic.get(p).split("\t\t");
                                //die ports und frames in der Statistik speichern.
                                int[] frms = new int[2];//frms[]= [ports,frames]
                                for (int f=0;f<2;f++)
                                {
                                    frms[f] = Integer.parseInt(s[f]);
                                }
                                //wenn die port der Inputs und irgendein port in der Statistik gleich sind
                                if(frms[0] == frameZahl[0])//frame 1 22 33 -> if(frms[0]=1)
                                {
                                    //erhöhen wir die frames in der Statistik um 1.
                                    sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                }
                            }

                            System.out.println("Frame wird gefiltert und verworfen.\n");
                        }
                        //Es gibt einen Eintrag fuer Ziel mit port x != p in der Tabelle. In diesem Fall erfolgt
                        //eine Ausgabe des Frames auf Port p.
                        //wenn die Zieladresse der Inputs und irgendeine Adresse in der Tabelle gleich sind
                        else if(tab[0] == z && prt != tab[1] ) // 1 23 54
                        {                     // 4 32 23
                            pause =true;
                            //Wenn die Zieladresse der Inputs und irgendeine Adresse in der Tabelle nicht gleich sind

                            int tablength2 = sTable.size();
                            for(int m=0;m<tablength2;m++)
                            {
                                int[] mab = toInt(sTable.get(m)); //tab[]= [adresse, port, zeit]
                                if(mab[0] == absnd)
                                {
                                    pause2 = true;
                                    sTable.set(m,absnd +"\t\t"+prt+"\t\t"+strTime);

                                }

                            }
                            //wenn es in der obigen if Bedingung in for loop nicht drin ist
                            if(!pause2) {
                                sTable.add(absnd +"\t\t" +prt+"\t\t"+strTime);
                            }


                            for(int p=0;p<sTatic.size();p++)
                            {
                                String s[] = sTatic.get(p).split("\t\t");
                                int[] frms = new int[2];//frms[]= [ports,frames]
                                for (int f=0;f<2;f++)
                                {
                                    frms[f] = Integer.parseInt(s[f]);
                                }
                                //für eingegebende ports ob die bereits in der Statistik ist
                                if(frms[0] == frameZahl[0])
                                {
                                    sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                }
                                //für ports der Tabelle ob die bereits in der Statistik ist
                                else if(tab[1] == frms[0])
                                {
                                    sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                }

                            }
                            System.out.println("Ausgabe auf Port "+ tab[1] + ".\n");

                        }
                        else
                        {
                            //für Broadcast Adresse
                            if(255 == z  )
                            {
                                pause =true;
                                sTable.add(absnd +"\t\t" + prt +"\t\t"+strTime);

                                for(int p=0;p<sTatic.size();p++)
                                {
                                    String s[] = sTatic.get(p).split("\t\t");
                                    int[] frms = new int[2];
                                    for (int f=0;f<2;f++)
                                    {
                                        frms[f] = Integer.parseInt(s[f]);
                                    }
                                    //für eingegebende ports ob die bereits in der Statistik ist
                                    if(frms[0] == prt)
                                    {

                                        sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                    }
                                    else
                                    {
                                        sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                    }

                                }
                                System.out.println("Broadcast: Ausgabe auf allen Ports außer Port "+ prt + ".\n");
                            }


                        }
                        //wenn es in der eine irgendeine der obigen if Bedingungen drin ist
                        if(pause)
                        {
                            break;
                        }

                    }
                    //wenn es in der eine irgendeine der obigen if Bedingungen nicht drin ist
                    if(!pause)
                    {
                        for (int u=0;u<tablength;u++)
                        {
                            //die Adresse, ports und zeit in der Tabelle speichern.
                            int[] tab2 = toInt(sTable.get(u)); //tab[]= [adresse, port, zeit]
                            port[frameZahl[0]-1] = frameZahl[0];    //port[0]=1
                            absender[frameZahl[1]-1] = frameZahl[1];//absender[21]=22
                            ziel[frameZahl[2]-1] = frameZahl[2];

                            //Es gibt keinen Eintrag fur ziel in der Switch-Tabelle. In diesem Fall erfolgt
                            //eine Ausgabe des Frames auf allen Ports außer Port p
                            if(frameZahl[2] != tab2[0])
                            {

                                sTable.add(frameZahl[1] +"\t\t"+ frameZahl[0] +"\t\t"+strTime);

                                for(int p=0;p<sTatic.size();p++)
                                {
                                    String s[] = sTatic.get(p).split("\t\t");
                                    int[] frms = new int[2];
                                    for (int f=0;f<2;f++)
                                    {
                                        frms[f] = Integer.parseInt(s[f]);
                                    }
                                    if(frms[0] == frameZahl[0])
                                    {

                                        sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                    }
                                    else
                                    {
                                        sTatic.set(frms[0]-1,frms[0]+"\t\t"+(frms[1]+1));
                                    }

                                }

                                System.out.println("Ausgabe auf allen Ports außer Port "+ frameZahl[0] + ".\n");
                                break;
                            }
                        }

                    }


                }

            }
            else
            {
                System.out.println("Ungültige Eingabe!\n");
            }

        }
        else
        {
            System.out.println("Ungültige Eingabe!\n");
        }
    }

    //String to int[]
    public int[] toInt(String s)
    {
        String[] a = s.split("\t\t");
        int[] b = new int[a.length];
        for(int i=0;i<a.length-1;i++)
        {
            b[i] = Integer.parseInt(a[i]);
        }
        return b;
    }

    public boolean hasNums(String text) {

        if (text.matches(".*\\d.*")) {
            return true;
        }
        return false;
    }
}
