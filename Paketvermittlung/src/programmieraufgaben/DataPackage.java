package programmieraufgaben;

/**
 * Hier sollen die Nutzereingaben sowie die Resultate gespeichert werden.
 * Die Struktur der Klasse und die Variablen können frei gewählt werden.
 */

public class DataPackage {
    //maximale Datenteil-Länge
    private int dataPackageLength;

    //
    private String version;
    private String absender;
    private String empfanger;
    private String nachricht;
    //
    /**
     * Erzeugt ein DataPackage Objekt und speichert beim erzeugen die maximale Datenteil-Länge
     * @param dataPackageLength
     */
    public DataPackage(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;

        //
        this.version = null;
        this.absender = null;
        this.empfanger = null;
        this.nachricht = null;
        //
    }

    //
    public DataPackage(int i,String version, String absender,String empfanger, String nachricht) {
        this.dataPackageLength = dataPackageLength;

        //
        this.version = version;
        this.absender = absender;
        this.empfanger = empfanger;
        this.nachricht = nachricht;
        //
    }
    //

    /**
     * Gibt die maximale Datenteil-Länge zurück
     * @return maximale Datenteil-Länge
     */
    public int getDataPackageLength() {
        return dataPackageLength;
    }

    //
    public String getVersion() {
        return version;
    }
    public String getAbsender() {
        return absender;
    }
    public String getEmpfanger() {
        return empfanger;
    }
    public String getNachricht() {
        return nachricht;
    }
    //

    /**
     * Setzt die maximale Datenteil-Länge
     * @param dataPackageLength
     */
    public void setDataPackageLength(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }

    //
    public void setVersion(String version) {
        this.version = version;
    }

    public void setAbsender(String absender) {
        this.absender = absender;
    }

    public void setEmpfanger(String empfanger) {
        this.empfanger = empfanger;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }
    //

    public String toString()
    {
        return "Version: " + version + "\nAbsender: " + absender + "\nEmpfänger: " + empfanger  ;
    }
}
